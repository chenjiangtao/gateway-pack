package net.zoneland.gateway.comm.cmpp3;

import net.zoneland.gateway.comm.PEventAdapter;
import net.zoneland.gateway.comm.PException;
import net.zoneland.gateway.comm.PLayer;
import net.zoneland.gateway.comm.cmpp.message.CMPPActiveRepMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPTerminateRepMessage;
import net.zoneland.gateway.comm.cmpp3.message.CMPP30DeliverMessage;

import org.apache.log4j.Logger;

class CMPP30EventAdapter extends PEventAdapter {

    private static final Logger logger = Logger.getLogger(CMPP30EventAdapter.class);

    private CMPP30SMProxy       smProxy;
    private CMPP30Connection    conn;

    public CMPP30EventAdapter(CMPP30SMProxy smProxy) {
        this.smProxy = null;
        conn = null;
        this.smProxy = smProxy;
        conn = smProxy.getConn();
    }

    public void childCreated(PLayer child) {
        CMPP30Transaction t = (CMPP30Transaction) child;
        CMPPMessage msg = t.getResponse();
        CMPPMessage resmsg = null;
        if (msg.getCommandId() == 2) {
            resmsg = new CMPPTerminateRepMessage();
            smProxy.onTerminate();
        } else if (msg.getCommandId() == 8) {
            resmsg = new CMPPActiveRepMessage(0);
        } else if (msg.getCommandId() == 5) {
            CMPP30DeliverMessage tmpmes = (CMPP30DeliverMessage) msg;
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
