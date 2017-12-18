/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.comm;

/**
 * 
 * @author liuzhenxing
 * @version $Id: PEventAdapter.java, v 0.1 2012-5-11 下午2:53:37 liuzhenxing Exp $
 */
public class PEventAdapter implements PEventListener {

    /** 
     * @see net.zoneland.gateway.comm.PEventListener#handle(net.zoneland.gateway.comm.PEvent)
     */
    public void handle(PEvent e) {

        switch (e.getType()) {
            case PEvent.CHILD_CREATED:
                childCreated((PLayer) e.getData());
                break;
            case PEvent.CREATED:
                created();
                break;
            case PEvent.DELETED:
                deleted();
                break;
            case PEvent.MESSAGE_DISPATCH_FAIL:
                messageDispatchFail((PMessage) e.getData());
                break;
            case PEvent.MESSAGE_DISPATCH_SUCCESS:
                messageDispatchSuccess((PMessage) e.getData());
                break;
            case PEvent.MESSAGE_SEND_SUCCESS:
                messageSendSuccess((PMessage) e.getData());
                break;
            case PEvent.MESSAGE_SEND_FAIL:
                messageSendFail((PMessage) e.getData());

        }
    }

    /**
     * 消息发送失败钩子方法
     * @param data
     */
    public void messageSendFail(PMessage data) {
    }

    /**
     * 消息发送成功钩子方法
     * @param data
     */
    public void messageSendSuccess(PMessage data) {
    }

    /**
     * 消息转发成功钩子方法
     * @param data
     */
    public void messageDispatchSuccess(PMessage data) {
    }

    /**
     * 消息转发失败钩子方法
     * @param data
     */
    public void messageDispatchFail(PMessage data) {
    }

    /**
     * 删除钩子方法
     */
    public void deleted() {
    }

    /**
     * 创建钩子方法
     */
    public void created() {
    }

    /**
     * 创建子钩子方法
     * @param data
     */
    public void childCreated(PLayer data) {
    }

}
