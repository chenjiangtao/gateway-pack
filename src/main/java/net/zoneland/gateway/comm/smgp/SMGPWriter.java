package net.zoneland.gateway.comm.smgp;

import java.io.IOException;
import java.io.OutputStream;

import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.PWriter;
import net.zoneland.gateway.comm.smgp.message.SMGPMessage;

public class SMGPWriter extends PWriter
{

    protected OutputStream out;

    public SMGPWriter(OutputStream out)
    {
        this.out = out;
    }

    public void write(PMessage message)
        throws IOException
    {
        SMGPMessage msg = (SMGPMessage)message;
        out.write(msg.getBytes());
    }

    public void writeHeartbeat()
        throws IOException
    {
    }
}
