package net.zoneland.gateway.comm.smgp3.message;


import net.zoneland.gateway.comm.smgp3.SMGPConstant;
import net.zoneland.gateway.util.TypeConvert;

public class SMGP3ActiveTestMessage extends SMGP3Message
{

    public SMGP3ActiveTestMessage()
    {
        int len = 12;
        super.buf = new byte[len];
        TypeConvert.int2byte(len, super.buf, 0);
        TypeConvert.int2byte(RequestId.ACTIVETEST, super.buf, 4);
    }

    public SMGP3ActiveTestMessage(byte buf[])
        throws IllegalArgumentException
    {
        super.buf = new byte[4];
        if(buf.length != 4)
        {
            throw new IllegalArgumentException(SMGPConstant.SMC_MESSAGE_ERROR);
        } else
        {
            System.arraycopy(buf, 0, super.buf, 0, 4);
            super.sequence_Id = TypeConvert.byte2int(super.buf, 0);
            return;
        }
    }

    public String toString()
    {
        StringBuffer strBuf = new StringBuffer(100);
        strBuf.append("SMGPActiveTestMessage: ");
        strBuf.append("Sequence_Id=".concat(String.valueOf(String.valueOf(getSequenceId()))));
        return strBuf.toString();
    }

    public int getRequestId()
    {
        return RequestId.ACTIVETEST;
    }
}
