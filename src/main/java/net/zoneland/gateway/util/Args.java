/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author liuzhenxing
 * @version $Id: Args.java, v 0.1 2012-5-11 下午3:31:32 liuzhenxing Exp $
 */
public class Args {

    public static final Args EMPTY = new Args().lock();
    boolean                  locked;
    Map                      args;

    public Args() {
        this(new HashMap());
    }

    public Args(Map theArgs) {
        if (theArgs == null) {
            throw new NullPointerException("argument is null");
        }
        this.args = theArgs;
    }

    public String get(String key, String def) {
        String str2;
        try {
            return this.args.get(key).toString();
        } catch (Exception ex) {
            str2 = def;
        }
        return str2;
    }

    public int get(String key, int def) {
        int j;
        try {
            return Integer.parseInt(this.args.get(key).toString());
        } catch (Exception ex) {
            j = def;
        }
        return j;
    }

    public long get(String key, long def) {
        long l2;
        try {
            return Long.parseLong(this.args.get(key).toString());
        } catch (Exception ex) {
            l2 = def;
        }
        return l2;
    }

    public float get(String key, float def) {
        float f2;
        try {
            return Float.parseFloat(this.args.get(key).toString());
        } catch (Exception ex) {
            f2 = def;
        }
        return f2;
    }

    public boolean get(String key, boolean def) {
        boolean bool2;
        try {
            return "true".equals(this.args.get(key));
        } catch (Exception ex) {
            bool2 = def;
        }
        return bool2;
    }

    public Object get(String key, Object def) {
        Object localObject1;
        try {
            Object obj = this.args.get(key);
            if (obj == null) {
                return def;
            }
            return obj;
        } catch (Exception ex) {
            localObject1 = def;
        }
        return localObject1;
    }

    public Args set(String key, Object value) {
        if (this.locked) {
            throw new UnsupportedOperationException("Args have locked,can modify");
        }
        this.args.put(key, value);
        return this;
    }

    public Args set(String key, int value) {
        if (this.locked) {
            throw new UnsupportedOperationException("Args have locked,can modify");
        }
        this.args.put(key, new Integer(value));
        return this;
    }

    public Args set(String key, boolean value) {
        if (this.locked) {
            throw new UnsupportedOperationException("Args have locked,can modify");
        }
        this.args.put(key, new Boolean(value));
        return this;
    }

    public Args set(String key, long value) {
        if (this.locked) {
            throw new UnsupportedOperationException("Args have locked,can modify");
        }
        this.args.put(key, new Long(value));
        return this;
    }

    public Args set(String key, float value) {
        if (this.locked) {
            throw new UnsupportedOperationException("Args have locked,can modify");
        }
        this.args.put(key, new Float(value));
        return this;
    }

    public Args set(String key, double value) {
        if (this.locked) {
            throw new UnsupportedOperationException("Args have locked,can modify");
        }
        this.args.put(key, new Double(value));
        return this;
    }

    public Args lock() {
        this.locked = true;
        return this;
    }

    public Map get() {
        return this.args;
    }

    public String toString() {
        return this.args.toString();
    }

}
