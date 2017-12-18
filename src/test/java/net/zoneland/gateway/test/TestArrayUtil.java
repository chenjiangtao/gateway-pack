/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.zoneland.gateway.util.ArrayUtil;

/**
 * 
 * @author gag
 * @version $Id: TestArrayUtil.java, v 0.1 2012-10-12 下午12:28:51 gag Exp $
 */
public class TestArrayUtil {

    public static void main(String[] args) {
        Charset c = Charset.forName("GBK");
        Charset d = Charset.forName("iso-10646-ucs-2");
        String m = "中文测试，中文测试acdfafdafewaf，中文测试，中文测试，中文测试中文测试，中文测试acdfafdafewaf，中文测试，中文测试，中文测试";
        System.out.println("中".getBytes(c).length);
        System.out.println(",".getBytes(c).length);
        System.out.println("，".getBytes(c).length);
        System.out.println("中".getBytes(d).length);
        System.out.println(",".getBytes(d).length);
        System.out.println("，".getBytes(d).length);
        List<byte[]> list = ArrayUtil.splitByteArray(m.getBytes(c), 154);
        Iterator<byte[]> it = list.iterator();
        while (it.hasNext()) {
            System.out.println(new String(it.next()));
        }

        list = split(m);
        it = list.iterator();
        while (it.hasNext()) {
            System.out.println(new String(it.next()));
        }
    }

    public static List<byte[]> split(String s) {
        List<byte[]> array = new ArrayList<byte[]>();
        byte[] bytes = s.getBytes();
        int width = 154;
        for (int i = 0; i < bytes.length; i += width) {
            int length = bytes.length - i > width ? width : bytes.length - i;
            byte[] bytes2 = new byte[length];
            bytes2 = Arrays.copyOfRange(bytes, i, i + length);
            ByteBuffer bb = ByteBuffer.wrap(bytes2);
            CharBuffer cb = Charset.defaultCharset().decode(bb);
            array.add(bb.array());
        }
        return array;
    }
}
