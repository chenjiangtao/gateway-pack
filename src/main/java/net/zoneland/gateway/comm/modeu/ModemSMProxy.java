/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.comm.modeu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.modeu.message.ModemMessage;
import net.zoneland.gateway.proxy.DeafultGatewayProxy;
import net.zoneland.gateway.util.Args;

import org.apache.log4j.Logger;
import org.smslib.AGateway;
import org.smslib.AGateway.GatewayStatuses;
import org.smslib.GatewayException;
import org.smslib.ICallNotification;
import org.smslib.IGatewayStatusNotification;
import org.smslib.IInboundMessageNotification;
import org.smslib.IOutboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.Library;
import org.smslib.Message.MessageEncodings;
import org.smslib.Message.MessageTypes;
import org.smslib.OutboundMessage;
import org.smslib.SMSLibException;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;

/**
 * 
 * @author liuzhenxing
 * @version $Id: ModemSMProxy.java, v 0.1 2012-6-5 下午9:54:33 liuzhenxing Exp $
 */
public class ModemSMProxy extends DeafultGatewayProxy {

    private Logger             logger = Logger.getLogger(ModemSMProxy.class);

    private Service            srv;

    private SerialModemGateway gateway;

    //private ReceiveThread      receiveThread;

    public ModemSMProxy(Map<String, String> params) {
        init(params);
    }

    /** 
     * @see net.zoneland.gateway.proxy.DeafultGatewayProxy#send(net.zoneland.gateway.comm.PMessage)
     */
    @Override
    public PMessage send(PMessage message) throws IOException {
        ModemMessage m = (ModemMessage) message;
        logger.info("wait send message " + m);
        boolean result = true;
        OutboundMessage msg = new OutboundMessage(m.getMobilePhone(), m.getContent());
        //OutboundMessage msg = new OutboundMessage("13758233926", "Hello from SMSLib1111111!");
        //        msg.setStatusReport(true);
        msg.setEncoding(MessageEncodings.ENCUCS2);
        try {
            result = srv.sendMessage(msg);
            logger.info(msg);
            //TimeUnit.SECONDS.sleep(1);
        } catch (TimeoutException e) {
            logger.error(e);
        } catch (GatewayException e) {
            logger.error(e);
        } catch (InterruptedException e) {
            logger.error(e);
        }
        return result == true ? message : null;
    }

    /** 
     * @see net.zoneland.gateway.proxy.GatewayProxy#onReport(net.zoneland.gateway.comm.PMessage)
     */
    public PMessage onReport(PMessage msg) {
        return null;
    }

    private void init(Map<String, String> params) {
        Args args = new Args(params);
        String id = args.get("id", "modem.com3");
        logger.info(" id = " + id);
        String comPort = args.get("comPort", "COM3");
        logger.info(" comPort = " + comPort);
        int baudRate = args.get("baudRate", 9600);
        logger.info(" baudRate = " + baudRate);
        String manufacturer = args.get("manufacturer", "wavecom");
        logger.info(" manufacturer = " + manufacturer);
        String model = args.get("model", "");
        logger.info(" model = " + model);
        boolean inbound = args.get("inbound", true);
        logger.info("inbound = " + inbound);
        boolean outbound = args.get("outbound", true);
        logger.info("outbound = " + outbound);
        String simPin = args.get("simPin", "0000");
        logger.info("simPin = " + simPin);
        logger.info(Library.getLibraryDescription());
        logger.info("Version: " + Library.getLibraryVersion());
        gateway = new SerialModemGateway(id, comPort, baudRate, manufacturer, model); // 设置端口与波特率
        //gateway = new SerialModemGateway("modem.com9", "COM9", 9600, "wavecom", "");
        gateway.setInbound(true);
        gateway.setOutbound(true);
        gateway.setSimPin("0000");
        srv = Service.getInstance();
        srv.setGatewayStatusNotification(new GatewayStatusNotification());//网关变化通知
        srv.setInboundMessageNotification(new InboundNotification());//接收处理
        srv.setCallNotification(new CallNotification()); //调用提醒
        srv.setOutboundMessageNotification(new IOutboundMessageNotification() {

            public void process(AGateway gateway, OutboundMessage om) {
                logger.info(gateway);
                logger.info(om);
            }
        });

        logger.info("GSM MODEM 初始化成功，准备开启服务");
        try {
            srv.addGateway(gateway);
            srv.startService();
            //            OutboundMessage msg = new OutboundMessage("13758233926", "Hello from SMSLib1111111!");
            //            srv.sendMessage(msg);
            //            logger.info(msg);
            //            receiveThread = new ReceiveThread("ModemSMProxy Receiver");
            //            receiveThread.start();
        } catch (TimeoutException e) {
            logger.error(e);
        } catch (GatewayException e) {
            logger.error(e);
        } catch (SMSLibException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        } catch (InterruptedException e) {
            logger.error(e);
        }
        logger.info("GSM MODEM 服务启动成功");
    }

