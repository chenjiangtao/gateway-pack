package net.zoneland.gateway.comm.sgip;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.zoneland.gateway.comm.PEvent;
import net.zoneland.gateway.comm.PException;
import net.zoneland.gateway.comm.PLayer;
import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.PReader;
import net.zoneland.gateway.comm.PWriter;
import net.zoneland.gateway.util.Args;
import net.zoneland.gateway.util.Debug;
import net.zoneland.gateway.util.Resource;
import net.zoneland.gateway.util.WatchThread;

public abstract class SGIPSocketConnection extends PLayer {

    protected static String     NOT_INIT;
    protected static String     CONNECTING;
    protected static String     RECONNECTING;
    protected static String     CONNECTED;
    protected static String     HEARTBEATING;
    protected static String     RECEIVEING;
    protected static String     CLOSEING;
    protected static String     CLOSED;
    protected static String     UNKNOWN_HOST;
    protected static String     PORT_ERROR;
    protected static String     CONNECT_REFUSE;
    protected static String     NO_ROUTE_TO_HOST;
    protected static String     RECEIVE_TIMEOUT;
    protected static String     CLOSE_BY_PEER;
    protected static String     RESET_BY_PEER;
    protected static String     CONNECTION_CLOSED;
    protected static String     COMMUNICATION_ERROR;
    protected static String     CONNECT_ERROR;
    protected static String     SEND_ERROR;
    protected static String     RECEIVE_ERROR;
    protected static String     CLOSE_ERROR;
    private String              error;
    protected Date              errorTime;
    protected String            name;
    protected String            host;
    protected int               port;
    protected String            localHost;
    protected int               localPort;
    protected int               heartbeatInterval;
    protected PReader           in;
    protected PWriter           out;
    protected static DateFormat df                 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    protected int               readTimeout;
    protected int               reconnectInterval;
    protected Socket            socket;
    protected WatchThread       heartbeatThread;
    protected WatchThread       receiveThread;
    protected int               transactionTimeout = 3 * 1000;
    protected Resource          resource;

    public SGIPSocketConnection(Args args) {
        super(null);
        errorTime = new Date();
        port = -1;
        localPort = -1;
        init(args);
    }

    protected SGIPSocketConnection() {
        super(null);
        errorTime = new Date();
        port = -1;
        localPort = -1;
    }

    protected void init(Args args) {
        resource = getResource();
        initResource();
        error = NOT_INIT;
        setAttributes(args);
        class ReceiveThread extends WatchThread {

            public void task() {
                try {
                    if (error == null) {
                        PMessage m = in.read();
                        if (m != null && m != null) {
                            onReceive(m);
                        }
                    } else {
                        if (error != SGIPSocketConnection.NOT_INIT) {
                            try {
                                Thread.sleep(reconnectInterval);
                            } catch (InterruptedException interruptedexception) {
                            }
                        }
                        connect();
                    }
                } catch (IOException ioexception) {
                }
            }

            public ReceiveThread() {
                super(String.valueOf(String.valueOf(name)).concat("-receive"));
            }
        }

        receiveThread = new ReceiveThread();
        receiveThread.setDaemon(true);
        receiveThread.start();
        if (this.heartbeatInterval > 0) {
            this.heartbeatThread = new HeartbeatThread();
            this.heartbeatThread.start();
        }
    }

    protected void init(Args args, Socket socket) {
        resource = getResource();
        initResource();
        error = NOT_INIT;
        if (socket != null) {
            this.socket = socket;
            try {
                out = getWriter(this.socket.getOutputStream());
                in = getReader(this.socket.getInputStream());
                setError(null);
            } catch (IOException ex) {
                setError(String.valueOf(CONNECT_ERROR) + String.valueOf(explain(ex)));
            }
            if (args != null) {
                setAttributes1(args);
            }
            class ReceiveThread1 extends WatchThread {

                public void task() {
                    try {
                        if (error == null) {
                            PMessage m = in.read();

                            if (m != null && m != null) {
                                onReceive(m);
                            }
                        }
                    } catch (IOException ex) {
                        setError(explain(ex));
                        if (error == SGIPSocketConnection.RECEIVE_TIMEOUT) {
                            setError(null);
                            onReadTimeOut();
                        }
                    }
                }

                public ReceiveThread1() {
                    super(String.valueOf(String.valueOf(name)).concat("-receive"));
                }
            }

            receiveThread = new ReceiveThread1();
            receiveThread.setDaemon(true);
            receiveThread.start();

        }
    }

    class HeartbeatThread extends WatchThread {
        public HeartbeatThread() {
            super(String.valueOf(String.valueOf(SGIPSocketConnection.this.name)).concat(
                "-heartbeat"));
        }

        public void task() {
            try {
                Thread.sleep(SGIPSocketConnection.this.heartbeatInterval);
            } catch (InterruptedException localInterruptedException) {
            }
            if ((SGIPSocketConnection.this.error != null)
                || (SGIPSocketConnection.this.out == null))
                return;
            try {
                SGIPSocketConnection.this.heartbeat();
            } catch (IOException ex) {
                SGIPSocketConnection.this.setError(String.valueOf(
                    String.valueOf(SGIPSocketConnection.SEND_ERROR)).concat(
                    String.valueOf(String.valueOf(SGIPSocketConnection.this.explain(ex)))));
            }
        }
    }

