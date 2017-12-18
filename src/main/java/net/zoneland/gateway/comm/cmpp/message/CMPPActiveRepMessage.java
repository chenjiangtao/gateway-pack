package net.zoneland.gateway.comm.cmpp.message;

import net.zoneland.gateway.comm.cmpp.CMPPConstant;
import net.zoneland.gateway.util.TypeConvert;

public class CMPPActiveRepMessage extends CMPPMessage
{

    public CMPPActiveRepMessage(int success_Id)
        throws IllegalArgumentException
    {
        if(success_Id <= 0 || success_Id > 255)
        {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.ACTIVE_REPINPUT_ERROR)))).append(":success_Id").append(CMPPConstant.INT_SCOPE_ERROR))));
        } else
        {
            int len = 13;
            super.buf = new byte[len];
            TypeConvert.int2byte(len, super.buf, 0);
            TypeConvert.int2byte(0x80000008, super.buf, 4);
            super.buf[12] = (byte)success_Id;
            return;
        }
    }

    public CMPPActiveRepMessage(byte buf[])
        throws IllegalArgumentException
    {
        super.buf = new byte[5];
        if(buf.length != 5)
        {
            throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
        } else
        {
            System.arraycopy(buf, 0, super.buf, 0, 5);
            super.sequence_Id = TypeConvert.byte2int(super.buf, 0);
            return;
        }
    }

    public int getSuccessId()
    {
        return super.buf[4];
    }

    public String toString()
    {
        String tmpStr = "CMPP_Active_Test_REP: ";
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append("Sequence_Id=").append(getSequenceId())));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append(",SuccessId=").append(String.valueOf(super.buf[4]))));
        return tmpStr;
    }

    public int getCommandId()
    {
        return 0x80000008;
    }
}
