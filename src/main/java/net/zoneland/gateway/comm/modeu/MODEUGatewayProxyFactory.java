/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.comm.modeu;

import java.util.Map;

import net.zoneland.gateway.comm.sgip.SGIPSMProxy;
import net.zoneland.gateway.proxy.GatewayProxy;
import net.zoneland.gateway.proxy.PGatewayProxyFactory;

/**
 * 
 * @author liuzhenxing
 * @version $Id: MODEUGatewayProxyFactory.java, v 0.1 2012-5-28 下午3:51:29 liuzhenxing Exp $
 */
public class MODEUGatewayProxyFactory extends PGatewayProxyFactory {

    /** 
     * @see net.zoneland.gateway.proxy.PGatewayProxyFactory#createGatewayProxy(java.util.Map)
     */
    @Override
    public GatewayProxy createGatewayProxy(Map<String, String> params) {
        ModemSMProxy proxy = new ModemSMProxy(params);
        return proxy;
    }

    /** 
     * @see net.zoneland.gateway.proxy.PGatewayProxyFactory#createGatewayProxy(java.lang.String, java.util.Map)
     */
    @Override
    public GatewayProxy createGatewayProxy(String id, Map<String, String> params) {
        return null;
    }

    /** 
     * @see net.zoneland.gateway.proxy.PGatewayProxyFactory#createSGIPGatewayProxy(java.lang.String, java.util.Map)
     */
    @Override
    public SGIPSMProxy createSGIPGatewayProxy(String id, Map<String, String> params) {
        return null;
    }

}
