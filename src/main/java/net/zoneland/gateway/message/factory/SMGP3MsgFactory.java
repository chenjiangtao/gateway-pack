/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.message.factory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3SubmitMessage;
import net.zoneland.gateway.util.Args;
import net.zoneland.gateway.util.ArrayUtil;

import org.springframework.util.StringUtils;

/**
 * 
 * @author gang
 * @version $Id: SMGP3MsgFactory.java, v 0.1 2012-8-21 下午9:13:16 gang Exp $
 */
public class SMGP3MsgFactory {

    private int    msgType          = 6;

    private int    needReport       = 0;

    private String serviceId        = "";

    private String feeType          = "";

    private String feeCode          = "";

    private String fixedFee         = "";

    private String chargeTermId     = "";

    private String reserve          = "";

    private int    tp_pid;

    private String linkId           = "";

    private String msgSrc           = "";

    private int    chargeUserType;

    private int    chargeTermType;

    private String chargeTermPseudo = "";

    private int    destTermType;

    private String destTermPseudo   = "";

    private int    submitMsgType;

    private int    spDealResult;

    private String mserviceId       = "";

    public List<SMGP3SubmitMessage> buildSMGP3SubmitMessage(String src, String[] dest,
                                                            String msgContent, int msg_Fmt,
                                                            int priority, Map<String, String> params)
                                                                                                     throws UnsupportedEncodingException {
        if (src == null || dest == null || msgContent == null) {
            return null;
        }
        List<SMGP3SubmitMessage> msgs = new ArrayList<SMGP3SubmitMessage>();
        List<String[]> list = null;
        if (dest.length > CMPP30MsgFactory.MAX_DEST) {
            list = ArrayUtil.splitArray(dest, CMPP30MsgFactory.MAX_DEST);
        } else {
            list = new ArrayList<String[]>(1);
            list.add(dest);
        }
        if (msg_Fmt == 15) {
            List<byte[]> bytesList = ArrayUtil.splitArray(msgContent, 67, 15);
            if (bytesList.size() == 1) {
                fillMsg(msgs, list, bytesList, 0, msg_Fmt, src, feeType, feeCode, priority,
                    needReport, params);
            } else {
                fillMsg(msgs, list, bytesList, 1, msg_Fmt, src, feeType, feeCode, priority,
                    needReport, params);
            }
        } else {
            byte[] msg = PMessage.getMsgContent(msgContent, msg_Fmt);
            if (msg.length > 154) {
                List<byte[]> bytesList = ArrayUtil.splitByteArray(msg, 160 - 6);
                fillMsg(msgs, list, bytesList, 1, msg_Fmt, src, feeType, feeCode, priority,
                    needReport, params);
            } else {
                List<byte[]> bytesList = new ArrayList<byte[]>(1);
                bytesList.add(msg);
                fillMsg(msgs, list, bytesList, 0, msg_Fmt, src, feeType, feeCode, priority,
                    needReport, params);
            }
        }
        return msgs;
    }

    public List<SMGP3SubmitMessage> buildSMGP3SubmitMessage(String src, String[] dest,
                                                            String msgContent, int msg_Fmt,
                                                            String feeType, String fee,
                                                            int priority, int needReport)
                                                                                         throws UnsupportedEncodingException {
        if (src == null || dest == null || msgContent == null) {
            return null;
        }
        List<SMGP3SubmitMessage> msgs = new ArrayList<SMGP3SubmitMessage>();
        List<String[]> list = null;
        if (dest.length > CMPP30MsgFactory.MAX_DEST) {
            list = ArrayUtil.splitArray(dest, CMPP30MsgFactory.MAX_DEST);
        } else {
            list = new ArrayList<String[]>(1);
            list.add(dest);
        }
        if (!StringUtils.hasText(feeType)) {
            feeType = this.feeType;
        }
        if (!StringUtils.hasText(fee)) {
            fee = this.feeCode;
        }

        List<byte[]> bytesList = ArrayUtil.splitArray(msgContent, 67, msg_Fmt);
        if (bytesList.size() == 1) {
            fillMsg(msgs, list, bytesList, 0, msg_Fmt, src, feeType, fee, priority, needReport,
                null);
        } else {
            fillMsg(msgs, list, bytesList, 1, msg_Fmt, src, feeType, fee, priority, needReport,
                null);
        }

        return msgs;
    }

