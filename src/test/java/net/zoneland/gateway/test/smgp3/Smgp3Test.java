/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.test.smgp3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.zoneland.gateway.comm.smgp3.SMGP3SMProxy;
import net.zoneland.gateway.comm.smgp3.message.SMGP3SubmitMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3SubmitRespMessage;

/**
 * 
 * @author gag
 * @version $Id: Smg3Test.java, v 0.1 2012-8-21 上午9:48:03 gag Exp $
 */
public class Smgp3Test {

    public static void main(String[] args) throws IllegalArgumentException, FileNotFoundException,
                                          IOException {

        Properties props = new Properties();
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        URL url = currentClassLoader.getResource("smgp3.properties");
        props.load(new FileInputStream(new File(url.getPath())));

        Map params = new HashMap(props);

        //        params.put("host", "localhost");
        //        params.put("port", "9890");
        //        params.put("local-host", "localhost");
        //        params.put("local-port", "6770");
        //        params.put("read-timeout", "3");
        //        params.put("reconnect-interval", "2");
        //        params.put("transaction-timeout", "3");
        //
        //        params.put("heartbeat-interval", "3");
        //        params.put("heartbeat-noresponseout", "2");
        //
        //        params.put("source-addr", "333");
        //        params.put("shared-secret", "0555");
        //        params.put("version", "48");
        //
        //        params.put("debug", "true");
        SMGP3SMProxy smgp3Proxy = new SMGP3SMProxy("id", params);

        //        int msgType, int needReport, int priority, String serviceId,
        //        String feeType, String feeCode, String fixedFee, int msgFormat,
        //        Date validTime, Date atTime, String srcTermId, String chargeTermId,
        //        String[] destTermId, String msgContent, String reserve
        SMGP3SubmitMessage msg = new SMGP3SubmitMessage(1, 0, 8, "1", "0", "1", "1", 8, new Date(),
            null, "1", "1", new String[] {}, "test", "1");
        try {
            SMGP3SubmitRespMessage rep = (SMGP3SubmitRespMessage) smgp3Proxy.send(msg);
            System.out.println(rep);

            while (true) {
                try {
                    Thread.sleep(3 * 1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                try {
                    rep = (SMGP3SubmitRespMessage) smgp3Proxy.send(msg);
                    System.out.println(rep);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