    class GatewayStatusNotification implements IGatewayStatusNotification {
        public void process(AGateway gateway, GatewayStatuses oldStatus, GatewayStatuses newStatus) {
            logger.info(">>> Gateway Status change for " + gateway.getGatewayId() + ", OLD: "
                        + oldStatus + " -> NEW: " + newStatus);
        }
    }

    class InboundNotification implements IInboundMessageNotification {

        /** 
         * @see org.smslib.IInboundMessageNotification#process(org.smslib.AGateway, org.smslib.Message.MessageTypes, org.smslib.InboundMessage)
         */
        public void process(AGateway gatewayId, MessageTypes msgType, InboundMessage msg) {
            logger.info(msg);
            if (msgType == MessageTypes.INBOUND) {
                logger.info(">>> New Inbound message detected from Gateway: " + gatewayId);
                ModemMessage m = new ModemMessage();
                m.setContent(msg.getText());
                m.setMobilePhone(msg.getOriginator().substring(
                    msg.getOriginator().indexOf("86") + 2));
                logger.info("mm " + m);
                //DefaultSMSProxy.receiveQueue.add(m);
            } else if (msgType == MessageTypes.STATUSREPORT)
                logger.info(">>> New Inbound Status Report message detected from Gateway: "
                            + gatewayId);
            try {
                // Uncomment following line if you wish to delete the message upon arrival.
                srv.deleteMessage(msg);
            } catch (Exception e) {
                logger.error("Oops!!! Something gone bad...", e);
            }
        }
    }

    class CallNotification implements ICallNotification {
        /** 
         * @see org.smslib.ICallNotification#process(org.smslib.AGateway, java.lang.String)
         */
        public void process(AGateway gatewayId, String callerId) {
            logger.info(">>> New call detected from Gateway: " + gatewayId + " : " + callerId);
        }
    }

    //    class ReceiveThread extends WatchThread {
    //
    //        /**
    //         * @param name
    //         */
    //        public ReceiveThread(String name) {
    //            super(name);
    //        }
    //
    //        /** 
    //         * @see net.zoneland.sms.gateway.util.WatchThread#task()
    //         */
    //        @Override
    //        public void task() {
    //            List inMessages = new ArrayList();
    //            try {
    //                int result = srv.readMessages(inMessages, MessageClasses.ALL);
    //                logger.info("receive " + result + " messages");
    //                for (int i = 0; i < inMessages.size(); i++) {
    //                    Object o = inMessages.get(i);
    //                    if (o instanceof InboundMessage) {
    //                        InboundMessage msg = (InboundMessage) inMessages.get(i);
    //                        logger.info("Source Info " + msg);
    //                        ModemMessage m = new ModemMessage();
    //                        m.setContent(msg.getText());
    //                        m.setMobilePhone(msg.getOriginator());
    //                        logger.info("mm " + m);
    //                        DefaultSMSProxy.receiveQueue.add(m);
    //                        srv.deleteMessage(msg);
    //                    }
    //
    //                }
    //                TimeUnit.SECONDS.sleep(1);
    //            } catch (TimeoutException e) {
    //                logger.error(e);
    //            } catch (GatewayException e) {
    //                logger.error(e);
    //            } catch (IOException e) {
    //                logger.error(e);
    //            } catch (InterruptedException e) {
    //                logger.error(e);
    //            }
    //        }
    //    }

    /** 
     * @see net.zoneland.gateway.proxy.DeafultGatewayProxy#close()
     */
    @Override
    public void close() {

        try {
            //receiveThread.kill();
            Service.getInstance().stopService();
            logger.info("GSM MODEM 服务停止完成");
        } catch (TimeoutException e) {
            logger.error(e);
        } catch (GatewayException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        } catch (InterruptedException e) {
            logger.error(e);
        } catch (SMSLibException e) {
            logger.error(e);
        }

    }

    public static void main(String[] args) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", "modem.com9");
        params.put("comPort", "COM9");
        params.put("inbound", "true");
        params.put("outbound", "true");
        params.put("simPin", "0000");
        ModemSMProxy proxy = new ModemSMProxy(params);
        proxy.send(new ModemMessage("13758233926", "test11111111111111"));
        System.in.read();
        proxy.close();
    }

}
