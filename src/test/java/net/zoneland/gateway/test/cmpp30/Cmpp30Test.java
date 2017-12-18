/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.test.cmpp30;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.zoneland.gateway.comm.cmpp3.CMPP30SMProxy;
import net.zoneland.gateway.comm.cmpp3.message.CMPP30SubmitMessage;
import net.zoneland.gateway.comm.cmpp3.message.CMPP30SubmitRepMessage;

/**
 * 
 * @author gag
 * @version $Id: CMPP3test.java, v 0.1 2012-8-18 下午4:07:16 gag Exp $
 */
public class Cmpp30Test {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Properties props = new Properties();
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        URL url = currentClassLoader.getResource("cmpp30.properties");
        props.load(new FileInputStream(new File(url.getPath())));

        Map params = new HashMap(props);
        //        params.put("host", "172.16.86.103");
        //        params.put("port", "5003");
        //        params.put("local-host", "localhost");
        //        params.put("local-port", "6667");
        //        params.put("read-timeout", "3");
        //        params.put("reconnect-interval", "2");
        //        params.put("transaction-timeout", "3");
        //
        //        params.put("source-addr", "333");
        //        params.put("shared-secret", "0555");
        //        params.put("version", "1");
        //
        //        params.put("debug", "true");
        CMPP30SMProxy cmpp30Proxy = new CMPP30SMProxy("id", params);

        CMPP30SubmitMessage msg = new CMPP30SubmitMessage(1, 1, 1, 3, "135887545", 1, "1", 8, 8, 8,
            8, "955", "01", "1", new Date(), new Date(), "95598", new String[] { "18912341234" },
            1, "test".getBytes(), "1");
        try {
            CMPP30SubmitRepMessage rep = (CMPP30SubmitRepMessage) cmpp30Proxy.send(msg);
            System.out.println(rep);
            while (true) {
                try {
                    Thread.sleep(3 * 1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                try {
                    rep = (CMPP30SubmitRepMessage) cmpp30Proxy.send(msg);
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
