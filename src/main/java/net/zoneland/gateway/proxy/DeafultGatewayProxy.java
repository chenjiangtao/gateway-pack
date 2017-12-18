/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.proxy;

import java.io.IOException;

import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.PSocketConnection;

/**
 * 
 * @author liuzhenxing
 * @version $Id: DeafultGatewayProxy.java, v 0.1 2012-5-28 上午9:43:27 liuzhenxing Exp $
 */
public abstract class DeafultGatewayProxy implements GatewayProxy {

    /** 
     * @see net.zoneland.gateway.proxy.GatewayProxy#send(net.zoneland.gateway.comm.PMessage)
     */
    public PMessage send(PMessage message) throws IOException {
        return null;
    }

    /** 
     * @see net.zoneland.gateway.proxy.GatewayProxy#onTerminate()
     */
    public void onTerminate() {
    }

    /** 
     * @see net.zoneland.gateway.proxy.GatewayProxy#onDeliver(net.zoneland.gateway.comm.PMessage)
     */
    public PMessage onDeliver(PMessage msg) {
        return null;
    }

    /** 
     * @see net.zoneland.gateway.proxy.GatewayProxy#close()
     */
    public void close() {
    }

    /** 
     * @see net.zoneland.gateway.proxy.GatewayProxy#getConn()
     */
    public PSocketConnection getConn() {
        return null;
    }

    /** 
     * @see net.zoneland.gateway.proxy.GatewayProxy#getConnState()
     */
    public String getConnState() {
        return null;
    }

}
