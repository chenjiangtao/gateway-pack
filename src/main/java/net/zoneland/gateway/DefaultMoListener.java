/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway;

import java.util.Map;

import net.zoneland.gateway.comm.PMessage;

import org.apache.log4j.Logger;

/**
 * 上行默认处理规则，需要自己单独实现
 * @author gag
 * @version $Id: DefaultMoInput.java, v 0.1 2012-8-21 上午9:01:11 gag Exp $
 */
public class DefaultMoListener implements MoListener {

    private final static Logger logger = Logger.getLogger(DefaultMoListener.class);

    /** 
     * @see net.zoneland.gateway.MoListener#OnTerminate()
     */
    public void OnTerminate() {
        if (logger.isInfoEnabled()) {
            logger.info("网关断开连接!");
        }
    }

    /** 
     * @see net.zoneland.gateway.MoListener#onReport(net.zoneland.gateway.comm.PMessage)
     */
    public PMessage onReport(PMessage msg) {
        if (logger.isInfoEnabled()) {
            logger.info("收到状态报告!");
        }
        return null;
    }

    /** 
     * @see net.zoneland.gateway.MoListener#onDeliver(net.zoneland.gateway.comm.PMessage, java.util.Map)
     */
    public void onDeliver(PMessage msg, Map<String, String> args) {
        logger.warn("未实现MO监听器，receive mo message:" + msg);
    }

}
