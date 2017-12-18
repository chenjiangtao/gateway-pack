/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.comm;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.zoneland.gateway.util.Args;
import net.zoneland.gateway.util.Resource;
import net.zoneland.gateway.util.WatchThread;

/**
 * 网关连接基本核心类，实现了基本的连接及收发框 架模型，具体网关实硐我只需要实现此类，覆盖一些基本的方法
 * 
 * 
 * @author liuzhenxing
 * @version $Id: PSocketConnection.java, v 0.1 2012-5-11 下午3:30:02 liuzhenxing Exp $
 */
public abstract class PSocketConnection extends PLayer {

    protected static String   NOT_INIT;
    protected static String   CONNECTING;
    protected static String   RECONNECTING;
    protected static String   CONNECTED;
    protected static String   HEARTBEATING;
    protected static String   RECEIVEING;
    protected static String   CLOSEING;
    protected static String   CLOSED;
    protected static String   UNKNOWN_HOST;
    protected static String   PORT_ERROR;
    protected static String   CONNECT_REFUSE;
    protected static String   NO_ROUTE_TO_HOST;
    protected static String   RECEIVE_TIMEOUT;
    protected static String   CLOSE_BY_PEER;
    protected static String   RESET_BY_PEER;
    protected static String   CONNECTION_CLOSED;
    protected static String   COMMUNICATION_ERROR;
    protected static String   CONNECT_ERROR;
    protected static String   SEND_ERROR;
    protected static String   RECEIVE_ERROR;
    protected static String   CLOSE_ERROR;

    private String            error;
    private Date              errorTime      = new Date();
    private String            name;
    private String            host;
    private int               port           = -1;
    private String            localHost;
    private int               localPort      = -1;
    private int               heartbeatInterval;
    private PReader           in;
    private PWriter           out;
    private static DateFormat df             = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private int               readTimeout;
    private int               reconnectInterval;
    private int               reconnectCount = 0;
    private Socket            socket;
    private WatchThread       heartbeatThread;
    private WatchThread       receiveThread;
    private int               transactionTimeout;
    private Resource          resource;

    /**
     * Getter method for property <tt>in</tt>.
     * 
     * @return property value of in
     */
    public PReader getIn() {
        return in;
    }

    /**
     * Getter method for property <tt>out</tt>.
     * 
     * @return property value of out
     */
    public PWriter getOut() {
        return out;
    }

    /**
     * Getter method for property <tt>transactionTimeout</tt>.
     * 
     * @return property value of transactionTimeout
     */
    public int getTransactionTimeout() {
        return transactionTimeout;
    }

    /**
     * Setter method for property <tt>transactionTimeout</tt>.
     * 
     * @param transactionTimeout value to be assigned to property transactionTimeout
     */
    public void setTransactionTimeout(int transactionTimeout) {
        this.transactionTimeout = transactionTimeout;
    }

    public PSocketConnection(Args args) {
        super(null);
        init(args);
    }

    protected PSocketConnection() {
        super(null);
    }

    /**
     * 初始化连接的所有参数，启动心跳和拉媚任务
     * 
     * @param args
     */
    protected void init(Args args) {
        //获取提示信息并初始化
        this.resource = getResource();
        initResource();

        //初始化状态及初始参数
        this.error = NOT_INIT;
        setAttributes(args);

        //心跳时间间隔大于0启动心跳线程
        if (this.heartbeatInterval > 0) {
            this.heartbeatThread = new HeartbeatThread();
            this.heartbeatThread.setDaemon(true);
            this.heartbeatThread.start();
        }

        //初始化并启动接收线程
        this.receiveThread = new ReceiveThread();
        this.receiveThread.setDaemon(true);
        this.receiveThread.start();
    }

