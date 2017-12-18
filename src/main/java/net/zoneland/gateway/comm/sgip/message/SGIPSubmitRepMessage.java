package net.zoneland.gateway.comm.sgip.message;

import net.zoneland.gateway.comm.sgip.SGIPConstant;
import net.zoneland.gateway.util.TypeConvert;

public class SGIPSubmitRepMessage extends SGIPMessage {

    public SGIPSubmitRepMessage(byte buf[]) throws IllegalArgumentException {
        super.buf = new byte[21];
        if (buf.length != 21) {
            throw new IllegalArgumentException(SGIPConstant.SMC_MESSAGE_ERROR);
        } else {
            System.arraycopy(buf, 0, super.buf, 0, 21);
            super.src_node_Id = TypeConvert.byte2int(super.buf, 0);
            super.time_Stamp = TypeConvert.byte2int(super.buf, 4);
            super.sequence_Id = TypeConvert.byte2int(super.buf, 8);
            return;
        }
    }

    public int getResult() {
        int tmpId = super.buf[12];
        return tmpId;
    }

    public String toString() {
        String tmpStr = "SGIP_SUBMIT_REP: ";
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String
            .valueOf(tmpStr)))).append("Sequence_Id=").append(getSequenceId())));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String
            .valueOf(tmpStr)))).append(",Result=").append(getResult())));
        return tmpStr;
    }

    public int getCommandId() {
        return 0x80000003;
    }

}