    protected void onReadTimeOut() {
        throw new UnsupportedOperationException("Not implement");
    }

    public void setAttributes(Args args) {
        if (name != null
            && name.equals(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String
                .valueOf(String.valueOf(host))))).append(':').append(port))))) {
            name = null;
        }
        String oldHost = host;
        int oldPort = port;
        String oldLocalHost = localHost;
        int oldLocalPort = localPort;
        host = args.get("host", null);
        port = args.get("port", -1);
        localHost = args.get("local-host", null);
        localPort = args.get("local-port", -1);
        name = args.get("name", null);
        if (name == null) {
            name = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String
                .valueOf(host)))).append(':').append(port)));
        }
        readTimeout = 0;
        if (socket != null) {
            try {
                socket.setSoTimeout(readTimeout);
            } catch (SocketException socketexception) {
            }
        }
        heartbeatInterval = 1000 * args.get("heartbeat-interval", 0);
        transactionTimeout = 1000 * args.get("transaction-timeout", 3);
        if (error == null && host != null && port != -1
            && (!host.equals(oldHost) || port != port || !host.equals(oldHost) || port != port)) {
            setError(resource.get("comm/need-reconnect"));
            receiveThread.interrupt();
        }
    }

    public void setAttributes1(Args args) {
        if (name != null
            && name.equals(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String
                .valueOf(String.valueOf(host))))).append(':').append(port))))) {
            name = null;
        }
        host = args.get("host", null);
        port = args.get("port", -1);
        localHost = args.get("local-host", null);
        localPort = args.get("local-port", -1);
        name = args.get("name", null);
        if (name == null) {
            name = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String
                .valueOf(host)))).append(':').append(port)));
        }
        readTimeout = 1000 * args.get("read-timeout", readTimeout / 1000);
        if (socket != null) {
            try {
                socket.setSoTimeout(readTimeout);
            } catch (SocketException socketexception) {
            }
        }
        heartbeatInterval = 1000 * args.get("heartbeat-interval", 0);
        transactionTimeout = 1000 * args.get("transaction-timeout", 3);
    }

    public void send(PMessage message) throws PException {
        if (error != null) {
            throw new PException(String.valueOf(SEND_ERROR) + String.valueOf(getError()));
        }
        try {
            if (SGIPConstant.debug) {
                Debug.dump("write:" + message + ",socket:" + toString());
            }
            out.write(message);
            fireEvent(new PEvent(8, this, message));
        } catch (PException ex) {
            fireEvent(new PEvent(16, this, message));
            setError(String.valueOf(SEND_ERROR) + String.valueOf(explain(ex)));
            throw ex;
        } catch (Exception ex) {
            fireEvent(new PEvent(16, this, message));
            setError(String.valueOf(SEND_ERROR) + String.valueOf(explain(ex)));
        }
    }

    public void sendResponse(PMessage message) throws PException {
        if (error != null) {
            throw new PException(String.valueOf(SEND_ERROR) + String.valueOf(getError()));
        }
        try {
            if (SGIPConstant.debug) {
                Debug.dump("write:" + message + ",socket:" + toString());
            }
            out.write(message);
            fireEvent(new PEvent(8, this, message));
        } catch (PException ex) {
            fireEvent(new PEvent(16, this, message));
            setError(String.valueOf(SEND_ERROR) + String.valueOf(explain(ex)));
            throw ex;
        } catch (Exception ex) {
            fireEvent(new PEvent(16, this, message));
            setError(String.valueOf(SEND_ERROR) + String.valueOf(explain(ex)));
        }
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getReconnectInterval() {
        return reconnectInterval / 1000;
    }

    public String toString() {
        if (socket != null) {
            return String.valueOf(String.valueOf((new StringBuffer("SGIPConnection:")).append(name)
                .append('(').append(socket.getLocalAddress()).append(':')
                .append(socket.getLocalPort()).append(')')));
        } else {
            return String.valueOf(String.valueOf((new StringBuffer("SGIPConnection:")).append(name)
                .append("socket:(").append(socket).append(':').append(')')));
        }
    }

    public int getReadTimeout() {
        return readTimeout / 1000;
    }

    public boolean available() {
        return error == null;
    }

    public String getError() {
        return error;
    }

    public Date getErrorTime() {
        return errorTime;
    }

    public boolean isClosed() {
        return this.socket == null || this.socket.isClosed() || !this.socket.isConnected();
    }

    public synchronized void close() {
        try {
            if (socket != null) {
                socket.close();
                in = null;
                out = null;
                socket = null;
            }
            if (heartbeatThread != null) {
                heartbeatThread.kill();
            }
            if (this.receiveThread != null) {
                receiveThread.kill();
            }
        } catch (Exception exception) {
        }
        setError(NOT_INIT);
    }

    protected synchronized void connect() {
        if (error == NOT_INIT) {
            error = CONNECTING;
        } else if (error == null) {
            error = RECONNECTING;
        }
        errorTime = new Date();
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ioexception) {
            }
        }
        try {
            if (port <= 0 || port > 65535) {
                setError(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String
                    .valueOf(PORT_ERROR)))).append("port:").append(port))));
                return;
            }
            if (localPort < -1 || localPort > 65535) {
                setError(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String
                    .valueOf(PORT_ERROR)))).append("local-port:").append(localPort))));
                return;
            }
            if (localHost != null) {
                boolean isConnected = false;
                InetAddress localAddr = InetAddress.getByName(localHost);
                if (localPort == -1) {
                    for (int p = (int) (Math.random() * (double) 64500); p < 0xdc758; p += 13) {
                        try {
                            socket = new Socket(host, port, localAddr, 1025 + p % 64500);
                            isConnected = true;
                            break;
                        } catch (IOException ioexception1) {
                        } catch (SecurityException securityexception) {
                        }
                    }

                    if (!isConnected) {
                        throw new SocketException("Can not find an avaliable local port");
                    }
                } else {
                    socket = new Socket(host, port, localAddr, localPort);
                }
            } else {
                socket = new Socket(host, port);
            }
            socket.setSoTimeout(readTimeout);
            out = getWriter(socket.getOutputStream());
            in = getReader(socket.getInputStream());
            setError(null);
        } catch (IOException ex) {
            setError(String.valueOf(CONNECT_ERROR) + String.valueOf(explain(ex)));
        }
    }

    protected void setError(String desc) {
        if (error == null && desc == null || desc != null && desc.equals(error)) {
            return;
        }
        error = desc;
        errorTime = new Date();
        if (desc == null) {
            desc = CONNECTED;
        }
    }

    protected abstract PWriter getWriter(OutputStream outputstream);

    protected abstract PReader getReader(InputStream inputstream);

    protected abstract Resource getResource();

    protected void heartbeat() throws IOException {
    }

    public void initResource() {
        NOT_INIT = getDef(resource.get("comm/not-init"), "not-init");
        CONNECTING = getDef(resource.get("comm/connecting"), "connecting");
        RECONNECTING = getDef(resource.get("comm/reconnecting"), "reconnecting");
        CONNECTED = getDef(resource.get("comm/connected"), "connected");
        HEARTBEATING = getDef(resource.get("comm/heartbeating"), "heartbeating");
        RECEIVEING = getDef(resource.get("comm/receiveing"), "receiveing");
        CLOSEING = getDef(resource.get("comm/closeing"), "closeing");
        CLOSED = getDef(resource.get("comm/closed"), "closed");
        UNKNOWN_HOST = getDef(resource.get("comm/unknown-host"), "unknown-host");
        PORT_ERROR = getDef(resource.get("comm/port-error"), "port-error");
        CONNECT_REFUSE = getDef(resource.get("comm/connect-refused"), "connect-refused");
        NO_ROUTE_TO_HOST = getDef(resource.get("comm/no-route"), "no-route");
        RECEIVE_TIMEOUT = getDef(resource.get("comm/receive-timeout"), "receive-timeout");
        CLOSE_BY_PEER = getDef(resource.get("comm/close-by-peer"), "close-by-peer");
        RESET_BY_PEER = getDef(resource.get("comm/reset-by-peer"), "reset-by-peer");
        CONNECTION_CLOSED = getDef(resource.get("comm/connection-closed"), "connection-closed");
        COMMUNICATION_ERROR = getDef(resource.get("comm/communication-error"),
            "communication-error");
        CONNECT_ERROR = getDef(resource.get("comm/connect-error"), "connect-error");
        SEND_ERROR = getDef(resource.get("comm/send-error"), "send-error");
        RECEIVE_ERROR = getDef(resource.get("comm/receive-error"), "receive-error");
        CLOSE_ERROR = getDef(resource.get("comm/close-error"), "close-error");
    }

    private String getDef(String val, String def) {
        if (val == null) {
            return def;
        }
        return val;

    }

    protected String explain(Exception ex) {
        String msg = ex.getMessage();
        if (msg == null) {
            msg = "";
        }
        if (ex instanceof PException) {
            return ex.getMessage();
        }
        if (ex instanceof EOFException) {
            return CLOSE_BY_PEER;
        }
        if (msg.indexOf("Connection reset by peer") != -1) {
            return RESET_BY_PEER;
        }
        if (msg.indexOf("SocketTimeoutException") != -1) {
            return RECEIVE_TIMEOUT;
        }
        if (ex instanceof SocketTimeoutException) {
            return RECEIVE_TIMEOUT;
        }
        if (ex instanceof NoRouteToHostException) {
            return NO_ROUTE_TO_HOST;
        }
        if (ex instanceof ConnectException) {
            return CONNECT_REFUSE;
        }
        if (ex instanceof UnknownHostException) {
            return UNKNOWN_HOST;
        }
        if (msg.indexOf("errno: 128") != -1) {
            return NO_ROUTE_TO_HOST;
        } else {
            ex.printStackTrace();
            return ex.toString();
        }
    }

    public boolean isConnect() {
        if (this.socket == null) {
            return false;
        }
        return this.socket.isConnected();
    }

}