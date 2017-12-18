package net.zoneland.gateway.comm.smgp3.message;


import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.util.TypeConvert;

public abstract class SMGP3Message extends PMessage implements Cloneable {

    protected byte buf[];
    protected int  sequence_Id;
    protected int  msgFormat = 0;

    public SMGP3Message() {
    }

    public Object clone() {
        try {
            SMGP3Message m = (SMGP3Message) super.clone();
            m.buf = (byte[]) buf.clone();
            PMessage smgpmessage = m;
            return smgpmessage;
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        Object obj = null;
        return obj;
    }

    public abstract String toString();

    public abstract int getRequestId();

    public int getSequenceId() {
        return sequence_Id;
    }

    public void setSequenceId(int sequence_Id) {
        this.sequence_Id = sequence_Id;
        TypeConvert.int2byte(sequence_Id, buf, 8);
    }

    public byte[] getBytes() {
        return buf;
    }
}
