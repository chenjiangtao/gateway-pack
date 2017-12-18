/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.test.demo;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author gang
 * @version $Id: GatewayInfoDemo.java, v 0.1 2012-8-22 上午6:43:43 gang Exp $
 */
public class GatewayInfoDemo {

    private List<Map<String, String>>        gateway;

    private Map<String, Map<String, String>> gatewayParams;

    public List<Map<String, String>> getGateway() {
        return gateway;
    }

    public void setGateway(List<Map<String, String>> gateway) {
        this.gateway = gateway;
    }

    public Map<String, Map<String, String>> getGatewayParams() {
        return gatewayParams;
    }

    public void setGatewayParams(Map<String, Map<String, String>> gatewayParams) {
        this.gatewayParams = gatewayParams;
    }

}
