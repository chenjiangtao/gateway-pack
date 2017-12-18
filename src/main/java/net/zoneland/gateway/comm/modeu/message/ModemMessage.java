/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.comm.modeu.message;

import net.zoneland.gateway.comm.PMessage;

/**
 * 
 * @author liuzhenxing
 * @version $Id: ModemMessage.java, v 0.1 2012-6-5 下午9:44:39 liuzhenxing Exp $
 */
public class ModemMessage extends PMessage {

    private String mobilePhone;

    private String content;

    public ModemMessage() {
    }

    public ModemMessage(String mobilePhone, String content) {
        this.mobilePhone = mobilePhone;
        this.content = content;
    }

    /**
     * Getter method for property <tt>mobilePhone</tt>.
     * 
     * @return property value of mobilePhone
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * Setter method for property <tt>mobilePhone</tt>.
     * 
     * @param mobilePhone value to be assigned to property mobilePhone
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
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

    public static ModemMessage newInstance(String phone, String content) {
        return new ModemMessage(phone, content);
    }

    /** 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ModemMessage [mobilePhone=" + mobilePhone + ", content=" + content + "]";
    }

}
