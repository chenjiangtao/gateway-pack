package net.zoneland.gateway.comm.smgp3.message;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.zoneland.gateway.comm.smgp.SMGPConstant;
import net.zoneland.gateway.util.TypeConvert;

public class SMGP3QueryRespMessage extends SMGP3Message
{

    public SMGP3QueryRespMessage(byte buf[])
        throws IllegalArgumentException
    {
        super.buf = new byte[63];
        if(buf.length != 63)
        {
            throw new IllegalArgumentException(SMGPConstant.SMC_MESSAGE_ERROR);
        } else
        {
            System.arraycopy(buf, 0, super.buf, 0, buf.length);
            super.sequence_Id = TypeConvert.byte2int(super.buf, 0);
            return;
        }
    }

    public Date getTime()
    {
        byte tmpstr[] = new byte[4];
        System.arraycopy(super.buf, 4, tmpstr, 0, 4);
        String tmpYear = new String(tmpstr);
        byte tmpstr1[] = new byte[2];
        System.arraycopy(super.buf, 8, tmpstr1, 0, 2);
        String tmpMonth = new String(tmpstr1);
        System.arraycopy(super.buf, 10, tmpstr1, 0, 2);
        String tmpDay = new String(tmpstr1);
        Date date = java.sql.Date.valueOf(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpYear)))).append("-").append(tmpMonth).append("-").append(tmpDay))));
        return date;
    }

    public int getQueryType()
    {
        return super.buf[12];
    }

    public String getQueryCode()
    {
        byte tmpQC[] = new byte[10];
        System.arraycopy(super.buf, 13, tmpQC, 0, 10);
        return (new String(tmpQC)).trim();
    }

    public int getMtTlmsg()
    {
        return TypeConvert.byte2int(super.buf, 23);
    }

    public int getMtTlusr()
    {
        return TypeConvert.byte2int(super.buf, 27);
    }

    public int getMtScs()
    {
        return TypeConvert.byte2int(super.buf, 31);
    }

    public int getMtWt()
    {
        return TypeConvert.byte2int(super.buf, 35);
    }

    public int getMtFl()
    {
        return TypeConvert.byte2int(super.buf, 39);
    }

    public int getMoScs()
    {
        return TypeConvert.byte2int(super.buf, 43);
    }

    public int getMoWt()
    {
        return TypeConvert.byte2int(super.buf, 47);
    }

    public int getMoFl()
    {
        return TypeConvert.byte2int(super.buf, 51);
    }

    public String getReserve()
    {
        byte tmpReserve[] = new byte[8];
        System.arraycopy(super.buf, 55, tmpReserve, 0, 8);
        return (new String(tmpReserve)).trim();
    }

    public String toString()
    {
        String tmpStr = "SMGPQueryRespMessage: ";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append("Sequence_Id=").append(getSequenceId())));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append(",Time=").append(dateFormat.format(getTime()))));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append(",AuthenticatorISMG=").append(getQueryType())));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append(",QueryCode=").append(getQueryCode())));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append(",MtTlmsg=").append(getMtTlmsg())));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append(",MtTlusr=").append(getMtTlusr())));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append(",MtScs=").append(getMtScs())));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append(",MtWt=").append(getMtWt())));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append(",MtFl=").append(getMtFl())));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append(",MoScs=").append(getMoScs())));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append(",MoWt=").append(getMoWt())));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append(",MoFl=").append(getMoFl())));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append(",Reverse=").append(getReserve())));
        return tmpStr;
    }

    public int getRequestId()
    {
        return 0x80000007;
    }
}
