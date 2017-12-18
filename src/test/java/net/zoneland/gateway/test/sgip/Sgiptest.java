/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.test.sgip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.zoneland.gateway.comm.sgip.SGIPSMProxy;
import net.zoneland.gateway.comm.sgip.message.SGIPSubmitMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPSubmitRepMessage;

/**
 * 
 * @author gag
 * @version $Id: SGIPtest.java, v 0.1 2012-8-18 下午4:42:39 gag Exp $
 */
public class Sgiptest {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Properties props = new Properties();
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        URL url = currentClassLoader.getResource("sgip.properties");
        props.load(new FileInputStream(new File(url.getPath())));

        Map params = new HashMap();
        params.put("host", "localhost");
        params.put("port", "8801");

        params.put("read-timeout", "3");
        params.put("reconnect-interval", "2");
        params.put("transaction-timeout", "3");

        params.put("heartbeat-interval", "0");
        params.put("heartbeat-noresponseout", "2");

        params.put("source-addr", "1234");
        params.put("shared-secret", "1234");
        params.put("version", "1");

        params.put("debug", "true");
        params.put("corp_id", "33439");
        SGIPSMProxy sgipProxy = new SGIPSMProxy("id", params);
        sgipProxy.startService("127.0.0.1", 8802);
        sgipProxy.connect("1234", "1234");

        //String SPNumber, String ChargeNumber, String UserNumber[],
        // String CorpId, String ServiceType, int FeeType, String FeeValue,
        // String GivenValue, int AgentFlag, int MorelatetoMTFlag, int Priority,
        //  Date ExpireTime, Date ScheduleTime, int ReportFlag, int TP_pid,
        // int TP_udhi, int MessageCoding, int MessageType, int MessageLen,
        // byte MessageContent[], String reserve
        SGIPSubmitMessage msg = new SGIPSubmitMessage("95598", "1", new String[] {}, "0", "1", 1,
            "1", "1", 8, 8, 8, new Date(), null, 2, 1, 1, 8, 0, 1, "test".getBytes(), "1");
        try {
            SGIPSubmitRepMessage rep = (SGIPSubmitRepMessage) sgipProxy.send(msg);
            System.out.println(rep);

            while (true) {
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                try {
                    rep = (SGIPSubmitRepMessage) sgipProxy.send(msg);
                    System.out.println(rep);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
