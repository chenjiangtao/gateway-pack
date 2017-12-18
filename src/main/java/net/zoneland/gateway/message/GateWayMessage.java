/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.message;

/**
 * 
 * @author ypz
 * @version $Id: GateWayMessage.java, v 0.1 2012-5-24 下午01:16:45 ypz Exp $
 */
public class GateWayMessage {
    
    private String id;
    private String msgId;
    private String msgSrc; //发送人UID
    private String msgSendAddr; //发送人手机号
    private String msgDest; //接收人UID
    private String msgReceiveAddr; //接收人手机号
    private String msgTitle;  //短信内容，彩信、邮件标题
    private String msgLengthType;
    private String priority;
    private String status;
    private String msgContent; //彩信、邮件内容

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgSrc() {
        return msgSrc;
    }

    public void setMsgSrc(String msgSrc) {
        this.msgSrc = msgSrc;
    }

    public String getMsgDest() {
        return msgDest;
    }

    public void setMsgDest(String msgDest) {
        this.msgDest = msgDest;
    }

    public String getMsgReceiveAddr() {
        return msgReceiveAddr;
    }

    public void setMsgReceiveAddr(String msgReceiveAddr) {
        this.msgReceiveAddr = msgReceiveAddr;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgLengthType() {
        return msgLengthType;
    }

    public void setMsgLengthType(String msgLengthType) {
        this.msgLengthType = msgLengthType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgSendAddr() {
        return msgSendAddr;
    }

    public void setMsgSendAddr(String msgSendAddr) {
        this.msgSendAddr = msgSendAddr;
    }
    
}
