/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.comm;

import java.net.ProtocolException;

/**
 * 
 * @author liuzhenxing
 * @version $Id: PException.java, v 0.1 2012-5-11 下午2:21:16 liuzhenxing Exp $
 */
public class PException extends ProtocolException {
    
    public PException(String message){
        super(message);
    }

}
