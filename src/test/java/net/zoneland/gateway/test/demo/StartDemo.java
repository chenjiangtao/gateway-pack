/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.test.demo;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.zoneland.gateway.comm.cmpp.CMPPSMProxy;
import net.zoneland.gateway.comm.cmpp.message.CMPPMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPSubmitMessage;
import net.zoneland.gateway.comm.cmpp3.CMPP30SMProxy;
import net.zoneland.gateway.comm.sgip.SGIPSMProxy;
import net.zoneland.gateway.comm.smgp.SMGPSMProxy;
import net.zoneland.gateway.comm.smgp3.SMGP3SMProxy;
import net.zoneland.gateway.message.factory.CMPPMsgFactory;
import net.zoneland.gateway.pool.GatewayPoolManager;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author gang
 * @version $Id: StartDemo.java, v 0.1 2012-8-22 上午6:42:08 gang Exp $
 */
public class StartDemo {

    public static void main(String args[]) throws IOException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:spring.xml");
        GatewayInfoDemo gi = (GatewayInfoDemo) ctx.getBean("gatewayInfo");
        System.out.println("gateway count:" + gi.getGateway().size());
        List<Map<String, String>> gates = gi.getGateway();

        LinkedList<CMPPSMProxy> cmpp = new LinkedList<CMPPSMProxy>();
        LinkedList<CMPP30SMProxy> cmpp30 = new LinkedList<CMPP30SMProxy>();
        LinkedList<SGIPSMProxy> sgip = new LinkedList<SGIPSMProxy>();
        LinkedList<SMGPSMProxy> smgp = new LinkedList<SMGPSMProxy>();
        LinkedList<SMGP3SMProxy> smgp3 = new LinkedList<SMGP3SMProxy>();

        Iterator<Map<String, String>> it = gates.iterator();
        while (it.hasNext()) {
            Map<String, String> map = it.next();
            String name = map.get("name");
            String id = map.get("id");
            if ("SGIP".equals(name)) {
                // sgip.add(new SGIPSMProxy(id, map));
            } else if ("CMPP".equals(name)) {
                System.out.println("begin new proxy");
                CMPPSMProxy proxy = new CMPPSMProxy(id, map);
                System.out.println(proxy);
                cmpp.add(proxy);
            } else if ("CMPP30".equals(name)) {
                // cmpp30.add(new CMPP30SMProxy(id, map));
            } else if ("SMGP".equals(name)) {
                // smgp.add(new SMGPSMProxy(id, map));
            } else if ("SMGP3".equals(name)) {
                // smgp3.add(new SMGP3SMProxy(id, map));
            } else {
                throw new RuntimeException("未知网关");
            }
        }
        GatewayPoolManager<CMPPSMProxy> cmppPool = new GatewayPoolManager<CMPPSMProxy>(cmpp);
        // GatewayPoolManager<CMPP30SMProxy> cmpp30Pool = new GatewayPoolManager<CMPP30SMProxy>(cmpp30);
        // GatewayPoolManager<SMGPSMProxy> smgpPool = new GatewayPoolManager<SMGPSMProxy>(smgp);
        //  GatewayPoolManager<SMGP3SMProxy> smgp3Pool = new GatewayPoolManager<SMGP3SMProxy>(smgp3);
        //  SGIPPoolManager sgipPool = new SGIPPoolManager(sgip);

        int i = 0;
        while (true) {
            CMPPSMProxy cmppProxy = null;
            try {
                System.out.println("获取网关..." + i);
                cmppProxy = cmppPool.fetchGateway();
                System.out.println("获取网关成功:" + i + "-" + cmppProxy);
                CMPPMsgFactory fac = new CMPPMsgFactory();
                List<CMPPSubmitMessage> list = fac.buildCMPPSubmitMessage("95598",
                    new String[] { "13588754577" }, "测试" + i, 8, 3);
                Iterator<CMPPSubmitMessage> itMsg = list.iterator();
                while (itMsg.hasNext()) {
                    CMPPSubmitMessage msg = itMsg.next();
                    System.out.println("发送消息:" + msg);
                    CMPPMessage cmm = cmppProxy.send(msg);
                    System.out.println("返回消息:" + cmm);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cmppProxy != null)
                    cmppPool.releaseGateway(cmppProxy);
            }
            ++i;
        }

    }
}
