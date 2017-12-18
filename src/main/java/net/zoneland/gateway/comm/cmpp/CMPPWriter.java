package net.zoneland.gateway.comm.cmpp;

import java.io.IOException;
import java.io.OutputStream;

import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.PWriter;
import net.zoneland.gateway.comm.cmpp.message.CMPPMessage;

public class CMPPWriter extends PWriter
{

    protected OutputStream out;

    public CMPPWriter(OutputStream out)
    {
        this.out = out;
    }

    public void write(PMessage message)
        throws IOException
    {
        CMPPMessage msg = (CMPPMessage)message;
        out.write(msg.getBytes());
    }

    public void writeHeartbeat()
        throws IOException
    {
    }
}
