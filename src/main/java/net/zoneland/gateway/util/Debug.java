/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.util;

import org.apache.log4j.Logger;

/**
 * 
 * @author liuzhenxing
 * @version $Id: Debug.java, v 0.1 2012-5-11 下午4:32:27 liuzhenxing Exp $
 */
public class Debug {

    private static final Logger logger = Logger.getLogger(Debug.class);

    public static final void dump(Object obj) {
        if (logger.isInfoEnabled()) {
            logger.info(obj);
        }

    }

}
