package net.zoneland.gateway.comm.smgp.message;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.zoneland.gateway.comm.smgp.SMGPConstant;
import net.zoneland.gateway.util.TypeConvert;

public class SMGPSubmitMessage extends SMGPMessage {

    private StringBuffer strBuf;

    /**
     * @param msgType 短消息类型
     *        对于回执消息该字段无效；对于文本短消息，该字段表示短消息的消息流向：
                0＝MO 消息（终端发给SP）；
                6＝MT 消息（SP 发给终端，包括WEB 上发送的点对点短消息）；
                7＝点对点短消息；其它保留。
     * @param needReport 是否要求返回状态报告
     *          是否要求返回状态报告。
                0＝不要求返回状态报告；
                1＝要求返回状态报告；
                其它保留。
     * @param priority 短消息发送优先级
     *                短消息发送优先级。
                        0＝低优先级；
                        1＝普通优先级；
                        2＝较高优先级；
                        3＝高优先级；
                        其它保留。
     * @param serviceId 业务代码
     *          业务代码，用于固定网业务。
                对于MO 消息或点对点短消息，该字段无效；
                对于MT 消息，该字段表示业务代码，是该条短消息所属的业务类别，由数字、字母
                和符号组合而成。对于从WEB 上发送的点对点短消息，要求业务代码为 “PC2P”，其它业
                务代码由SP 自定义。
     * @param feeType 收费类型
     *          对计费用户采取的收费类型。
                对于MO 消息或点对点短消息，该字段无效。对于MT 消息，该字段用法如下：
                00＝免费，此时FixedFee 和FeeCode 无效；
                01＝按条计信息费，此时FeeCode 表示每条费用，FixedFee 无效；
                02＝按包月收取信息费，此时FeeCode 无效，FixedFee 表示包月费用；
                03＝按封顶收取信息费，若按条收费的费用总和达到或超过封顶费后，则按照封顶费
                用收取信息费；若按条收费的费用总和没有达到封顶费用，则按照每条费用总和收取信息费。
     * @param feeCode 资费代码
     *         每条短消息费率，单位为“分”。
                对于MO 消息或点对点短消息，该字段无效；对于MT 消息，该字段具体使用方法参
                见7.2.13 节。
     * @param fixedFee 包月费/封顶费
     *           短消息的包月费/封顶费，单位为“分”。
                    对于MO 消息或点对点短消息，该字段无效；对于MT 消息，该字段具体使用方法参
                    见7.2.13 节。
     * @param msgFormat 短消息格式
     *          短消息内容体的编码格式。
                0＝ASCII 编码；
                3＝短消息写卡操作；
                4＝二进制短消息；
                8＝UCS2 编码；
                15＝GB18030 编码；
                246（F6）＝UIM 相关消息UIM 相关消息，用于与UIM 卡相关的OTA 等业务，终端
                收到该类型消息直接转发给UIM 卡，由UIM 卡来处理该类型消息；
                其它保留。
                对于文字短消息，要求MsgFormat＝15。对于回执消息，要求MsgFormat＝0。
     * @param validTime 短消息有效时间
     *         短消息有效时间，格式遵循SMPP3.3 以上版本协议。
               短消息有效时间在转发过程中保持不变。
     * @param atTime 短消息定时发送时间
     *         短消息定时发送时间，格式遵循SMPP3.3 以上版本协议。
               短消息定时发送时间在转发过程中保持不变。
     * @param srcTermId 短信息发送方号码
     *         短消息发送方号码。
                对于MT 消息，SrcTermID 格式为“118＋SP 服务代码＋其它（可选）”，例如SP 服务
                代码为1234 时，SrcTermID 可以为1181234 或118123456 等。
                对于MO 消息，固定网中SrcTermID 格式为“区号+号码（区号前添零）”，例如
                02087310323，07558780808，移动网中SrcTermID 格式为MSISDN 号码格式。
                对于固定网点对点消息，主叫号码为普通终端时，SrcTermID 格式为“区号+号码（区
                号前添零）”；主叫号码为爱因平台时，SrcTermID 格式为“10631＋区号+号码（区号前添零）”。
     * @param chargeTermId 计费用户号码
     *         计费用户号码。
                ChargeTermID 为空时，如果是MT 消息，则表示对被叫用户号码计费；如果是MO 或
                点对点消息，则表示对主叫用户号码计费。
                ChargeTermID 为非空时，表示对计费用户号码计费。
     * @param destTermId 短消息接收号码 短消息接收号码总数（≤100），用于SP 实现群发短消息。
     *         对于MT 消息，DestTermID 连续存储DestTermIDCount 个号码，每一个接收方号码为
                21 位，固定网中DestTermID 格式为“区号+号码（区号前添零）”，移动网中DestTermID 格
                式为MSISDN 号码格式，不足21 位时应左对齐，右补0x00。
                对于MO 消息，DestTermID 格式为“118＋SP 服务代码＋其它（可选）”。对于点对点
                短消息，DestTermID 格式为“区号+号码（区号前添零）” ，不足21 位时应左对齐，右补
                0x00。
     * @param msgContent 短消息内容
     *                短消息长度应小于或等于140。
                      当IsReport＝1 时，MsgContent 中内容为状态报告，其格式遵循7.2.87 节描述。
     * @param reserve 
     * @throws IllegalArgumentException
     */
    public SMGPSubmitMessage(int msgType, int needReport, int priority, String serviceId,
                             String feeType, String feeCode, String fixedFee, int msgFormat,
                             Date validTime, Date atTime, String srcTermId, String chargeTermId,
                             String destTermId[], String msgContent, String reserve)
                                                                                    throws IllegalArgumentException {
        if (msgType < 0 || msgType > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":MsgType ").append(SMGPConstant.INT_SCOPE_ERROR))));
        }
        if (needReport < 0 || needReport > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":NeedReport ").append(SMGPConstant.INT_SCOPE_ERROR))));
        }
        if (priority < 0 || priority > 9) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(":")
                .append(SMGPConstant.PRIORITY_ERROR))));
        }
        if (serviceId == null) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":ServiceID ").append(SMGPConstant.STRING_NULL))));
        }
        if (serviceId.length() > 10) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))))
                .append(":ServiceID ").append(SMGPConstant.STRING_LENGTH_GREAT).append("10"))));
        }
        if (feeType == null) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":FeeType ").append(SMGPConstant.STRING_NULL))));
        }
        if (feeType.length() > 2) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))))
                .append(":FeeType ").append(SMGPConstant.STRING_LENGTH_GREAT).append("2"))));
        }
        if (feeCode == null) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":FeeCode ").append(SMGPConstant.STRING_NULL))));
        }
        if (feeCode.length() > 6) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))))
                .append(":FeeCode ").append(SMGPConstant.STRING_LENGTH_GREAT).append("6"))));
        }
        if (fixedFee == null) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":FixedFee ").append(SMGPConstant.STRING_NULL))));
        }
        if (fixedFee.length() > 6) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))))
                .append(":FixedFee ").append(SMGPConstant.STRING_LENGTH_GREAT).append("6"))));
        }
        if (msgFormat < 0 || msgFormat > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":MsgFormat ").append(SMGPConstant.INT_SCOPE_ERROR))));
        }
        if (srcTermId == null) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":SrcTermID ").append(SMGPConstant.STRING_NULL))));
        }
        //        if (srcTermId.length() > 21) {
        //            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
        //                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))))
        //                .append(":SrcTermID ").append(SMGPConstant.STRING_LENGTH_GREAT).append("21"))));
        //        }
        if (chargeTermId == null) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":ChargeTermID ").append(SMGPConstant.STRING_NULL))));
        }
        if (chargeTermId.length() > 21) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))))
                .append(":ChargeTermID ").append(SMGPConstant.STRING_LENGTH_GREAT).append("21"))));
        }
        if (destTermId == null) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":DestTermID ").append(SMGPConstant.STRING_NULL))));
        }
        if (destTermId.length > 100) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(":")
                .append(SMGPConstant.DESTTERMID_ERROR))));
        }
        for (int i = 0; i < destTermId.length; i++) {
            if (destTermId[i].length() > 21) {
                throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                    String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))))
                    .append(":one DestTermID ").append(SMGPConstant.STRING_LENGTH_GREAT)
                    .append("21"))));
            }
        }

        if (msgContent == null) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":MsgContent ").append(SMGPConstant.STRING_NULL))));
        }
        if (msgContent.length() > 252) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))))
                .append(":MsgContent ").append(SMGPConstant.STRING_LENGTH_GREAT).append("252"))));
        }
        if (reserve != null && reserve.length() > 8) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))))
                .append(":reserve ").append(SMGPConstant.STRING_LENGTH_GREAT).append("8"))));
        }
        int len = 126 + 21 * destTermId.length + msgContent.length();
        super.buf = new byte[len];
        TypeConvert.int2byte(len, super.buf, 0);
        TypeConvert.int2byte(2, super.buf, 4);
        super.buf[12] = (byte) msgType;
        super.buf[13] = (byte) needReport;
        super.buf[14] = (byte) priority;
        System.arraycopy(serviceId.getBytes(), 0, super.buf, 15, serviceId.length());
        System.arraycopy(feeType.getBytes(), 0, super.buf, 25, feeType.length());
        System.arraycopy(feeCode.getBytes(), 0, super.buf, 27, feeCode.length());
        System.arraycopy(fixedFee.getBytes(), 0, super.buf, 33, fixedFee.length());
        super.buf[39] = (byte) msgFormat;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        if (validTime != null) {
            String tmpTime = String.valueOf(String.valueOf(dateFormat.format(validTime))).concat(
                "032+");
            System.arraycopy(tmpTime.getBytes(), 0, super.buf, 40, 16);
        }
        if (atTime != null) {
            String tmpTime = String.valueOf(String.valueOf(dateFormat.format(atTime))).concat(
                "032+");
            System.arraycopy(tmpTime.getBytes(), 0, super.buf, 57, 16);
        }
        System.arraycopy(srcTermId.getBytes(), 0, super.buf, 74, srcTermId.length());
        System.arraycopy(chargeTermId.getBytes(), 0, super.buf, 95, chargeTermId.length());
        super.buf[116] = (byte) destTermId.length;
        int i = 0;
        for (i = 0; i < destTermId.length; i++) {
            System.arraycopy(destTermId[i].getBytes(), 0, super.buf, 117 + i * 21,
                destTermId[i].length());
        }

        int loc = 117 + i * 21;
        super.buf[loc] = (byte) msgContent.length();
        System.arraycopy(msgContent.getBytes(), 0, super.buf, loc + 1, msgContent.length());
        loc = loc + 1 + msgContent.length();
        if (reserve != null) {
            System.arraycopy(reserve.getBytes(), 0, super.buf, loc, reserve.length());
        }
        strBuf = new StringBuffer(600);
        strBuf.append(",MsgType=".concat(String.valueOf(msgType)));
        strBuf.append(",NeedReport=".concat(String.valueOf(needReport)));
        strBuf.append(",Priority=".concat(String.valueOf(priority)));
        strBuf.append(",ServiceID=".concat(String.valueOf(serviceId)));
        strBuf.append(",FeeType=".concat(String.valueOf(feeType)));
        strBuf.append(",FeeCode=".concat(String.valueOf(feeCode)));
        strBuf.append(",FixedFee=".concat(String.valueOf(fixedFee)));
        strBuf.append(",MsgFormat=".concat(String.valueOf(msgFormat)));
        if (validTime != null) {
            strBuf.append(",ValidTime=".concat(String.valueOf(String.valueOf(dateFormat
                .format(validTime)))));
        } else {
            strBuf.append(",ValidTime=null");
        }
        if (atTime != null) {
            strBuf
                .append(",AtTime=".concat(String.valueOf(String.valueOf(dateFormat.format(atTime)))));
        } else {
            strBuf.append(",at_Time=null");
        }
        strBuf.append(",SrcTermID=".concat(String.valueOf(srcTermId)));
        strBuf.append(",ChargeTermID=".concat(String.valueOf(chargeTermId)));
        strBuf.append(",DestTermIDCount=".concat(String.valueOf(destTermId.length)));
        for (int t = 0; t < destTermId.length; t++) {
            strBuf.append(String.valueOf((new StringBuffer(",DestTermID[")).append(t).append("]=")
                .append(destTermId[t])));
        }

        strBuf.append(",MsgLength=".concat(String.valueOf(msgContent.length())));
        strBuf.append(",MsgContent=".concat(String.valueOf(msgContent)));
        strBuf.append(",Reserve=".concat(String.valueOf(reserve)));
    }

    /**
     * @param msgType 短消息类型
     *        对于回执消息该字段无效；对于文本短消息，该字段表示短消息的消息流向：
                0＝MO 消息（终端发给SP）；
                6＝MT 消息（SP 发给终端，包括WEB 上发送的点对点短消息）；
                7＝点对点短消息；其它保留。
     * @param needReport 是否要求返回状态报告
     *          是否要求返回状态报告。
                0＝不要求返回状态报告；
                1＝要求返回状态报告；
                其它保留。
     * @param priority 短消息发送优先级
     *                短消息发送优先级。
                        0＝低优先级；
                        1＝普通优先级；
                        2＝较高优先级；
                        3＝高优先级；
                        其它保留。
     * @param serviceId 业务代码
     *          业务代码，用于固定网业务。
                对于MO 消息或点对点短消息，该字段无效；
                对于MT 消息，该字段表示业务代码，是该条短消息所属的业务类别，由数字、字母
                和符号组合而成。对于从WEB 上发送的点对点短消息，要求业务代码为 “PC2P”，其它业
                务代码由SP 自定义。
     * @param feeType 收费类型
     *          对计费用户采取的收费类型。
                对于MO 消息或点对点短消息，该字段无效。对于MT 消息，该字段用法如下：
                00＝免费，此时FixedFee 和FeeCode 无效；
                01＝按条计信息费，此时FeeCode 表示每条费用，FixedFee 无效；
                02＝按包月收取信息费，此时FeeCode 无效，FixedFee 表示包月费用；
                03＝按封顶收取信息费，若按条收费的费用总和达到或超过封顶费后，则按照封顶费
                用收取信息费；若按条收费的费用总和没有达到封顶费用，则按照每条费用总和收取信息费。
     * @param feeCode 资费代码
     *         每条短消息费率，单位为“分”。
                对于MO 消息或点对点短消息，该字段无效；对于MT 消息，该字段具体使用方法参
                见7.2.13 节。
     * @param fixedFee 包月费/封顶费
     *           短消息的包月费/封顶费，单位为“分”。
                    对于MO 消息或点对点短消息，该字段无效；对于MT 消息，该字段具体使用方法参
                    见7.2.13 节。
     * @param msgFormat 短消息格式
     *          短消息内容体的编码格式。
                0＝ASCII 编码；
                3＝短消息写卡操作；
                4＝二进制短消息；
                8＝UCS2 编码；
                15＝GB18030 编码；
                246（F6）＝UIM 相关消息UIM 相关消息，用于与UIM 卡相关的OTA 等业务，终端
                收到该类型消息直接转发给UIM 卡，由UIM 卡来处理该类型消息；
                其它保留。
                对于文字短消息，要求MsgFormat＝15。对于回执消息，要求MsgFormat＝0。
     * @param validTime 短消息有效时间
     *         短消息有效时间，格式遵循SMPP3.3 以上版本协议。
               短消息有效时间在转发过程中保持不变。
     * @param atTime 短消息定时发送时间
     *         短消息定时发送时间，格式遵循SMPP3.3 以上版本协议。
               短消息定时发送时间在转发过程中保持不变。
     * @param srcTermId 短信息发送方号码
     *         短消息发送方号码。
                对于MT 消息，SrcTermID 格式为“118＋SP 服务代码＋其它（可选）”，例如SP 服务
                代码为1234 时，SrcTermID 可以为1181234 或118123456 等。
                对于MO 消息，固定网中SrcTermID 格式为“区号+号码（区号前添零）”，例如
                02087310323，07558780808，移动网中SrcTermID 格式为MSISDN 号码格式。
                对于固定网点对点消息，主叫号码为普通终端时，SrcTermID 格式为“区号+号码（区
                号前添零）”；主叫号码为爱因平台时，SrcTermID 格式为“10631＋区号+号码（区号前添零）”。
     * @param chargeTermId 计费用户号码
     *         计费用户号码。
                ChargeTermID 为空时，如果是MT 消息，则表示对被叫用户号码计费；如果是MO 或
                点对点消息，则表示对主叫用户号码计费。
                ChargeTermID 为非空时，表示对计费用户号码计费。
     * @param destTermId 短消息接收号码 短消息接收号码总数（≤100），用于SP 实现群发短消息。
     *         对于MT 消息，DestTermID 连续存储DestTermIDCount 个号码，每一个接收方号码为
                21 位，固定网中DestTermID 格式为“区号+号码（区号前添零）”，移动网中DestTermID 格
                式为MSISDN 号码格式，不足21 位时应左对齐，右补0x00。
                对于MO 消息，DestTermID 格式为“118＋SP 服务代码＋其它（可选）”。对于点对点
                短消息，DestTermID 格式为“区号+号码（区号前添零）” ，不足21 位时应左对齐，右补
                0x00。
     * @param msgContent 短消息内容
     *                短消息长度应小于或等于140。
                      当IsReport＝1 时，MsgContent 中内容为状态报告，其格式遵循7.2.87 节描述。
     * @param reserve 
     * @throws IllegalArgumentException
     * @throws UnsupportedEncodingException 
     */
    public SMGPSubmitMessage(int msgType, int needReport, int priority, String serviceId,
                             String feeType, String feeCode, String fixedFee, int msgFormat,
                             Date validTime, Date atTime, String srcTermId, String chargeTermId,
                             String destTermId[], byte[] msgContent, String reserve)
                                                                                    throws IllegalArgumentException,
                                                                                    UnsupportedEncodingException {
        if (msgType < 0 || msgType > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":MsgType ").append(SMGPConstant.INT_SCOPE_ERROR))));
        }
        if (needReport < 0 || needReport > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":NeedReport ").append(SMGPConstant.INT_SCOPE_ERROR))));
        }
        if (priority < 0 || priority > 9) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(":")
                .append(SMGPConstant.PRIORITY_ERROR))));
        }
        if (serviceId == null) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":ServiceID ").append(SMGPConstant.STRING_NULL))));
        }
        if (serviceId.length() > 10) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))))
                .append(":ServiceID ").append(SMGPConstant.STRING_LENGTH_GREAT).append("10"))));
        }
        if (feeType == null) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":FeeType ").append(SMGPConstant.STRING_NULL))));
        }
        if (feeType.length() > 2) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))))
                .append(":FeeType ").append(SMGPConstant.STRING_LENGTH_GREAT).append("2"))));
        }
        if (feeCode == null) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":FeeCode ").append(SMGPConstant.STRING_NULL))));
        }
        if (feeCode.length() > 6) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))))
                .append(":FeeCode ").append(SMGPConstant.STRING_LENGTH_GREAT).append("6"))));
        }
        if (fixedFee == null) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":FixedFee ").append(SMGPConstant.STRING_NULL))));
        }
        if (fixedFee.length() > 6) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))))
                .append(":FixedFee ").append(SMGPConstant.STRING_LENGTH_GREAT).append("6"))));
        }
        if (msgFormat < 0 || msgFormat > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":MsgFormat ").append(SMGPConstant.INT_SCOPE_ERROR))));
        }
        if (srcTermId == null) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":SrcTermID ").append(SMGPConstant.STRING_NULL))));
        }
        if (srcTermId.length() > 21) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))))
                .append(":SrcTermID ").append(SMGPConstant.STRING_LENGTH_GREAT).append("21"))));
        }
        if (chargeTermId == null) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":ChargeTermID ").append(SMGPConstant.STRING_NULL))));
        }
        if (chargeTermId.length() > 21) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))))
                .append(":ChargeTermID ").append(SMGPConstant.STRING_LENGTH_GREAT).append("21"))));
        }
        if (destTermId == null) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":DestTermID ").append(SMGPConstant.STRING_NULL))));
        }
        if (destTermId.length > 100) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(":")
                .append(SMGPConstant.DESTTERMID_ERROR))));
        }
        for (int i = 0; i < destTermId.length; i++) {
            if (destTermId[i].length() > 21) {
                throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                    String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))))
                    .append(":one DestTermID ").append(SMGPConstant.STRING_LENGTH_GREAT)
                    .append("21"))));
            }
        }

        if (msgContent == null) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR)))).append(
                ":MsgContent ").append(SMGPConstant.STRING_NULL))));
        }
        if (msgContent.length > 252) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))))
                .append(":MsgContent ").append(SMGPConstant.STRING_LENGTH_GREAT).append("252"))));
        }
        if (reserve != null && reserve.length() > 8) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.SUBMIT_INPUT_ERROR))))
                .append(":reserve ").append(SMGPConstant.STRING_LENGTH_GREAT).append("8"))));
        }
        int len = 126 + 21 * destTermId.length + msgContent.length;
        super.buf = new byte[len];
        TypeConvert.int2byte(len, super.buf, 0);
        TypeConvert.int2byte(2, super.buf, 4);
        super.buf[12] = (byte) msgType;
        super.buf[13] = (byte) needReport;
        super.buf[14] = (byte) priority;
        System.arraycopy(serviceId.getBytes(), 0, super.buf, 15, serviceId.length());
        System.arraycopy(feeType.getBytes(), 0, super.buf, 25, feeType.length());
        System.arraycopy(feeCode.getBytes(), 0, super.buf, 27, feeCode.length());
        System.arraycopy(fixedFee.getBytes(), 0, super.buf, 33, fixedFee.length());
        super.buf[39] = (byte) msgFormat;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        if (validTime != null) {
            String tmpTime = String.valueOf(String.valueOf(dateFormat.format(validTime))).concat(
                "032+");
            System.arraycopy(tmpTime.getBytes(), 0, super.buf, 40, 16);
        }
        if (atTime != null) {
            String tmpTime = String.valueOf(String.valueOf(dateFormat.format(atTime))).concat(
                "032+");
            System.arraycopy(tmpTime.getBytes(), 0, super.buf, 57, 16);
        }
        System.arraycopy(srcTermId.getBytes(), 0, super.buf, 74, srcTermId.length());
        System.arraycopy(chargeTermId.getBytes(), 0, super.buf, 95, chargeTermId.length());
        super.buf[116] = (byte) destTermId.length;
        int i = 0;
        for (i = 0; i < destTermId.length; i++) {
            System.arraycopy(destTermId[i].getBytes(), 0, super.buf, 117 + i * 21,
                destTermId[i].length());
        }

        int loc = 117 + i * 21;
        super.buf[loc] = (byte) msgContent.length;
        System.arraycopy(msgContent, 0, super.buf, loc + 1, msgContent.length);
        loc = loc + 1 + msgContent.length;
        if (reserve != null) {
            System.arraycopy(reserve.getBytes(), 0, super.buf, loc, reserve.length());
        }
        strBuf = new StringBuffer(600);
        strBuf.append(",MsgType=".concat(String.valueOf(String.valueOf(msgType))));
        strBuf.append(",NeedReport=".concat(String.valueOf(String.valueOf(needReport))));
        strBuf.append(",Priority=".concat(String.valueOf(String.valueOf(priority))));
        strBuf.append(",ServiceID=".concat(String.valueOf(String.valueOf(serviceId))));
        strBuf.append(",FeeType=".concat(String.valueOf(String.valueOf(feeType))));
        strBuf.append(",FeeCode=".concat(String.valueOf(String.valueOf(feeCode))));
        strBuf.append(",FixedFee=".concat(String.valueOf(String.valueOf(fixedFee))));
        strBuf.append(",MsgFormat=".concat(String.valueOf(String.valueOf(msgFormat))));
        if (validTime != null) {
            strBuf.append(",ValidTime=".concat(String.valueOf(String.valueOf(dateFormat
                .format(validTime)))));
        } else {
            strBuf.append(",ValidTime=null");
        }
        if (atTime != null) {
            strBuf
                .append(",AtTime=".concat(String.valueOf(String.valueOf(dateFormat.format(atTime)))));
        } else {
            strBuf.append(",at_Time=null");
        }
        strBuf.append(",SrcTermID=".concat(String.valueOf(String.valueOf(srcTermId))));
        strBuf.append(",ChargeTermID=".concat(String.valueOf(String.valueOf(chargeTermId))));
        strBuf
            .append(",DestTermIDCount=".concat(String.valueOf(String.valueOf(destTermId.length))));
        for (int t = 0; t < destTermId.length; t++) {
            strBuf.append(String.valueOf(String.valueOf((new StringBuffer(",DestTermID["))
                .append(t).append("]=").append(destTermId[t]))));
        }

        strBuf.append(",MsgLength=".concat(String.valueOf(msgContent.length)));
        strBuf
            .append(",MsgContent=".concat(String.valueOf(getMsgContentStr(msgContent, msgFormat))));
        strBuf.append(",Reserve=".concat(String.valueOf(reserve)));
    }

    public String toString() {
        StringBuffer outBuf = new StringBuffer(600);
        outBuf.append("SMGPSubmitMessage: ");
        outBuf.append("PacketLength=".concat(String.valueOf(String.valueOf(super.buf.length))));
        outBuf.append(",RequestID=2");
        outBuf.append(",SequenceID=".concat(String.valueOf(String.valueOf(getSequenceId()))));
        if (strBuf != null) {
            outBuf.append(strBuf.toString());
        }
        return outBuf.toString();
    }

    public int getRequestId() {
        return 2;
    }
}
