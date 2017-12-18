/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.test.demo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.zoneland.gateway.comm.cmpp.CMPPSMProxy;
import net.zoneland.gateway.comm.cmpp.message.CMPPMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPSubmitMessage;
import net.zoneland.gateway.message.factory.CMPPMsgFactory;
import net.zoneland.gateway.pool.GatewayPoolManager;

/**
 * 
 * @author gag
 * @version $Id: ExsamplePoolDemo.java, v 0.1 2012-8-25 下午4:15:47 gag Exp $
 */
public class ExsamplePoolDemo {

    public static void main(String[] args) {
        //创建网关
        LinkedList<CMPPSMProxy> cmppList = new LinkedList<CMPPSMProxy>();

        Map<String, String> param = new HashMap<String, String>();
        param.put("host", "192.168.2.11");
        //.....param.put("port", 123);其他参数略
        CMPPSMProxy proxy1 = new CMPPSMProxy("123", param);
        Map<String, String> param2 = new HashMap<String, String>();
        param2.put("host", "192.168.2.11");
        //.....param.put("port", 123);其他参数略
        CMPPSMProxy proxy2 = new CMPPSMProxy("123", param2);
        cmppList.add(proxy1);
        cmppList.add(proxy2);
        //初始化网关连接池
        GatewayPoolManager<CMPPSMProxy> cmppPool = new GatewayPoolManager<CMPPSMProxy>(cmppList);
        CMPPSMProxy cmppProxy = null;
        try {
            System.out.println("获取网关...");
            //从连接池获取网关
            cmppProxy = cmppPool.fetchGateway();
            CMPPMsgFactory fac = new CMPPMsgFactory();
            List<CMPPSubmitMessage> list = fac.buildCMPPSubmitMessage("95598",
                new String[] { "13588754577" }, "测试", 8, 3);
            Iterator<CMPPSubmitMessage> itMsg = list.iterator();
            while (itMsg.hasNext()) {
                CMPPSubmitMessage msg = itMsg.next();
                CMPPMessage cmm = cmppProxy.send(msg);
                System.out.println("返回消息:" + cmm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //一定要释放连接
            if (cmppProxy != null)
                cmppPool.releaseGateway(cmppProxy);
        }
    }

}
