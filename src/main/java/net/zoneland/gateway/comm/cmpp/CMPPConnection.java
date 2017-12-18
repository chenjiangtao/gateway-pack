package net.zoneland.gateway.comm.cmpp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import net.zoneland.gateway.comm.PException;
import net.zoneland.gateway.comm.PLayer;
import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.PReader;
import net.zoneland.gateway.comm.PSocketConnection;
import net.zoneland.gateway.comm.PWriter;
import net.zoneland.gateway.comm.cmpp.message.CMPPActiveMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPActiveRepMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPConnectMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPConnectRepMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPTerminateMessage;
import net.zoneland.gateway.util.Args;
import net.zoneland.gateway.util.Resource;

import org.apache.log4j.Logger;

public class CMPPConnection extends PSocketConnection {

    private static final Logger logger = Logger.getLogger(CMPPConnection.class);

    private int                 degree;
    private int                 hbnoResponseOut;
    private String              source_addr;
    private int                 version;
    private String              shared_secret;

    public CMPPConnection(Args args) {
        degree = 0;
        hbnoResponseOut = 3;
        source_addr = null;
        hbnoResponseOut = args.get("heartbeat-noresponseout", 3);
        source_addr = args.get("source-addr", "huawei");
        version = args.get("version", 1);
        shared_secret = args.get("shared-secret", "");
        CMPPConstant.debug = args.get("debug", false);
        CMPPConstant.initConstant(getResource());
        init(args);
    }

    protected PWriter getWriter(OutputStream out) {
        return new CMPPWriter(out);
    }

    protected PReader getReader(InputStream in) {
        return new CMPPReader(in);
    }

    public int getChildId(PMessage message) {
        CMPPMessage mes = (CMPPMessage) message;
        int sequenceId = mes.getSequenceId();
        if (mes.getCommandId() == 5 || mes.getCommandId() == 8 || mes.getCommandId() == 2) {
            return -1;
        } else {
            return sequenceId;
        }
    }

    public PLayer createChild() {
        return new CMPPTransaction(this);
    }

    public int getTransactionTimeout() {
        return super.getTransactionTimeout();
    }

    public Resource getResource() {
        try {
            Resource resource = new Resource(getClass(), "resource");
            return resource;
        } catch (IOException e) {
            logger.error("读取资源错误", e);
        }
        Resource resource1 = null;
        return resource1;
    }

    public synchronized void waitAvailable() {
        try {
            if (getError() == PSocketConnection.NOT_INIT) {
                wait(super.getTransactionTimeout());
            }
        } catch (InterruptedException interruptedexception) {
        }
    }

    public void close() {
        try {
            CMPPTerminateMessage msg = new CMPPTerminateMessage();
            send(msg);
        } catch (PException pexception) {
        }
        super.close();
    }

    public void closeSocket() {
        super.close();
    }

    protected void heartbeat() throws IOException {
        CMPPTransaction t = (CMPPTransaction) createChild();
        CMPPActiveMessage hbmes = new CMPPActiveMessage();
        t.send(hbmes);
        t.waitResponse();
        CMPPActiveRepMessage rsp = (CMPPActiveRepMessage) t.getResponse();
        if (rsp == null) {
            degree++;
            if (degree == hbnoResponseOut) {
                logger.error("active test no response ,,,reconnect...");
                degree = 0;
                throw new IOException(CMPPConstant.HEARTBEAT_ABNORMITY);
            }
        } else {
            degree = 0;
        }
        t.close();
    }

    protected synchronized void connect() {
        super.connect();
        if (!available()) {
            return;
        }
        CMPPConnectMessage request = null;
        CMPPConnectRepMessage rsp = null;
        try {
            request = new CMPPConnectMessage(source_addr, version, shared_secret, new Date());
        } catch (IllegalArgumentException e) {
            logger.error("连接服务端错误", e);
            close();
            setError(CMPPConstant.CONNECT_INPUT_ERROR);
        }
        CMPPTransaction t = (CMPPTransaction) createChild();
        try {
            t.send(request);
            PMessage m = super.getIn().read();
            onReceive(m);
        } catch (IOException e) {
            e.printStackTrace();
            close();
            setError(String.valueOf(CMPPConstant.LOGIN_ERROR) + String.valueOf(explain(e)));
        }
        rsp = (CMPPConnectRepMessage) t.getResponse();
        if (rsp == null) {
            close();
            setError(CMPPConstant.CONNECT_TIMEOUT);
        }
        t.close();
        if (rsp != null && rsp.getStatus() != 0) {
            close();
            if (rsp.getStatus() == 1) {
                setError(CMPPConstant.STRUCTURE_ERROR);
            } else if (rsp.getStatus() == 2) {
                setError(CMPPConstant.NONLICETSP_ID);
            } else if (rsp.getStatus() == 3) {
                setError(CMPPConstant.SP_ERROR);
            } else if (rsp.getStatus() == 4) {
                setError(CMPPConstant.VERSION_ERROR);
            } else {
                setError(CMPPConstant.OTHER_ERROR);
            }
        }
        notifyAll();
    }

    public String toString() {
        return super.toString();
    }
}
