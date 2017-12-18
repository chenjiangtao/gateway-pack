/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.util;

import org.apache.log4j.Logger;

/**
 * 
 * @author liuzhenxing
 * @version $Id: WatchThread.java, v 0.1 2012-5-11 下午4:02:23 liuzhenxing Exp $
 */
public abstract class WatchThread extends Thread {

    private static final Logger     logger = Logger.getLogger(WatchThread.class);

    private boolean                 alive  = true;

    public static final ThreadGroup tg     = new ThreadGroup("watch-thread");

    public WatchThread(String name) {
        super(tg, name);
        super.setDaemon(true);
    }

    public void kill() {
        this.alive = false;
    }

    public final void run() {
        while (this.alive)
            try {
                task();
            } catch (Exception ex) {
                logger.error("receive Thread error", ex);
            } catch (Throwable t) {
                logger.error("receive Thread error", t);
            }
    }

    /**
     * 
     */
    public abstract void task();

}
