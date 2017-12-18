/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.comm;

import java.io.UnsupportedEncodingException;

/**
 * 鎶借薄娑堟伅锛屾墍鏈変氦浜掔殑娑堟伅閮界户鎵挎绫�
 * @author liuzhenxing
 * @version $Id: PMessage.java, v 0.1 2012-5-11 涓嬪崍1:42:25 liuzhenxing Exp $
 */
public abstract class PMessage {

    public static final byte[] getMsgContent(String content, int msgFormat)
                                                                           throws UnsupportedEncodingException {
        byte[] msgByte = content.getBytes();
        switch (msgFormat) {
            case 0:
                msgByte = content.getBytes();
                break;
            case 8:
                msgByte = content.getBytes("iso-10646-ucs-2");
                break;
            case 15:
                msgByte = content.getBytes("GBK");
                break;
            case 4:
                msgByte = getByte(content);
                break;
            case 21:
                msgByte = getByte(content);
                break;
        }
        return msgByte;
    }

    /**
     * 鍔犲叆闀跨煭淇″ご
     * 
     * @param content
     * @param total
     * @param num
     * @return
     */
    public static final byte[] addContentHeader(byte[] content, int total, int num, int serial) {
        byte[] newcontent = new byte[content.length + 6];
        newcontent[0] = 0x05;
        newcontent[1] = 0x00;
        newcontent[2] = 0x03;
        newcontent[3] = (byte) serial;
        newcontent[4] = (byte) total;
        newcontent[5] = (byte) num;
        System.arraycopy(content, 0, newcontent, 6, content.length);
        return newcontent;
    }

    public static final byte[] add7ContentHeader(byte[] content, int total, int num, int serial) {
        byte[] newcontent = new byte[content.length + 7];
        newcontent[0] = 0x06;
        newcontent[1] = 0x08;
        newcontent[2] = 0x04;
        newcontent[3] = (byte) serial;

        newcontent[5] = (byte) total;
        newcontent[6] = (byte) num;
        System.arraycopy(content, 0, newcontent, 7, content.length);
        return newcontent;
    }

    public static byte[] getByte(String msg) {
        int length = msg.getBytes().length;
        byte temp[] = new byte[length];
        int i = 0;
        int j = 0;
        for (i = 0; i < length; i++) {
            int b1 = Integer.parseInt(msg.substring(i, i + 1), 16);
            b1 <<= 4;
            if (++i < length) {
                int b2 = Integer.parseInt(msg.substring(i, i + 1), 16);
                b1 += b2;
            }
            temp[j] = (byte) b1;
            j++;
        }

        byte result[] = new byte[j];
        for (int f = 0; f < j; f++) {
            result[f] = temp[f];
        }

        return result;
    }

    public static final String getMsgContentStr(byte[] content, int msgFormat)
                                                                              throws UnsupportedEncodingException {
        if (content == null) {
            return null;
        }
        String result = new String(content);
        switch (msgFormat) {
            case 0:
                result = new String(content);
                break;
            case 8:
                result = new String(content, "iso-10646-ucs-2");
                break;
            case 15:
                result = new String(content, "GBK");
                break;
            case 4:
                result = getASCII(content, content.length);
                break;
            case 21:
                result = getASCII(content, content.length);
                break;
        }
        return result;
    }

    public static String getASCII(byte[] shortMsg, int smLen) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < smLen; i++) {
            byte b = shortMsg[i];
            String ascii = Integer.toHexString(b);
            if (b < 16 && b >= 0) {
                ascii = "0" + ascii;
            }
            if (ascii.length() > 2) {
                ascii = ascii.substring(6, 8);
            }
            result.append(ascii);
        }

        return result.toString().toUpperCase();
    }
    
    public static String getMsgIdStr(byte[] bt){
    	if(bt == null||bt.length!=8){
    		return null;
    	}
    	
    	StringBuilder buf = new StringBuilder();
    	int mask = 0xf0;
        int month =  (bt[0] & mask)>>4;
        buf.append(month);

        mask=0xF;
        int date =  (bt[0] & mask)<<1;
        mask=0x80;
        int date2 = bt[1]>>7;
        date = (date&(date2|0x1E));
        buf.append(date);
       
        //01111100
        mask=0x7C;
        int hour = (bt[1]&mask)>>2;
        buf.append(hour);
       
        mask=0x3;
        int minute = (bt[1]&mask)<<4;
        minute |= (bt[2]&0xF0)>>4;
        buf.append(minute);
       
        mask=0xf;
        int second = (bt[2]&mask)<<2;
        second |= (bt[3]&0xff)>>6;
        buf.append(second);
       
        mask = 0x3F;
        int Msg_Id = (bt[3]&mask)<<16;
        Msg_Id |= (bt[4]&0xFF)<<8;
        Msg_Id |= (bt[5]&0xFF);
        buf.append(Msg_Id);
       
        mask = 0xFF;
        int seq_id = (bt[6]&mask)<<8;
        seq_id |= (bt[7]&mask);
        buf.append(seq_id);
        return buf.toString();
    }

}
