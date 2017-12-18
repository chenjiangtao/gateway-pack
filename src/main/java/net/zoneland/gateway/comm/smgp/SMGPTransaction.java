package net.zoneland.gateway.comm.smgp;

import net.zoneland.gateway.comm.PException;
import net.zoneland.gateway.comm.PLayer;
import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.smgp.message.SMGPMessage;
import net.zoneland.gateway.util.Debug;

public class SMGPTransaction extends PLayer {

    private SMGPMessage receive;
    private int         sequenceId;

    public SMGPTransaction(PLayer connection) {
        super(connection);
        sequenceId = super.getId();
    }

    public synchronized void onReceive(PMessage msg) {
        receive = (SMGPMessage) msg;
        sequenceId = receive.getSequenceId();
        if (SMGPConstant.debug) {
            Debug.dump(receive.toString());
        }
        notifyAll();
    }

    public void send(PMessage message) throws PException {
        SMGPMessage mes = (SMGPMessage) message;
        mes.setSequenceId(sequenceId);
        super.getParent().send(message);
        if (SMGPConstant.debug) {
            Debug.dump(mes.toString());
        }
    }

    public SMGPMessage getResponse() {
        return receive;
    }

    public synchronized void waitResponse() {
        if (receive == null) {
            try {
                wait(((SMGPConnection) super.getParent()).getTransactionTimeout());
            } catch (InterruptedException interruptedexception) {
            }
        }
    }
}
