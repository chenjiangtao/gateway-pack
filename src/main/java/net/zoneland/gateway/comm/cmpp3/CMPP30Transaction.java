package net.zoneland.gateway.comm.cmpp3;

import net.zoneland.gateway.comm.PException;
import net.zoneland.gateway.comm.PLayer;
import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.cmpp.CMPPConstant;
import net.zoneland.gateway.comm.cmpp.message.CMPPMessage;
import net.zoneland.gateway.util.Debug;

public class CMPP30Transaction extends PLayer {

    private CMPPMessage receive;
    private int         sequenceId;

    public CMPP30Transaction(PLayer connection) {
        super(connection);
        sequenceId = super.getId();
    }

    public synchronized void onReceive(PMessage msg) {
        receive = (CMPPMessage) msg;
        sequenceId = receive.getSequenceId();
        if (CMPPConstant.debug) {
            Debug.dump(receive.toString());
        }
        notifyAll();
    }

    public void send(PMessage message) throws PException {
        CMPPMessage mes = (CMPPMessage) message;
        mes.setSequenceId(sequenceId);
        super.getParent().send(message);
        if (CMPPConstant.debug) {
            Debug.dump(mes.toString());
        }
    }

    public CMPPMessage getResponse() {
        return receive;
    }

    public synchronized void waitResponse() {
        if (receive == null) {
            try {
                wait(((CMPP30Connection) super.getParent()).getTransactionTimeout());
            } catch (InterruptedException interruptedexception) {
            }
        }
    }
}
