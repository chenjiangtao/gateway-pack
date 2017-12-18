/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.comm;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * 
 * @author liuzhenxing
 * @version $Id: PEvent.java, v 0.1 2012-5-11 下午1:42:15 liuzhenxing Exp $
 */
public class PEvent {

    public static final int              CREATED                  = 1;
    public static final int              CHILD_CREATED            = 2;
    public static final int              DELETED                  = 4;
    public static final int              MESSAGE_SEND_SUCCESS     = 8;
    public static final int              MESSAGE_SEND_FAIL        = 16;
    public static final int              MESSAGE_DISPATCH_SUCCESS = 32;
    public static final int              MESSAGE_DISPATCH_FAIL    = 64;
    static final HashMap<Object, String> names                    = new HashMap<Object, String>();
    private PLayer                     source;
    private int                        type;
    private Object                     data;

    public PEvent(int type, PLayer source, Object data) {
        this.type = type;
        this.source = source;
        this.data = data;
    }

    public PLayer getSource() {
        return source;
    }

    public int getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public String toString() {
        return String.valueOf(String.valueOf((new StringBuffer("PEvent:source=")).append(source)
            .append(",type=").append(names.get(new Integer(type))).append(",data=").append(data)));
    }

    static {
        try {
            Field[] fields = PEvent.class.getFields();
            for (Field f : fields) {
                String name = f.getName();
                Object id = f.get(null);
                names.put(id, name);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
