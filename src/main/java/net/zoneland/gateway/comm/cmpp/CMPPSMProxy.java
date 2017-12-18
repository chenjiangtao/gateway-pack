package net.zoneland.gateway.comm.cmpp;

import java.io.IOException;
import java.util.Map;

import net.zoneland.gateway.DefaultMoListener;
import net.zoneland.gateway.MoListener;
import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPDeliverMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPDeliverRepMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPMessage;
import net.zoneland.gateway.proxy.DeafultGatewayProxy;
import net.zoneland.gateway.util.Args;

public class CMPPSMProxy extends DeafultGatewayProxy {

    private MoListener          moListener = new DefaultMoListener();

    private CMPPConnection      conn;

    private String              id;

    private Map<String, String> args;

    public CMPPSMProxy(Map<String, String> args) {
        this(new Args(args));
    }

    public CMPPSMProxy(String id, Map<String, String> args) {
        this(new Args(args));
        this.id = id;
        this.args = args;
        this.args.put("gatewayId", id);
    }

    public boolean isClosed() {
        if (conn == null) {
            return true;
        }
        return conn.isClosed();
    }

    public CMPPSMProxy(Args args) {
        conn = new CMPPConnection(args);
        conn.addEventListener(new CMPPEventAdapter(this));
        conn.waitAvailable();
        if (!conn.available()) {
            throw new IllegalStateException(conn.getError());
        } else {
            return;
        }
    }

    public void closeSocket() {
        if (conn != null)
            conn.closeSocket();
    }

    public CMPPMessage send(PMessage message) throws IOException {
        if (message == null) {
            return null;
        }
        if (!conn.isConnect()) {
            throw new RuntimeException("cmpp connect was closed!.");
        }
        CMPPTransaction t = null;
        try {
            t = (CMPPTransaction) conn.createChild();

            t.send(message);
            t.waitResponse();
            CMPPMessage rsp = t.getResponse();
            CMPPMessage cmppmessage = rsp;
            return cmppmessage;
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

    public CMPPMessage onDeliver(CMPPDeliverMessage msg) {
        moListener.onDeliver(msg, args);
        return new CMPPDeliverRepMessage(msg.getMsgId(), 0);
    }

    public void close() {
        if (conn != null)
            conn.close();
    }

    public CMPPConnection getConn() {
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
        return new StringBuilder().append("CMPP-gateway-").append(String.valueOf(conn)).toString();
    }

}