    public void setAttributes(Args args) {
        if ((this.name != null)
            && (this.name
                .equals(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String
                    .valueOf(String.valueOf(this.host)))).append(':').append(this.port)))))) {
            this.name = null;
        }

        String oldHost = this.host;
        int oldPort = this.port;
        String oldLocalHost = this.localHost;
        int oldLocalPort = this.localPort;

        this.host = args.get("host", null);

        this.port = args.get("port", -1);

        this.localHost = args.get("local-host", null);

        this.localPort = args.get("local-port", -1);

        this.name = args.get("name", null);
        if (this.name == null) {
            this.name = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String
                .valueOf(this.host))).append(':').append(this.port)));
        }

        this.readTimeout = (1000 * args.get("read-timeout", this.readTimeout / 1000));

        if (this.socket != null)
            try {
                this.socket.setSoTimeout(this.readTimeout);
            } catch (SocketException localSocketException) {

            }
        this.reconnectInterval = (1000 * args.get("reconnect-interval", 2));

        this.heartbeatInterval = (1000 * args.get("heartbeat-interval", -1));

        this.transactionTimeout = (1000 * args.get("transaction-timeout", 2));

        if ((this.error != null)
            || (this.host == null)
            || (this.port == -1)
            || ((this.host.equals(oldHost)) && (this.port == oldPort)
                && (this.localHost.equals(oldLocalHost)) && (this.localPort == oldLocalPort))) {
            return;
        }

        setError(this.resource.get("comm/need-reconnect"));
        this.receiveThread.interrupt();
    }

    /**
     * 发送消息，发送成功，调用发送成功处理事件，发送失败，调用发送失见识处理事件
     * @see net.zoneland.gateway.comm.PLayer#send(net.zoneland.gateway.comm.PMessage)
     */
    public void send(PMessage message) throws PException {
        if (this.error != null)
            throw new PException("["
                                 + String.valueOf(String.valueOf(SEND_ERROR)).concat(
                                     String.valueOf(String.valueOf(getError()))) + "]");
        try {
            this.out.write(message);
            super.fireEvent(new PEvent(PEvent.MESSAGE_SEND_SUCCESS, this, message));
        } catch (PException ex) {
            super.fireEvent(new PEvent(PEvent.MESSAGE_SEND_FAIL, this, message));
            setError(String.valueOf(String.valueOf(SEND_ERROR)).concat(
                String.valueOf(String.valueOf(explain(ex)))));
            throw ex;
        } catch (Exception ex) {
            super.fireEvent(new PEvent(PEvent.MESSAGE_SEND_FAIL, this, message));
            setError(String.valueOf(String.valueOf(SEND_ERROR)).concat(
                String.valueOf(String.valueOf(explain(ex)))));
        }
    }

    public void sendResponse(PMessage message) throws PException {
        if (this.error != null)
            throw new PException("["
                                 + String.valueOf(String.valueOf(SEND_ERROR)).concat(
                                     String.valueOf(String.valueOf(getError()))) + "]");
        try {
            this.out.write(message);
            super.fireEvent(new PEvent(PEvent.MESSAGE_SEND_SUCCESS, this, message));
        } catch (PException ex) {
            super.fireEvent(new PEvent(PEvent.MESSAGE_SEND_FAIL, this, message));
            setError(String.valueOf(String.valueOf(SEND_ERROR)).concat(
                String.valueOf(String.valueOf(explain(ex)))));
            throw ex;
        } catch (Exception ex) {
            super.fireEvent(new PEvent(PEvent.MESSAGE_SEND_FAIL, this, message));
            setError(String.valueOf(String.valueOf(SEND_ERROR)).concat(
                String.valueOf(String.valueOf(explain(ex)))));
        }
    }

    public String getName() {
        return this.name;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public int getReconnectInterval() {
        return (this.reconnectInterval / 1000);
    }

    public String toString() {
        if (socket == null) {
            return String.valueOf(String.valueOf(new StringBuffer("PSocketConnection:")
                .append(this.name).append("socket(").append(this.socket).append(')')));
        } else {
            return String.valueOf(String.valueOf(new StringBuffer("PSocketConnection:")
                .append(this.name).append('(').append(socket.getLocalAddress()).append(':')
                .append(socket.getLocalPort()).append(')')));
        }
    }

    public int getReadTimeout() {
        return (this.readTimeout / 1000);
    }

    public boolean available() {
        return (this.error == null);
    }

    public String getError() {
        return this.error;
    }

    public Date getErrorTime() {
        return this.errorTime;
    }

    public boolean isClosed() {
        return this.socket == null;
    }

    /**
     * 关闭连接
     * @see net.zoneland.gateway.comm.PLayer#close()
     */
    public synchronized void close() {
        try {
            if (this.socket != null) {
                this.socket.close();
                this.in = null;
                this.out = null;
                this.socket = null;
            }
            if (this.heartbeatThread != null) {
                this.heartbeatThread.kill();
            }
            if (this.receiveThread != null) {
                this.receiveThread.kill();
            }
        } catch (Exception localException) {
        }
        setError(NOT_INIT);
    }

    /**
     * 建立连接
     * 初始从NOT_INIT-->CONNECTING,重连，是从NULL-->RECONNECTING
     */
    protected synchronized void connect() {

        if (this.error == NOT_INIT)
            this.error = CONNECTING;
        else if (this.error == null) {
            this.error = RECONNECTING;
        }
        this.errorTime = new Date();

        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException localIOException1) {

            }
        }
        try {
            if ((this.port <= 0) || (this.port > 65535)) {
                setError(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String
                    .valueOf(PORT_ERROR))).append("port:").append(this.port))));
                return;
            }

            if ((this.localPort < -1) || (this.localPort > 65535)) {
                setError(String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String
                    .valueOf(PORT_ERROR))).append("local-port:").append(this.localPort))));
                return;
            }

            if (this.localHost != null) {
                boolean isConnected = false;
                InetAddress localAddr = InetAddress.getByName(this.localHost);
                if (this.localPort == -1) {
                    label254: for (int p = (int) (Math.random() * 64500); p < 903000; p += 13) {
                        try {
                            this.socket = new Socket(this.host, this.port, localAddr,
                                1025 + p % 64500);
                            isConnected = true;
                            break label254;
                        } catch (IOException localIOException2) {
                            break label254;
                        } catch (SecurityException localSecurityException) {
                        }
                    }
                    if (!(isConnected))
                        throw new SocketException("Can not find an avaliable local port");
                } else {
                    this.socket = new Socket(this.host, this.port, localAddr, this.localPort);
                }
            } else {
                this.socket = new Socket(this.host, this.port);
            }

            this.socket.setSoTimeout(this.readTimeout);

            this.out = getWriter(this.socket.getOutputStream());

            this.in = getReader(this.socket.getInputStream());

            setError(null);
        } catch (IOException ex) {
            setError(String.valueOf(String.valueOf(CONNECT_ERROR)).concat(
                String.valueOf(String.valueOf(explain(ex)))));
        }
    }

    protected void setError(String desc) {
        if (((this.error == null) && (desc == null))
            || ((desc != null) && (desc.equals(this.error)))) {
            return;
        }
        this.error = desc;
        this.errorTime = new Date();

        if (desc == null)
            desc = CONNECTED;
    }

    protected abstract PWriter getWriter(OutputStream paramOutputStream);

    protected abstract PReader getReader(InputStream paramInputStream);

    protected abstract Resource getResource();

    protected void heartbeat() throws IOException {
    }

    public void initResource() {
        NOT_INIT = getDef(this.resource.get("comm/not-init"), "not_init");
        CONNECTING = getDef(this.resource.get("comm/connecting"), "connecting");
        RECONNECTING = getDef(this.resource.get("comm/reconnecting"), "reconnecting");
        CONNECTED = getDef(this.resource.get("comm/connected"), "connected");
        HEARTBEATING = getDef(this.resource.get("comm/heartbeating"), "heartbeating");
        RECEIVEING = getDef(this.resource.get("comm/receiveing"), "receiveing");
        CLOSEING = getDef(this.resource.get("comm/closeing"), "closeing");
        CLOSED = getDef(this.resource.get("comm/closed"), "closed");
        UNKNOWN_HOST = getDef(this.resource.get("comm/unknown-host"), "unknown-host");
        PORT_ERROR = getDef(this.resource.get("comm/port-error"), "port-error");
        CONNECT_REFUSE = getDef(this.resource.get("comm/connect-refused"), "connect-refused");
        NO_ROUTE_TO_HOST = getDef(this.resource.get("comm/no-route"), "no-route");
        RECEIVE_TIMEOUT = getDef(this.resource.get("comm/receive-timeout"), "receive-timeout");
        CLOSE_BY_PEER = getDef(this.resource.get("comm/close-by-peer"), "close-by-peer");
        RESET_BY_PEER = getDef(this.resource.get("comm/reset-by-peer"), "reset-by-peer");
        CONNECTION_CLOSED = getDef(this.resource.get("comm/connection-closed"), "connection-closed");
        COMMUNICATION_ERROR = getDef(this.resource.get("comm/communication-error"),
            "communication-error");
        CONNECT_ERROR = getDef(this.resource.get("comm/connect-error"), "connect-error");
        SEND_ERROR = getDef(this.resource.get("comm/send-error"), "send-error");
        RECEIVE_ERROR = getDef(this.resource.get("comm/receive-error"), "receive-error");
        CLOSE_ERROR = getDef(this.resource.get("comm/close-error"), "close-error");
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
            msg = "[null]";
        }
        if (ex instanceof PException)
            return ex.getMessage();
        if (ex instanceof EOFException)
            return CLOSE_BY_PEER;
        if (msg.indexOf("Connection reset by peer") != -1)
            return RESET_BY_PEER;
        if (msg.indexOf("SocketTimeoutException") != -1)
            return RECEIVE_TIMEOUT;
        if (ex instanceof NoRouteToHostException)
            return NO_ROUTE_TO_HOST;
        if (ex instanceof ConnectException)
            return CONNECT_REFUSE;
        if (ex instanceof UnknownHostException)
            return UNKNOWN_HOST;
        if (msg.indexOf("errno: 128") != -1) {
            return NO_ROUTE_TO_HOST;
        }
        ex.printStackTrace();
        return ex.toString();
    }

    class HeartbeatThread extends WatchThread {
        public HeartbeatThread() {
            super(String.valueOf(String.valueOf(PSocketConnection.this.name)).concat("-heartbeat"));
            //super.setState(PSocketConnection.HEARTBEATING);
        }

        public void task() {
            try {
                Thread.sleep(PSocketConnection.this.heartbeatInterval);
            } catch (InterruptedException localInterruptedException) {
            }
            if ((PSocketConnection.this.error != null) || (PSocketConnection.this.out == null))
                return;
            try {
                PSocketConnection.this.heartbeat();
            } catch (IOException ex) {
                PSocketConnection.this.setError(String.valueOf(
                    String.valueOf(PSocketConnection.SEND_ERROR)).concat(
                    String.valueOf(String.valueOf(PSocketConnection.this.explain(ex)))));
            }
        }
    }

    class ReceiveThread extends WatchThread {
        public ReceiveThread() {
            super(String.valueOf(String.valueOf(PSocketConnection.this.name)).concat("-receive"));
        }

        public void task() {
            try {
                if (PSocketConnection.this.error == null) {
                    PMessage m = PSocketConnection.this.in.read();
                    if ((m == null) || (m == null))
                        return;
                    PSocketConnection.this.onReceive(m);
                    return;
                }

                if (PSocketConnection.this.error != PSocketConnection.NOT_INIT)
                    try {
                        if (PSocketConnection.this.reconnectCount < 10) {
                            Thread.sleep(PSocketConnection.this.reconnectInterval);
                        } else {
                            Thread.sleep(1000 * 60 * 10);
                        }
                    } catch (InterruptedException localInterruptedException) {
                    }
                try {
                    PSocketConnection.this.connect();
                    if (PSocketConnection.this.error != null) {
                        PSocketConnection.this.reconnectCount++;
                    } else {
                        PSocketConnection.this.reconnectCount = 0;
                    }
                } catch (Exception e) {
                    PSocketConnection.this.reconnectCount++;
                }
            } catch (IOException localIOException) {

            }
        }
    }

    public boolean isConnect() {
        if (this.socket == null) {
            return false;
        }
        return this.socket.isConnected();
    }

}
