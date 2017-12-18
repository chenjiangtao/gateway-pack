/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.test;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.zoneland.gateway.comm.sgip.message.SGIPSubmitMessage;
import net.zoneland.gateway.message.factory.SGIPMsgFactory;

/**
 * 
 * @author gag
 * @version $Id: Test.java, v 0.1 2012-12-28 下午3:53:44 gag Exp $
 */
public class Test {

    public static void main(String[] args) throws UnsupportedEncodingException {
        SGIPMsgFactory f = new SGIPMsgFactory();
        String s = "12345678901234567890net我的长读测试场短信我的长读测试场短信我的长读测试场短信.zoneland.gateway.message.factory.SGIPMsgFactorynet.zoneland.gateway.comm.sgip.message.SGIPSubmitMessage";
        System.out.println(s.length());
        List<SGIPSubmitMessage> n = f.buildSGIPSubmitMessage("95598", new String[] { "15558135733",
                "13588754574" }, s, 5, 1, "2", 3);

        for (int i = 0, len = n.size(); i < len; ++i) {
            System.out.println(n.get(i));
        }
    }

}
