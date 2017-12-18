/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.comm.smgp;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3DeliverMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3SubmitMessage;
import net.zoneland.gateway.message.GateWayMessage;
import net.zoneland.gateway.message.GateWayReceiveMessage;
import net.zoneland.gateway.proxy.PMessageFactory;

import org.apache.log4j.Logger;

/**
 * 
 * @author liuzhenxing
 * @version $Id: SMGPMessageFactory.java, v 0.1 2012-5-28 下午3:21:01 liuzhenxing Exp $
 */
public class SMGPMessageFactory extends PMessageFactory {

    private Logger logger = Logger.getLogger(SMGPMessageFactory.class);

    /** 
     * @see net.zoneland.gateway.proxy.PMessageFactory#createMessasge(net.zoneland.sms.biz.common.msg.GateWayMessage, java.util.Map)
     */
    @Override
    public List<PMessage> createMessasge(GateWayMessage message, Map<String, String> params) {
        List<PMessage> messages = new ArrayList<PMessage>();
        String content = message.getMsgTitle();
        String srcPhone = message.getMsgSendAddr();
        String destPhone = message.getMsgReceiveAddr();
        int msgLength = Integer.valueOf(params.get("msg_length"));
        int msgFormat = Integer.valueOf(params.get("msg_format"));
        SMGP3SubmitMessage m;
        try {
            m = new SMGP3SubmitMessage(Integer.valueOf(params.get("msgtype")).intValue(), Integer
                .valueOf(params.get("needreport")).intValue(), Integer.valueOf(
                params.get("priority")).intValue(), params.get("serviceid"), params.get("feetype"),
                params.get("feecode"), params.get("fixedfee"), msgFormat, null, null, srcPhone, "",
                new String[] { destPhone }, content, params.get("reserve"));
            messages.add(m);
        } catch (NumberFormatException e) {
            logger.error("", e);
        } catch (IllegalArgumentException e) {
            logger.error("", e);
        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
        }
        return messages;
    }

    /** 
     * @see net.zoneland.gateway.proxy.PMessageFactory#createReceiveMessage(net.zoneland.gateway.comm.PMessage)
     */
    @Override
    public GateWayReceiveMessage createReceiveMessage(PMessage pMessage) {
        SMGP3DeliverMessage m = (SMGP3DeliverMessage) pMessage;
        GateWayReceiveMessage rm = new GateWayReceiveMessage();
        rm.setContent(m.getMessageContentStr());
        rm.setDestPhone(m.getDestTermID());
        rm.setSrcPhone(m.getSrcTermID());
        rm.setReceiveTime(Calendar.getInstance().getTime().toString());
        return rm;
    }

}
