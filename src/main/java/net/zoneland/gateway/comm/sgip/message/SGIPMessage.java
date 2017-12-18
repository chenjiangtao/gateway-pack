package net.zoneland.gateway.comm.sgip.message;

import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.util.TypeConvert;

public abstract class SGIPMessage extends PMessage implements Cloneable {

    protected byte buf[];
    protected long src_node_Id;
    protected int  time_Stamp;
    protected int  sequence_Id;

    public SGIPMessage() {
    }

    public Object clone() {
        try {
            SGIPMessage m = (SGIPMessage) super.clone();
            m.buf = (byte[]) buf.clone();
            SGIPMessage sgipmessage = m;
            return sgipmessage;
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        Object obj = null;
        return obj;
    }

    public abstract String toString();

    public abstract int getCommandId();

    public long getSrcNodeId() {
        return src_node_Id;
    }

    public void setSrcNodeId(long src_node_Id) {
        this.src_node_Id = src_node_Id;
        TypeConvert.int2byte((int) src_node_Id, buf, 8);
    }

    public int getTimeStamp() {
        return time_Stamp;
    }

    public void setTimeStamp(int time_Stamp) {
        this.time_Stamp = time_Stamp;
        TypeConvert.int2byte(time_Stamp, buf, 12);
    }

    public int getSequenceId() {
        return sequence_Id;
    }

    public void setSequenceId(int sequence_Id) {
        this.sequence_Id = sequence_Id;
        TypeConvert.int2byte(sequence_Id, buf, 16);
    }

    public byte[] getBytes() {
        return buf;
    }

}
