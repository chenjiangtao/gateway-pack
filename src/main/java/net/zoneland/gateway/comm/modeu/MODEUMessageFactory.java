/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.comm.modeu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.modeu.message.ModemMessage;
import net.zoneland.gateway.message.GateWayMessage;
import net.zoneland.gateway.message.GateWayReceiveMessage;
import net.zoneland.gateway.proxy.PMessageFactory;

/**
 * 
 * @author liuzhenxing
 * @version $Id: MODEUMessageFactory.java, v 0.1 2012-5-28 下午3:53:17 liuzhenxing Exp $
 */
public class MODEUMessageFactory extends PMessageFactory {

    /** 
     * @see net.zoneland.gateway.proxy.PMessageFactory#createMessasge(net.zoneland.sms.biz.common.msg.GateWayMessage, java.util.Map)
     */
    @Override
    public List<PMessage> createMessasge(GateWayMessage message, Map params) {
        ModemMessage m = ModemMessage.newInstance(message.getMsgReceiveAddr(),
            message.getMsgTitle());
        ArrayList<PMessage> messages = new ArrayList<PMessage>();
        messages.add(m);
        return messages;
    }

    /** 
     * @see net.zoneland.gateway.proxy.PMessageFactory#createReceiveMessage(net.zoneland.gateway.comm.PMessage)
     */
    @Override
    public GateWayReceiveMessage createReceiveMessage(PMessage pMessage) {
        ModemMessage m = (ModemMessage) pMessage;
        GateWayReceiveMessage rm = new GateWayReceiveMessage();
        rm.setContent(m.getContent());
        rm.setDestPhone(m.getMobilePhone());
        rm.setSrcPhone(m.getMobilePhone());
        rm.setReceiveTime(Calendar.getInstance().getTime().toString());
        return rm;
    }

}
