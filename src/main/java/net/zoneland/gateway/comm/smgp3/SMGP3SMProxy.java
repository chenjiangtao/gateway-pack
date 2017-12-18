package net.zoneland.gateway.comm.smgp3;

import java.io.IOException;
import java.util.Map;

import net.zoneland.gateway.DefaultMoListener;
import net.zoneland.gateway.MoListener;
import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3DeliverMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3DeliverRespMessage;
import net.zoneland.gateway.proxy.DeafultGatewayProxy;
import net.zoneland.gateway.util.Args;

import org.apache.log4j.Logger;

public class SMGP3SMProxy extends DeafultGatewayProxy {

    private MoListener          moListener = new DefaultMoListener();

    private Logger              logger     = Logger.getLogger(SMGP3SMProxy.class);

    private SMGPConnection      conn;

    private String              id;

    private Map<String, String> args;

    public SMGP3SMProxy(Map<String, String> args) {
        this(new Args(args));
    }

    public void closeSocket() {
        if (conn != null)
            conn.closeSocket();
    }

    public SMGP3SMProxy(String id, Map<String, String> args) {
        this(new Args(args));
        this.id = id;
        this.args = args;
        this.args.put("gatewayId", id);
    }

    public SMGP3SMProxy(Args args) {
        conn = new SMGPConnection(args);
        conn.addEventListener(new SMGP3EventAdapter(this));
        conn.waitAvailable();
        if (!conn.available()) {
            logger.error("Connection Init Error,Message=" + conn.getError());
            throw new IllegalStateException(conn.getError());
        } else {
            logger.info("Connection Init Successful!");
            return;
        }
    }

    public boolean isClosed() {
        if (conn == null) {
            return true;
        }
        return conn.isClosed();
    }

    public PMessage send(PMessage message) throws IOException {
        if (message == null) {
            return null;
        }
        if (!conn.isConnect()) {
            throw new RuntimeException("connect was closed!.");
        }
        SMGPTransaction t = null;
        try {
            t = (SMGPTransaction) conn.createChild();

            t.send(message);
            t.waitResponse();
            PMessage rsp = t.getResponse();
            PMessage smgpmessage = rsp;
            return smgpmessage;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (t != null) {
                t.close();
            }
        }
        return null;
    }

    public void onTerminate() {
        moListener.OnTerminate();

    }

    public PMessage onDeliver(SMGP3DeliverMessage msg) {
        moListener.onDeliver(msg, args);
        return new SMGP3DeliverRespMessage(msg.getMsgID_BCD(), 0);
    }

    public void close() {
        if (conn != null)
            conn.close();
    }

    public SMGPConnection getConn() {
        return conn;
    }

    public String getConnState() {
        return conn.getError();
    }

    /** 
     * @see net.zoneland.gateway.proxy.GatewayProxy#onReport(net.zoneland.gateway.comm.PMessage)
     */
    public PMessage onReport(PMessage msg) {
        return moListener.onReport(msg);
    }

    public void setMoListener(MoListener moListener) {
        this.moListener = moListener;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString() {
        return new StringBuilder().append("SMGP3-gateway-").append(String.valueOf(conn)).toString();
    }
}
