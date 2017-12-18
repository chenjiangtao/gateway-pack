/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;

/**
 * 
 * @author liuzhenxing
 * @version $Id: Resource.java, v 0.1 2012-5-11 下午4:02:07 liuzhenxing Exp $
 */
public class Resource {
    private Cfg resource;

    public Resource(String url) throws IOException {
        init(url);
    }

    public Resource(Class c, String url) throws IOException {
        String className = c.getName();

        int i = className.lastIndexOf(46);
        if (i > 0) {
            className = className.substring(i + 1);
        }
        URL u = new URL(c.getResource(String.valueOf(String.valueOf(className)).concat(".class")),
            url);
        init(u.toString());
    }

    public void init(String url) throws IOException {
        String str = String.valueOf(String.valueOf(new StringBuffer(String.valueOf(String
            .valueOf(url))).append('_').append(Locale.getDefault())));
        InputStream in = null;
        while (true)
            try {
                this.resource = new Cfg(String.valueOf(String.valueOf(str)).concat(".xml"), false);
                return;
            } catch (IOException ex) {
                int i = str.lastIndexOf(95);
                if (i < 0) {
                    throw new MissingResourceException(String.valueOf(String
                        .valueOf(new StringBuffer("Can't find resource url:").append(url).append(
                            ".xml"))), super.getClass().getName(), null);
                }

                str = str.substring(0, i);
            }
    }

    public String get(String key) {
        return this.resource.get(key, key);
    }

    public String[] childrenNames(String key) {
        return this.resource.childrenNames(key);
    }

    public String get(String key, Object[] params) {
        String value = this.resource.get(key, key);
        String str2;
        try {
            return MessageFormat.format(value, params);
        } catch (Exception ex) {
            ex.printStackTrace();
            str2 = key;
        }
        return str2;
    }

    public String get(String key, Object param) {
        return get(key, new Object[] { param });
    }

    public String get(String key, Object param1, Object param2) {
        return get(key, new Object[] { param1, param2 });
    }

    public String get(String key, Object param1, Object param2, Object param3) {
        return get(key, new Object[] { param1, param2, param3 });
    }
}
