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
import net.zoneland.gateway.comm.cmpp3.message.CMPP30SubmitMessage;
import net.zoneland.gateway.util.Args;
import net.zoneland.gateway.util.ArrayUtil;

import org.springframework.util.StringUtils;

/**
 * 
 * @author gag
 * @version $Id: CMPP3MsgFactory.java, v 0.1 2012-8-21 下午5:11:34 gag Exp $
 */
public class CMPP30MsgFactory {

    public static final int MAX_DEST        = 100;

    private int             registered_Delivery;

    private String          service_Id      = "";

    private int             fee_UserType;

    private String          fee_Terminal_Id = "";

    private int             fee_Terminal_Type;

    private int             tp_Pid;

    /** 0代表内容体里不含有协议头信息 1代表内容含有协议头信息（长短信，push短信等都是在内容体上含有头内容的）当设置内容体包含协议头，需要根据协议写入相应的信息，长短信协议头有两种：
       6位协议头格式：05 00 03 XX MM NN
             byte 1 : 05, 表示剩余协议头的长度
             byte 2 : 00, 这个值在GSM 03.40规范9.2.3.24.1中规定，表示随后的这批超长短信的标识位长度为1（格式中的XX值）。
             byte 3 : 03, 这个值表示剩下短信标识的长度
             byte 4 : XX，这批短信的唯一标志，事实上，SME(手机或者SP)把消息合并完之后，就重新记录，所以这个标志是否唯
                         一并不是很 重要。
             byte 5 : MM, 这批短信的数量。如果一个超长短信总共5条，这里的值就是5。
             byte 6 : NN, 这批短信的数量。如果当前短信是这批短信中的第一条的值是1，第二条的值是2。
             例如：05 00 03 39 02 01  */
    //private int    tp_Udhi;

    private String          fee_Type        = "";

    private String          fee_Code        = "";

    /** 运营商指定 */
    private String          msg_Src         = "";

    private int             dest_Terminal_Type;

    private String          linkID          = "";

    private int             priority;

    /**
     * 
     * @param src
     * @param dest
     * @param msgContent
     * @param msg_Fmt
     * @return
     * @throws UnsupportedEncodingException
     */
    public List<CMPP30SubmitMessage> buildCMPP30SubmitMessage(String src, String[] dest,
                                                              String msgContent, int msg_Fmt,
                                                              int priority)
                                                                           throws UnsupportedEncodingException {
        return buildCMPP30SubmitMessage(src, dest, msgContent, msg_Fmt, priority, null);
    }

    public List<CMPP30SubmitMessage> buildCMPP30SubmitMessage(String src, String[] dest,
                                                              String msgContent, int msg_Fmt,
                                                              String feeType, String fee)
                                                                                         throws UnsupportedEncodingException {
        return buildCMPP30SubmitMessage(src, dest, msgContent, msg_Fmt, feeType, fee, priority);
    }

