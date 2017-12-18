package net.zoneland.gateway.comm.cmpp3.message;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.zoneland.gateway.comm.cmpp.CMPPConstant;
import net.zoneland.gateway.comm.cmpp.message.CMPPMessage;
import net.zoneland.gateway.util.TypeConvert;

public class CMPP30SubmitMessage extends CMPPMessage {

    private String outStr;

    /**
     * @param pk_Total Unsigned Integer 相同Msg_Id 的信息总条数，从1 开始。
     * @param pk_Number 相同Msg_Id 的信息序号，从1 开始。
     * @param registered_Delivery 是否要求返回状态确认报告：
                        0：不需要；
                        1：需要。
     * @param msg_Level 信息级别。
     * @param service_Id 业务标识，是数字、字母和符号的组合
     * @param fee_UserType 计费用户类型字段：
                0：对目的终端MSISDN 计费；
                1：对源终端MSISDN 计费；
                2：对SP 计费；
                3 ： 表示本字段无效， 对谁计费参见
                Fee_terminal_Id 字段。
     * @param fee_Terminal_Id 被计费用户的号码，当Fee_UserType 为3
                时该值有效，当Fee_UserType 为0、1、2
                时该值无意义。
     * @param fee_Terminal_Type 被计费用户的号码类型，0：真实号码；1：伪码
     * @param tp_Pid GSM 协议类型
     * @param tp_Udhi GSM 协议类型
     * @param msg_Fmt 信息格式：
                    0：ASCII 串；
                    3：短信写卡操作；
                    4：二进制信息；
                    8：UCS2 编码；
                    15：含GB 汉字
     * @param msg_Src 信息内容来源(SP_Id)
     * @param fee_Type 资费类别：
            01：对“计费用户号码”免费；
            02：对“计费用户号码”按条计信息费；
            03：对“计费用户号码”按包月收取信息
            费
     * @param fee_Code 资费代码（以分为单位）。
     * @param valid_Time 存活有效期，格式遵循SMPP3.3 协议
     * @param at_Time 定时发送时间，格式遵循SMPP3.3 协议
     * @param src_Terminal_Id 源号码。SP 的服务代码或前缀为服务代
                码的长号码, 网关将该号码完整的填到
                SMPP 协议Submit_SM 消息相应的
                source_addr 字段，该号码最终在用户手机
                上显示为短消息的主叫号码。
     * @param dest_Terminal_Id 接收短信的MSISDN 号码
     * @param dest_Terminal_Type 接收短信的用户的号码类型，0：真实号码；1：伪码。
     * @param msg_Content 信息长度(Msg_Fmt 值为0 时：<160 个字节；其它<=140 个字节)
     * @param LinkID
     * @throws IllegalArgumentException
     */
    public CMPP30SubmitMessage(int pk_Total, int pk_Number, int registered_Delivery, int msg_Level,
                               String service_Id, int fee_UserType, String fee_Terminal_Id,
                               int fee_Terminal_Type, int tp_Pid, int tp_Udhi, int msg_Fmt,
                               String msg_Src, String fee_Type, String fee_Code, Date valid_Time,
                               Date at_Time, String src_Terminal_Id, String dest_Terminal_Id[],
                               int dest_Terminal_Type, byte msg_Content[], String LinkID)
                                                                                         throws IllegalArgumentException {
        if (pk_Total < 1 || pk_Total > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))).append(":")
                .append(CMPPConstant.PK_TOTAL_ERROR))));
        }
        if (pk_Number < 1 || pk_Number > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))).append(":")
                .append(CMPPConstant.PK_NUMBER_ERROR))));
        }
        if (registered_Delivery < 0 || registered_Delivery > 1) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))).append(":")
                .append(CMPPConstant.REGISTERED_DELIVERY_ERROR))));
        }
        if (msg_Level < 0 || msg_Level > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":msg_Level").append(CMPPConstant.INT_SCOPE_ERROR))));
        }
        if (service_Id.length() > 10) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))))
                .append(":service_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("10"))));
        }
        if (fee_UserType < 0 || fee_UserType > 3) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))).append(":")
                .append(CMPPConstant.FEE_USERTYPE_ERROR))));
        }
        if (fee_Terminal_Id.length() > 32) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))))
                .append(":fee_Terminal_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("32"))));
        }
        if (fee_Terminal_Type < 0 || fee_Terminal_Type > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":fee_terminal_type").append(CMPPConstant.INT_SCOPE_ERROR))));
        }
        if (tp_Pid < 0 || tp_Pid > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))).append(":tp_Pid")
                .append(CMPPConstant.INT_SCOPE_ERROR))));
        }
        if (tp_Udhi < 0 || tp_Udhi > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))))
                .append(":tp_Udhi").append(CMPPConstant.INT_SCOPE_ERROR))));
        }
        if (msg_Fmt < 0 || msg_Fmt > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))))
                .append(":msg_Fmt").append(CMPPConstant.INT_SCOPE_ERROR))));
        }
        //        if (msg_Src.length() > 6) {
        //            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
        //                String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))))
        //                .append(":msg_Src").append(CMPPConstant.STRING_LENGTH_GREAT).append("6"))));
        //        }
        if (fee_Type.length() > 2) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))))
                .append(":fee_Type").append(CMPPConstant.STRING_LENGTH_GREAT).append("2"))));
        }
        if (fee_Code.length() > 6) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))))
                .append(":fee_Code").append(CMPPConstant.STRING_LENGTH_GREAT).append("6"))));
        }
        if (src_Terminal_Id.length() > 21) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))))
                .append(":src_Terminal_Id").append(CMPPConstant.STRING_LENGTH_GREAT).append("21"))));
        }
        if (dest_Terminal_Id.length > 100) {
            throw new IllegalArgumentException(String.valueOf(String
                .valueOf((new StringBuffer(String.valueOf(String
                    .valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))).append(":dest_Terminal_Id")
                    .append(CMPPConstant.STRING_LENGTH_GREAT).append("100"))));
        }
        for (int i = 0; i < dest_Terminal_Id.length; i++) {
            if (dest_Terminal_Id[i].length() > 32) {
                throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                    String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))))
                    .append(":dest_Terminal_Id").append(CMPPConstant.STRING_LENGTH_GREAT)
                    .append("32"))));
            }
        }

        if (dest_Terminal_Type < 0 || dest_Terminal_Type > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":dest_terminal_type").append(CMPPConstant.INT_SCOPE_ERROR))));
        }
        if (msg_Fmt == 0) {
            if (msg_Content.length > 160) {
                throw new IllegalArgumentException(
                    String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String
                        .valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))).append(":msg_Content")
                        .append(CMPPConstant.STRING_LENGTH_GREAT).append("160"))));
            }
        } else if (msg_Content.length > 140) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR))))
                .append(":msg_Content").append(CMPPConstant.STRING_LENGTH_GREAT).append("140"))));
        }
        if (LinkID.length() > 20) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(CMPPConstant.SUBMIT_INPUT_ERROR)))).append(":LinkID")
                .append(CMPPConstant.STRING_LENGTH_GREAT).append("20"))));
        }
        int len = 163 + 32 * dest_Terminal_Id.length + msg_Content.length;
        super.buf = new byte[len];
        TypeConvert.int2byte(len, super.buf, 0);
        TypeConvert.int2byte(4, super.buf, 4);
        super.buf[20] = (byte) pk_Total;
        super.buf[21] = (byte) pk_Number;
        super.buf[22] = (byte) registered_Delivery;
        super.buf[23] = (byte) msg_Level;
        System.arraycopy(service_Id.getBytes(), 0, super.buf, 24, service_Id.length());
        super.buf[34] = (byte) fee_UserType;
        System.arraycopy(fee_Terminal_Id.getBytes(), 0, super.buf, 35, fee_Terminal_Id.length());
        super.buf[67] = (byte) fee_Terminal_Type;
        super.buf[68] = (byte) tp_Pid;
        super.buf[69] = (byte) tp_Udhi;
        super.buf[70] = (byte) msg_Fmt;
        System.arraycopy(msg_Src.getBytes(), 0, super.buf, 71, msg_Src.length());
        System.arraycopy(fee_Type.getBytes(), 0, super.buf, 77, fee_Type.length());
        System.arraycopy(fee_Code.getBytes(), 0, super.buf, 79, fee_Code.length());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        if (valid_Time != null) {
            String tmpTime = String.valueOf(String.valueOf(dateFormat.format(valid_Time))).concat(
                "032+");
            System.arraycopy(tmpTime.getBytes(), 0, super.buf, 85, 16);
        }
        if (at_Time != null) {
            String tmpTime = String.valueOf(String.valueOf(dateFormat.format(at_Time))).concat(
                "032+");
            System.arraycopy(tmpTime.getBytes(), 0, super.buf, 102, 16);
        }
        System.arraycopy(src_Terminal_Id.getBytes(), 0, super.buf, 119, src_Terminal_Id.length());
        super.buf[140] = (byte) dest_Terminal_Id.length;
        int i = 0;
        for (i = 0; i < dest_Terminal_Id.length; i++) {
            System.arraycopy(dest_Terminal_Id[i].getBytes(), 0, super.buf, 141 + i * 32,
                dest_Terminal_Id[i].length());
        }

        int loc = 141 + i * 32;
        super.buf[loc] = (byte) dest_Terminal_Type;
        loc++;
        super.buf[loc] = (byte) msg_Content.length;
        loc++;
        System.arraycopy(msg_Content, 0, super.buf, loc, msg_Content.length);
        loc += msg_Content.length;
        System.arraycopy(LinkID.getBytes(), 0, super.buf, loc, LinkID.length());

        outStr = ",msg_id=00000000";
        StringBuffer buf = new StringBuffer();
        buf.append(outStr).append(",pk_Total=").append(pk_Total).append(",pk_Number=")
            .append(pk_Number).append(",registered_Delivery=").append(registered_Delivery)
            .append(",msg_Level=").append(msg_Level).append(",service_Id=").append(service_Id)
            .append(",fee_UserType=").append(fee_UserType).append(",fee_Terminal_Id=")
            .append(fee_Terminal_Id).append(",fee_Terminal_type=").append(fee_Terminal_Type)
            .append(",tp_Pid=").append(tp_Pid).append(",tp_Udhi=").append(tp_Udhi)
            .append(",msg_Fmt=").append(msg_Fmt).append(",msg_Src=").append(msg_Src)
            .append(",fee_Type=").append(fee_Type).append(",fee_Code=").append(fee_Code)
            .append(",valid_Time=").append(valid_Time).append(",at_Time=").append(at_Time)
            .append(",src_Terminal_Id=").append(src_Terminal_Id).append(",destusr_Tl=")
            .append(dest_Terminal_Id.length);
        for (int t = 0; t < dest_Terminal_Id.length; t++) {
            buf.append(",dest_Terminal_Id[").append(t).append("]=").append(dest_Terminal_Id[t]);
        }

        buf.append(",dest_Terminal_type=").append(dest_Terminal_Type).append(",msg_Length=")
            .append(msg_Content.length);
        try {
            buf.append(",msg_Content=").append(super.getMsgContentStr(msg_Content, msg_Fmt));
        } catch (UnsupportedEncodingException e) {
            buf.append(",msg_Content=").append(new String(msg_Content));
        }
        buf.append(",LinkID=").append(LinkID);
        outStr = buf.toString();

    }

    public String toString() {
        String tmpStr = "CMPP30_Submit: ";
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String
            .valueOf(tmpStr)))).append("Sequence_Id=").append(getSequenceId())));
        tmpStr = String.valueOf(tmpStr) + String.valueOf(outStr);
        return tmpStr;
    }

    public int getCommandId() {
        return 4;
    }
}
