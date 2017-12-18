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
import net.zoneland.gateway.comm.sgip.message.SGIPSubmitMessage;
import net.zoneland.gateway.util.Args;
import net.zoneland.gateway.util.ArrayUtil;

import org.springframework.util.StringUtils;

/**
 * 
 * @author gag
 * @version $Id: SGIPMsgFactory.java, v 0.1 2012-8-21 下午5:20:29 gag Exp $
 */
public class SGIPMsgFactory {

    /** 付费号码，字符，手机号码前加“86”国别标志 */
    private String chargeNumber = "000000000000000000000";

    /** 企业代码，取值范围0-99999，字符 */
    private String corpId       = "";

    /** 业务代码，由SP定义，字符 */
    private String serviceType  = "";

    /** 取值范围0-99999，该条短消息的收费值，单位为分，由SP定义 */
    private int    feeType;

    /** 取值范围0-99999，赠送用户的话费，单位为分，由SP定义 */
    private String feeValue     = "";

    /** 赠送用户的话费，单位为分，由SP定义 */
    private String givenValue   = "";

    private int    agentFlag;

    private int    morelatetoMTFlag;

    private int    reportFlag;

    private int    tp_pid;

    /**
     * 信息类型： 0-短消息信息 其它：待定 十六进制数字
     */
    private int    messageType  = 0;

    /** 保留 */
    private String reserve      = "";

    /**
     * 
     * @param src
     * @param dest
     * @param msgContent
     * @param msg_Fmt
     * @return
     * @throws UnsupportedEncodingException
     */
    public List<SGIPSubmitMessage> buildSGIPSubmitMessage(String src, String[] dest,
                                                          String msgContent, int msg_Fmt,
                                                          int priority, Map<String, String> params)
                                                                                                   throws UnsupportedEncodingException {
        if (src == null || dest == null || msgContent == null) {
            return null;
        }
        List<SGIPSubmitMessage> msgs = new ArrayList<SGIPSubmitMessage>();

        List<String[]> list = null;
        if (dest.length > 1) {
            list = ArrayUtil.splitArray(dest, 1);
        } else {
            list = new ArrayList<String[]>(1);
            list.add(dest);
        }

        if (msg_Fmt == 15) {
            List<byte[]> bytesList = ArrayUtil.splitArray(msgContent, 44, 15);
            if (bytesList.size() == 1) {
                fillMsg(msgs, list, bytesList, 0, msg_Fmt, src, feeType, feeValue, priority,
                    reportFlag, params);
            } else {
                fillMsg(msgs, list, bytesList, 1, msg_Fmt, src, feeType, feeValue, priority,
                    reportFlag, params);
            }
        } else {
            byte[] msg = PMessage.getMsgContent(msgContent, msg_Fmt);
            if (msg.length > 130) {
                List<byte[]> bytesList = ArrayUtil.splitByteArray(msg, 130 - 6);
                fillMsg(msgs, list, bytesList, 1, msg_Fmt, src, feeType, feeValue, priority,
                    reportFlag, params);
            } else {
                List<byte[]> bytesList = new ArrayList<byte[]>(1);
                bytesList.add(msg);
                fillMsg(msgs, list, bytesList, 0, msg_Fmt, src, feeType, feeValue, priority,
                    reportFlag, params);
            }
        }
        return msgs;

    }

