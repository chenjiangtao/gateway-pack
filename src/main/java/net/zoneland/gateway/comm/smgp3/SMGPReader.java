package net.zoneland.gateway.comm.smgp3;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.zoneland.gateway.comm.PMessage;
import net.zoneland.gateway.comm.PReader;
import net.zoneland.gateway.comm.smgp3.message.RequestId;
import net.zoneland.gateway.comm.smgp3.message.SMGP3ActiveTestMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3ActiveTestRespMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3DeliverMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3ExitMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3ExitRespMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3ForwardRespMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3LoginRespMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3MoRouteUpdateRespMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3MtRouteUpdateRespMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3QueryRespMessage;
import net.zoneland.gateway.comm.smgp3.message.SMGP3SubmitRespMessage;

public class SMGPReader extends PReader {

    protected DataInputStream in;

    public SMGPReader(InputStream is) {
        in = new DataInputStream(is);
    }

    public PMessage read() throws IOException {
        PMessage result = null;
        int total_Length = in.readInt();//总长度
        int request_id = in.readInt(); //请求编号
        byte buf[] = new byte[total_Length - 8];
        in.readFully(buf);
        switch (request_id) {
            case RequestId.LOGIN_RESP:
                result = new SMGP3LoginRespMessage(buf);
                break;
            case RequestId.DELIVER:
                result = new SMGP3DeliverMessage(buf);
                break;
            case RequestId.SUBMIT_RESP:
                result = new SMGP3SubmitRespMessage(buf);
                break;
            case RequestId.FORWARD_RESP:
                result = new SMGP3ForwardRespMessage(buf);
                break;
            case RequestId.QUERY_RESP:
                result = new SMGP3QueryRespMessage(buf);
                break;
            case RequestId.ACTIVETEST_RESP:
                result = new SMGP3ActiveTestRespMessage(buf);
                break;
            case RequestId.ACTIVETEST:
                result = new SMGP3ActiveTestMessage(buf);
                break;
            case RequestId.EXIT:
                result = new SMGP3ExitMessage(buf);
                break;
            case RequestId.EXIT_RESP:
                result = new SMGP3ExitRespMessage(buf);
                break;
            case RequestId.QUERY_TE_ROUTE_RESP:
                result = new SMGP3MtRouteUpdateRespMessage(buf);
                break;
            case RequestId.QUERY_SP_ROUTE_RESP:
                result = new SMGP3MoRouteUpdateRespMessage(buf);
                break;
        }
        return result;
    }
}
