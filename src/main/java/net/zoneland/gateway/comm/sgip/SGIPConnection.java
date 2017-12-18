package net.zoneland.gateway.comm.sgip;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import net.zoneland.gateway.comm.PException;
import net.zoneland.gateway.comm.PLayer;
import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.PReader;
import net.zoneland.gateway.comm.PWriter;
import net.zoneland.gateway.comm.sgip.message.SGIPBindMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPBindRepMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPSubmitMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPUnbindMessage;
import net.zoneland.gateway.util.Args;
import net.zoneland.gateway.util.Resource;

import org.apache.log4j.Logger;

public class SGIPConnection extends SGIPSocketConnection {

    private static final Logger logger = Logger.getLogger(SGIPConnection.class);

    private int                 degree;
    private int                 hbnoResponseOut;
    private String              source_addr;
    private int                 version;
    private String              shared_secret;
    private boolean             asClient;
    private String              login_name;
    private String              login_pass;
    private int                 src_nodeid;
    private String              ipaddr;
    private int                 port;
    private HashMap             connmap;

    private SGIPSMProxy         proxy;

    private Args                args;

    public SGIPConnection(Args args, boolean ifasClient, HashMap connmap, SGIPSMProxy proxy) {
        this.args = args;
        degree = 0;
        hbnoResponseOut = 3;
        source_addr = null;
        hbnoResponseOut = args.get("heartbeat-noresponseout", 3);
        source_addr = args.get("source-addr", "huawei");
        version = args.get("version", 1);
        shared_secret = args.get("shared-secret", "");
        SGIPConstant.debug = args.get("debug", false);
        login_name = args.get("login-name", "");
        login_pass = args.get("login-pass", "");
        src_nodeid = args.get("node_id", 0);
        this.connmap = connmap;
        SGIPConstant.initConstant(getResource());
        this.proxy = proxy;
        asClient = ifasClient;
        if (asClient) {
            init(args);
        }
    }

    public synchronized void attach(Args args, Socket socket) {
        if (asClient) {
            throw new UnsupportedOperationException("Client socket can not accept connection");
        } else {
            init(args, socket);
            ipaddr = socket.getInetAddress().getHostAddress();
            port = socket.getPort();
            return;
        }
    }

    protected void onReadTimeOut() {
        closeSelf();
    }

    protected PWriter getWriter(OutputStream out) {
        return new SGIPWriter(out);
    }

    protected PReader getReader(InputStream in) {
        return new SGIPReader(in);
    }

    public int getChildId(PMessage message) {
        SGIPMessage mes = (SGIPMessage) message;
        int sequenceId = mes.getSequenceId();
        if (!asClient)
            ;
        if (mes.getCommandId() == 4 || mes.getCommandId() == 5 || mes.getCommandId() == 17
            || mes.getCommandId() == 1 || mes.getCommandId() == 2) {
            return -1;
        } else {
            return sequenceId;
        }
    }

    public PLayer createChild() {
        return new SGIPTransaction(this);
    }

    public int getTransactionTimeout() {
        return super.transactionTimeout;
    }

