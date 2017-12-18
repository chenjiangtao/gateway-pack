/**
 * zoneland.net Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package net.zoneland.gateway.pool;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import net.zoneland.gateway.proxy.GatewayProxy;

/**
 * 网关池管理
 * 通过fetchGateway从网关池中获取一个网关，
 * 通过releaseGateway释放网关
 * @author gag
 * @version $Id: GatewayPoolManager.java, v 0.1 2012-8-15 下午1:30:09 gag Exp $
 */
public class GatewayPoolManager<E extends GatewayProxy> {

    private DefaultSemaphore semaphore;

    private LinkedList<E>    inactiveGateway;

    private long             timeoutMilliseconds;

    private boolean          isClosed;

    /**
     * 根据LinkedList<E> proxy初始化网关连接池。
     * 使用默认的超时时间3秒
     * @param inactiveGateway
     */
    public GatewayPoolManager(LinkedList<E> inactiveGateway) {
        this(inactiveGateway, 3);
    }

    /**
     * 根据LinkedList<E> proxy初始化网关连接池
     * @param inactiveGateway 网关连接
     * @param timeout 获取网关的超时时间
     */
    public GatewayPoolManager(LinkedList<E> inactiveGateway, int timeout) {
        if (inactiveGateway == null || inactiveGateway.size() == 0) {
            throw new RuntimeException("网关为空，无法初始化连接池");
        }
        this.inactiveGateway = inactiveGateway;
        semaphore = new DefaultSemaphore(inactiveGateway.size());
        this.timeoutMilliseconds = timeout * 1000L;

    }

    /**
     * 从网关连接池获取网关，默认超时时间为3秒，3秒获取不到则抛出超时异常
     * @return
     */
    public E fetchGateway() {
        return featchGatewayWithTimeout(timeoutMilliseconds);
    }

    /**
     * 释放网关，将网关放到网关连接池中。
     * @param proxy
     */
    public synchronized void releaseGateway(E proxy) {
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

    /**
     * 关闭一个网关。
     * 从网关连接池中移除该网关，并关闭该网关的连接
     * @param proxy
     */
    public synchronized void closeGateway(E proxy) {
        if (proxy == null) {
            return;
        }
        if (!inactiveGateway.remove(proxy)) {
            semaphore.release();
        }

        proxy.close();

    }

    /**
     * 等待一个超时时间，获取网关
     * @param timeoutMs
     * @return
     */
    public E featchGatewayWithTimeout(long timeoutMs) {
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
            E proxy = fetchProxy();
            ok = true;
            return proxy;
        } finally {
            if (!ok) {
                semaphore.release();
            }
        }
    }

    /**
     * 获取网关
     * @return
     */
    private synchronized E fetchProxy() {
        if (isClosed) { // test again within synchronized lock
            throw new IllegalStateException("gateway pool has been disposed.");
        }
        E proxy = null;
        if (!inactiveGateway.isEmpty()) {
            proxy = inactiveGateway.remove();
        } else {
            throw new RuntimeException("网关已用完");
        }

        return proxy;
    }

    /**
     * 动态将网关添加到网关连接池中
     * @param gateway
     */
    public synchronized void addGateway(E gateway) {
        if (gateway == null) {
            return;
        }
        if (isClosed) {
            return;
        }
        inactiveGateway.add(gateway);
        semaphore.setPermits(semaphore.getPermits() + 1);
    }

    /**
     * 动态从网关连接池中移除一个网关。并关闭该网关的连接。
     * @param gateway
     * @return
     */
    public synchronized boolean removeGateway(E gateway) {
        if (gateway == null) {
            return false;
        }
        if (isClosed) {
            return true;
        }
        if (inactiveGateway.remove(gateway)) {
            semaphore.setPermits(semaphore.getPermits() - 1);
            gateway.close();
            return true;
        }
        return false;
    }

    /**
     * 清空网关连接池，并销毁所有连接
     * 
     */
    public synchronized void close() {
        if (isClosed) {
            return;
        }
        isClosed = true;
        while (!inactiveGateway.isEmpty()) {
            GatewayProxy proxy = inactiveGateway.remove();
            try {
                proxy.close();
            } catch (Exception e2) {

            }
        }
    }

}
