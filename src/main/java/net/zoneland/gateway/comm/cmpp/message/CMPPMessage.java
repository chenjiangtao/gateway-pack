package net.zoneland.gateway.comm.cmpp.message;

import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.util.TypeConvert;

public abstract class CMPPMessage extends PMessage
    implements Cloneable
{

    protected byte buf[];
    protected int sequence_Id;

    public CMPPMessage()
    {
    }

    public Object clone()
    {
        try
        {
            CMPPMessage m = (CMPPMessage)super.clone();
            m.buf = (byte[])buf.clone();
            CMPPMessage cmppmessage = m;
            return cmppmessage;
        }
        catch(CloneNotSupportedException ex)
        {
            ex.printStackTrace();
        }
        Object obj = null;
        return obj;
    }

    public abstract String toString();

    public abstract int getCommandId();

    public int getSequenceId()
    {
        return sequence_Id;
    }

    public void setSequenceId(int sequence_Id)
    {
        this.sequence_Id = sequence_Id;
        TypeConvert.int2byte(sequence_Id, buf, 8);
    }

    public byte[] getBytes()
    {
        return buf;
    }
}
