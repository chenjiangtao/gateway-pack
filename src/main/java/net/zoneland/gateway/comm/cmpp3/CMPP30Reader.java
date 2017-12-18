package net.zoneland.gateway.comm.cmpp3;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.PReader;
import net.zoneland.gateway.comm.cmpp.message.CMPPActiveMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPActiveRepMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPCancelRepMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPQueryRepMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPTerminateMessage;
import net.zoneland.gateway.comm.cmpp.message.CMPPTerminateRepMessage;
import net.zoneland.gateway.comm.cmpp3.message.CMPP30ConnectRepMessage;
import net.zoneland.gateway.comm.cmpp3.message.CMPP30DeliverMessage;
import net.zoneland.gateway.comm.cmpp3.message.CMPP30SubmitRepMessage;

public class CMPP30Reader extends PReader
{

    protected DataInputStream in;

    public CMPP30Reader(InputStream is)
    {
        in = new DataInputStream(is);
    }

    public PMessage read()
        throws IOException
    {
        int total_Length = in.readInt();
        int command_Id = in.readInt();
        byte buf[] = new byte[total_Length - 8];
        in.readFully(buf);
        if(command_Id == 0x80000001)
        {
            return new CMPP30ConnectRepMessage(buf);
        }
        if(command_Id == 5)
        {
            return new CMPP30DeliverMessage(buf);
        }
        if(command_Id == 0x80000004)
        {
            return new CMPP30SubmitRepMessage(buf);
        }
        if(command_Id == 0x80000006)
        {
            return new CMPPQueryRepMessage(buf);
        }
        if(command_Id == 0x80000007)
        {
            return new CMPPCancelRepMessage(buf);
        }
        if(command_Id == 0x80000008)
        {
            return new CMPPActiveRepMessage(buf);
        }
        if(command_Id == 8)
        {
            return new CMPPActiveMessage(buf);
        }
        if(command_Id == 2)
        {
            return new CMPPTerminateMessage(buf);
        }
        if(command_Id == 0x80000002)
        {
            return new CMPPTerminateRepMessage(buf);
        } else
        {
            return null;
        }
    }
}
