package net.zoneland.gateway.comm.sgip;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.zoneland.gateway.DefaultMoListener;
import net.zoneland.gateway.MoListener;
import net.zoneland.gateway.comm.sgip.message.SGIPDeliverMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPDeliverRepMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPReportMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPReportRepMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPUserReportMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPUserReportRepMessage;
import net.zoneland.gateway.util.Args;

import org.apache.log4j.Logger;

public class SGIPSMProxy implements SSEventListener {

    private static final Logger             logger      = Logger.getLogger(SGIPSMProxy.class);

    private MoListener                      moListener  = new DefaultMoListener();

    private SGIPConnection                  conn;
    private SSListener                      listener;
    private Args                            args;
    private HashMap<String, SGIPConnection> serconns;

    private static final long               SGIP_NODEID = 3057100000L;

    private long                            node_id     = SGIP_NODEID;

    private int                             corpId      = 0;

    private String                          id;

    Lock                                    connectLock = new ReentrantLock();

    public SGIPSMProxy(Map<String, String> args) {
        this(new Args(args));
    }

    public SGIPSMProxy(String id, Map<String, String> args) {
        this(new Args(args));
        this.id = id;
        this.args.get().put("gatewayId", id);

        corpId = this.args.get("corpId", 62292);
        node_id = SGIP_NODEID + corpId;
        this.args.get().put("node_id", node_id);

    }

    public boolean closeSocket() {
        if (conn != null) {
            conn.closeSocket();
            stopService();
            return true;
        }
        return false;
    }

    public boolean isClosed() {
        if (conn == null || conn.getError() != null) {
            return true;
        }
        return conn.isClosed();
    }

    public SGIPSMProxy(Args args) {
        this.args = args;
        corpId = args.get("corpId", 62292);
        node_id = SGIP_NODEID + corpId;
    }

    public boolean connect(String loginName, String loginPass) {
        connectLock.lock();
        try {
            if (isClosed()) {
                boolean result = true;
                if (loginName != null) {
                    args.set("login-name", loginName.trim());
                }
                if (loginPass != null) {
                    args.set("login-pass", loginPass.trim());
                }
                conn = new SGIPConnection(args, true, null, this);
                conn.addEventListener(new SGIPEventAdapter(this, conn));
                conn.waitAvailable();
                if (!conn.available()) {
                    result = false;
                    throw new IllegalStateException(conn.getError());
                } else {

                    return result;
                }
            }
            return true;
        } finally {
            connectLock.unlock();
        }
    }

    public void startService(String localhost, int localport) {
        connectLock.lock();
        try {
            if (listener != null) {
                return;
            }
            try {
                listener = new SSListener(localhost, localport, this);
                listener.beginListen();
            } catch (Exception exception) {
                exception.printStackTrace();
                close();
                throw new RuntimeException("sgip start service exception.", exception);
            }
        } finally {
            connectLock.unlock();
        }
    }

    public void stopService() {
        connectLock.lock();
        try {
            if (listener == null) {
                return;
            }
            listener.stopListen();

            if (serconns != null) {
                HashMap<String, SGIPConnection> temp = new HashMap<String, SGIPConnection>();
                temp.putAll(serconns);
                SGIPConnection conn;
                for (Iterator<String> iterator = temp.keySet().iterator(); iterator.hasNext(); conn
                    .closeSelf()) {
                    String addr = iterator.next();
                    conn = (SGIPConnection) serconns.get(addr);
                }

                serconns.clear();
                temp.clear();
                temp = null;
            }
        } finally {
            connectLock.unlock();
        }
    }

    public void onConnect(Socket socket) {
        connectLock.lock();
        try {
            String peerIP = socket.getInetAddress().getHostAddress();
            int port = socket.getPort();
            if (serconns == null) {
                serconns = new HashMap<String, SGIPConnection>();
            }
            SGIPConnection conn = new SGIPConnection(args, false, serconns, this);
            conn.addEventListener(new SGIPEventAdapter(this, conn));
            conn.attach(args, socket);
            serconns.put(new String(String.valueOf(peerIP) + String.valueOf(port)), conn);
        } finally {
            connectLock.unlock();
        }
    }

    public SGIPMessage send(SGIPMessage message) throws IOException {
        if (message == null) {
            return null;
        }
        SGIPTransaction t = null;
        if (!conn.isConnect()) {
            throw new RuntimeException("connect was closed!.");
        }
        try {
            t = (SGIPTransaction) conn.createChild();
            t.setSPNumber(node_id);
            Date nowtime = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
            String tmpTime = dateFormat.format(nowtime);
            Integer timestamp = new Integer(tmpTime);
            t.setTimestamp(timestamp.intValue());

            t.send(message);
            t.waitResponse();
            SGIPMessage rsp = t.getResponse();
            SGIPMessage sgipmessage = rsp;
            return sgipmessage;
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

    public SGIPMessage onDeliver(SGIPDeliverMessage msg) {
        moListener.onDeliver(msg, args.get());
        //        SGIPDeliverRepMessage rep = new SGIPDeliverRepMessage(0);
        //        System.out.println(node_id);
        //        rep.setSrcNodeId(node_id);
        //        rep.setSequenceId(msg.getSequenceId());
        //        Date nowtime = new Date();
        //        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
        //        String tmpTime = dateFormat.format(nowtime);
        //        Integer timestamp = new Integer(tmpTime);
        //        rep.setTimeStamp(timestamp.intValue());
        SGIPDeliverRepMessage rep = new SGIPDeliverRepMessage(msg, 0);
        return rep;
    }

    public SGIPMessage onReport(SGIPReportMessage msg) {
        SGIPReportRepMessage rep = new SGIPReportRepMessage(0);
        rep.setSrcNodeId(node_id);
        Date nowtime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
        String tmpTime = dateFormat.format(nowtime);
        Integer timestamp = new Integer(tmpTime);
        rep.setTimeStamp(timestamp.intValue());
        return rep;
    }

    public SGIPMessage onUserReport(SGIPUserReportMessage msg) {
        moListener.onReport(msg);
        SGIPUserReportRepMessage rep = new SGIPUserReportRepMessage(0);
        rep.setSrcNodeId(node_id);
        Date nowtime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
        String tmpTime = dateFormat.format(nowtime);
        Integer timestamp = new Integer(tmpTime);
        rep.setTimeStamp(timestamp.intValue());
        return rep;
    }

    public void close() {
        if (conn != null) {
            conn.close();
        }
    }

    public SGIPConnection getConn() {
        return conn;
    }

    public String getConnState() {
        if (conn != null) {
            return conn.getError();
        } else {
            return "\u5C1A\u672A\u5EFA\u7ACB\u8FDE\u63A5";
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, SGIPConnection> getSerconns() {
        return serconns;
    }

    public void setSerconns(HashMap<String, SGIPConnection> serconns) {
        this.serconns = serconns;
    }

    public void setMoListener(MoListener moListener) {
        this.moListener = moListener;
    }

    public String toString() {
        return new StringBuilder().append("SGIP-gateway-").append(String.valueOf(conn)).toString();
    }

    public void setConn(SGIPConnection conn) {
        this.conn = conn;
    }

    public void setListener(SSListener listener) {
        this.listener = listener;
    }

    public void setArgs(Args args) {
        this.args = args;
    }

    public long getNode_id() {
        return node_id;
    }

    public void setNode_id(long node_id) {
        this.node_id = node_id;
    }

    public int getCorpId() {
        return corpId;
    }

    public void setCorpId(int corpId) {
        this.corpId = corpId;
    }

}
