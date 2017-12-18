/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.message;

/**
 * 
 * @author liuzhenxing
 * @version $Id: GateWayReceiveMessage.java, v 0.1 2012-5-25 下午1:58:10 liuzhenxing Exp $
 */
public class GateWayReceiveMessage {

    private String srcPhone;

    private String destPhone;

    private String content;

    private String receiveTime;

    /**
     * Getter method for property <tt>srcPhone</tt>.
     * 
     * @return property value of srcPhone
     */
    public String getSrcPhone() {
        return srcPhone;
    }

    /**
     * Setter method for property <tt>srcPhone</tt>.
     * 
     * @param srcPhone value to be assigned to property srcPhone
     */
    public void setSrcPhone(String srcPhone) {
        this.srcPhone = srcPhone;
    }

    /**
     * Getter method for property <tt>destPhone</tt>.
     * 
     * @return property value of destPhone
     */
    public String getDestPhone() {
        return destPhone;
    }

    /**
     * Setter method for property <tt>destPhone</tt>.
     * 
     * @param destPhone value to be assigned to property destPhone
     */
    public void setDestPhone(String destPhone) {
        this.destPhone = destPhone;
    }

    /**
     * Getter method for property <tt>content</tt>.
     * 
     * @return property value of content
     */
    public String getContent() {
        return content;
    }

    /**
     * Setter method for property <tt>content</tt>.
     * 
     * @param content value to be assigned to property content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Getter method for property <tt>receiveTime</tt>.
     * 
     * @return property value of receiveTime
     */
    public String getReceiveTime() {
        return receiveTime;
    }

    /**
     * Setter method for property <tt>receiveTime</tt>.
     * 
     * @param receiveTime value to be assigned to property receiveTime
     */
    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    /** 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GateWayReceiveMessage [srcPhone=" + srcPhone + ", destPhone=" + destPhone
               + ", content=" + content + ", receiveTime=" + receiveTime + "]";
    }

    /** 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + ((destPhone == null) ? 0 : destPhone.hashCode());
        result = prime * result + ((receiveTime == null) ? 0 : receiveTime.hashCode());
        result = prime * result + ((srcPhone == null) ? 0 : srcPhone.hashCode());
        return result;
    }

    /** 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GateWayReceiveMessage other = (GateWayReceiveMessage) obj;
        if (content == null) {
            if (other.content != null)
                return false;
        } else if (!content.equals(other.content))
            return false;
        if (destPhone == null) {
            if (other.destPhone != null)
                return false;
        } else if (!destPhone.equals(other.destPhone))
            return false;
        if (receiveTime == null) {
            if (other.receiveTime != null)
                return false;
        } else if (!receiveTime.equals(other.receiveTime))
            return false;
        if (srcPhone == null) {
            if (other.srcPhone != null)
                return false;
        } else if (!srcPhone.equals(other.srcPhone))
            return false;
        return true;
    }

}
