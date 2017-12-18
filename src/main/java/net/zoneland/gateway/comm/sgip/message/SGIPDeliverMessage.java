package net.zoneland.gateway.comm.sgip.message;

import java.io.UnsupportedEncodingException;

import net.zoneland.gateway.comm.sgip.SGIPConstant;
import net.zoneland.gateway.util.TypeConvert;

import org.apache.log4j.Logger;

public class SGIPDeliverMessage extends SGIPMessage {

    private Logger logger = Logger.getLogger(SGIPDeliverMessage.class);

    public SGIPDeliverMessage(byte buf[]) throws IllegalArgumentException {
        int len = TypeConvert.byte2int(buf, 57);
        len = 69 + len;
        if (buf.length != len) {
            throw new IllegalArgumentException(SGIPConstant.SMC_MESSAGE_ERROR);
        } else {
            super.buf = new byte[len];
            System.arraycopy(buf, 0, super.buf, 0, buf.length);
            super.src_node_Id = TypeConvert.byte2int(super.buf, 0);
            super.time_Stamp = TypeConvert.byte2int(super.buf, 4);
            super.sequence_Id = TypeConvert.byte2int(super.buf, 8);
            return;
        }
    }

    /**
     * 发送该短消息的手机号，字符，手机号码前加“86”国别标志
     * @return
     */
    public String getUserNumber() {
        byte tmpId[] = new byte[21];
        System.arraycopy(super.buf, 12, tmpId, 0, 21);
        String tmpStr = (new String(tmpId)).trim();
        if (tmpStr.indexOf('\0') >= 0) {
            return tmpStr.substring(0, tmpStr.indexOf('\0'));
        } else {
            return tmpStr;
        }
    }

    /**
     * 接收该短消息的SP的接入号码，字符
     * @return
     */
    public String getSPNumber() {
        byte tmpId[] = new byte[21];
        System.arraycopy(super.buf, 33, tmpId, 0, 21);
        String tmpStr = (new String(tmpId)).trim();
        if (tmpStr.indexOf('\0') >= 0) {
            return tmpStr.substring(0, tmpStr.indexOf('\0'));
        } else {
            return tmpStr;
        }
    }

    public int getTpPid() {
        int tmpId = super.buf[54];
        return tmpId;
    }

    public int getTpUdhi() {
        int tmpId = super.buf[55];
        return tmpId;
    }

    public int getMsgFmt() {
        int tmpFmt = super.buf[56];
        return tmpFmt;
    }

    public int getMsgLength() {
        return TypeConvert.byte2int(super.buf, 57);
    }

    public byte[] getMsgContent() {
        int len = getMsgLength();
        byte tmpContent[] = new byte[len];
        System.arraycopy(super.buf, 61, tmpContent, 0, len);
        return tmpContent;
    }

    public String getMsgContentStr() {
        try {
            return super.getMsgContentStr(getMsgContent(), getMsgFmt());
        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
        }
        return "";
    }

    public String getReserve() {
        int loc = 61 + getMsgLength();
        byte tmpReserve[] = new byte[8];
        System.arraycopy(super.buf, loc, tmpReserve, 0, 8);
        return (new String(tmpReserve)).trim();
    }

    public String toString() {
        String tmpStr = "SGIP_DELIVER: ";
        StringBuilder buf = new StringBuilder(String.valueOf(tmpStr)).append("Sequence_Id=")
            .append(getSequenceId()).append(",UserNumber=").append(getUserNumber())
            .append(",SPNumber=").append(getSPNumber()).append(",TpPid=").append(getTpPid())
            .append(",TpUdhi=").append(getTpUdhi()).append(",MsgFmt=").append(getMsgFmt())
            .append(",MsgLength=").append(getMsgLength()).append(",MsgContent=")
            .append(new String(getMsgContent())).append(",Reserve=").append(getReserve())
            .append(",src_node_id").append(super.getSrcNodeId()).append(",timestamp=")
            .append(super.getTimeStamp());
        return buf.toString();
    }

    public int getCommandId() {
        return 4;
    }
}
