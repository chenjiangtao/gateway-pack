package net.zoneland.gateway.comm.cmpp.message;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.zoneland.gateway.comm.cmpp.CMPPConstant;
import net.zoneland.gateway.util.TypeConvert;

public class CMPPQueryMessage extends CMPPMessage
{

    private String outStr;

    public CMPPQueryMessage(Date time, int query_Type, String query_Code, String reserve)
        throws IllegalArgumentException
    {
        if(query_Type != 0 && query_Type != 1)
        {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.QUERY_INPUT_ERROR)))).append(":query_Type").append(CMPPConstant.VALUE_ERROR))));
        }
        if(query_Code.length() > 10)
        {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.QUERY_INPUT_ERROR)))).append(":query_Code").append(CMPPConstant.STRING_LENGTH_GREAT).append("10"))));
        }
        if(reserve.length() > 8)
        {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(CMPPConstant.QUERY_INPUT_ERROR)))).append(":reserve").append(CMPPConstant.STRING_LENGTH_GREAT).append("8"))));
        } else
        {
            int len = 39;
            super.buf = new byte[len];
            TypeConvert.int2byte(len, super.buf, 0);
            TypeConvert.int2byte(6, super.buf, 4);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            System.arraycopy(dateFormat.format(time).getBytes(), 0, super.buf, 12, dateFormat.format(time).length());
            super.buf[20] = (byte)query_Type;
            System.arraycopy(query_Code.getBytes(), 0, super.buf, 21, query_Code.length());
            System.arraycopy(reserve.getBytes(), 0, super.buf, 31, reserve.length());
            outStr = ",time=".concat(String.valueOf(String.valueOf(dateFormat.format(time))));
            outStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(outStr)))).append(",query_Type=").append(query_Type)));
            outStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(outStr)))).append(",query_Code=").append(query_Code)));
            outStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(outStr)))).append(",reserve=").append(reserve)));
            return;
        }
    }

    public String toString()
    {
        String tmpStr = "CMPP_Query: ";
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append("Sequence_Id=").append(getSequenceId())));
        tmpStr = String.valueOf(tmpStr) + String.valueOf(outStr);
        return tmpStr;
    }

    public int getCommandId()
    {
        return 6;
    }
}
