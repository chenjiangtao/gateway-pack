/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.proxy;

import java.util.Map;

import net.zoneland.gateway.comm.sgip.SGIPSMProxy;

/**
 * 网关代理工厂方法类，各子工厂方法由具体的网关实现包实现
 * @author liuzhenxing
 * @version $Id: PGatewayFactory.java, v 0.1 2012-5-28 下午3:18:54 liuzhenxing Exp $
 */
public abstract class PGatewayProxyFactory {

    public abstract GatewayProxy createGatewayProxy(Map<String, String> params);

    public abstract GatewayProxy createGatewayProxy(String id, Map<String, String> params);

    public abstract SGIPSMProxy createSGIPGatewayProxy(String id, Map<String, String> params);

}