    public List<CMPP30SubmitMessage> buildCMPP30SubmitMessage(String src, String[] dest,
                                                              String msgContent, int msg_Fmt,
                                                              int priority,
                                                              Map<String, String> params)
                                                                                         throws UnsupportedEncodingException {
        if (src == null || dest == null || !StringUtils.hasText(msgContent)) {
            return null;
        }
        List<CMPP30SubmitMessage> msgs = new ArrayList<CMPP30SubmitMessage>();
        List<String[]> list = null;
        if (dest.length > MAX_DEST) {
            list = ArrayUtil.splitArray(dest, MAX_DEST);
        } else {
            list = new ArrayList<String[]>(1);
            list.add(dest);
        }

        if (msg_Fmt == 0) {
            byte[] msg = PMessage.getMsgContent(msgContent, msg_Fmt);
            if (msg.length > 154) {
                List<byte[]> bytesList = ArrayUtil.splitByteArray(msg, 160 - 6);
                fillMsg(msgs, list, bytesList, 1, msg_Fmt, src, fee_Type, fee_Code, priority,
                    registered_Delivery, params);
            } else {
                List<byte[]> bytesList = new ArrayList<byte[]>(1);
                bytesList.add(msg);
                fillMsg(msgs, list, bytesList, 0, msg_Fmt, src, fee_Type, fee_Code, priority,
                    registered_Delivery, params);
            }
        } else if (msg_Fmt == 15) {
            List<byte[]> bytesList = ArrayUtil.splitArray(msgContent, 67, 15);
            if (bytesList.size() == 1) {
                fillMsg(msgs, list, bytesList, 0, msg_Fmt, src, fee_Type, fee_Code, priority,
                    registered_Delivery, params);
            } else {
                fillMsg(msgs, list, bytesList, 1, msg_Fmt, src, fee_Type, fee_Code, priority,
                    registered_Delivery, params);
            }
        } else {
            byte[] msg = PMessage.getMsgContent(msgContent, msg_Fmt);
            if (msg.length > 134) {
                List<byte[]> bytesList = ArrayUtil.splitByteArray(msg, 140 - 6);
                fillMsg(msgs, list, bytesList, 1, msg_Fmt, src, fee_Type, fee_Code, priority,
                    registered_Delivery, params);
            } else {
                List<byte[]> bytesList = new ArrayList<byte[]>(1);
                bytesList.add(msg);
                fillMsg(msgs, list, bytesList, 0, msg_Fmt, src, fee_Type, fee_Code, priority,
                    registered_Delivery, params);
            }
        }

        return msgs;
    }

    public List<CMPP30SubmitMessage> buildCMPP30SubmitMessage(String src, String[] dest,
                                                              String msgContent, int msg_Fmt,
                                                              String feeType, String fee,
                                                              int priority)
                                                                           throws UnsupportedEncodingException {

        return buildCMPP30SubmitMessage(src, dest, msgContent, msg_Fmt, feeType, fee, priority,
            registered_Delivery);
    }

    public List<CMPP30SubmitMessage> buildCMPP30SubmitMessage(String src, String[] dest,
                                                              String msgContent, int msg_Fmt,
                                                              String feeType, String fee,
                                                              int priority, int registeredDelivery)
                                                                                                   throws UnsupportedEncodingException {
        if (src == null || dest == null || !StringUtils.hasText(msgContent)) {
            return null;
        }
        List<CMPP30SubmitMessage> msgs = new ArrayList<CMPP30SubmitMessage>();
        List<String[]> list = null;
        if (dest.length > MAX_DEST) {
            list = ArrayUtil.splitArray(dest, MAX_DEST);
        } else {
            list = new ArrayList<String[]>(1);
            list.add(dest);
        }
        if (!StringUtils.hasText(feeType)) {
            feeType = this.fee_Type;
        }
        if (!StringUtils.hasText(fee)) {
            fee = this.fee_Code;
        }
        if (msg_Fmt == 0) {
            byte[] msg = PMessage.getMsgContent(msgContent, msg_Fmt);
            if (msg.length > 154) {
                List<byte[]> bytesList = ArrayUtil.splitByteArray(msg, 160 - 6);
                fillMsg(msgs, list, bytesList, 1, msg_Fmt, src, feeType, fee, priority,
                    registeredDelivery, null);
            } else {
                List<byte[]> bytesList = new ArrayList<byte[]>(1);
                bytesList.add(msg);
                fillMsg(msgs, list, bytesList, 0, msg_Fmt, src, feeType, fee, priority,
                    registeredDelivery, null);
            }
        } else if (msg_Fmt == 15) {
            List<byte[]> bytesList = ArrayUtil.splitArray(msgContent, 67, 15);
            if (bytesList.size() == 1) {
                fillMsg(msgs, list, bytesList, 0, msg_Fmt, src, feeType, fee, priority,
                    registeredDelivery, null);
            } else {
                fillMsg(msgs, list, bytesList, 1, msg_Fmt, src, feeType, fee, priority,
                    registeredDelivery, null);
            }
        } else {
            byte[] msg = PMessage.getMsgContent(msgContent, msg_Fmt);
            if (msg.length > 134) {
                List<byte[]> bytesList = ArrayUtil.splitByteArray(msg, 140 - 6);
                fillMsg(msgs, list, bytesList, 1, msg_Fmt, src, feeType, fee, priority,
                    registeredDelivery, null);
            } else {
                List<byte[]> bytesList = new ArrayList<byte[]>(1);
                bytesList.add(msg);
                fillMsg(msgs, list, bytesList, 0, msg_Fmt, src, feeType, fee, priority,
                    registeredDelivery, null);
            }
        }

        return msgs;
    }

