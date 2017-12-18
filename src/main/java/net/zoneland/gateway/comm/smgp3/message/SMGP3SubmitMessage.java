package net.zoneland.gateway.comm.smgp3.message;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zoneland.gateway.comm.smgp.SMGPConstant;
import net.zoneland.gateway.comm.smgp3.message.tlv.Tlv;
import net.zoneland.gateway.comm.smgp3.message.tlv.TlvId;
import net.zoneland.gateway.comm.smgp3.message.tlv.TlvUtil;
import net.zoneland.gateway.util.TypeConvert;

public class SMGP3SubmitMessage extends SMGP3Message {

    private StringBuffer      strBuf;

    private Map<Integer, Tlv> tlvs           = new HashMap<Integer, Tlv>(15);

    private int               msgType        = 6;

    private int               needReport     = 0;

    private int               priority       = 1;

    private String            serviceId;

    private String            feeType;

    private String            feeCode;

    private String            fixedFee;

    private int               msgFormat      = 0;

    private Date              validTime;

    private Date              atTime;

    private String            srcTermId;

    private String            chargeTermId;

    private String[]          destTermId;

    private String            msgContent;

    private String            reserve;

    private int               tp_pid         = -1;

    private int               tp_udhi        = -1;

    private String            linkId;

    private String            msgSrc;

    private int               chargeUserType = -1;

    private int               chargeTermType = -1;

    private String            chargeTermPseudo;

    private int               destTermType   = -1;

    private String            DestTermPseudo;

    private int               pkTotal        = -1;

    private int               pkNumber       = -1;

    private int               submitMsgType  = -1;

    private int               spDealResult   = -1;

    private String            mserviceId;

    /**
     * 
     * @param msgType
     * @param needReport
     * @param priority
     * @param serviceId
     * @param feeType
     * @param feeCode
     * @param fixedFee
     * @param msgFormat
     * @param validTime
     * @param atTime
     * @param srcTermId
     * @param chargeTermId
     * @param destTermId
     * @param msgContent
     * @param reserve
     * @throws IllegalArgumentException
     * @throws UnsupportedEncodingException 
     */
    public SMGP3SubmitMessage(int msgType, int needReport, int priority, String serviceId,
                              String feeType, String feeCode, String fixedFee, int msgFormat,
                              Date validTime, Date atTime, String srcTermId, String chargeTermId,
                              String[] destTermId, String msgContent, String reserve)
                                                                                     throws IllegalArgumentException,
                                                                                     UnsupportedEncodingException {
        this(msgType, needReport, priority, serviceId, feeType, feeCode, fixedFee, msgFormat,
            validTime, atTime, srcTermId, chargeTermId, destTermId, msgContent, reserve, -1, -1,
            null, null, -1, -1, null, -1, null, -1, -1, -1, -1, null);
    }

