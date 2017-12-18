/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.test.cmpp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.zoneland.gateway.comm.cmpp.CMPPSMProxy;
import net.zoneland.gateway.comm.cmpp.message.CMPPSubmitMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPSubmitRepMessage;

/**
 * 
 * @author gag
 * @version $Id: CMPPtest.java, v 0.1 2012-8-17 上午11:34:01 gag Exp $
 */
public class Cmpptest {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Properties props = new Properties();
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        URL url = currentClassLoader.getResource("cmpp.properties");
        props.load(new FileInputStream(new File(url.getPath())));

        Map params = new HashMap(props);
        //        params.put("host", "localhost");
        //        params.put("port", "7890");
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
        CMPPSMProxy cmppProxy = new CMPPSMProxy("id", params);
        //int pk_Total, int pk_Number, int registered_Delivery, int msg_Level, String service_Id, int fee_UserType, String fee_Terminal_Id, 
        // int tp_Pid, int tp_Udhi, int msg_Fmt, String msg_Src, String fee_Type, String fee_Code, Date valid_Time, 
        // Date at_Time, String src_Terminal_Id, String dest_Terminal_Id[], byte msg_Content[], String reserve
        CMPPSubmitMessage msg = new CMPPSubmitMessage(1, 1, 1, 3, "13588754", 1, "1", 8, 8, 8,
            "1358", "01", "1", new Date(), new Date(), "95598", new String[] { "18912341234" },
            "test".getBytes(), "1");
        try {
            CMPPSubmitRepMessage rep = (CMPPSubmitRepMessage) cmppProxy.send(msg);
            System.out.println(rep);
            while (true) {
                try {
                    Thread.sleep(3 * 1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                try {
                    rep = (CMPPSubmitRepMessage) cmppProxy.send(msg);
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