    public void fillMsg(List<CMPP30SubmitMessage> msgs, List<String[]> users,
                        List<byte[]> bytesList, int tp_Udhi, int msg_Fmt, String src,
                        String feeType, String fee, int priority, int registered_Delivery,
                        Map<String, String> params) {
        for (int j = 0, lenj = users.size(); j < lenj; ++j) {
            String[] user = users.get(j);

            for (int i = 0, len = bytesList.size(); i < len; ++i) {
                CMPP30SubmitMessage sm = null;
                if (params == null) {
                    sm = new CMPP30SubmitMessage(bytesList.size(), i + 1, registered_Delivery,
                        priority, service_Id, fee_UserType, fee_Terminal_Id, fee_Terminal_Type,
                        tp_Pid, tp_Udhi, msg_Fmt, msg_Src, feeType, fee, null, null, src, user,
                        dest_Terminal_Type, bytesList.get(i), linkID);
                } else {
                    Args args = new Args(params);
                    sm = new CMPP30SubmitMessage(bytesList.size(), i + 1, args.get(
                        "registered_Delivery", registered_Delivery), priority, args.get(
                        "service_Id", service_Id), args.get("fee_UserType", fee_UserType),
                        args.get("fee_Terminal_Id", fee_Terminal_Id), args.get("fee_Terminal_Type",
                            fee_Terminal_Type), args.get("tp_Pid", tp_Pid), tp_Udhi, msg_Fmt,
                        args.get("msg_Src", msg_Src), args.get("fee_Type", feeType), args.get(
                            "fee_Code", fee), null, null, src, user, args.get("dest_Terminal_Type",
                            dest_Terminal_Type), bytesList.get(i), args.get("linkID", linkID));
                }
                msgs.add(sm);
            }
        }
    }

    public int getRegistered_Delivery() {
        return registered_Delivery;
    }

    public void setRegistered_Delivery(int registered_Delivery) {
        this.registered_Delivery = registered_Delivery;
    }

    public String getService_Id() {
        return service_Id;
    }

    public void setService_Id(String service_Id) {
        this.service_Id = service_Id;
    }

    public int getFee_UserType() {
        return fee_UserType;
    }

    public void setFee_UserType(int fee_UserType) {
        this.fee_UserType = fee_UserType;
    }

    public String getFee_Terminal_Id() {
        return fee_Terminal_Id;
    }

    public void setFee_Terminal_Id(String fee_Terminal_Id) {
        this.fee_Terminal_Id = fee_Terminal_Id;
    }

    public int getFee_Terminal_Type() {
        return fee_Terminal_Type;
    }

    public void setFee_Terminal_Type(int fee_Terminal_Type) {
        this.fee_Terminal_Type = fee_Terminal_Type;
    }

    public int getTp_Pid() {
        return tp_Pid;
    }

    public void setTp_Pid(int tp_Pid) {
        this.tp_Pid = tp_Pid;
    }

    public String getFee_Type() {
        return fee_Type;
    }

    public void setFee_Type(String fee_Type) {
        this.fee_Type = fee_Type;
    }

    public String getFee_Code() {
        return fee_Code;
    }

    public void setFee_Code(String fee_Code) {
        this.fee_Code = fee_Code;
    }

    public int getDest_Terminal_Type() {
        return dest_Terminal_Type;
    }

    public void setDest_Terminal_Type(int dest_Terminal_Type) {
        this.dest_Terminal_Type = dest_Terminal_Type;
    }

    public String getLinkID() {
        return linkID;
    }

    public void setLinkID(String linkID) {
        this.linkID = linkID;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public String getMsg_Src() {
        return msg_Src;
    }

    public void setMsg_Src(String msg_Src) {
        this.msg_Src = msg_Src;
    }

}
