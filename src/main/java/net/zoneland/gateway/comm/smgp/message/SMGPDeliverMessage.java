package net.zoneland.gateway.comm.smgp.message;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.zoneland.gateway.comm.smgp.SMGPConstant;
import net.zoneland.gateway.util.TypeConvert;

import org.apache.log4j.Logger;

public class SMGPDeliverMessage extends SMGPMessage {

    private Logger logger = Logger.getLogger(SMGPDeliverMessage.class);

    public SMGPDeliverMessage(byte buf[]) throws IllegalArgumentException {
        //TODO  原有数据为81，实际测试中要注意下
        int len = 73 + (buf[72] & 0xff);
        if (buf.length != len) {
            throw new IllegalArgumentException(SMGPConstant.SMC_MESSAGE_ERROR);
        } else {
            super.buf = new byte[len];
            System.arraycopy(buf, 0, super.buf, 0, buf.length);
            super.sequence_Id = TypeConvert.byte2int(super.buf, 0);
            return;
        }
    }

    public byte[] getMsgId() {
        byte msgId[] = new byte[10];
        System.arraycopy(super.buf, 4, msgId, 0, 10);
        return msgId;
    }

    public int getIsReport() {
        return super.buf[14];
    }

    public int getMsgFormat() {
        return super.buf[15];
    }

    public Date getRecvTime() {
        Date date;
        try {
            byte tmpbyte[] = new byte[4];
            System.arraycopy(super.buf, 16, tmpbyte, 0, 4);
            String tmpstr = new String(tmpbyte);
            int tmpYear = Integer.parseInt(tmpstr);
            tmpbyte = new byte[2];
            System.arraycopy(super.buf, 20, tmpbyte, 0, 2);
            tmpstr = new String(tmpbyte);
            int tmpMonth = Integer.parseInt(tmpstr) - 1;
            System.arraycopy(super.buf, 22, tmpbyte, 0, 2);
            tmpstr = new String(tmpbyte);
            int tmpDay = Integer.parseInt(tmpstr);
            System.arraycopy(super.buf, 24, tmpbyte, 0, 2);
            tmpstr = new String(tmpbyte);
            int tmpHour = Integer.parseInt(tmpstr);
            System.arraycopy(super.buf, 26, tmpbyte, 0, 2);
            tmpstr = new String(tmpbyte);
            int tmpMinute = Integer.parseInt(tmpstr);
            System.arraycopy(super.buf, 28, tmpbyte, 0, 2);
            tmpstr = new String(tmpbyte);
            int tmpSecond = Integer.parseInt(tmpstr);
            Calendar calendar = Calendar.getInstance();
            calendar.set(tmpYear, tmpMonth, tmpDay, tmpHour, tmpMinute, tmpSecond);
            Date date1 = calendar.getTime();
            return date1;
        } catch (Exception e) {
            date = null;
        }
        return date;
    }

    public String getSrcTermID() {
        byte srcTermId[] = new byte[21];
        System.arraycopy(super.buf, 30, srcTermId, 0, 21);
        return (new String(srcTermId)).trim();
    }

    public String getDestTermID() {
        byte destTermId[] = new byte[21];
        System.arraycopy(super.buf, 51, destTermId, 0, 21);
        return (new String(destTermId)).trim();
    }

    public int getMsgLength() {
        return super.buf[72] & 0xff;
    }

    public byte[] getMsgContent() {
        int len = super.buf[72] & 0xff;
        byte content[] = new byte[len];
        System.arraycopy(super.buf, 73, content, 0, len);
        return content;
    }

    public String getReserve() {
        //TODO  原有数据为73+(super.buf[72] & 0xff)，实际测试中要注意下
        int loc = 65 + (super.buf[72] & 0xff);
        byte reserve[] = new byte[8];
        System.arraycopy(super.buf, loc, reserve, 0, 8);
        return (new String(reserve)).trim();
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer(600);
        strBuf.append("SMGPDeliverMessage: ");
        strBuf.append("Sequence_Id=".concat(String.valueOf(String.valueOf(getSequenceId()))));
        strBuf.append(",MsgID=".concat(String.valueOf(String.valueOf(new String(getMsgId(), Charset
            .forName("UTF-8"))))));
        strBuf.append(",IsReport=".concat(String.valueOf(String.valueOf(getIsReport()))));
        strBuf.append(",MsgFormat=".concat(String.valueOf(String.valueOf(getMsgFormat()))));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        if (getRecvTime() != null) {
            strBuf.append(",RecvTime=".concat(String.valueOf(String.valueOf(dateFormat
                .format(getRecvTime())))));
        } else {
            strBuf.append(",RecvTime=null");
        }
        strBuf.append(",SrcTermID=".concat(String.valueOf(String.valueOf(getSrcTermID()))));
        strBuf.append(",DestTermID=".concat(String.valueOf(String.valueOf(getDestTermID()))));
        strBuf.append(",MsgLength=".concat(String.valueOf(String.valueOf(getMsgLength()))));
        strBuf.append(",MsgContent=".concat(String.valueOf(String.valueOf(new String(
            getMsgContent())))));
        strBuf.append(",reserve=".concat(String.valueOf(String.valueOf(getReserve()))));
        return strBuf.toString();
    }

    public String getMsgContentStr() {
        try {
            return super.getMsgContentStr(getMsgContent(), getMsgFormat());
        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
        }
        return "";
    }

    public int getRequestId() {
        return 3;
    }
}
