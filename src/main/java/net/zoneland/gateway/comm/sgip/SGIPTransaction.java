package net.zoneland.gateway.comm.sgip;

import net.zoneland.gateway.comm.PException;
import net.zoneland.gateway.comm.PLayer;
import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPMessage;
import net.zoneland.gateway.util.Debug;

public class SGIPTransaction extends PLayer {

    private SGIPMessage receive;
    private long        src_nodeid;
    private int         timestamp;
    private int         sequenceId;

    public SGIPTransaction(PLayer connection) {
        super(connection);
        sequenceId = super.getId();
    }

    public void setSPNumber(long spNumber) {
        src_nodeid = spNumber;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public synchronized void onReceive(PMessage msg) {
        receive = (SGIPMessage) msg;
        src_nodeid = receive.getSrcNodeId();
        timestamp = receive.getTimeStamp();
        sequenceId = receive.getSequenceId();
        if (SGIPConstant.debug) {
            Debug.dump(receive.toString());
        }
        notifyAll();
    }

    public void send(PMessage message) throws PException {
        SGIPMessage mes = (SGIPMessage) message;
        mes.setSrcNodeId(src_nodeid);
        mes.setTimeStamp(timestamp);
        mes.setSequenceId(sequenceId);
        super.getParent().send(message);
        if (SGIPConstant.debug) {
            Debug.dump(mes.toString());
        }
    }

    public SGIPMessage getResponse() {
        return receive;
    }

    public boolean isChildOf(PLayer connection) {
        if (super.getParent() == null) {
            return false;
        } else {
            return connection == super.getParent();
        }
    }

    public PLayer getParent() {
        return super.getParent();
    }

    public synchronized void waitResponse() {
        if (receive == null) {
            try {
                wait(((SGIPConnection) super.getParent()).getTransactionTimeout());
            } catch (InterruptedException interruptedexception) {
            }
        }
    }
}
