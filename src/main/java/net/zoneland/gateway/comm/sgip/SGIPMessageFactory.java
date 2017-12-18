/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.comm.sgip;

import java.util.List;
import java.util.Map;

import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.message.GateWayMessage;
import net.zoneland.gateway.message.GateWayReceiveMessage;
import net.zoneland.gateway.proxy.PMessageFactory;

/**
 * 
 * @author liuzhenxing
 * @version $Id: SGIPMessageFactory.java, v 0.1 2012-5-28 下午3:42:41 liuzhenxing Exp $
 */
public class SGIPMessageFactory extends PMessageFactory {

    /** 
     * @see net.zoneland.gateway.proxy.PMessageFactory#createMessasge(net.zoneland.sms.biz.common.msg.GateWayMessage, java.util.Map)
     */
    @Override
    public List<PMessage> createMessasge(GateWayMessage message, Map params) {

        return null;
    }

    /** 
     * @see net.zoneland.gateway.proxy.PMessageFactory#createReceiveMessage(net.zoneland.gateway.comm.PMessage)
     */
    @Override
    public GateWayReceiveMessage createReceiveMessage(PMessage pMessage) {
        return null;
    }

}
