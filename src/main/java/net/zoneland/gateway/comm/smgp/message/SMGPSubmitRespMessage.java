package net.zoneland.gateway.comm.smgp.message;

import net.zoneland.gateway.comm.smgp.SMGPConstant;
import net.zoneland.gateway.util.TypeConvert;

public class SMGPSubmitRespMessage extends SMGPMessage
{

    public SMGPSubmitRespMessage(byte buf[])
        throws IllegalArgumentException
    {
        super.buf = new byte[18];
        if(buf.length != 18)
        {
            throw new IllegalArgumentException(SMGPConstant.SMC_MESSAGE_ERROR);
        } else
        {
            System.arraycopy(buf, 0, super.buf, 0, 18);
            super.sequence_Id = TypeConvert.byte2int(super.buf, 0);
            return;
        }
    }

    public byte[] getMsgId()
    {
        byte tmpMsgId[] = new byte[10];
        System.arraycopy(super.buf, 4, tmpMsgId, 0, 10);
        return tmpMsgId;
    }


    public String getMsgIdStr() {

        return bcd2Str(getMsgId());
    }

    public int getStatus() {
        return TypeConvert.byte2int(super.buf, 14);
    }

    public static String bcd2Str(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    public String toString()
    {
        StringBuffer strBuf = new StringBuffer(200);
        strBuf.append("SMGPSubmitRespMessage: ");
        strBuf.append("Sequence_Id=".concat(String.valueOf(String.valueOf(getSequenceId()))));
        strBuf.append(",MsgId=".concat(String.valueOf(String.valueOf(new String(getMsgId())))));
        strBuf.append(",Status=".concat(String.valueOf(String.valueOf(getStatus()))));
        return strBuf.toString();
    }

    public int getRequestId()
    {
        return 0x80000002;
    }
}