    public List<SMGP3SubmitMessage> buildSMGP3SubmitMessage(String src, String[] dest,
                                                            String msgContent, int msg_Fmt,
                                                            String feeType, String fee, int priority)
                                                                                                     throws UnsupportedEncodingException {

        return buildSMGP3SubmitMessage(src, dest, msgContent, msg_Fmt, feeType, fee, priority,
            needReport);
    }

    public void fillMsg(List<SMGP3SubmitMessage> msgs, List<String[]> users,
                        List<byte[]> bytesList, int tp_Udhi, int msg_Fmt, String src,
                        String feeType, String fee, int priority, int needReport,
                        Map<String, String> params) throws IllegalArgumentException,
                                                   UnsupportedEncodingException {
        for (int j = 0, lenj = users.size(); j < lenj; ++j) {
            String[] user = users.get(j);
            for (int i = 0, len = bytesList.size(); i < len; ++i) {
                SMGP3SubmitMessage sm = null;
                if (params == null) {
                    sm = new SMGP3SubmitMessage(msgType, needReport, priority, serviceId, feeType,
                        fee, fixedFee, msg_Fmt, null, null, src, chargeTermId, user,
                        bytesList.get(i), reserve, tp_pid, tp_Udhi, linkId, msgSrc, chargeUserType,
                        chargeTermType, chargeTermPseudo, destTermType, destTermPseudo,
                        bytesList.size(), i + 1, submitMsgType, spDealResult, mserviceId);
                } else {
                    Args args = new Args(params);
                    sm = new SMGP3SubmitMessage(args.get("msgType", msgType), args.get(
                        "needReport", needReport), priority, args.get("serviceId", serviceId),
                        args.get("feeType", feeType), args.get("feeCode", fee), args.get(
                            "fixedFee", fixedFee), msg_Fmt, null, null, src, args.get(
                            "chargeTermId", chargeTermId), user, bytesList.get(i), args.get(
                            "reserve", reserve), args.get("tp_pid", tp_pid), tp_Udhi, args.get(
                            "linkId", linkId), args.get("msgSrc", msgSrc), args.get(
                            "chargeUserType", chargeUserType), args.get("chargeTermType",
                            chargeTermType), args.get("chargeTermPseudo", chargeTermPseudo),
                        args.get("destTermType", destTermType), args.get("destTermPseudo",
                            destTermPseudo), bytesList.size(), i + 1, args.get("submitMsgType",
                            submitMsgType), args.get("spDealResult", spDealResult), args.get(
                            "mserviceId", mserviceId));
                }
                msgs.add(sm);
            }
        }
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public void setNeedReport(int needReport) {
        this.needReport = needReport;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    public void setFixedFee(String fixedFee) {
        this.fixedFee = fixedFee;
    }

    public void setChargeTermId(String chargeTermId) {
        this.chargeTermId = chargeTermId;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public void setTp_pid(int tp_pid) {
        this.tp_pid = tp_pid;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public void setMsgSrc(String msgSrc) {
        this.msgSrc = msgSrc;
    }

    public void setChargeUserType(int chargeUserType) {
        this.chargeUserType = chargeUserType;
    }

    public void setChargeTermType(int chargeTermType) {
        this.chargeTermType = chargeTermType;
    }

    public void setChargeTermPseudo(String chargeTermPseudo) {
        this.chargeTermPseudo = chargeTermPseudo;
    }

    public void setDestTermType(int destTermType) {
        this.destTermType = destTermType;
    }

    public void setDestTermPseudo(String destTermPseudo) {
        this.destTermPseudo = destTermPseudo;
    }

    public void setSubmitMsgType(int submitMsgType) {
        this.submitMsgType = submitMsgType;
    }

    public void setSpDealResult(int spDealResult) {
        this.spDealResult = spDealResult;
    }

    public void setMserviceId(String mserviceId) {
        this.mserviceId = mserviceId;
    }
}
