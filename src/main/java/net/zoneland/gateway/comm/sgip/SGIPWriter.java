package net.zoneland.gateway.comm.sgip;

import java.io.IOException;
import java.io.OutputStream;

import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.PWriter;
import net.zoneland.gateway.comm.sgip.message.SGIPMessage;

public class SGIPWriter extends PWriter
{

    protected OutputStream out;

    public SGIPWriter(OutputStream out)
    {
        this.out = out;
    }

    public void write(PMessage message)
        throws IOException
    {
        SGIPMessage msg = (SGIPMessage)message;
        out.write(msg.getBytes());
    }

    public void writeHeartbeat()
        throws IOException
    {
    }
}