    public List<SGIPSubmitMessage> buildSGIPSubmitMessage(String src, String[] dest,
                                                          String msgContent, int msg_Fmt,
                                                          int feeType, String feeValue, int priority)
                                                                                                     throws UnsupportedEncodingException {
        if (src == null || dest == null || msgContent == null) {
            return null;
        }
        List<SGIPSubmitMessage> msgs = new ArrayList<SGIPSubmitMessage>();

        List<String[]> list = null;
        if (dest.length > 1) {
            list = ArrayUtil.splitArray(dest, 1);
        } else {
            list = new ArrayList<String[]>(1);
            list.add(dest);
        }
        if (!StringUtils.hasText(feeValue)) {
            feeValue = this.feeValue;
        }
        if (msg_Fmt == 15) {
            List<byte[]> bytesList = ArrayUtil.splitArray(msgContent, 65, 15);
            if (bytesList.size() == 1) {
                fillMsg(msgs, list, bytesList, 0, msg_Fmt, src, feeType, String.valueOf(feeValue),
                    priority, reportFlag, null);
            } else {
                fillMsg(msgs, list, bytesList, 1, msg_Fmt, src, feeType, String.valueOf(feeValue),
                    priority, reportFlag, null);
            }
        } else {
            byte[] msg = PMessage.getMsgContent(msgContent, msg_Fmt);
            if (msg.length > 130) {
                List<byte[]> bytesList = ArrayUtil.splitByteArray(msg, 130 - 6);
                fillMsg(msgs, list, bytesList, 1, msg_Fmt, src, feeType, String.valueOf(feeValue),
                    priority, reportFlag, null);
            } else {
                List<byte[]> bytesList = new ArrayList<byte[]>(1);
                bytesList.add(msg);
                fillMsg(msgs, list, bytesList, 0, msg_Fmt, src, feeType, String.valueOf(feeValue),
                    priority, reportFlag, null);
            }
        }
        return msgs;

    }

    public List<SGIPSubmitMessage> buildSGIPSubmitMessage(String src, String[] dest,
                                                          String msgContent, int msg_Fmt,
                                                          int feeType, String feeValue,
                                                          int priority, String corpId)
                                                                                      throws UnsupportedEncodingException {

        return buildSGIPSubmitMessage(src, dest, msgContent, msg_Fmt, feeType, feeValue, priority,
            corpId, reportFlag);

    }

    public List<SGIPSubmitMessage> buildSGIPSubmitMessage(String src, String[] dest,
                                                          String msgContent, int msg_Fmt,
                                                          int feeType, String feeValue,
                                                          int priority, String corpId,
                                                          int reportFlag)
                                                                         throws UnsupportedEncodingException {
        if (src == null || dest == null || msgContent == null) {
            return null;
        }
        List<SGIPSubmitMessage> msgs = new ArrayList<SGIPSubmitMessage>();

        List<String[]> list = null;
        if (dest.length > 1) {
            list = ArrayUtil.splitArray(dest, 1);
        } else {
            list = new ArrayList<String[]>(1);
            list.add(dest);
        }
        if (!StringUtils.hasText(feeValue)) {
            feeValue = this.feeValue;
        }
        if (msg_Fmt == 15) {
            List<byte[]> bytesList = ArrayUtil.splitArray(msgContent, 65, 15);
            if (bytesList.size() == 1) {
                fillMsg(corpId, msgs, list, bytesList, 0, msg_Fmt, src, feeType,
                    String.valueOf(feeValue), priority, reportFlag);
            } else {
                fillMsg(corpId, msgs, list, bytesList, 1, msg_Fmt, src, feeType,
                    String.valueOf(feeValue), priority, reportFlag);
            }
        } else {
            byte[] msg = PMessage.getMsgContent(msgContent, msg_Fmt);
            if (msg.length > 130) {
                List<byte[]> bytesList = ArrayUtil.splitByteArray(msg, 130 - 6);
                fillMsg(corpId, msgs, list, bytesList, 1, msg_Fmt, src, feeType,
                    String.valueOf(feeValue), priority, reportFlag);
            } else {
                List<byte[]> bytesList = new ArrayList<byte[]>(1);
                bytesList.add(msg);
                fillMsg(corpId, msgs, list, bytesList, 0, msg_Fmt, src, feeType,
                    String.valueOf(feeValue), priority, reportFlag);
            }
        }
        return msgs;

    }

    public List<SGIPSubmitMessage> buildSGIPSubmitMessage(String src, String[] dest,
                                                          String msgContent, int msg_Fmt,
                                                          int priority)
                                                                       throws UnsupportedEncodingException {
        return buildSGIPSubmitMessage(src, dest, msgContent, msg_Fmt, this.feeType, this.feeValue,
            priority);

    }

