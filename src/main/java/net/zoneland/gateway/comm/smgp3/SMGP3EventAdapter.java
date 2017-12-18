package net.zoneland.gateway.comm.smgp3;

import net.zoneland.gateway.comm.PEventAdapter;
import net.zoneland.gateway.comm.PException;
import net.zoneland.gateway.comm.PLayer;
import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.smgp3.message.RequestId;
import net.zoneland.gateway.comm.smgp3.message.SMGP3ActiveTestRespMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3DeliverMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3ExitRespMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3Message;

import org.apache.log4j.Logger;

class SMGP3EventAdapter extends PEventAdapter {

    private static final Logger logger = Logger.getLogger(SMGP3EventAdapter.class);

    private SMGP3SMProxy        smProxy;
    private SMGPConnection      conn;

    public SMGP3EventAdapter(SMGP3SMProxy smProxy) {
        this.smProxy = null;
        conn = null;
        this.smProxy = smProxy;
        conn = smProxy.getConn();
    }

    public void childCreated(PLayer child) {
        SMGPTransaction t = (SMGPTransaction) child;
        SMGP3Message msg = t.getResponse();
        PMessage resmsg = null;
        if (msg.getRequestId() == RequestId.EXIT_RESP) {
            resmsg = new SMGP3ExitRespMessage();
            smProxy.onTerminate();
        } else if (msg.getRequestId() == RequestId.ACTIVETEST_RESP) {
            resmsg = new SMGP3ActiveTestRespMessage();
        } else if (msg.getRequestId() == RequestId.DELIVER) {
            SMGP3DeliverMessage tmpmes = (SMGP3DeliverMessage) msg;
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
        if (msg.getRequestId() == RequestId.EXIT_RESP) {
            logger.warn("服务端请求关闭");
            conn.close();
        }
    }
}