    public Resource getResource() {
        try {
            Resource resource = new Resource(getClass(), "resource");
            return resource;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Resource resource1 = null;
        return resource1;
    }

    public synchronized void waitAvailable() {
        try {
            if (getError() == SGIPSocketConnection.NOT_INIT) {
                wait(super.transactionTimeout);
            }
        } catch (InterruptedException interruptedexception) {
        }
    }

    public void close() {
        SGIPTransaction t = (SGIPTransaction) createChild();
        t.setSPNumber(src_nodeid);
        Date nowtime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
        String tmpTime = dateFormat.format(nowtime);
        Integer timestamp = new Integer(tmpTime);
        t.setTimestamp(timestamp.intValue());
        try {
            SGIPUnbindMessage msg = new SGIPUnbindMessage();
            t.send(msg);
            t.waitResponse();
            SGIPMessage sgipmessage = t.getResponse();
        } catch (Exception pexception) {
        } finally {
            t.close();
        }
        super.close();
        if (this.proxy != null) {
            this.proxy.stopService();
        }
        if (!asClient && connmap != null) {
            connmap.remove(new String(String.valueOf(ipaddr) + String.valueOf(port)));
        }
    }

    public void closeSelf() {
        SGIPTransaction t = (SGIPTransaction) createChild();
        t.setSPNumber(src_nodeid);
        Date nowtime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
        String tmpTime = dateFormat.format(nowtime);
        Integer timestamp = new Integer(tmpTime);
        t.setTimestamp(timestamp.intValue());
        try {
            SGIPUnbindMessage msg = new SGIPUnbindMessage();
            t.send(msg);
            t.waitResponse();
            SGIPMessage sgipmessage = t.getResponse();
        } catch (PException pexception) {
        } finally {
            t.close();
        }
        super.close();

        if (!asClient && connmap != null) {
            connmap.remove(new String(String.valueOf(ipaddr) + String.valueOf(port)));
        }
    }

    public void closeSocket() {
        super.close();
    }

    protected void heartbeat() throws IOException {
        SGIPTransaction t = (SGIPTransaction) createChild();
        t.setSPNumber(src_nodeid);
        Date nowtime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
        String tmpTime = dateFormat.format(nowtime);
        Integer timestamp = new Integer(tmpTime);
        t.setTimestamp(timestamp.intValue());
        try {
            SGIPSubmitMessage msg = new SGIPSubmitMessage(args.get("spNumber", "95598"),
                "000000000000000000000", new String[] { "861555813" }, args.get("corpId", "62292"),
                args.get("serviceType", "9999"), args.get("feeType", 0), args.get("feeValue",
                    "000005"), args.get("givenValue", "0"), args.get("agentFlag", 1), args.get(
                    "morelatetoMTFlag", 2), 3, null, null, 0, 0, 0, 8, 0, 0,
                PMessage.getMsgContent("heartbeat", 8), "");
            t.send(msg);
            t.waitResponse();
            SGIPMessage rsp = t.getResponse();
            if (rsp == null) {
                degree++;
                if (degree == hbnoResponseOut) {
                    logger.error("active test no response ,,,reconnect...");
                    degree = 0;
                    throw new IOException(SGIPConstant.HEARTBEAT_ABNORMITY);
                }
            } else {
                degree = 0;
            }
        } catch (PException pexception) {
        } finally {
            t.close();
        }
    }

    protected synchronized void connect() {
        super.connect();
        if (!available()) {
            return;
        }
        SGIPBindMessage request = null;
        SGIPBindRepMessage rsp = null;
        try {
            request = new SGIPBindMessage(1, login_name, login_pass);
        } catch (IllegalArgumentException e) {
            logger.error("登录异常", e);
            close();
            setError(SGIPConstant.CONNECT_INPUT_ERROR);
        }
        SGIPTransaction t = (SGIPTransaction) createChild();
        try {
            t.send(request);
            PMessage m = super.in.read();
            onReceive(m);
        } catch (IOException e) {
            logger.error("login error", e);
            close();
            setError(String.valueOf(SGIPConstant.LOGIN_ERROR) + String.valueOf(explain(e)));
        }
        rsp = (SGIPBindRepMessage) t.getResponse();
        if (rsp == null) {
            close();
            setError(SGIPConstant.CONNECT_TIMEOUT);
        }
        t.close();
        if (rsp != null && rsp.getResult() != 0) {
            close();
            if (rsp.getResult() == 1) {
                setError(SGIPConstant.NONLICETSP_LOGNAME);
            } else {
                setError(SGIPConstant.OTHER_ERROR);
            }
        }
        notifyAll();
    }

    public String toString() {
        return super.toString();
    }
}
