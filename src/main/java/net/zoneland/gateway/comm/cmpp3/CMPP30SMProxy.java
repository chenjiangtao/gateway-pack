package net.zoneland.gateway.comm.cmpp3;

import java.io.IOException;
import java.util.Map;

import net.zoneland.gateway.DefaultMoListener;
import net.zoneland.gateway.MoListener;
import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPMessage;
import net.zoneland.gateway.comm.cmpp3.message.CMPP30DeliverMessage;
import net.zoneland.gateway.comm.cmpp3.message.CMPP30DeliverRepMessage;
import net.zoneland.gateway.proxy.DeafultGatewayProxy;
import net.zoneland.gateway.util.Args;

public class CMPP30SMProxy extends DeafultGatewayProxy {

    private MoListener          moListener = new DefaultMoListener();

    private CMPP30Connection    conn;

    private String              id;

    private Map<String, String> args;

    public CMPP30SMProxy(Map<String, String> args) {
        this(new Args(args));
    }

    public boolean isClosed() {
        if (conn == null) {
            return true;
        }
        return conn.isClosed();
    }

    public void closeSocket() {
        if (conn != null)
            conn.closeSocket();
    }

    public CMPP30SMProxy(String id, Map<String, String> args) {
        this(new Args(args));
        this.id = id;
        this.args = args;
        this.args.put("gatewayId", id);
    }

    public CMPP30SMProxy(Args args) {
        conn = new CMPP30Connection(args);
        conn.addEventListener(new CMPP30EventAdapter(this));
        conn.waitAvailable();
        if (!conn.available()) {
            throw new IllegalStateException(conn.getError());
        } else {
            return;
        }
    }

    public CMPPMessage send(CMPPMessage message) throws IOException {
        if (message == null) {
            return null;
        }
        if (!conn.isConnect()) {
            throw new RuntimeException("connect was closed!.");
        }
        CMPP30Transaction t = (CMPP30Transaction) conn.createChild();
        try {
            t.send(message);
            t.waitResponse();
            CMPPMessage rsp = t.getResponse();
            CMPPMessage cmppmessage = rsp;
            return cmppmessage;
        } finally {
            t.close();
        }
    }

    public void onTerminate() {
        moListener.OnTerminate();
    }

    public CMPPMessage onDeliver(CMPP30DeliverMessage msg) {
        moListener.onDeliver(msg, args);
        return new CMPP30DeliverRepMessage(msg.getMsgId(), 0);
    }

    public void close() {
        if (conn != null)
            conn.close();
    }

    public CMPP30Connection getConn() {
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
        return new StringBuilder().append("CMPP30-gateway-").append(String.valueOf(conn))
            .toString();
    }
}
