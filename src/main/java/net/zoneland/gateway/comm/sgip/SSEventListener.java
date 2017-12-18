package net.zoneland.gateway.comm.sgip;

import java.net.Socket;

public interface SSEventListener
{

    public abstract void onConnect(Socket socket);
}
