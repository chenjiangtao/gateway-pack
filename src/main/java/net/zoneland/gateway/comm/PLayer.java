/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author liuzhenxing
 * @version $Id: PLayer.java, v 0.1 2012-5-11 下午1:40:14 liuzhenxing Exp $
 */
public abstract class PLayer {

    /**
     * 最大的顺序号
     */
    public static final int          MAX_ID = 1000000000;

    /**
     * 当前事务顺序号
     */
    private int                      id;

    /**
     * 下一事务顺序号
     */
    public int                       nextChildId;

    /**
     * 事务的父，在具体实现中应该是一个连接。，每个连接可以有多个发送都事务，完成后退出，这里记录它的父，发送的消盵顺序号不会重复
     */
    private PLayer                   parent;

    /**
     * 子，形成一颗树
     */
    private HashMap<Integer, PLayer> children;

    /**
     * 事件监听，处理PEvent源的事件
     */
    private List<PEventListener>     listeners;

    /**
     * 如果父不为空（就是已经产生了一个连接），此处就计算顺号及把自己加到父的子中
     * @param theParent
     */
    protected PLayer(PLayer theParent) {
        if (theParent != null) {
            id = ++theParent.nextChildId;
            if (theParent.nextChildId >= MAX_ID) {
                theParent.nextChildId = 0;
            }
            if (theParent.children == null) {
                theParent.children = new HashMap<Integer, PLayer>();
            }
            theParent.children.put(id, this);
            parent = theParent;
        }
    }

    /**
     * 
     * 关键业务方法之一，发送消息，实现类需要实现真实的发送业务逻辑，延迟到子类实现
     * @param pmessage
     * @throws PException
     */
    public abstract void send(PMessage pMessage) throws PException;

    /**
     * 关键业务方法之一，接收消息，此方法是一个模板，实现了基本的接收消息处理过程，实现类也可以覆盖自行实现拉媚处理
     * 这里主要的是根据PMessage类型来决定是否创建新的交易事务来接收及处理
     * 
     * @param pMessage
     */
    public void onReceive(PMessage pMessage) {
        int childId = getChildId(pMessage);
        //如果childId为-1创建新的交易事务来处理PMessage，否则查找对应的事务来处理
        if (childId == -1) {
            PLayer child = createChild();
            child.onReceive(pMessage);
            fireEvent(new PEvent(PEvent.CHILD_CREATED, this, child));
        } else {
            PLayer child = children.get(childId);
            if (child == null) {
                fireEvent(new PEvent(PEvent.MESSAGE_DISPATCH_FAIL, this, pMessage));
            } else {
                child.onReceive(pMessage);
            }
        }

    }

    /**
     * 处理事件
     * @param pEvent
     */
    protected void fireEvent(PEvent pEvent) {
        if (listeners == null) {
            return;
        }
        for (PEventListener pListener : listeners) {
            pListener.handle(pEvent);
        }
    }

    /**
     * 创建一个交易事务
     * @return
     */
    protected PLayer createChild() {
        throw new UnsupportedOperationException("Not implement");
    }

    /**
     * 得到一个交易事务的Id
     * @param pMessage
     * @return
     */
    protected int getChildId(PMessage pMessage) {
        throw new UnsupportedOperationException("Not implement");
    }

    /**
     * 关闭一个交易事务，主要是从子中把事务除去
     */
    public void close() {
        if (parent == null) {
            throw new UnsupportedOperationException("Not implement");
        } else {
            parent.children.remove(new Integer(id));
            return;
        }
    }

    public void addEventListener(PEventListener l) {
        if (listeners == null) {
            listeners = new ArrayList<PEventListener>();
        }
        listeners.add(l);
    }

    public void removeEventListener(PEventListener l) {
        listeners.remove(l);
    }

    /**
     * Getter method for property <tt>id</tt>.
     * 
     * @return property value of id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter method for property <tt>id</tt>.
     * 
     * @param id value to be assigned to property id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter method for property <tt>parent</tt>.
     * 
     * @return property value of parent
     */
    public PLayer getParent() {
        return parent;
    }

}
