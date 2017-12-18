package net.zoneland.gateway.comm.smgp3;

import net.zoneland.gateway.comm.PException;
import net.zoneland.gateway.comm.PLayer;
import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3Message;
import net.zoneland.gateway.util.Debug;

public class SMGPTransaction extends PLayer
{

    private SMGP3Message receive;
    private int sequenceId;

    public SMGPTransaction(PLayer connection)
    {
        super(connection);
        sequenceId = super.getId();
    }

    public synchronized void onReceive(PMessage msg)
    {
        receive = (SMGP3Message)msg;
        sequenceId = receive.getSequenceId();
        if(SMGPConstant.debug)
        {
            Debug.dump(receive.toString());
        }
        notifyAll();
    }

    public void send(PMessage message)
        throws PException
    {
        SMGP3Message mes = (SMGP3Message)message;
        mes.setSequenceId(sequenceId);
        super.getParent().send(message);
        if(SMGPConstant.debug)
        {
            Debug.dump(mes.toString());
        }
    }

    public SMGP3Message getResponse()
    {
        return receive;
    }

    public synchronized void waitResponse()
    {
        if(receive == null)
        {
            try
            {
                wait(((SMGPConnection)super.getParent()).getTransactionTimeout());
            }
            catch(InterruptedException interruptedexception) { }
        }
    }
}
