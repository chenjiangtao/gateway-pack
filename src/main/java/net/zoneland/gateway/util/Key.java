package net.zoneland.gateway.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Key {

    public static byte[] GenerateAuthenticatorClient(String sourceAddr, String key,
                                                     String timestamp, int length) {
        int sharekeylen = 0;
        if (key != null) {
            sharekeylen = sourceAddr.length() + key.length() + 10 + length;
        } else {
            sharekeylen = sourceAddr.length() + 10 + length;
        }
        byte tmpbuf[] = new byte[sharekeylen];
        int tmploc = 0;
        System.arraycopy(sourceAddr.getBytes(), 0, tmpbuf, 0, sourceAddr.length());
        tmploc = sourceAddr.length() + length;
        if (key != null) {
            System.arraycopy(key.getBytes(), 0, tmpbuf, tmploc, key.length());
            tmploc += key.length();
        }

        System.arraycopy((FormatTimeStamp(timestamp)).getBytes(), 0, tmpbuf, tmploc, 10);
        return MD5(tmpbuf);
    }

    private static String FormatTimeStamp(String timestampstr) {
        String tmpstr = timestampstr;
        if (timestampstr.length() < 10) {
            for (int i = 0; i < 10 - timestampstr.length(); i++) {
                tmpstr = "0" + tmpstr;
            }
        }
        return tmpstr;
    }

    public static byte[] GenerateAuthenticatorServer(int status, byte[] authenticatorClient,
                                                     String sharekey)
                                                                     throws NoSuchAlgorithmException {
        byte[] keybyte = sharekey.getBytes();
        byte[] buf = new byte[authenticatorClient.length + 4 + keybyte.length];
        TypeConvert.int2byte(status, buf, 0);
        System.arraycopy(authenticatorClient, 0, buf, 4, authenticatorClient.length);
        System.arraycopy(keybyte, 0, buf, 4 + authenticatorClient.length, keybyte.length);
        return MD5(buf);
    }

    public static boolean checkAuth(byte[] authenticatorclient, String account, String password,
                                    String timestamp, int length) {

        String clientstr = Hex.rhex(authenticatorclient);
        String serverstr = Hex.rhex(GenerateAuthenticatorClient(account, password, timestamp,
            length));
        if (clientstr.equals(serverstr)) {
            return true;
        } else {
            return false;
        }

    }

    private static byte[] MD5(byte[] sourecebuf) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No MD5 AlgorithmException");
        }
        md5.reset();
        md5.update(sourecebuf);
        return md5.digest();

    }

}
