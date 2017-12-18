/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.zoneland.gateway.comm.PMessage;

import org.springframework.util.StringUtils;

/**
 * 
 * @author gag
 * @version $Id: ArrayUtil.java, v 0.1 2012-8-21 下午3:22:50 gag Exp $
 */
public class ArrayUtil {

    /**
     * 按照size的长度 分隔字符串数组
     * 
     * @param array
     * @param size
     *            分割成的每个数组的长度
     * @return
     */
    public static List<String[]> splitArray(String[] array, int size) {
        if (array == null || array.length == 0) {
            return null;
        }
        List<String[]> list = new ArrayList<String[]>();
        if (size < 1) {
            list.add(array);
            return list;
        }

        String[] temp = null;

        int len = array.length / size;
        if (array.length % size != 0) {
            len++;
        }
        for (int i = 0; i < len; ++i) {
            if (i == len - 1) {
                temp = new String[(array.length - i * size)];
            } else {
                temp = new String[size];
            }
            System.arraycopy(array, i * size, temp, 0, temp.length);
            list.add(temp);
        }
        return list;
    }

    public static List<byte[]> splitArray(String str, int size, int fmt)
                                                                        throws UnsupportedEncodingException {
        if (!StringUtils.hasText(str)) {
            return null;
        }
        if (str.length() < size) {
            List<byte[]> bytesList = new ArrayList<byte[]>(1);
            bytesList.add(PMessage.getMsgContent(str, fmt));
            return bytesList;
        }
        int len = str.length();
        int count = len / size;
        if (len % size != 0) {
            ++count;
        }
        int serial = RandomUtils.getRandom() + 1;
        List<byte[]> bytesList = new ArrayList<byte[]>(count);
        for (int i = 0; i < count; ++i) {
            String temp = str.substring(i * size, Math.min(len, (i + 1) * size));
            byte[] b = PMessage.getMsgContent(temp, fmt);
            bytesList.add(PMessage.addContentHeader(b, count, i + 1, serial));
        }
        return bytesList;
    }

    public static List<byte[]> splitByteArray(byte[] bytes, int size) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        List<byte[]> list = new ArrayList<byte[]>();
        if (size < 1) {
            list.add(bytes);
            return list;
        }

        byte[] temp = null;

        int len = bytes.length / size;
        if (bytes.length % size != 0) {
            len++;
        }
        int serial = RandomUtils.getRandom() + 1;
        for (int i = 0; i < len; ++i) {
            if (i == len - 1) {
                temp = new byte[(bytes.length - i * size)];
            } else {
                temp = new byte[size];
            }
            System.arraycopy(bytes, i * size, temp, 0, temp.length);
            list.add(PMessage.addContentHeader(temp, len, i + 1, serial));
        }
        return list;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String a = "我的是一个测试的例";
        // byte[] len = PMessage.getMsgContent(a, 15);
        // System.out.println(PMessage.getMsgContentStr(len, 15));
        // int l = len.length;
        // System.out.println(l);
        // List<byte[]> list = splitByteArray(len, 4);
        // Iterator<byte[]> it = list.iterator();
        // while (it.hasNext()) {
        // byte[] tt = it.next();
        // byte[] temp = new byte[tt.length - 6];
        // System.arraycopy(tt, 6, temp, 0, tt.length - 6);
        // System.out.println(PMessage.getMsgContentStr(temp, 15));
        // }

        System.out.println(a.substring(0, 3));
        System.out.println(a.substring(3, 6));
        List<byte[]> res = splitArray(a, 3, 15);
        for (int i = 0, len = res.size(); i < len; ++i) {
            System.out.println(new String(res.get(i), "GBK"));
        }
    }

}
