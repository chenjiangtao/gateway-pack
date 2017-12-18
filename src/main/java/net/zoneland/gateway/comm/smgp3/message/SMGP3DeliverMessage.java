package net.zoneland.gateway.comm.smgp3.message;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import net.zoneland.gateway.comm.smgp3.message.tlv.Tlv;
import net.zoneland.gateway.comm.smgp3.message.tlv.TlvId;
import net.zoneland.gateway.comm.smgp3.message.tlv.TlvUtil;
import net.zoneland.gateway.util.Hex;
import net.zoneland.gateway.util.TypeConvert;

import org.apache.log4j.Logger;

public class SMGP3DeliverMessage extends SMGP3Message {
    private Logger logger = Logger.getLogger(SMGP3DeliverMessage.class);
    private byte[] msgID_BCD;
    private String msgID;
    private int    isReport;
    private int    msgFormat;
    private String recvTime;
    private String srcTermID;
    private String destTermID;
    private int    msgLength;
    private byte[] msgContent;
    private byte[] reserve;
    private String linkID;
    private int    tp_pid;
    private int    tp_udhi;
    private int    srcTermType;
    private String srcTermPseudo;
    private int    submitMsgType;
    private int    spDealResult;
    private String reportMsgID;

    public SMGP3DeliverMessage(byte buf[]) throws IllegalArgumentException {
        //header 4byte + body 77byte + content byte(buf[72]-->content length)
        int base_len = 81 + (buf[72] & 0xff);
        int tlv_len = buf.length - base_len;
        super.buf = new byte[base_len + tlv_len];
        System.arraycopy(buf, 0, super.buf, 0, buf.length);
        //s_id 
        super.sequence_Id = TypeConvert.byte2int(super.buf, 0);
        //msg_id
        this.msgID_BCD = new byte[10];
        System.arraycopy(super.buf, 4, msgID_BCD, 0, 10);
        this.msgID = TypeConvert.getHexString(super.buf, 4, 0, 10);
        //isReport
        this.isReport = super.buf[14];
        //MsgFormat
        this.msgFormat = super.buf[15];
        //recvTime
        this.recvTime = TypeConvert.getString(super.buf, 16, 0, 14);
        //srcTermID
        this.srcTermID = TypeConvert.getString(super.buf, 30, 0, 21);
        //destTermID
        this.destTermID = TypeConvert.getString(super.buf, 51, 0, 21);
        //msgLength
        this.msgLength = buf[72] & 0xff;
        //msgContent
        this.msgContent = new byte[msgLength];
        System.arraycopy(super.buf, 73, this.msgContent, 0, this.msgLength);
        //isReport ==1
        if (this.isReport == 1) {
            byte[] tmpmsgid = new byte[10];
            System.arraycopy(this.msgContent, 3, tmpmsgid, 0, 10);
            this.reportMsgID = Hex.rhex(tmpmsgid);
        }

        //reserve
        this.reserve = new byte[8];
        System.arraycopy(super.buf, 73 + this.msgLength, this.reserve, 0, this.reserve.length);

        //tlvs
        byte[] tlv = new byte[tlv_len];
        System.arraycopy(super.buf, base_len, tlv, 0, tlv.length);
        Map<Integer, Tlv> otherTlv = TlvUtil.TlvAnalysis(tlv);
        Tlv tmp = null;
        tp_pid = ((tmp = otherTlv.get(TlvId.TP_pid)) == null ? -1 : Integer.valueOf(tmp.Value));
        tp_udhi = ((tmp = otherTlv.get(TlvId.TP_udhi)) == null ? -1 : Integer.valueOf(tmp.Value));
        linkID = ((tmp = otherTlv.get(TlvId.LinkID)) == null ? "" : tmp.Value);
        srcTermType = ((tmp = otherTlv.get(TlvId.SrcTermType)) == null ? -1 : Integer
            .valueOf(tmp.Value));
        srcTermPseudo = ((tmp = otherTlv.get(TlvId.SrcTermPseudo)) == null ? "" : tmp.Value);
        submitMsgType = ((tmp = otherTlv.get(TlvId.SubmitMsgType)) == null ? -1 : Integer
            .valueOf(tmp.Value));
        spDealResult = ((tmp = otherTlv.get(TlvId.SPDealResult)) == null ? -1 : Integer
            .valueOf(tmp.Value));

    }

    /**
     * Getter method for property <tt>msgID_BCD</tt>.
     * 
     * @return property value of msgID_BCD
     */
    public byte[] getMsgID_BCD() {
        return msgID_BCD;
    }

    /**
     * Getter method for property <tt>msgID</tt>.
     * 
     * @return property value of msgID
     */
    public String getMsgID() {
        return msgID;
    }

    /**
     * Getter method for property <tt>isReport</tt>.
     * 
     * @return property value of isReport
     */
    public int getIsReport() {
        return isReport;
    }

    /**
     * Getter method for property <tt>msgFormat</tt>.
     * 
     * @return property value of msgFormat
     */
    public int getMsgFormat() {
        return msgFormat;
    }

