/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway;

import java.util.Map;

import net.zoneland.gateway.comm.PMessage;

/**
 * 实现此接口，在接收到上行短信后，会调用相关方法
 * @author gag
 * @version $Id: MoListener.java, v 0.1 2012-8-21 上午8:48:48 gag Exp $
 */
public interface MoListener {

    /**
     * 当接收到上行短信后，会调用此方法
     * @param msg
     * @param args
     */
    public void onDeliver(PMessage msg, Map<String, String> args);

    /**
     * 当断开连接的时候，会调用此方法；
     */
    public void OnTerminate();

    /**
     * 收到状态报告的处理
     * @param msg
     * @return
     */
    public PMessage onReport(PMessage msg);

}
