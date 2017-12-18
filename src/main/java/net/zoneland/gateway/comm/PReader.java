/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.comm;

import java.io.IOException;

/**
 * 抽象读取类，所有从流中接收消息处理都继承此类
 * @author liuzhenxing
 * @version $Id: PReader.java, v 0.1 2012-5-11 下午1:45:05 liuzhenxing Exp $
 */
public abstract class PReader {
    
    public abstract PMessage read() throws IOException;

}
