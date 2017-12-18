/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.test.cmpp;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.zoneland.gateway.MoListener;
import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.cmpp.CMPPSMProxy;
import net.zoneland.gateway.comm.cmpp.message.CMPPSubmitMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPSubmitRepMessage;

/**
 * 
 * @author gang
 * @version $Id: CmppT.java, v 0.1 2012-8-26 上午6:56:48 gang Exp $
 */
public class CmppT {

    public static void main(String[] args) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("host", "localhost");
        params.put("port", "7890");
        params.put("local-host", "localhost");
        params.put("local-port", "6667");
        params.put("read-timeout", "3");
        params.put("reconnect-interval", "2");
        params.put("transaction-timeout", "3");
        params.put("source-addr", "333");
        params.put("shared-secret", "0555");
        params.put("version", "1");
        params.put("debug", "true");
        CMPPSMProxy cmppProxy = new CMPPSMProxy("id", params);
        class MyMoListenter implements MoListener {

            public void OnTerminate() {
                System.out.println("服务端断开连接。");
            }

            /** 
             * @see net.zoneland.gateway.MoListener#onReport(net.zoneland.gateway.comm.PMessage)
             */

            public PMessage onReport(PMessage msg) {
                return null;
            }

            /** 
             * @see net.zoneland.gateway.MoListener#onDeliver(net.zoneland.gateway.comm.PMessage, java.util.Map)
             */

            public void onDeliver(PMessage msg, Map<String, String> args) {
            }
        }
        ;
        cmppProxy.setMoListener(new MyMoListenter());
        CMPPSubmitMessage msg = new CMPPSubmitMessage(1,//int pk_Total,
            1, /*int pk_Number*/1, /*registered_Delivery*/3, /*msg_Level*/
            "13588754", /*service_Id*/1, /*fee_UserType*/
            "1", /*fee_Terminal_Id*/8, /*int tp_Pid*/8, /*int tp_Udhi*/
            8, /*int msg_Fmt*/"1358", /*String msg_Src*/"01", /*String fee_Type*/
            "1", /*String fee_Code*/new Date(), /*Date valid_Time*/new Date(),/*Date at_Time*/
            "95598", /*String src_Terminal_Id*/new String[] { "18912341234" }, /*String dest_Terminal_Id[]*/
            "test".getBytes(), /*byte msg_Content[]*/"1"/* String reserve*/
        );
        CMPPSubmitRepMessage rep = (CMPPSubmitRepMessage) cmppProxy.send(msg);
    }

}
