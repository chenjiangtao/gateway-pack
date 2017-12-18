package net.zoneland.gateway.comm.sgip.message;

import net.zoneland.gateway.comm.sgip.SGIPConstant;
import net.zoneland.gateway.util.TypeConvert;

public class SGIPUnbindMessage extends SGIPMessage
{

    public SGIPUnbindMessage()
    {
        int len = 20;
        super.buf = new byte[len];
        TypeConvert.int2byte(len, super.buf, 0);
        TypeConvert.int2byte(2, super.buf, 4);
    }

    public SGIPUnbindMessage(byte buf[])
        throws IllegalArgumentException
    {
        super.buf = new byte[12];
        if(buf.length != 12)
        {
            throw new IllegalArgumentException(SGIPConstant.SMC_MESSAGE_ERROR);
        } else
        {
            System.arraycopy(buf, 0, super.buf, 0, 12);
            super.src_node_Id = TypeConvert.byte2int(super.buf, 0);
            super.time_Stamp = TypeConvert.byte2int(super.buf, 4);
            super.sequence_Id = TypeConvert.byte2int(super.buf, 8);
            return;
        }
    }

    public String toString()
    {
        String tmpStr = "SGIP_UNBIND: ";
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append("Sequence_Id=").append(getSequenceId())));
        return tmpStr;
    }

    public int getCommandId()
    {
        return 2;
    }
}
