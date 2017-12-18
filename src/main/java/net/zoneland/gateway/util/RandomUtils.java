/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author gang
 * @version $Id: RandomUtils.java, v 0.1 2012-8-23 下午10:43:00 gang Exp $
 */
public class RandomUtils {

    private static AtomicInteger serial = new AtomicInteger(0);

    public static int getRandom() {
        return serial.getAndIncrement();
    }

}
