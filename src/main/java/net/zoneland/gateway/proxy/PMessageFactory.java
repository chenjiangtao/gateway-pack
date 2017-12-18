/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.proxy;

import java.util.List;
import java.util.Map;

import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.message.GateWayMessage;
import net.zoneland.gateway.message.GateWayReceiveMessage;

/**
 * 
 * @author liuzhenxing
 * @version $Id: PMessageFactory.java, v 0.1 2012-5-28 下午3:15:51 liuzhenxing Exp $
 */
public abstract class PMessageFactory {

    public abstract List<PMessage> createMessasge(GateWayMessage message, Map<String, String> params);

    public abstract GateWayReceiveMessage createReceiveMessage(PMessage pMessage);

}
