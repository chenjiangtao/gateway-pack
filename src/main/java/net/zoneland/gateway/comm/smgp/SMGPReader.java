package net.zoneland.gateway.comm.smgp;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.PReader;
import net.zoneland.gateway.comm.smgp.message.SMGPActiveTestMessage;
import net.zoneland.gateway.comm.smgp.message.SMGPActiveTestRespMessage;
import net.zoneland.gateway.comm.smgp.message.SMGPDeliverMessage;
import net.zoneland.gateway.comm.smgp.message.SMGPExitMessage;
import net.zoneland.gateway.comm.smgp.message.SMGPExitRespMessage;
import net.zoneland.gateway.comm.smgp.message.SMGPForwardRespMessage;
import net.zoneland.gateway.comm.smgp.message.SMGPLoginRespMessage;
import net.zoneland.gateway.comm.smgp.message.SMGPMoRouteUpdateRespMessage;
import net.zoneland.gateway.comm.smgp.message.SMGPMtRouteUpdateRespMessage;
import net.zoneland.gateway.comm.smgp.message.SMGPQueryRespMessage;
import net.zoneland.gateway.comm.smgp.message.SMGPSubmitRespMessage;

public class SMGPReader extends PReader
{

    protected DataInputStream in;

    public SMGPReader(InputStream is)
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
            return new SMGPLoginRespMessage(buf);
        }
        if(command_Id == 3)
        {
            return new SMGPDeliverMessage(buf);
        }
        if(command_Id == 0x80000002)
        {
            return new SMGPSubmitRespMessage(buf);
        }
        if(command_Id == 0x80000005)
        {
            return new SMGPForwardRespMessage(buf);
        }
        if(command_Id == 0x80000007)
        {
            return new SMGPQueryRespMessage(buf);
        }
        if(command_Id == 0x80000004)
        {
            return new SMGPActiveTestRespMessage(buf);
        }
        if(command_Id == 4)
        {
            return new SMGPActiveTestMessage(buf);
        }
        if(command_Id == 6)
        {
            return new SMGPExitMessage(buf);
        }
        if(command_Id == 0x80000006)
        {
            return new SMGPExitRespMessage(buf);
        }
        if(command_Id == 0x80000008)
        {
            return new SMGPMtRouteUpdateRespMessage(buf);
        }
        if(command_Id == 0x80000009)
        {
            return new SMGPMoRouteUpdateRespMessage(buf);
        } else
        {
            return null;
        }
    }
}
