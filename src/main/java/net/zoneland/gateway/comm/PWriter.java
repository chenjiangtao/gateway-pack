/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.comm;

import java.io.IOException;

/**
 * 换象发送类，向流中写入消息都继承此类
 * @author liuzhenxing
 * @version $Id: PWriter.java, v 0.1 2012-5-11 下午1:44:56 liuzhenxing Exp $
 */
public abstract class PWriter {

    public abstract void write(PMessage pMessage) throws IOException;

}
