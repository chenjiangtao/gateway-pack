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
 * @version $Id: GatewayProxy.java, v 0.1 2012-5-28 上午9:40:53 liuzhenxing Exp $
 */
public interface GatewayProxy {

    public PMessage send(PMessage message) throws IOException;

    public void onTerminate();

    public PMessage onDeliver(PMessage msg);

    public PMessage onReport(PMessage msg);

    public void close();

    public PSocketConnection getConn();

    public String getConnState();

}
