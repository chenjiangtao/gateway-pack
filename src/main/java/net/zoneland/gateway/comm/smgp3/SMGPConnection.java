package net.zoneland.gateway.comm.smgp3;

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
import net.zoneland.gateway.comm.smgp3.message.SMGP3ActiveTestMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3ActiveTestRespMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3ExitMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3LoginMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3LoginRespMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3Message;
import net.zoneland.gateway.util.Args;
import net.zoneland.gateway.util.Resource;

import org.apache.log4j.Logger;

public class SMGPConnection extends PSocketConnection {

    private static final Logger logger = Logger.getLogger(SMGPConnection.class);

    private int                 degree;
    private int                 hbnoResponseOut;
    private String              sourceAddr;
    private int                 version;
    private String              shared_secret;

    public SMGPConnection(Args args) {
        degree = 0;
        hbnoResponseOut = 3;
        sourceAddr = null;
        hbnoResponseOut = args.get("heartbeat-noresponseout", 3);
        sourceAddr = args.get("source-addr", "huawei");
        version = args.get("version", 1);
        shared_secret = args.get("shared-secret", "");
        SMGPConstant.debug = args.get("debug", false);
        SMGPConstant.initConstant(getResource());
        init(args);
    }

    protected PWriter getWriter(OutputStream out) {
        return new SMGPWriter(out);
    }

    protected PReader getReader(InputStream in) {
        return new SMGPReader(in);
    }

    public int getChildId(PMessage message) {
        SMGP3Message mes = (SMGP3Message) message;
        int sequenceId = mes.getSequenceId();
        if (mes.getRequestId() == 3 || mes.getRequestId() == 4 || mes.getRequestId() == 6) {
            return -1;
        } else {
            return sequenceId;
        }
    }

    public PLayer createChild() {
        return new SMGPTransaction(this);
    }

    public int getTransactionTimeout() {
        return super.getTransactionTimeout();
    }

    public Resource getResource() {
        try {
            Resource resource = new Resource(getClass(), "resource");
            return resource;
        } catch (IOException e) {
            logger.error("", e);
        }
        Resource resource1 = null;
        return resource1;
    }

    public synchronized void waitAvailable() {
        try {
            if (getError() == PSocketConnection.NOT_INIT) {
                //Debug.dump(super.transactionTimeout);
                wait(super.getTransactionTimeout());
            }
        } catch (InterruptedException interruptedexception) {
        }
    }

    public void close() {
        try {
            SMGP3ExitMessage msg = new SMGP3ExitMessage();
            send(msg);
        } catch (PException pexception) {
        }
        super.close();
    }

    public void closeSocket() {
        super.close();
    }

    protected void heartbeat() throws IOException {
        SMGPTransaction t = (SMGPTransaction) createChild();
        SMGP3ActiveTestMessage hbmes = new SMGP3ActiveTestMessage();
        t.send(hbmes);
        t.waitResponse();
        SMGP3ActiveTestRespMessage rsp = (SMGP3ActiveTestRespMessage) t.getResponse();
        if (rsp == null) {
            degree++;
            if (degree == hbnoResponseOut) {
                logger.error("active test no response ,,,reconnect...");
                degree = 0;
                throw new IOException(SMGPConstant.HEARTBEAT_ABNORMITY);
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
        SMGP3LoginMessage request = null;
        SMGP3LoginRespMessage rsp = null;
        try {
            request = new SMGP3LoginMessage(sourceAddr, shared_secret, 2, new Date(), version);
        } catch (IllegalArgumentException e) {
            logger.error("", e);
            close();
            setError(SMGPConstant.CONNECT_INPUT_ERROR);
        }
        SMGPTransaction t = (SMGPTransaction) createChild();
        try {
            t.send(request);
            PMessage m = super.getIn().read();
            onReceive(m);
        } catch (IOException e) {
            logger.error("", e);
            close();
            setError(String.valueOf(SMGPConstant.LOGIN_ERROR) + String.valueOf(explain(e)));
        }
        rsp = (SMGP3LoginRespMessage) t.getResponse();
        if (rsp == null) {
            close();
            setError(SMGPConstant.CONNECT_TIMEOUT);
        }
        t.close();
        if (rsp != null && rsp.getStatus() != 0) {
            close();
            setError("Fail to login,the status code id ".concat(String.valueOf(String.valueOf(rsp
                .getStatus()))));
        }
        notifyAll();
    }

    public String toString() {
        return super.toString();
    }
}
