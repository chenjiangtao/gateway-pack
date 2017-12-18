package net.zoneland.gateway.comm.sgip;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SSListener extends Thread {

    private ServerSocket     serversocket;
    private SSEventListener  listener;
    private String           ip;
    private int              port;
    private int              status;
    private static final int ON  = 0;
    private static final int OFF = 1;

    private int              threadStatus;

    public SSListener(String ip, int port, SSEventListener lis) {
        status = 1;
        this.ip = ip;
        this.port = port;
        listener = lis;
        this.threadStatus = ON;
        start();
    }

    public void run() {
        do {
            if (status == 0) {
                try {
                    Socket incoming = serversocket.accept();
                    if (status == 0) {
                        listener.onConnect(incoming);
                    }
                } catch (Exception ex) {
                    if (status != 0)
                        ;
                }
            } else {
                synchronized (this) {
                    try {
                        wait(10000L);
                    } catch (Exception exception) {
                    }
                }
            }
        } while (threadStatus == ON);
    }

    public synchronized void beginListen() throws Exception {
        if (status == 0) {
            return;
        }
        try {
            serversocket = new ServerSocket();
            serversocket.setReuseAddress(true);
            if (ip == null) {
                serversocket.bind(new InetSocketAddress(port));
            } else {
                serversocket.bind(new InetSocketAddress(ip, port));
            }
            status = 0;
            notifyAll();
        } catch (Exception ioex) {
            throw ioex;
        }
    }

    public synchronized void stopListen() {
        //if (status == 0) {
        threadStatus = OFF;
        try {
            if (serversocket != null) {
                status = 1;
                serversocket.close();
                serversocket = null;

            }

        } catch (IOException ioexception) {
        }
        // }
    }

}
