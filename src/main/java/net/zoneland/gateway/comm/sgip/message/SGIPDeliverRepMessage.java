package net.zoneland.gateway.comm.sgip.message;

import net.zoneland.gateway.comm.sgip.SGIPConstant;
import net.zoneland.gateway.util.TypeConvert;

public class SGIPDeliverRepMessage extends SGIPMessage {

    private String outStr;

    public SGIPDeliverRepMessage(int result) throws IllegalArgumentException {
        if (result < 0 || result > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.DELIVER_REPINPUT_ERROR)))).append(
                ":result").append(SGIPConstant.INT_SCOPE_ERROR))));
        } else {
            int len = 29;
            super.buf = new byte[len];
            TypeConvert.int2byte(len, super.buf, 0);
            TypeConvert.int2byte(0x80000004, super.buf, 4);
            super.buf[20] = (byte) result;
            outStr = ",result=".concat(String.valueOf(String.valueOf(result)));
            return;
        }
    }

    public SGIPDeliverRepMessage(SGIPDeliverMessage msg, int result)
                                                                    throws IllegalArgumentException {
        if (msg == null) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.DELIVER_REPINPUT_ERROR)))).append(
                ":result").append(SGIPConstant.INT_SCOPE_ERROR))));
        }
        int len = 29;
        super.buf = new byte[len];
        TypeConvert.int2byte(len, super.buf, 0);
        TypeConvert.int2byte(0x80000004, super.buf, 4);
        super.setSequenceId(msg.getSequenceId());
        super.setSrcNodeId(msg.getSrcNodeId());
        super.setTimeStamp(msg.getTimeStamp());
        super.buf[20] = (byte) result;
        outStr = ",result=".concat(String.valueOf(String.valueOf(result)));

    }

    public String toString() {
        String tmpStr = "SGIP_DELIVER_REP: ";
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String
            .valueOf(tmpStr)))).append("src_node_id:").append(super.getSrcNodeId())
            .append("timestamp:").append(super.getTimeStamp()).append("Sequence_Id=")
            .append(getSequenceId())));
        tmpStr = String.valueOf(tmpStr) + String.valueOf(outStr);
        return tmpStr;
    }

    public int getCommandId() {
        return 0x80000004;
    }
}