    public String getMessageContentStr() {
        String msgStr = null;
        try {
            msgStr = super.getMsgContentStr(this.msgContent, this.msgFormat);
        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
        }
        return msgStr;

    }

    /**
     * Getter method for property <tt>recvTime</tt>.
     * 
     * @return property value of recvTime
     */
    public String getRecvTime() {
        return recvTime;
    }

    /**
     * Getter method for property <tt>srcTermID</tt>.
     * 
     * @return property value of srcTermID
     */
    public String getSrcTermID() {
        return srcTermID;
    }

    /**
     * Getter method for property <tt>destTermID</tt>.
     * 
     * @return property value of destTermID
     */
    public String getDestTermID() {
        return destTermID;
    }

    /**
     * Getter method for property <tt>msgLength</tt>.
     * 
     * @return property value of msgLength
     */
    public int getMsgLength() {
        return msgLength;
    }

    /**
     * Getter method for property <tt>msgContent</tt>.
     * 
     * @return property value of msgContent
     */
    public byte[] getMsgContent() {
        return msgContent;
    }

    /**
     * Getter method for property <tt>reserve</tt>.
     * 
     * @return property value of reserve
     */
    public byte[] getReserve() {
        return reserve;
    }

    /**
     * Getter method for property <tt>linkID</tt>.
     * 
     * @return property value of linkID
     */
    public String getLinkID() {
        return linkID;
    }

    /**
     * Getter method for property <tt>tp_pid</tt>.
     * 
     * @return property value of tp_pid
     */
    public int getTp_pid() {
        return tp_pid;
    }

    /**
     * Getter method for property <tt>tp_udhi</tt>.
     * 
     * @return property value of tp_udhi
     */
    public int getTp_udhi() {
        return tp_udhi;
    }

    /**
     * Getter method for property <tt>srcTermType</tt>.
     * 
     * @return property value of srcTermType
     */
    public int getSrcTermType() {
        return srcTermType;
    }

    /**
     * Getter method for property <tt>srcTermPseudo</tt>.
     * 
     * @return property value of srcTermPseudo
     */
    public String getSrcTermPseudo() {
        return srcTermPseudo;
    }

    /**
     * Getter method for property <tt>submitMsgType</tt>.
     * 
     * @return property value of submitMsgType
     */
    public int getSubmitMsgType() {
        return submitMsgType;
    }

    /**
     * Getter method for property <tt>spDealResult</tt>.
     * 
     * @return property value of spDealResult
     */
    public int getSpDealResult() {
        return spDealResult;
    }

    /**
     * Getter method for property <tt>reportMsgID</tt>.
     * 
     * @return property value of reportMsgID
     */
    public String getReportMsgID() {
        return reportMsgID;
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer(600);
        strBuf.append("SMGPDeliverMessage: ");
        strBuf.append("Sequence_Id=".concat(String.valueOf(String.valueOf(getSequenceId()))));
        strBuf.append(",MsgID=".concat(String.valueOf(String.valueOf(new String(getMsgID())))));
        strBuf.append(",IsReport=".concat(String.valueOf(String.valueOf(getIsReport()))));
        strBuf.append(",MsgFormat=".concat(String.valueOf(String.valueOf(getMsgFormat()))));
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        if (getRecvTime() != null) {
            strBuf.append(",RecvTime=".concat(String.valueOf(getRecvTime())));
        } else {
            strBuf.append(",RecvTime=null");
        }
        strBuf.append(",SrcTermID=".concat(String.valueOf(String.valueOf(getSrcTermID()))));
        strBuf.append(",DestTermID=".concat(String.valueOf(String.valueOf(getDestTermID()))));
        strBuf.append(",MsgLength=".concat(String.valueOf(String.valueOf(getMsgLength()))));
        strBuf.append(",MsgContent=".concat(String.valueOf(String.valueOf(new String(
            getMsgContent())))));
        strBuf.append(",reserve=".concat(String.valueOf(String.valueOf(getReserve()))));
        strBuf.append(",tp_pid=".concat(String.valueOf(String.valueOf(getTp_pid()))));
        strBuf.append(",tp_udhi=".concat(String.valueOf(String.valueOf(getTp_udhi()))));
        strBuf.append(",linkID=".concat(String.valueOf(String.valueOf(getLinkID()))));
        strBuf.append(",srcTermType=".concat(String.valueOf(String.valueOf(getSrcTermType()))));
        strBuf.append(",srcTermPseudo=".concat(String.valueOf(String.valueOf(getSrcTermPseudo()))));
        strBuf.append(",submitMsgType=".concat(String.valueOf(String.valueOf(getSubmitMsgType()))));
        strBuf.append(",spDealResult=".concat(String.valueOf(String.valueOf(getSpDealResult()))));
        strBuf.append(",reportMsgID=".concat(String.valueOf(String.valueOf(getReportMsgID()))));
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
        return RequestId.DELIVER;
    }
}
