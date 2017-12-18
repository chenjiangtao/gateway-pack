package net.zoneland.gateway.comm.cmpp3.message;

import net.zoneland.gateway.comm.cmpp.CMPPConstant;
import net.zoneland.gateway.comm.cmpp.message.CMPPMessage;
import net.zoneland.gateway.util.TypeConvert;

public class CMPP30ConnectRepMessage extends CMPPMessage
{

    public CMPP30ConnectRepMessage(byte buf[])
        throws IllegalArgumentException
    {
        super.buf = new byte[25];
        if(buf.length != 25)
        {
            throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
        } else
        {
            System.arraycopy(buf, 0, super.buf, 0, buf.length);
            super.sequence_Id = TypeConvert.byte2int(super.buf, 0);
            return;
        }
    }

    public int getStatus()
    {
        return TypeConvert.byte2int(super.buf, 4);
    }

    public byte[] getAuthenticatorISMG()
    {
        byte tmpbuf[] = new byte[16];
        System.arraycopy(super.buf, 8, tmpbuf, 0, 16);
        return tmpbuf;
    }

    public byte getVersion()
    {
        return super.buf[24];
    }

    public String toString()
    {
        String tmpStr = "CMPP_Connect_REP: ";
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append("Sequence_Id=").append(getSequenceId())));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append(",Status=").append(getStatus())));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append(",AuthenticatorISMG=").append(new String(getAuthenticatorISMG()))));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append(",Version=").append(getVersion())));
        return tmpStr;
    }

    public int getCommandId()
    {
        return 0x80000001;
    }
}