    public void fillMsg(String corpId, List<SGIPSubmitMessage> msgs, List<String[]> users,
                        List<byte[]> bytesList, int tp_Udhi, int msg_Fmt, String src, int feeType,
                        String fee, int priority, int reportFlag) {
        for (int j = 0, lenj = users.size(); j < lenj; ++j) {
            String[] user = users.get(j);
            for (int i = 0, len = bytesList.size(); i < len; ++i) {
                SGIPSubmitMessage sm = null;

                sm = new SGIPSubmitMessage(src, chargeNumber, user, corpId, serviceType, feeType,
                    fee, givenValue, agentFlag, morelatetoMTFlag, priority, null, null, reportFlag,
                    tp_pid, tp_Udhi, msg_Fmt, messageType, bytesList.get(i).length,
                    bytesList.get(i), reserve);

                msgs.add(sm);
            }
        }
    }

    public void fillMsg(List<SGIPSubmitMessage> msgs, List<String[]> users, List<byte[]> bytesList,
                        int tp_Udhi, int msg_Fmt, String src, int feeType, String fee,
                        int priority, int reportFlag, Map<String, String> params) {
        for (int j = 0, lenj = users.size(); j < lenj; ++j) {
            String[] user = users.get(j);
            for (int i = 0, len = bytesList.size(); i < len; ++i) {
                SGIPSubmitMessage sm = null;
                if (params == null) {
                    sm = new SGIPSubmitMessage(src, chargeNumber, user, corpId, serviceType,
                        feeType, fee, givenValue, agentFlag, morelatetoMTFlag, priority, null,
                        null, reportFlag, tp_pid, tp_Udhi, msg_Fmt, messageType,
                        bytesList.get(i).length, bytesList.get(i), reserve);
                } else {
                    Args args = new Args(params);
                    sm = new SGIPSubmitMessage(src, args.get("chargeNumber", chargeNumber), user,
                        args.get("corpId", corpId), args.get("serviceType", serviceType), args.get(
                            "feeType", feeType), args.get("feeValue", fee), args.get("givenValue",
                            givenValue), args.get("agentFlag", agentFlag), args.get(
                            "morelatetoMTFlag", morelatetoMTFlag), priority, null, null, args.get(
                            "reportFlag", reportFlag), args.get("tp_pid", tp_pid), tp_Udhi,
                        msg_Fmt, args.get("messageType", messageType), bytesList.get(i).length,
                        bytesList.get(i), args.get("reserve", reserve));
                }
                msgs.add(sm);
            }
        }
    }

    public String getChargeNumber() {
        return chargeNumber;
    }

    public void setChargeNumber(String chargeNumber) {
        this.chargeNumber = chargeNumber;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public int getFeeType() {
        return feeType;
    }

    public void setFeeType(int feeType) {
        this.feeType = feeType;
    }

    public String getFeeValue() {
        return feeValue;
    }

    public void setFeeValue(String feeValue) {
        this.feeValue = feeValue;
    }

    public String getGivenValue() {
        return givenValue;
    }

    public void setGivenValue(String givenValue) {
        this.givenValue = givenValue;
    }

    public int getAgentFlag() {
        return agentFlag;
    }

    public void setAgentFlag(int agentFlag) {
        this.agentFlag = agentFlag;
    }

    public int getMorelatetoMTFlag() {
        return morelatetoMTFlag;
    }

    public void setMorelatetoMTFlag(int morelatetoMTFlag) {
        this.morelatetoMTFlag = morelatetoMTFlag;
    }

    public int getReportFlag() {
        return reportFlag;
    }

    public void setReportFlag(int reportFlag) {
        this.reportFlag = reportFlag;
    }

    public int getTp_pid() {
        return tp_pid;
    }

    public void setTp_pid(int tp_pid) {
        this.tp_pid = tp_pid;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

}
