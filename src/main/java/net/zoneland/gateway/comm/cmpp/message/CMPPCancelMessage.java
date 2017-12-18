package net.zoneland.gateway.comm.cmpp.message;

import net.zoneland.gateway.comm.cmpp.CMPPConstant;
import net.zoneland.gateway.util.TypeConvert;

public class CMPPCancelMessage extends CMPPMessage
{

    private String outStr;

    public CMPPCancelMessage(byte msg_Id[])
        throws IllegalArgumentException
    {
        if(msg_Id.length > 8)
        {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.CONNECT_INPUT_ERROR)))).append(":msg_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("8"))));
        } else
        {
            int len = 20;
            super.buf = new byte[len];
            TypeConvert.int2byte(len, super.buf, 0);
            TypeConvert.int2byte(7, super.buf, 4);
            System.arraycopy(msg_Id, 0, super.buf, 12, msg_Id.length);
            return;
        }
    }

    public String toString()
    {
        String tmpStr = "CMPP_Cancel: ";
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append("Sequence_Id=").append(getSequenceId())));
        return tmpStr;
    }

    public int getCommandId()
    {
        return 7;
    }
}
