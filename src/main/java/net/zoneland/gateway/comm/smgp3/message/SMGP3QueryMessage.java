package net.zoneland.gateway.comm.smgp3.message;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.zoneland.gateway.comm.smgp.SMGPConstant;
import net.zoneland.gateway.util.TypeConvert;

public class SMGP3QueryMessage extends SMGP3Message
{

    private StringBuffer strBuf;

    public SMGP3QueryMessage(Date time, int queryType, String queryCode)
        throws IllegalArgumentException
    {
        if(time == null)
        {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.QUERY_INPUT_ERROR)))).append(":Time ").append(SMGPConstant.STRING_NULL))));
        }
        if(queryType < 0 || queryType > 255)
        {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.QUERY_INPUT_ERROR)))).append(":QueryType ").append(SMGPConstant.INT_SCOPE_ERROR))));
        }
        if(queryCode != null && queryCode.length() > 10)
        {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(SMGPConstant.QUERY_INPUT_ERROR)))).append(":QueryCode ").append(SMGPConstant.STRING_LENGTH_GREAT).append("10"))));
        }
        int len = 31;
        super.buf = new byte[len];
        TypeConvert.int2byte(len, super.buf, 0);
        TypeConvert.int2byte(7, super.buf, 4);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        System.arraycopy(dateFormat.format(time).getBytes(), 0, super.buf, 12, dateFormat.format(time).length());
        super.buf[20] = (byte)queryType;
        if(queryCode != null)
        {
            System.arraycopy(queryCode.getBytes(), 0, super.buf, 21, queryCode.length());
        }
        strBuf = new StringBuffer(200);
        strBuf.append(",time=".concat(String.valueOf(String.valueOf(time))));
        strBuf.append(",Query_Type=".concat(String.valueOf(String.valueOf(queryType))));
        strBuf.append(",Query_Code=".concat(String.valueOf(String.valueOf(queryCode))));
    }

    public String toString()
    {
        StringBuffer outStr = new StringBuffer(200);
        outStr.append("SMGPQueryMessage:");
        outStr.append("PacketLength=".concat(String.valueOf(String.valueOf(super.buf.length))));
        outStr.append(",RequestID=7");
        outStr.append(",Sequence_Id=".concat(String.valueOf(String.valueOf(getSequenceId()))));
        if(strBuf != null)
        {
            outStr.append(strBuf.toString());
        }
        return outStr.toString();
    }

    public int getRequestId()
    {
        return 7;
    }
}