    /**
     * 
     * @param msgType
     * @param needReport
     * @param priority
     * @param serviceId
     * @param feeType
     * @param feeCode
     * @param fixedFee
     * @param msgFormat
     * @param validTime
     * @param atTime
     * @param srcTermId
     * @param chargeTermId
     * @param destTermId
     * @param msgContent
     * @param reserve
     * @param tp_pid
     * @param tp_udhi
     * @param linkId
     * @param msgSrc
     * @param ChargeUserType
     * @param ChargeTermType
     * @param ChargeTermPseudo
     * @param DestTermType
     * @param DestTermPseudo
     * @param pkTotal
     * @param pkNumber
     * @param submitMsgType
     * @param spDealResult
     * @param mserviceId
     * @throws IllegalArgumentException
     * @throws UnsupportedEncodingException 
     */
    public SMGP3SubmitMessage(int msgType, int needReport, int priority, String serviceId,
                              String feeType, String feeCode, String fixedFee, int msgFormat,
                              Date validTime, Date atTime, String srcTermId, String chargeTermId,
                              String destTermId[], String msgContent, String reserve, int tp_pid,
                              int tp_udhi, String linkId, String msgSrc, int ChargeUserType,
                              int ChargeTermType, String ChargeTermPseudo, int DestTermType,
                              String DestTermPseudo, int pkTotal, int pkNumber, int submitMsgType,
                              int spDealResult, String mserviceId) throws IllegalArgumentException,
                                                                  UnsupportedEncodingException {

        if (msgType < 0 || msgType > 255) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":MsgType ").append(
                SMGPConstant.INT_SCOPE_ERROR)));
        }
        if (needReport < 0 || needReport > 255) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":NeedReport ").append(
                SMGPConstant.INT_SCOPE_ERROR)));
        }
        if (priority < 0 || priority > 9) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":").append(
                SMGPConstant.PRIORITY_ERROR)));
        }
        if (serviceId == null) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":ServiceID ").append(
                SMGPConstant.STRING_NULL)));
        }
        if (serviceId.length() > 10) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":ServiceID ")
                .append(SMGPConstant.STRING_LENGTH_GREAT).append("10")));
        }
        if (feeType == null) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":FeeType ").append(
                SMGPConstant.STRING_NULL)));
        }
        if (feeType.length() > 2) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":FeeType ")
                .append(SMGPConstant.STRING_LENGTH_GREAT).append("2")));
        }
        if (feeCode == null) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":FeeCode ").append(
                SMGPConstant.STRING_NULL)));
        }
        if (feeCode.length() > 6) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":FeeCode ")
                .append(SMGPConstant.STRING_LENGTH_GREAT).append("6")));
        }
        if (fixedFee == null) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":FixedFee ").append(
                SMGPConstant.STRING_NULL)));
        }
        if (fixedFee.length() > 6) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":FixedFee ")
                .append(SMGPConstant.STRING_LENGTH_GREAT).append("6")));
        }
        if (msgFormat < 0 || msgFormat > 255) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":MsgFormat ").append(
                SMGPConstant.INT_SCOPE_ERROR)));
        }
        if (srcTermId == null) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":SrcTermID ").append(
                SMGPConstant.STRING_NULL)));
        }
        //        if (srcTermId.length() > 21) {
        //            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
        //                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":SrcTermID ")
        //                .append(SMGPConstant.STRING_LENGTH_GREAT).append("21")));
        //        }
        if (chargeTermId == null) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":ChargeTermID ").append(
                SMGPConstant.STRING_NULL)));
        }
        if (chargeTermId.length() > 21) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":ChargeTermID ")
                .append(SMGPConstant.STRING_LENGTH_GREAT).append("21")));
        }
        if (destTermId == null) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":DestTermID ").append(
                SMGPConstant.STRING_NULL)));
        }
        if (destTermId.length > 100) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":").append(
                SMGPConstant.DESTTERMID_ERROR)));
        }
        for (int i = 0; i < destTermId.length; i++) {
            if (destTermId[i].length() > 21) {
                throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                    .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":one DestTermID ")
                    .append(SMGPConstant.STRING_LENGTH_GREAT).append("21")));
            }
        }

        if (msgContent == null) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":MsgContent ").append(
                SMGPConstant.STRING_NULL)));
        }
        if (msgContent.length() > 252) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":MsgContent ")
                .append(SMGPConstant.STRING_LENGTH_GREAT).append("252")));
        }
        if (reserve != null && reserve.length() > 8) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":reserve ")
                .append(SMGPConstant.STRING_LENGTH_GREAT).append("8")));
        }

        //tp_pid
        if (tp_pid >= 0) {
            tlvs.put(TlvId.TP_pid, new Tlv(TlvId.TP_pid, String.valueOf(tp_pid)));
        }
        //tp_udhi
        if (tp_udhi >= 0) {
            tlvs.put(TlvId.TP_udhi, new Tlv(TlvId.TP_udhi, String.valueOf(tp_udhi)));
        }
        //linkId
        if (linkId != null) {
            tlvs.put(TlvId.LinkID, new Tlv(TlvId.LinkID, linkId));
        }
        //msgSrc
        if (msgSrc != null) {
            tlvs.put(TlvId.MsgSrc, new Tlv(TlvId.MsgSrc, msgSrc));
        }
        //ChargeUserType
        if (ChargeUserType >= 0) {
            tlvs.put(TlvId.ChargeUserType,
                new Tlv(TlvId.ChargeUserType, String.valueOf(ChargeUserType)));
        }
        //ChargeTermType
        if (ChargeTermType >= 0) {
            tlvs.put(TlvId.ChargeTermType,
                new Tlv(TlvId.ChargeTermType, String.valueOf(ChargeTermType)));
        }
        //ChargeTermPseudo
        if (ChargeTermPseudo != null) {
            tlvs.put(TlvId.ChargeTermPseudo, new Tlv(TlvId.ChargeTermPseudo, ChargeTermPseudo));
        }
        //DestTermType
        if (DestTermType >= 0) {
            tlvs.put(TlvId.DestTermType, new Tlv(TlvId.DestTermType, String.valueOf(DestTermType)));
        }
        //DestTermPseudo
        if (DestTermPseudo != null) {
            tlvs.put(TlvId.DestTermPseudo, new Tlv(TlvId.DestTermPseudo, DestTermPseudo));
        }
        //pkTotal
        if (pkTotal >= 0) {
            tlvs.put(TlvId.PkTotal, new Tlv(TlvId.PkTotal, String.valueOf(pkTotal)));
        }
        //pkNumber
        if (pkNumber >= 0) {
            tlvs.put(TlvId.PkNumber, new Tlv(TlvId.PkNumber, String.valueOf(pkNumber)));
        }
        //submitMsgType
        if (submitMsgType >= 0) {
            tlvs.put(TlvId.SubmitMsgType,
                new Tlv(TlvId.SubmitMsgType, String.valueOf(submitMsgType)));
        }
        //spDealResult
        if (spDealResult >= 0) {
            tlvs.put(TlvId.SPDealResult, new Tlv(TlvId.SPDealResult, String.valueOf(spDealResult)));
        }
        //mserviceId
        if (mserviceId != null) {
            tlvs.put(TlvId.Mserviceid, new Tlv(TlvId.Mserviceid, mserviceId));
        }

        int len = 0;//total length

        //content length encode 
        super.msgFormat = msgFormat;
        int content_len = 0;
        byte[] msgContentByte = getMsgContent(msgContent, msgFormat);
        content_len = msgContentByte.length;

        //base content length
        int base_len = 126 + 21 * destTermId.length + content_len;

        //tlv content length
        byte[] tlvsBuf = TlvUtil.mapToTlvs(tlvs);
        int tlv_len = tlvsBuf.length;

        len = base_len + tlv_len;

        super.buf = new byte[len];
        TypeConvert.int2byte(len, super.buf, 0);
        TypeConvert.int2byte(RequestId.SUBMIT, super.buf, 4);
        super.buf[12] = (byte) msgType;
        super.buf[13] = (byte) needReport;
        super.buf[14] = (byte) priority;
        System.arraycopy(serviceId.getBytes(), 0, super.buf, 15, serviceId.length());
        System.arraycopy(feeType.getBytes(), 0, super.buf, 25, feeType.length());
        System.arraycopy(feeCode.getBytes(), 0, super.buf, 27, feeCode.length());
        System.arraycopy(fixedFee.getBytes(), 0, super.buf, 33, fixedFee.length());
        super.buf[39] = (byte) msgFormat;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        if (validTime != null) {
            String tmpTime = String.valueOf(dateFormat.format(validTime)).concat("032+");
            System.arraycopy(tmpTime.getBytes(), 0, super.buf, 40, 16);
        }
        if (atTime != null) {
            String tmpTime = String.valueOf(dateFormat.format(atTime)).concat("032+");
            System.arraycopy(tmpTime.getBytes(), 0, super.buf, 57, 16);
        }
        System.arraycopy(srcTermId.getBytes(), 0, super.buf, 74, srcTermId.length());
        System.arraycopy(chargeTermId.getBytes(), 0, super.buf, 95, chargeTermId.length());
        super.buf[116] = (byte) destTermId.length;
        int i = 0;
        for (i = 0; i < destTermId.length; i++) {
            System.arraycopy(destTermId[i].getBytes(), 0, super.buf, 117 + i * 21,
                destTermId[i].length());
        }

        int loc = 117 + i * 21;
        super.buf[loc] = (byte) content_len;
        System.arraycopy(msgContentByte, 0, super.buf, loc + 1, content_len);
        loc = loc + 1 + content_len;
        if (reserve != null) {
            System.arraycopy(reserve.getBytes(), 0, super.buf, loc, reserve.length());
        }

        //tlvs copy to super.buf
        System.arraycopy(tlvsBuf, 0, super.buf, base_len, tlv_len);

        strBuf = new StringBuffer(1000);
        strBuf.append(",MsgType=".concat(String.valueOf(msgType)));
        strBuf.append(",NeedReport=".concat(String.valueOf(needReport)));
        strBuf.append(",Priority=".concat(String.valueOf(priority)));
        strBuf.append(",ServiceID=".concat(String.valueOf(serviceId)));
        strBuf.append(",FeeType=".concat(String.valueOf(feeType)));
        strBuf.append(",FeeCode=".concat(String.valueOf(feeCode)));
        strBuf.append(",FixedFee=".concat(String.valueOf(fixedFee)));
        strBuf.append(",MsgFormat=".concat(String.valueOf(msgFormat)));
        if (validTime != null) {
            strBuf.append(",ValidTime=".concat(String.valueOf(dateFormat.format(validTime))));
        } else {
            strBuf.append(",ValidTime=null");
        }
        if (atTime != null) {
            strBuf.append(",AtTime=".concat(String.valueOf(dateFormat.format(atTime))));
        } else {
            strBuf.append(",at_Time=null");
        }
        strBuf.append(",SrcTermID=".concat(String.valueOf(srcTermId)));
        strBuf.append(",ChargeTermID=".concat(String.valueOf(chargeTermId)));
        strBuf.append(",DestTermIDCount=".concat(String.valueOf(destTermId.length)));
        for (int t = 0; t < destTermId.length; t++) {
            strBuf.append(String.valueOf((new StringBuffer(",DestTermID[")).append(t).append("]=")
                .append(destTermId[t])));
        }

        strBuf.append(",MsgLength=".concat(String.valueOf(msgContent.length())));
        strBuf.append(",MsgContent=".concat(String.valueOf(msgContent)));
        strBuf.append(",Reserve=".concat(String.valueOf(reserve)));
        strBuf.append(TlvUtil.mapToString(tlvs));
    }

    /**
     * 
     * @param msgType
     * @param needReport
     * @param priority
     * @param serviceId
     * @param feeType
     * @param feeCode
     * @param fixedFee
     * @param msgFormat
     * @param validTime
     * @param atTime
     * @param srcTermId
     * @param chargeTermId
     * @param destTermId
     * @param msgContent
     * @param reserve
     * @param tp_pid
     * @param tp_udhi
     * @param linkId
     * @param msgSrc
     * @param ChargeUserType
     * @param ChargeTermType
     * @param ChargeTermPseudo
     * @param DestTermType
     * @param DestTermPseudo
     * @param pkTotal
     * @param pkNumber
     * @param submitMsgType
     * @param spDealResult
     * @param mserviceId
     * @throws IllegalArgumentException
     * @throws UnsupportedEncodingException 
     */
    public SMGP3SubmitMessage(int msgType, int needReport, int priority, String serviceId,
                              String feeType, String feeCode, String fixedFee, int msgFormat,
                              Date validTime, Date atTime, String srcTermId, String chargeTermId,
                              String destTermId[], byte[] msgContent, String reserve, int tp_pid,
                              int tp_udhi, String linkId, String msgSrc, int ChargeUserType,
                              int ChargeTermType, String ChargeTermPseudo, int DestTermType,
                              String DestTermPseudo, int pkTotal, int pkNumber, int submitMsgType,
                              int spDealResult, String mserviceId) throws IllegalArgumentException,
                                                                  UnsupportedEncodingException {

        if (msgType < 0 || msgType > 255) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":MsgType ").append(
                SMGPConstant.INT_SCOPE_ERROR)));
        }
        if (needReport < 0 || needReport > 255) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":NeedReport ").append(
                SMGPConstant.INT_SCOPE_ERROR)));
        }
        if (priority < 0 || priority > 9) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":").append(
                SMGPConstant.PRIORITY_ERROR)));
        }
        if (serviceId == null) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":ServiceID ").append(
                SMGPConstant.STRING_NULL)));
        }
        if (serviceId.length() > 10) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":ServiceID ")
                .append(SMGPConstant.STRING_LENGTH_GREAT).append("10")));
        }
        if (feeType == null) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":FeeType ").append(
                SMGPConstant.STRING_NULL)));
        }
        if (feeType.length() > 2) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":FeeType ")
                .append(SMGPConstant.STRING_LENGTH_GREAT).append("2")));
        }
        if (feeCode == null) {
            throw new IllegalArgumentException(new StringBuffer(SMGPConstant.SUBMIT_INPUT_ERROR)
                .append(":FeeCode ").append(SMGPConstant.STRING_NULL).toString());
        }
        if (feeCode.length() > 6) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":FeeCode ")
                .append(SMGPConstant.STRING_LENGTH_GREAT).append("6")));
        }
        if (fixedFee == null) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":FixedFee ").append(
                SMGPConstant.STRING_NULL)));
        }
        if (fixedFee.length() > 6) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":FixedFee ")
                .append(SMGPConstant.STRING_LENGTH_GREAT).append("6")));
        }
        if (msgFormat < 0 || msgFormat > 255) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":MsgFormat ").append(
                SMGPConstant.INT_SCOPE_ERROR)));
        }
        if (srcTermId == null) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":SrcTermID ").append(
                SMGPConstant.STRING_NULL)));
        }
        if (srcTermId.length() > 21) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":SrcTermID ")
                .append(SMGPConstant.STRING_LENGTH_GREAT).append("21")));
        }
        if (chargeTermId == null) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":ChargeTermID ").append(
                SMGPConstant.STRING_NULL)));
        }
        if (chargeTermId.length() > 21) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":ChargeTermID ")
                .append(SMGPConstant.STRING_LENGTH_GREAT).append("21")));
        }
        if (destTermId == null) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":DestTermID ").append(
                SMGPConstant.STRING_NULL)));
        }
        if (destTermId.length > 100) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":").append(
                SMGPConstant.DESTTERMID_ERROR)));
        }
        for (int i = 0; i < destTermId.length; i++) {
            if (destTermId[i].length() > 21) {
                throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                    .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":one DestTermID ")
                    .append(SMGPConstant.STRING_LENGTH_GREAT).append("21")));
            }
        }

        if (msgContent == null) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":MsgContent ").append(
                SMGPConstant.STRING_NULL)));
        }
        if (msgContent.length > 140) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":MsgContent ")
                .append(SMGPConstant.STRING_LENGTH_GREAT).append("140")));
        }
        if (reserve != null && reserve.length() > 8) {
            throw new IllegalArgumentException(String.valueOf((new StringBuffer(String
                .valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))).append(":reserve ")
                .append(SMGPConstant.STRING_LENGTH_GREAT).append("8")));
        }

        //tp_pid
        if (tp_pid >= 0) {
            tlvs.put(TlvId.TP_pid, new Tlv(TlvId.TP_pid, String.valueOf(tp_pid)));
        }
        //tp_udhi
        if (tp_udhi >= 0) {
            tlvs.put(TlvId.TP_udhi, new Tlv(TlvId.TP_udhi, String.valueOf(tp_udhi)));
        }
        //linkId
        if (linkId != null) {
            tlvs.put(TlvId.LinkID, new Tlv(TlvId.LinkID, linkId));
        }
        //msgSrc
        if (msgSrc != null) {
            tlvs.put(TlvId.MsgSrc, new Tlv(TlvId.MsgSrc, msgSrc));
        }
        //ChargeUserType
        if (ChargeUserType >= 0) {
            tlvs.put(TlvId.ChargeUserType,
                new Tlv(TlvId.ChargeUserType, String.valueOf(ChargeUserType)));
        }
        //ChargeTermType
        if (ChargeTermType >= 0) {
            tlvs.put(TlvId.ChargeTermType,
                new Tlv(TlvId.ChargeTermType, String.valueOf(ChargeTermType)));
        }
        //ChargeTermPseudo
        if (ChargeTermPseudo != null) {
            tlvs.put(TlvId.ChargeTermPseudo, new Tlv(TlvId.ChargeTermPseudo, ChargeTermPseudo));
        }
        //DestTermType
        if (DestTermType >= 0) {
            tlvs.put(TlvId.DestTermType, new Tlv(TlvId.DestTermType, String.valueOf(DestTermType)));
        }
        //DestTermPseudo
        if (DestTermPseudo != null) {
            tlvs.put(TlvId.DestTermPseudo, new Tlv(TlvId.DestTermPseudo, DestTermPseudo));
        }
        //pkTotal
        if (pkTotal >= 0) {
            tlvs.put(TlvId.PkTotal, new Tlv(TlvId.PkTotal, String.valueOf(pkTotal)));
        }
        //pkNumber
        if (pkNumber >= 0) {
            tlvs.put(TlvId.PkNumber, new Tlv(TlvId.PkNumber, String.valueOf(pkNumber)));
        }
        //submitMsgType
        if (submitMsgType >= 0) {
            tlvs.put(TlvId.SubmitMsgType,
                new Tlv(TlvId.SubmitMsgType, String.valueOf(submitMsgType)));
        }
        //spDealResult
        if (spDealResult >= 0) {
            tlvs.put(TlvId.SPDealResult, new Tlv(TlvId.SPDealResult, String.valueOf(spDealResult)));
        }
        //mserviceId
        if (mserviceId != null) {
            tlvs.put(TlvId.Mserviceid, new Tlv(TlvId.Mserviceid, mserviceId));
        }

        int len = 0;//total length

        //content length encode 
        super.msgFormat = msgFormat;
        int content_len = 0;
        byte[] msgContentByte = msgContent;
        content_len = msgContentByte.length;

        //base content length
        int base_len = 126 + 21 * destTermId.length + content_len;

        //tlv content length
        byte[] tlvsBuf = TlvUtil.mapToTlvs(tlvs);
        int tlv_len = tlvsBuf.length;

        len = base_len + tlv_len;

        super.buf = new byte[len];
        TypeConvert.int2byte(len, super.buf, 0);
        TypeConvert.int2byte(RequestId.SUBMIT, super.buf, 4);
        super.buf[12] = (byte) msgType;
        super.buf[13] = (byte) needReport;
        super.buf[14] = (byte) priority;
        System.arraycopy(serviceId.getBytes(), 0, super.buf, 15, serviceId.length());
        System.arraycopy(feeType.getBytes(), 0, super.buf, 25, feeType.length());
        System.arraycopy(feeCode.getBytes(), 0, super.buf, 27, feeCode.length());
        System.arraycopy(fixedFee.getBytes(), 0, super.buf, 33, fixedFee.length());
        super.buf[39] = (byte) msgFormat;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        if (validTime != null) {
            String tmpTime = String.valueOf(dateFormat.format(validTime)).concat("032+");
            System.arraycopy(tmpTime.getBytes(), 0, super.buf, 40, 16);
        }
        if (atTime != null) {
            String tmpTime = String.valueOf(dateFormat.format(atTime)).concat("032+");
            System.arraycopy(tmpTime.getBytes(), 0, super.buf, 57, 16);
        }
        System.arraycopy(srcTermId.getBytes(), 0, super.buf, 74, srcTermId.length());
        System.arraycopy(chargeTermId.getBytes(), 0, super.buf, 95, chargeTermId.length());
        super.buf[116] = (byte) destTermId.length;
        int i = 0;
        for (i = 0; i < destTermId.length; i++) {
            System.arraycopy(destTermId[i].getBytes(), 0, super.buf, 117 + i * 21,
                destTermId[i].length());
        }

        int loc = 117 + i * 21;
        super.buf[loc] = (byte) content_len;
        System.arraycopy(msgContentByte, 0, super.buf, loc + 1, content_len);
        loc = loc + 1 + content_len;
        if (reserve != null) {
            System.arraycopy(reserve.getBytes(), 0, super.buf, loc, reserve.length());
        }

        //tlvs copy to super.buf
        System.arraycopy(tlvsBuf, 0, super.buf, base_len, tlv_len);

        strBuf = new StringBuffer(1000);
        strBuf.append(",MsgType=".concat(String.valueOf(msgType)));
        strBuf.append(",NeedReport=".concat(String.valueOf(needReport)));
        strBuf.append(",Priority=".concat(String.valueOf(priority)));
        strBuf.append(",ServiceID=".concat(String.valueOf(serviceId)));
        strBuf.append(",FeeType=".concat(String.valueOf(feeType)));
        strBuf.append(",FeeCode=".concat(String.valueOf(feeCode)));
        strBuf.append(",FixedFee=".concat(String.valueOf(fixedFee)));
        strBuf.append(",MsgFormat=".concat(String.valueOf(msgFormat)));
        if (validTime != null) {
            strBuf.append(",ValidTime=".concat(String.valueOf(dateFormat.format(validTime))));
        } else {
            strBuf.append(",ValidTime=null");
        }
        if (atTime != null) {
            strBuf.append(",AtTime=".concat(String.valueOf(dateFormat.format(atTime))));
        } else {
            strBuf.append(",at_Time=null");
        }
        strBuf.append(",SrcTermID=".concat(String.valueOf(srcTermId)));
        strBuf.append(",ChargeTermID=".concat(String.valueOf(chargeTermId)));
        strBuf.append(",DestTermIDCount=".concat(String.valueOf(destTermId.length)));
        for (int t = 0; t < destTermId.length; t++) {
            strBuf.append(String.valueOf((new StringBuffer(",DestTermID[")).append(t).append("]=")
                .append(destTermId[t])));
        }

        strBuf.append(",MsgLength=".concat(String.valueOf(msgContent.length)));
        strBuf.append(",MsgContent=".concat(String.valueOf(msgContent)));
        strBuf.append(",Reserve=".concat(String.valueOf(reserve)));
        strBuf.append(TlvUtil.mapToString(tlvs));
    }

    /**
     * Getter method for property <tt>strBuf</tt>.
     * 
     * @return property value of strBuf
     */
    public StringBuffer getStrBuf() {
        return strBuf;
    }

    /**
     * Setter method for property <tt>strBuf</tt>.
     * 
     * @param strBuf value to be assigned to property strBuf
     */
    public void setStrBuf(StringBuffer strBuf) {
        this.strBuf = strBuf;
    }

    /**
     * Getter method for property <tt>tlvs</tt>.
     * 
     * @return property value of tlvs
     */
    public Map<Integer, Tlv> getTlvs() {
        return tlvs;
    }

    /**
     * Setter method for property <tt>tlvs</tt>.
     * 
     * @param tlvs value to be assigned to property tlvs
     */
    public void setTlvs(Map<Integer, Tlv> tlvs) {
        this.tlvs = tlvs;
    }

    /**
     * Getter method for property <tt>msgType</tt>.
     * 
     * @return property value of msgType
     */
    public int getMsgType() {
        return msgType;
    }

    /**
     * Setter method for property <tt>msgType</tt>.
     * 
     * @param msgType value to be assigned to property msgType
     */
    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    /**
     * Getter method for property <tt>needReport</tt>.
     * 
     * @return property value of needReport
     */
    public int getNeedReport() {
        return needReport;
    }

    /**
     * Setter method for property <tt>needReport</tt>.
     * 
     * @param needReport value to be assigned to property needReport
     */
    public void setNeedReport(int needReport) {
        this.needReport = needReport;
    }

    /**
     * Getter method for property <tt>priority</tt>.
     * 
     * @return property value of priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Setter method for property <tt>priority</tt>.
     * 
     * @param priority value to be assigned to property priority
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Getter method for property <tt>serviceId</tt>.
     * 
     * @return property value of serviceId
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Setter method for property <tt>serviceId</tt>.
     * 
     * @param serviceId value to be assigned to property serviceId
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * Getter method for property <tt>feeType</tt>.
     * 
     * @return property value of feeType
     */
    public String getFeeType() {
        return feeType;
    }

    /**
     * Setter method for property <tt>feeType</tt>.
     * 
     * @param feeType value to be assigned to property feeType
     */
    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    /**
     * Getter method for property <tt>feeCode</tt>.
     * 
     * @return property value of feeCode
     */
    public String getFeeCode() {
        return feeCode;
    }

    /**
     * Setter method for property <tt>feeCode</tt>.
     * 
     * @param feeCode value to be assigned to property feeCode
     */
    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    /**
     * Getter method for property <tt>fixedFee</tt>.
     * 
     * @return property value of fixedFee
     */
    public String getFixedFee() {
        return fixedFee;
    }

    /**
     * Setter method for property <tt>fixedFee</tt>.
     * 
     * @param fixedFee value to be assigned to property fixedFee
     */
    public void setFixedFee(String fixedFee) {
        this.fixedFee = fixedFee;
    }

    /**
     * Getter method for property <tt>msgFormat</tt>.
     * 
     * @return property value of msgFormat
     */
    public int getMsgFormat() {
        return msgFormat;
    }

    /**
     * Setter method for property <tt>msgFormat</tt>.
     * 
     * @param msgFormat value to be assigned to property msgFormat
     */
    public void setMsgFormat(int msgFormat) {
        this.msgFormat = msgFormat;
    }

    /**
     * Getter method for property <tt>validTime</tt>.
     * 
     * @return property value of validTime
     */
    public Date getValidTime() {
        return validTime;
    }

    /**
     * Setter method for property <tt>validTime</tt>.
     * 
     * @param validTime value to be assigned to property validTime
     */
    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

    /**
     * Getter method for property <tt>atTime</tt>.
     * 
     * @return property value of atTime
     */
    public Date getAtTime() {
        return atTime;
    }

    /**
     * Setter method for property <tt>atTime</tt>.
     * 
     * @param atTime value to be assigned to property atTime
     */
    public void setAtTime(Date atTime) {
        this.atTime = atTime;
    }

    /**
     * Getter method for property <tt>srcTermId</tt>.
     * 
     * @return property value of srcTermId
     */
    public String getSrcTermId() {
        return srcTermId;
    }

    /**
     * Setter method for property <tt>srcTermId</tt>.
     * 
     * @param srcTermId value to be assigned to property srcTermId
     */
    public void setSrcTermId(String srcTermId) {
        this.srcTermId = srcTermId;
    }

    /**
     * Getter method for property <tt>chargeTermId</tt>.
     * 
     * @return property value of chargeTermId
     */
    public String getChargeTermId() {
        return chargeTermId;
    }

    /**
     * Setter method for property <tt>chargeTermId</tt>.
     * 
     * @param chargeTermId value to be assigned to property chargeTermId
     */
    public void setChargeTermId(String chargeTermId) {
        this.chargeTermId = chargeTermId;
    }

    /**
     * Getter method for property <tt>destTermId</tt>.
     * 
     * @return property value of destTermId
     */
    public String[] getDestTermId() {
        return destTermId;
    }

    /**
     * Setter method for property <tt>destTermId</tt>.
     * 
     * @param destTermId value to be assigned to property destTermId
     */
    public void setDestTermId(String[] destTermId) {
        this.destTermId = destTermId;
    }

    /**
     * Getter method for property <tt>msgContent</tt>.
     * 
     * @return property value of msgContent
     */
    public String getMsgContent() {
        return msgContent;
    }

    /**
     * Setter method for property <tt>msgContent</tt>.
     * 
     * @param msgContent value to be assigned to property msgContent
     */
    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    /**
     * Getter method for property <tt>reserve</tt>.
     * 
     * @return property value of reserve
     */
    public String getReserve() {
        return reserve;
    }

    /**
     * Setter method for property <tt>reserve</tt>.
     * 
     * @param reserve value to be assigned to property reserve
     */
    public void setReserve(String reserve) {
        this.reserve = reserve;
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
     * Setter method for property <tt>tp_pid</tt>.
     * 
     * @param tp_pid value to be assigned to property tp_pid
     */
    public void setTp_pid(int tp_pid) {
        this.tp_pid = tp_pid;
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
     * Setter method for property <tt>tp_udhi</tt>.
     * 
     * @param tp_udhi value to be assigned to property tp_udhi
     */
    public void setTp_udhi(int tp_udhi) {
        this.tp_udhi = tp_udhi;
    }

    /**
     * Getter method for property <tt>linkId</tt>.
     * 
     * @return property value of linkId
     */
    public String getLinkId() {
        return linkId;
    }

    /**
     * Setter method for property <tt>linkId</tt>.
     * 
     * @param linkId value to be assigned to property linkId
     */
    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    /**
     * Getter method for property <tt>msgSrc</tt>.
     * 
     * @return property value of msgSrc
     */
    public String getMsgSrc() {
        return msgSrc;
    }

    /**
     * Setter method for property <tt>msgSrc</tt>.
     * 
     * @param msgSrc value to be assigned to property msgSrc
     */
    public void setMsgSrc(String msgSrc) {
        this.msgSrc = msgSrc;
    }

    /**
     * Getter method for property <tt>chargeUserType</tt>.
     * 
     * @return property value of chargeUserType
     */
    public int getChargeUserType() {
        return chargeUserType;
    }

    /**
     * Setter method for property <tt>chargeUserType</tt>.
     * 
     * @param chargeUserType value to be assigned to property chargeUserType
     */
    public void setChargeUserType(int chargeUserType) {
        this.chargeUserType = chargeUserType;
    }

    /**
     * Getter method for property <tt>chargeTermType</tt>.
     * 
     * @return property value of chargeTermType
     */
    public int getChargeTermType() {
        return chargeTermType;
    }

    /**
     * Setter method for property <tt>chargeTermType</tt>.
     * 
     * @param chargeTermType value to be assigned to property chargeTermType
     */
    public void setChargeTermType(int chargeTermType) {
        this.chargeTermType = chargeTermType;
    }

    /**
     * Getter method for property <tt>chargeTermPseudo</tt>.
     * 
     * @return property value of chargeTermPseudo
     */
    public String getChargeTermPseudo() {
        return chargeTermPseudo;
    }

    /**
     * Setter method for property <tt>chargeTermPseudo</tt>.
     * 
     * @param chargeTermPseudo value to be assigned to property chargeTermPseudo
     */
    public void setChargeTermPseudo(String chargeTermPseudo) {
        this.chargeTermPseudo = chargeTermPseudo;
    }

    /**
     * Getter method for property <tt>destTermType</tt>.
     * 
     * @return property value of destTermType
     */
    public int getDestTermType() {
        return destTermType;
    }

    /**
     * Setter method for property <tt>destTermType</tt>.
     * 
     * @param destTermType value to be assigned to property destTermType
     */
    public void setDestTermType(int destTermType) {
        this.destTermType = destTermType;
    }

    /**
     * Getter method for property <tt>destTermPseudo</tt>.
     * 
     * @return property value of DestTermPseudo
     */
    public String getDestTermPseudo() {
        return DestTermPseudo;
    }

    /**
     * Setter method for property <tt>destTermPseudo</tt>.
     * 
     * @param DestTermPseudo value to be assigned to property destTermPseudo
     */
    public void setDestTermPseudo(String destTermPseudo) {
        DestTermPseudo = destTermPseudo;
    }

    /**
     * Getter method for property <tt>pkTotal</tt>.
     * 
     * @return property value of pkTotal
     */
    public int getPkTotal() {
        return pkTotal;
    }

    /**
     * Setter method for property <tt>pkTotal</tt>.
     * 
     * @param pkTotal value to be assigned to property pkTotal
     */
    public void setPkTotal(int pkTotal) {
        this.pkTotal = pkTotal;
    }

    /**
     * Getter method for property <tt>pkNumber</tt>.
     * 
     * @return property value of pkNumber
     */
    public int getPkNumber() {
        return pkNumber;
    }

    /**
     * Setter method for property <tt>pkNumber</tt>.
     * 
     * @param pkNumber value to be assigned to property pkNumber
     */
    public void setPkNumber(int pkNumber) {
        this.pkNumber = pkNumber;
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
     * Setter method for property <tt>submitMsgType</tt>.
     * 
     * @param submitMsgType value to be assigned to property submitMsgType
     */
    public void setSubmitMsgType(int submitMsgType) {
        this.submitMsgType = submitMsgType;
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
     * Setter method for property <tt>spDealResult</tt>.
     * 
     * @param spDealResult value to be assigned to property spDealResult
     */
    public void setSpDealResult(int spDealResult) {
        this.spDealResult = spDealResult;
    }

    /**
     * Getter method for property <tt>mserviceId</tt>.
     * 
     * @return property value of mserviceId
     */
    public String getMserviceId() {
        return mserviceId;
    }

    /**
     * Setter method for property <tt>mserviceId</tt>.
     * 
     * @param mserviceId value to be assigned to property mserviceId
     */
    public void setMserviceId(String mserviceId) {
        this.mserviceId = mserviceId;
    }

    public static List<SMGP3SubmitMessage> getMessageInstance() {

        return null;
    }

    public String toString() {
        StringBuffer outBuf = new StringBuffer(600);
        outBuf.append("SMGPSubmitMessage: ");
        outBuf.append("PacketLength=".concat(String.valueOf(super.buf.length)));
        outBuf.append(",RequestID=2");
        outBuf.append(",SequenceID=".concat(String.valueOf(getSequenceId())));
        if (strBuf != null) {
            outBuf.append(strBuf.toString());
        }
        return outBuf.toString();
    }

    public int getRequestId() {
        return RequestId.SUBMIT;
    }
}
