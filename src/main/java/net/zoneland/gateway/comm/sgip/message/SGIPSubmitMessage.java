package net.zoneland.gateway.comm.sgip.message;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.zoneland.gateway.comm.sgip.SGIPConstant;
import net.zoneland.gateway.util.TypeConvert;

public class SGIPSubmitMessage extends SGIPMessage {

    private StringBuffer buf;

    /**
     * @param SPNumber SP的接入号码，字符
     * @param ChargeNumber 付费号码，字符，手机号码前加“86”国别标志；
     *       当且仅当群发且对用户收费时为空；如果为空，则该条短消息产生的费用
     *       由UserNumber代表的用户支付；如果为全零字符串“000000000000000000000”，
     *       表示该条短消息产生的费用由SP支付。
     * @param UserNumber 一个或多个接收该短消息的手机号，手机号之间用逗号(,)隔开，字符，手机号码前加“86”国别标志，如8613001125453,8613001132345
     * @param CorpId 企业代码，取值范围0-99999，字符
     * @param ServiceType 业务代码，由SP定义，字符
     * @param FeeType 计费类型，字符
     * @param FeeValue 取值范围0-99999，该条短消息的收费值，单位为分，由SP定义，字符
                        对于包月制收费的用户，该值为月租费的值

     * @param GivenValue 取值范围0-99999，赠送用户的话费，单位为分，由SP定义，
     *              特指由SP向用户发送广告时的赠送话费，字符
     * @param AgentFlag 代收费标志，0：应收；1：实收，字符
     * @param MorelatetoMTFlag
     *         引起MT消息的原因
                0-MO点播引起的第一条MT消息；
                1-MO点播引起的非第一条MT消息；
                2-非MO点播引起的MT消息；
                3-系统反馈引起的MT消息。
     * @param Priority 优先级0-9从低到高，默认为0，十六进制数字
     * @param ExpireTime 短消息寿命的终止时间，如果为空，表示使用短消息中心的缺省值。时间内容为16个字符，格式为“yymmddhhmmsstnnp”，
     *              其中“tnnp”取固定值“032+”，即默认系统为北京时间
     * @param ScheduleTime
     *         短消息定时发送的时间，如果为空，表示立刻发送该短消息。
     *         时间内容为16个字符，格式为“yymmddhhmmsstnnp”，
     *         其中“tnnp”取固定值“032+”，即默认系统为北京时间
     * @param ReportFlag
     *         状态报告标记
                0-该条消息只有最后出错时要返回状态报告
                1-该条消息无论最后是否成功都要返回状态报告
                2-该条消息不需要返回状态报告
                3-该条消息仅携带包月计费信息，不下发给用户，要返回状态报告
                其它-保留
                缺省设置为0，十六进制数字

     * @param TP_pid GSM协议类型。详细解释请参考GSM03.40中的9.2.3.9 十六进制数字

     * @param TP_udhi GSM协议类型。详细解释请参考GSM03.40中的9.2.3.23,仅使用1位，右对齐,十六进制数字
     * @param MessageCoding
     *         短消息的编码格式。
                0：纯ASCII字符串
                3：写卡操作
                4：二进制编码
                8：UCS2编码
                15：GBK编码
                其它参见GSM3.38第4节：SMS Data Coding Scheme
                十六进制数字

     * @param MessageType
     *         信息类型：
                0-短消息信息
                其它：待定
                十六进制数字

     * @param MessageLen GSM协议类型。详细解释请参考GSM03.40中的9.2.3.23,仅使用1位，右对齐,十六进制数字
     * @param MessageContent
     *          
     * @param reserve
     * @throws IllegalArgumentException
     */
    public SGIPSubmitMessage(String SPNumber, String ChargeNumber, String UserNumber[],
                             String CorpId, String ServiceType, int FeeType, String FeeValue,
                             String GivenValue, int AgentFlag, int MorelatetoMTFlag, int Priority,
                             Date ExpireTime, Date ScheduleTime, int ReportFlag, int TP_pid,
                             int TP_udhi, int MessageCoding, int MessageType, int MessageLen,
                             byte MessageContent[], String reserve) throws IllegalArgumentException {
        if (SPNumber.length() > 21) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))))
                .append(":SPNumber").append(SGIPConstant.STRING_LENGTH_GREAT).append("21"))));
        }
        if (ChargeNumber.length() > 21) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))))
                .append(":ChargeNumber").append(SGIPConstant.STRING_LENGTH_GREAT).append("21"))));
        }
        if (UserNumber.length > 100) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))))
                .append(":UserNumber").append(SGIPConstant.STRING_LENGTH_GREAT).append("100"))));
        }
        for (int i = 0; i < UserNumber.length; i++) {
            if (UserNumber[i].length() > 21) {
                throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                    String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))))
                    .append(":UserNumber[").append(i).append("]")
                    .append(SGIPConstant.STRING_LENGTH_GREAT).append("21"))));
            }
        }

        if (CorpId.length() > 5) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR)))).append(":CorpId")
                .append(SGIPConstant.STRING_LENGTH_GREAT).append("5"))));
        }
        if (ServiceType.length() > 10) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))))
                .append(":ServiceType").append(SGIPConstant.STRING_LENGTH_GREAT).append("10"))));
        }
        if (FeeType < 0 || FeeType > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))))
                .append(":FeeType").append(SGIPConstant.INT_SCOPE_ERROR))));
        }
        if (FeeValue.length() > 6) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))))
                .append(":FeeValue").append(SGIPConstant.STRING_LENGTH_GREAT).append("6"))));
        }
        if (GivenValue.length() > 6) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))))
                .append(":GivenValue").append(SGIPConstant.STRING_LENGTH_GREAT).append("6"))));
        }
        if (AgentFlag < 0 || AgentFlag > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":AgentFlag").append(SGIPConstant.INT_SCOPE_ERROR))));
        }
        if (MorelatetoMTFlag < 0 || MorelatetoMTFlag > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":MorelatetoMTFlag").append(SGIPConstant.INT_SCOPE_ERROR))));
        }
        if (MorelatetoMTFlag < 0 || MorelatetoMTFlag > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":MorelatetoMTFlag").append(SGIPConstant.INT_SCOPE_ERROR))));
        }
        if (Priority < 0 || Priority > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":Priority").append(SGIPConstant.INT_SCOPE_ERROR))));
        }
        if (ReportFlag < 0 || ReportFlag > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":ReportFlag").append(SGIPConstant.INT_SCOPE_ERROR))));
        }
        if (TP_pid < 0 || TP_pid > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR)))).append(":TP_pid")
                .append(SGIPConstant.INT_SCOPE_ERROR))));
        }
        if (TP_udhi < 0 || TP_udhi > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))))
                .append(":TP_udhi").append(SGIPConstant.INT_SCOPE_ERROR))));
        }
        if (MessageCoding < 0 || MessageCoding > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":MessageCoding").append(SGIPConstant.INT_SCOPE_ERROR))));
        }
        if (MessageType < 0 || MessageType > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":MessageType").append(SGIPConstant.INT_SCOPE_ERROR))));
        }
        if (MessageLen < 0 || MessageLen > 160) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":MessageLen").append(SGIPConstant.INT_SCOPE_ERROR))));
        }
        if (MessageContent.length > 160) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))))
                .append(":MessageContent").append(SGIPConstant.STRING_LENGTH_GREAT).append("160"))));
        }
        if (reserve.length() > 8) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SGIPConstant.SUBMIT_INPUT_ERROR))))
                .append(":reserve").append(SGIPConstant.STRING_LENGTH_GREAT).append("8"))));
        }
        int len = 143 + 21 * UserNumber.length + MessageLen;
        super.buf = new byte[len];
        TypeConvert.int2byte(len, super.buf, 0);
        TypeConvert.int2byte(3, super.buf, 4);
        System.arraycopy(SPNumber.getBytes(), 0, super.buf, 20, SPNumber.length());
        System.arraycopy(ChargeNumber.getBytes(), 0, super.buf, 41, ChargeNumber.length());
        super.buf[62] = (byte) UserNumber.length;
        int i = 0;
        for (i = 0; i < UserNumber.length; i++) {
            System.arraycopy(UserNumber[i].getBytes(), 0, super.buf, 63 + i * 21,
                UserNumber[i].length());
        }

        int loc = 63 + i * 21;
        System.arraycopy(CorpId.getBytes(), 0, super.buf, loc, CorpId.length());
        loc += 5;
        System.arraycopy(ServiceType.getBytes(), 0, super.buf, loc, ServiceType.length());
        loc += 10;
        super.buf[loc++] = (byte) FeeType;
        System.arraycopy(FeeValue.getBytes(), 0, super.buf, loc, FeeValue.length());
        loc += 6;
        System.arraycopy(GivenValue.getBytes(), 0, super.buf, loc, GivenValue.length());
        loc += 6;
        super.buf[loc++] = (byte) AgentFlag;
        super.buf[loc++] = (byte) MorelatetoMTFlag;
        super.buf[loc++] = (byte) Priority;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        if (ExpireTime != null) {
            String tmpTime = String.valueOf(String.valueOf(dateFormat.format(ExpireTime))).concat(
                "032+");
            System.arraycopy(tmpTime.getBytes(), 0, super.buf, loc, 16);
        }
        loc += 16;
        if (ScheduleTime != null) {
            String tmpTime = String.valueOf(String.valueOf(dateFormat.format(ScheduleTime)))
                .concat("032+");
            System.arraycopy(tmpTime.getBytes(), 0, super.buf, loc, 16);
        }
        loc += 16;
        super.buf[loc++] = (byte) ReportFlag;
        super.buf[loc++] = (byte) TP_pid;
        super.buf[loc++] = (byte) TP_udhi;
        super.buf[loc++] = (byte) MessageCoding;
        super.buf[loc++] = (byte) MessageType;
        TypeConvert.int2byte(MessageLen, super.buf, loc);
        loc += 4;
        System.arraycopy(MessageContent, 0, super.buf, loc, MessageLen);
        loc += MessageLen;
        System.arraycopy(reserve.getBytes(), 0, super.buf, loc, reserve.length());

        buf = new StringBuffer();
        buf.append(",SPNumber=").append(SPNumber).append(",ChargeNumber=").append(ChargeNumber)
            .append(",UserCount=").append(UserNumber.length);
        for (int t = 0; t < UserNumber.length; t++) {
            buf.append(",UserNumber[").append(t).append("]=").append(UserNumber[t]);

        }
        buf.append(",CorpId=").append(CorpId).append(",ServiceType=").append(ServiceType)
            .append(",FeeType=").append(FeeType).append(",FeeValue=").append(FeeValue)
            .append(",GivenValue=").append(GivenValue).append(",AgentFlag=").append(AgentFlag)
            .append(",MorelatetoMTFlag=").append(MorelatetoMTFlag).append(",Priority=")
            .append(Priority).append(",ExpireTime=").append(ExpireTime).append(",ScheduleTime=")
            .append(ScheduleTime).append(",ReportFlag=").append(ReportFlag).append(",ExpireTime=")
            .append(ExpireTime).append(",ScheduleTime=").append(ScheduleTime).append(",TP_pid=")
            .append(TP_pid).append(",TP_udhi=").append(TP_udhi).append(",MessageCoding=")
            .append(MessageCoding).append(",MessageType=").append(MessageType)
            .append(",MessageLength=").append(MessageLen).append(",MessageContent=")
            .append(",src_node_id=").append(super.getSrcNodeId()).append(",timestamp=")
            .append(super.getTimeStamp());
        try {
            buf.append(super.getMsgContentStr(MessageContent, MessageCoding));
        } catch (UnsupportedEncodingException e) {
            buf.append(new String(MessageContent));
        }
        buf.append(",reserve=").append(reserve);

    }

    public String toString() {
        String tmpStr = "SGIP_Submit: ";
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String
            .valueOf(tmpStr)))).append("Sequence_Id=").append(getSequenceId())));
        tmpStr = String.valueOf(tmpStr) + String.valueOf(buf);
        return tmpStr;
    }

    public int getCommandId() {
        return 3;
    }
}
