/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.pool;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import net.zoneland.gateway.comm.sgip.SGIPSMProxy;

/**
 * 
 * @author gag
 * @version $Id: SGIPPoolManager.java, v 0.1 2012-8-19 下午4:03:53 gag Exp $
 */
public class SGIPPoolManager {

    private DefaultSemaphore        semaphore;

    private LinkedList<SGIPSMProxy> inactiveGateway;

    private long                    timeoutMilliseconds;

    private boolean                 isClosed;

    public SGIPPoolManager(LinkedList<SGIPSMProxy> inactiveGateway) {
        this(inactiveGateway, 3);
    }

    public SGIPPoolManager(LinkedList<SGIPSMProxy> inactiveGateway, int timeout) {
        if (inactiveGateway == null || inactiveGateway.size() == 0) {
            throw new RuntimeException("网关为空，无法初始化连接池");
        }
        this.inactiveGateway = inactiveGateway;
        semaphore = new DefaultSemaphore(inactiveGateway.size());
        this.timeoutMilliseconds = timeout * 1000L;

    }

    public synchronized void addGateway(SGIPSMProxy gateway) {
        if (gateway == null) {
            return;
        }
        if (isClosed) {
            return;
        }
        inactiveGateway.add(gateway);
        semaphore.setPermits(semaphore.getPermits() + 1);
    }

    public synchronized void removeGateway(SGIPSMProxy gateway) {
        if (gateway == null) {
            return;
        }
        if (isClosed) {
            return;
        }
        if (inactiveGateway.remove(gateway)) {
            semaphore.setPermits(semaphore.getPermits() - 1);
        }
    }

    public synchronized void close() {
        if (isClosed) {
            return;
        }
        isClosed = true;
        while (!inactiveGateway.isEmpty()) {
            SGIPSMProxy proxy = inactiveGateway.remove();
            try {
                proxy.close();
            } catch (Exception e2) {

            }
        }
    }

    public SGIPSMProxy fetchGateway() {
        return featchGatewayWithTimeout(timeoutMilliseconds);
    }

    public synchronized void releaseGateway(SGIPSMProxy proxy) {
        if (proxy == null) {
            return;
        }
        if (isClosed) {
            closeGateway(proxy);
            return;
        }

        semaphore.release();
        inactiveGateway.add(proxy);

    }

    private synchronized void closeGateway(SGIPSMProxy proxy) {
        if (proxy == null) {
            return;
        }
        if (!inactiveGateway.remove(proxy)) {
            semaphore.release();
        }
        proxy.close();

    }

    public SGIPSMProxy featchGatewayWithTimeout(long timeoutMs) {
        synchronized (this) {
            if (isClosed) {
                throw new IllegalStateException("gateway pool has been closed.");
            }
        }
        try {
            if (!semaphore.tryAcquire(timeoutMs, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Timeout while waiting for a free gateway.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while waiting for a free gateway.", e);
        }
        boolean ok = false;
        try {
            SGIPSMProxy proxy = fetchProxy();
            ok = true;
            return proxy;
        } finally {
            if (!ok) {
                semaphore.release();
            }
        }
    }

    private synchronized SGIPSMProxy fetchProxy() {
        if (isClosed) { // test again within synchronized lock
            throw new IllegalStateException("gateway pool has been disposed.");
        }
        SGIPSMProxy proxy = null;
        if (!inactiveGateway.isEmpty()) {
            proxy = inactiveGateway.remove();
        } else {
            throw new RuntimeException("网关已用完");
        }

        return proxy;
    }

}
