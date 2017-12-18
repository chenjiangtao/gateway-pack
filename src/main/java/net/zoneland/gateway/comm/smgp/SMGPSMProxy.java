package net.zoneland.gateway.comm.smgp;

import java.io.IOException;
import java.util.Map;

import net.zoneland.gateway.DefaultMoListener;
import net.zoneland.gateway.MoListener;
import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.smgp.message.SMGPDeliverMessage;
import net.zoneland.gateway.comm.smgp.message.SMGPDeliverRespMessage;
import net.zoneland.gateway.comm.smgp.message.SMGPMessage;
import net.zoneland.gateway.proxy.DeafultGatewayProxy;
import net.zoneland.gateway.util.Args;

public class SMGPSMProxy extends DeafultGatewayProxy {

    private MoListener          moListener = new DefaultMoListener();

    private SMGPConnection      conn;

    private String              id;

    private Map<String, String> args;

    public SMGPSMProxy(Map<String, String> args) {
        this(new Args(args));
    }

    public SMGPSMProxy(String id, Map<String, String> args) {
        this(new Args(args));
        this.id = id;
        this.args = args;
        this.args.put("gatewayId", id);
    }

    public void closeSocket() {
        if (conn != null)
            conn.closeSocket();
    }

    public SMGPSMProxy(Args args) {
        conn = new SMGPConnection(args);
        conn.addEventListener(new SMGPEventAdapter(this));
        conn.waitAvailable();
        if (!conn.available()) {
            throw new IllegalStateException(conn.getError());
        } else {
            return;
        }
    }

    public boolean isClosed() {
        if (conn == null) {
            return true;
        }
        return conn.isClosed();
    }

    public SMGPMessage send(PMessage message) throws IOException {
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
            SMGPMessage rsp = t.getResponse();
            SMGPMessage smgpmessage = rsp;
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

    public SMGPMessage onDeliver(SMGPDeliverMessage msg) {
        moListener.onDeliver(msg, args);
        return new SMGPDeliverRespMessage(msg.getMsgId(), 0);
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String toString() {
        return new StringBuilder().append("SMGP-gateway-").append(String.valueOf(conn)).toString();
    }
}
