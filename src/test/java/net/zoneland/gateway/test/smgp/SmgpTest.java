/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.test.smgp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.zoneland.gateway.comm.smgp.SMGPSMProxy;
import net.zoneland.gateway.comm.smgp.message.SMGPSubmitMessage;
import net.zoneland.gateway.comm.smgp.message.SMGPSubmitRespMessage;

/**
 * 
 * @author gag
 * @version $Id: SmgpTest.java, v 0.1 2012-8-18 下午5:41:55 gag Exp $
 */
public class SmgpTest {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Properties props = new Properties();
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        URL url = currentClassLoader.getResource("smgp.properties");
        props.load(new FileInputStream(new File(url.getPath())));

        Map params = new HashMap(props);
        //        params.put("host", "localhost");
        //        params.put("port", "9890");
        //        params.put("local-host", "localhost");
        //        params.put("local-port", "6688");
        //        params.put("read-timeout", "3");
        //        params.put("reconnect-interval", "2");
        //        params.put("transaction-timeout", "3");
        //
        //        params.put("source-addr", "333");
        //        params.put("shared-secret", "0555");
        //        params.put("version", "19");
        //
        //        params.put("debug", "true");
        SMGPSMProxy smgpProxy = new SMGPSMProxy("id", params);

        // int msgType, int needReport, int priority, String serviceId,
        // String feeType, String feeCode, String fixedFee, int msgFormat,
        // Date validTime, Date atTime, String srcTermId, String chargeTermId,
        // String destTermId[], String msgContent, String reserve
        SMGPSubmitMessage msg = new SMGPSubmitMessage(1, 0, 8, "0", "1", "1", "1", 8, new Date(),
            null, "1", "1", new String[] { "1345" }, "test", "1");
        try {
            SMGPSubmitRespMessage rep = (SMGPSubmitRespMessage) smgpProxy.send(msg);
            System.out.println(rep);

            while (true) {
                try {
                    Thread.sleep(3 * 1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                try {
                    rep = (SMGPSubmitRespMessage) smgpProxy.send(msg);
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
