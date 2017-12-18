package net.zoneland.gateway.comm.smgp.message;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.zoneland.gateway.comm.smgp.SMGPConstant;
import net.zoneland.gateway.util.Key;
import net.zoneland.gateway.util.TypeConvert;

public class SMGPLoginMessage extends SMGPMessage {

    private StringBuffer strBuf;

    public SMGPLoginMessage(String sourceAddr, String shared_Secret, int loginMode, Date timestamp,
                            int version) throws IllegalArgumentException {
        if (sourceAddr == null) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.CONNECT_INPUT_ERROR)))).append(
                ":sourceAddr ").append(SMGPConstant.STRING_NULL))));
        }
        if (sourceAddr.length() > 8) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.CONNECT_INPUT_ERROR))))
                .append(":sourceAddr ").append(SMGPConstant.STRING_LENGTH_GREAT).append("8"))));
        }
        if (loginMode < 0 || loginMode > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.CONNECT_INPUT_ERROR)))).append(
                ":loginMode ").append(SMGPConstant.INT_SCOPE_ERROR))));
        }
        if (version < 0 || version > 255) {
            throw new IllegalArgumentException(String.valueOf(String.valueOf((new StringBuffer(
                String.valueOf(String.valueOf(SMGPConstant.CONNECT_INPUT_ERROR)))).append(
                ":version ").append(SMGPConstant.INT_SCOPE_ERROR))));
        }
        int len = 42;
        super.buf = new byte[len];
        TypeConvert.int2byte(len, super.buf, 0);
        TypeConvert.int2byte(1, super.buf, 4);
        System.arraycopy(sourceAddr.getBytes(), 0, super.buf, 12, sourceAddr.length());
        //        if (shared_Secret != null) {
        //            len = sourceAddr.length() + 17 + shared_Secret.length();
        //        } else {
        //            len = sourceAddr.length() + 17;
        //        }
        //        byte tmpbuf[] = new byte[len];
        //        int tmploc = 0;
        //        System.arraycopy(sourceAddr.getBytes(), 0, tmpbuf, 0, sourceAddr.length());
        //        tmploc = sourceAddr.length() + 7;
        //        if (shared_Secret != null) {
        //            System.arraycopy(shared_Secret.getBytes(), 0, tmpbuf, tmploc, shared_Secret.length());
        //            tmploc += shared_Secret.length();
        //        }
        //        String tmptime = GetTimeString();
        //        System.arraycopy(tmptime.getBytes(), 0, tmpbuf, tmploc, 10);
        //        SecurityTools.md5(tmpbuf, 0, len, super.buf, 20);
        String tmptime = GetTimeString();
        byte[] auth = Key.GenerateAuthenticatorClient(sourceAddr, shared_Secret, tmptime, 7);
        System.arraycopy(auth, 0, super.buf, 20, auth.length); // sharekey

        super.buf[36] = (byte) loginMode;
        TypeConvert.int2byte(Integer.parseInt(tmptime), super.buf, 37);
        super.buf[41] = (byte) version;

        strBuf = new StringBuffer(300);
        strBuf.append(",sourceAddr=".concat(String.valueOf(String.valueOf(sourceAddr))));
        strBuf.append(",shared_Secret=".concat(String.valueOf(String.valueOf(shared_Secret))));
        strBuf.append(",loginMode=".concat(String.valueOf(String.valueOf(loginMode))));
        strBuf.append(",version=".concat(String.valueOf(String.valueOf(version))));
    }

    public static String GetTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
        return sdf.format(new Date());
    }

    public String toString() {
        StringBuffer outStr = new StringBuffer(300);
        outStr.append("SMGPLoginMessage:");
        outStr.append("PacketLength=".concat(String.valueOf(String.valueOf(super.buf.length))));
        outStr.append(",RequestID=1");
        outStr.append(",Sequence_Id=".concat(String.valueOf(String.valueOf(getSequenceId()))));
        if (strBuf != null) {
            outStr.append(strBuf.toString());
        }
        return outStr.toString();
    }

    public int getRequestId() {
        return 1;
    }

    public static void main(String[] args) {
        byte x = 0x30;
        System.out.println((byte) 1);
    }
}
