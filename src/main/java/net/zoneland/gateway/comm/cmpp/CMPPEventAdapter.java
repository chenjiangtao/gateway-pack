package net.zoneland.gateway.comm.cmpp;

import net.zoneland.gateway.comm.PEventAdapter;
import net.zoneland.gateway.comm.PException;
import net.zoneland.gateway.comm.PLayer;
import net.zoneland.gateway.comm.cmpp.message.CMPPActiveRepMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPDeliverMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPTerminateRepMessage;

import org.apache.log4j.Logger;

class CMPPEventAdapter extends PEventAdapter {

    private static final Logger logger = Logger.getLogger(CMPPEventAdapter.class);

    private CMPPSMProxy         smProxy;
    private CMPPConnection      conn;

    public CMPPEventAdapter(CMPPSMProxy smProxy) {
        this.smProxy = null;
        conn = null;
        this.smProxy = smProxy;
        conn = smProxy.getConn();
    }

    public void childCreated(PLayer child) {
        CMPPTransaction t = (CMPPTransaction) child;
        CMPPMessage msg = t.getResponse();
        CMPPMessage resmsg = null;
        if (msg.getCommandId() == 2) {
            resmsg = new CMPPTerminateRepMessage();
            smProxy.onTerminate();
        } else if (msg.getCommandId() == 8) {
            resmsg = new CMPPActiveRepMessage(0);
        } else if (msg.getCommandId() == 5) {
            CMPPDeliverMessage tmpmes = (CMPPDeliverMessage) msg;
            resmsg = smProxy.onDeliver(tmpmes);
        } else {
            t.close();
        }
        if (resmsg != null) {
            try {
                t.send(resmsg);
            } catch (PException e) {
                e.printStackTrace();
            }
            t.close();
        }
        if (msg.getCommandId() == 2) {
            logger.warn("服务端请求关闭");
            conn.close();
        }
    }
}
