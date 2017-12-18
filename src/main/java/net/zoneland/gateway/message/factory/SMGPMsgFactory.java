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
import net.zoneland.gateway.comm.smgp.message.SMGPSubmitMessage;
import net.zoneland.gateway.util.Args;
import net.zoneland.gateway.util.ArrayUtil;

import org.springframework.util.StringUtils;

/**
 * 
 * @author gang
 * @version $Id: SMGPMsgFactory.java, v 0.1 2012-8-21 下午8:36:34 gang Exp $
 */
public class SMGPMsgFactory {

    private int    msgType      = 6;

    private int    needReport;

    /** 业务代码，用于固定网业务。 */
    private String serviceId    = "";

    private String feeType      = "";

    private String feeCode      = "";

    private String fixedFee     = "";

    private String chargeTermId = "";

    private String reserve      = "";

    public List<SMGPSubmitMessage> buildSMGPSubmitMessage(String src, String[] dest,
                                                          String msgContent, int msg_Fmt,
                                                          int priority, Map<String, String> params)
                                                                                                   throws UnsupportedEncodingException {
        if (src == null || dest == null || msgContent == null) {
            return null;
        }
        List<SMGPSubmitMessage> msgs = new ArrayList<SMGPSubmitMessage>();

        List<String[]> list = null;
        if (dest.length > CMPP30MsgFactory.MAX_DEST) {
            list = ArrayUtil.splitArray(dest, CMPP30MsgFactory.MAX_DEST);
        } else {
            list = new ArrayList<String[]>(1);
            list.add(dest);
        }
        byte[] msg = PMessage.getMsgContent(msgContent, msg_Fmt);
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

    public List<SMGPSubmitMessage> buildSMGPSubmitMessage(String src, String[] dest,
                                                          String msgContent, int msg_Fmt,
                                                          String feeType, String fee, int priority)
                                                                                                   throws UnsupportedEncodingException {

        return buildSMGPSubmitMessage(src, dest, msgContent, msg_Fmt, feeType, fee, priority,
            needReport);

    }

    public List<SMGPSubmitMessage> buildSMGPSubmitMessage(String src, String[] dest,
                                                          String msgContent, int msg_Fmt,
                                                          String feeType, String fee, int priority,
                                                          int needReport)
                                                                         throws UnsupportedEncodingException {
        if (src == null || dest == null || msgContent == null) {
            return null;
        }
        List<SMGPSubmitMessage> msgs = new ArrayList<SMGPSubmitMessage>();
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
        if (msg_Fmt == 15) {
            List<byte[]> bytesList = ArrayUtil.splitArray(msgContent, 67, 15);
            if (bytesList.size() == 1) {
                fillMsg(msgs, list, bytesList, 0, msg_Fmt, src, String.valueOf(feeType),
                    String.valueOf(fee), priority, needReport, null);
            } else {
                fillMsg(msgs, list, bytesList, 1, msg_Fmt, src, String.valueOf(feeType),
                    String.valueOf(fee), priority, needReport, null);
            }
        } else {
            byte[] msg = PMessage.getMsgContent(msgContent, msg_Fmt);
            if (msg.length > 160) {
                List<byte[]> bytesList = ArrayUtil.splitByteArray(msg, 160 - 6);
                fillMsg(msgs, list, bytesList, 1, msg_Fmt, src, String.valueOf(feeType),
                    String.valueOf(fee), priority, needReport, null);
            } else {
                List<byte[]> bytesList = new ArrayList<byte[]>(1);
                bytesList.add(msg);
                fillMsg(msgs, list, bytesList, 0, msg_Fmt, src, String.valueOf(feeType),
                    String.valueOf(fee), priority, needReport, null);
            }
        }
        return msgs;

    }

    public void fillMsg(List<SMGPSubmitMessage> msgs, List<String[]> users, List<byte[]> bytesList,
                        int tp_Udhi, int msg_Fmt, String src, String feeType, String fee,
                        int priority, int needReport, Map<String, String> params)
                                                                                 throws IllegalArgumentException,
                                                                                 UnsupportedEncodingException {

        for (int j = 0, lenj = users.size(); j < lenj; ++j) {
            String[] user = users.get(j);

            for (int i = 0, len = bytesList.size(); i < len; ++i) {
                SMGPSubmitMessage sm = null;
                if (params == null) {
                    sm = new SMGPSubmitMessage(msgType, needReport, priority, serviceId, feeType,
                        fee, fixedFee, msg_Fmt, null, null, src, chargeTermId, user,
                        bytesList.get(i), reserve);
                } else {
                    Args args = new Args(params);
                    sm = new SMGPSubmitMessage(args.get("msgType", msgType), args.get("needReport",
                        needReport), priority, args.get("serviceId", serviceId), args.get(
                        "feeType", feeType), args.get("feeCode", fee), args.get("fixedFee",
                        fixedFee), msg_Fmt, null, null, src,
                        args.get("chargeTermId", chargeTermId), user, bytesList.get(i), args.get(
                            "reserve", reserve));
                }
                msgs.add(sm);
            }
        }

    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getNeedReport() {
        return needReport;
    }

    public void setNeedReport(int needReport) {
        this.needReport = needReport;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFeeCode() {
        return feeCode;
    }

    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    public String getFixedFee() {
        return fixedFee;
    }

    public void setFixedFee(String fixedFee) {
        this.fixedFee = fixedFee;
    }

    public String getChargeTermId() {
        return chargeTermId;
    }

    public void setChargeTermId(String chargeTermId) {
        this.chargeTermId = chargeTermId;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

}
