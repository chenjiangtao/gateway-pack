package net.zoneland.gateway.comm.smgp;

import net.zoneland.gateway.comm.PEventAdapter;
import net.zoneland.gateway.comm.PException;
import net.zoneland.gateway.comm.PLayer;
import net.zoneland.gateway.comm.smgp.message.SMGPActiveTestRespMessage;
import net.zoneland.gateway.comm.smgp.message.SMGPDeliverMessage;
import net.zoneland.gateway.comm.smgp.message.SMGPExitRespMessage;
import net.zoneland.gateway.comm.smgp.message.SMGPMessage;

import org.apache.log4j.Logger;

class SMGPEventAdapter extends PEventAdapter {

    private static final Logger logger = Logger.getLogger(SMGPEventAdapter.class);

    private SMGPSMProxy         smProxy;
    private SMGPConnection      conn;

    public SMGPEventAdapter(SMGPSMProxy smProxy) {
        this.smProxy = null;
        conn = null;
        this.smProxy = smProxy;
        conn = smProxy.getConn();
    }

    public void childCreated(PLayer child) {
        SMGPTransaction t = (SMGPTransaction) child;
        SMGPMessage msg = t.getResponse();
        SMGPMessage resmsg = null;
        if (msg.getRequestId() == 6) {
            resmsg = new SMGPExitRespMessage();
            smProxy.onTerminate();
        } else if (msg.getRequestId() == 4) {
            resmsg = new SMGPActiveTestRespMessage();
        } else if (msg.getRequestId() == 3) {
            SMGPDeliverMessage tmpmes = (SMGPDeliverMessage) msg;
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
        if (msg.getRequestId() == 6) {
            logger.warn("服务端请求关闭");
            conn.close();
        }
    }
}
