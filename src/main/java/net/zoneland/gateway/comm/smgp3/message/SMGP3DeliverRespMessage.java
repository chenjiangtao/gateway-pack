package net.zoneland.gateway.comm.smgp3.message;

import net.zoneland.gateway.comm.smgp.SMGPConstant;
import net.zoneland.gateway.util.TypeConvert;

public class SMGP3DeliverRespMessage extends SMGP3Message
{

    private StringBuffer strBuf;

    public SMGP3DeliverRespMessage(byte msg_Id[], int status)
        throws IllegalArgumentException
    {
        if(msg_Id.length > 10)
        {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.DELIVER_REPINPUT_ERROR)))).append(":msg_Id").append(SMGPConstant.STRING_LENGTH_GREAT).append("10"))));
        }
        if(status < 0)
        {
            throw new IllegalArgumentException(String.valueOf(String.valueOf(SMGPConstant.DELIVER_REPINPUT_ERROR)).concat(":result"));
        } else
        {
            int len = 26;
            super.buf = new byte[len];
            TypeConvert.int2byte(len, super.buf, 0);
            TypeConvert.int2byte(RequestId.DELIVER_RESP, super.buf, 4);
            System.arraycopy(msg_Id, 0, super.buf, 12, msg_Id.length);
            TypeConvert.int2byte(status, super.buf, 22);
            strBuf = new StringBuffer(100);
            strBuf.append(",status=".concat(String.valueOf(String.valueOf(status))));
            return;
        }
    }

    public String toString()
    {
        StringBuffer outStr = new StringBuffer(100);
        outStr.append("SMGPDeliverRespMessage:");
        outStr.append(",Sequence_Id=".concat(String.valueOf(String.valueOf(getSequenceId()))));
        if(strBuf != null)
        {
            outStr.append(strBuf.toString());
        }
        return outStr.toString();
    }

    public int getRequestId()
    {
        return RequestId.DELIVER_RESP;
    }
}
