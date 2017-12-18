package net.zoneland.gateway.comm.sgip;

import net.zoneland.gateway.comm.PEventAdapter;
import net.zoneland.gateway.comm.PLayer;
import net.zoneland.gateway.comm.sgip.message.SGIPBindMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPBindRepMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPDeliverMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPReportMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPUnbindRepMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPUserReportMessage;

import org.apache.log4j.Logger;

class SGIPEventAdapter extends PEventAdapter {

    private static final Logger logger = Logger.getLogger(SGIPEventAdapter.class);

    private SGIPSMProxy         smProxy;
    private SGIPConnection      conn;

    public SGIPEventAdapter(SGIPSMProxy smProxy, SGIPConnection conn) {
        this.smProxy = null;
        this.conn = null;
        this.smProxy = smProxy;
        this.conn = conn;
    }

    public void childCreated(PLayer child) {
        SGIPTransaction t = (SGIPTransaction) child;
        SGIPMessage msg = t.getResponse();
        SGIPMessage resmsg = null;
        if (msg.getCommandId() == 2) {
            resmsg = new SGIPUnbindRepMessage();
            if (t.isChildOf(conn)) {
                smProxy.onTerminate();
            }
        } else if (msg.getCommandId() == 1) {
            SGIPBindMessage tmpmes = (SGIPBindMessage) msg;
            int logintype = tmpmes.getLoginType();
            if (logintype != 2 && logintype != 11) {
                resmsg = new SGIPBindRepMessage(4);
            }
            resmsg = new SGIPBindRepMessage(0);
        } else if (msg.getCommandId() == 4) {
            SGIPDeliverMessage tmpmes = (SGIPDeliverMessage) msg;
            resmsg = smProxy.onDeliver(tmpmes);
        } else if (msg.getCommandId() == 5) {
            SGIPReportMessage tmpmes = (SGIPReportMessage) msg;
            resmsg = smProxy.onReport(tmpmes);
        } else if (msg.getCommandId() == 17) {
            SGIPUserReportMessage tmpmes = (SGIPUserReportMessage) msg;
            resmsg = smProxy.onUserReport(tmpmes);
        } else {
            t.close();
        }
        if (resmsg != null) {
            try {

                t.send(resmsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            t.close();
        }
        if (msg.getCommandId() == 2) {
            SGIPConnection theconn = (SGIPConnection) t.getParent();
            logger.warn("服务端请求关闭");
            theconn.closeSelf();
        }
    }
}
