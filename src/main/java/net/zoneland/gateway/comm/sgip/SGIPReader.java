package net.zoneland.gateway.comm.sgip;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.PReader;
import net.zoneland.gateway.comm.sgip.message.SGIPBindMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPBindRepMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPDeliverMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPReportMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPSubmitRepMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPUnbindMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPUnbindRepMessage;
import net.zoneland.gateway.comm.sgip.message.SGIPUserReportMessage;

public class SGIPReader extends PReader
{

    protected DataInputStream in;

    public SGIPReader(InputStream is)
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
            return new SGIPBindRepMessage(buf);
        }
        if(command_Id == 1)
        {
            return new SGIPBindMessage(buf);
        }
        if(command_Id == 4)
        {
            return new SGIPDeliverMessage(buf);
        }
        if(command_Id == 0x80000003)
        {
            return new SGIPSubmitRepMessage(buf);
        }
        if(command_Id == 5)
        {
            return new SGIPReportMessage(buf);
        }
        if(command_Id == 17)
        {
            return new SGIPUserReportMessage(buf);
        }
        if(command_Id == 2)
        {
            return new SGIPUnbindMessage(buf);
        }
        if(command_Id == 0x80000002)
        {
            return new SGIPUnbindRepMessage(buf);
        } else
        {
            return null;
        }
    }
}
