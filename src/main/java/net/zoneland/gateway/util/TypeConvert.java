package net.zoneland.gateway.util;

public class TypeConvert {

    public TypeConvert() {
    }

    public static int byte2int(byte b[], int offset) {
        return b[offset + 3] & 0xff | (b[offset + 2] & 0xff) << 8 | (b[offset + 1] & 0xff) << 16
               | (b[offset] & 0xff) << 24;
    }

    public static int byte2int(byte b[]) {
        return b[3] & 0xff | (b[2] & 0xff) << 8 | (b[1] & 0xff) << 16 | (b[0] & 0xff) << 24;
    }

    public static long byte2long(byte b[]) {
        return (long) b[7] & (long) 255 | ((long) b[6] & (long) 255) << 8
               | ((long) b[5] & (long) 255) << 16 | ((long) b[4] & (long) 255) << 24
               | ((long) b[3] & (long) 255) << 32 | ((long) b[2] & (long) 255) << 40
               | ((long) b[1] & (long) 255) << 48 | (long) b[0] << 56;
    }

    public static long byte2long(byte b[], int offset) {
        return (long) b[offset + 7] & (long) 255 | ((long) b[offset + 6] & (long) 255) << 8
               | ((long) b[offset + 5] & (long) 255) << 16
               | ((long) b[offset + 4] & (long) 255) << 24
               | ((long) b[offset + 3] & (long) 255) << 32
               | ((long) b[offset + 2] & (long) 255) << 40
               | ((long) b[offset + 1] & (long) 255) << 48 | (long) b[offset] << 56;
    }

    public static byte[] int2byte(int n) {
        byte b[] = new byte[4];
        b[0] = (byte) (n >> 24);
        b[1] = (byte) (n >> 16);
        b[2] = (byte) (n >> 8);
        b[3] = (byte) n;
        return b;
    }

    public static void int2byte(int n, byte buf[], int offset) {
        buf[offset] = (byte) (n >> 24);
        buf[offset + 1] = (byte) (n >> 16);
        buf[offset + 2] = (byte) (n >> 8);
        buf[offset + 3] = (byte) n;
    }

    public static byte[] short2byte(int n) {
        byte b[] = new byte[2];
        b[0] = (byte) (n >> 8);
        b[1] = (byte) n;
        return b;
    }

    public static void short2byte(int n, byte buf[], int offset) {
        buf[offset] = (byte) (n >> 8);
        buf[offset + 1] = (byte) n;
    }

    public static byte[] long2byte(long n) {
        byte b[] = new byte[8];
        b[0] = (byte) (int) (n >> 56);
        b[1] = (byte) (int) (n >> 48);
        b[2] = (byte) (int) (n >> 40);
        b[3] = (byte) (int) (n >> 32);
        b[4] = (byte) (int) (n >> 24);
        b[5] = (byte) (int) (n >> 16);
        b[6] = (byte) (int) (n >> 8);
        b[7] = (byte) (int) n;
        return b;
    }

    public static void long2byte(long n, byte buf[], int offset) {
        //        buf[offset] = (byte) (int) (n >> 56);
        //        buf[offset + 1] = (byte) (int) (n >> 48);
        //        buf[offset + 2] = (byte) (int) (n >> 40);
        //        buf[offset + 3] = (byte) (int) (n >> 32);
        //        buf[offset + 4] = (byte) (int) (n >> 24);
        //        buf[offset + 5] = (byte) (int) (n >> 16);
        //        buf[offset + 6] = (byte) (int) (n >> 8);
        //        buf[offset + 7] = (byte) (int) n;
        byte[] b = longToByte(n);
        System.arraycopy(b, 0, buf, offset, 8);
    }

    /**
     * 
     * @param tag
     * @param tlvBuf
     * @param i
     */
    public static void int2byte2(int n, byte buf[], int offset) {
        buf[offset] = (byte) (n >> 8);
        buf[offset + 1] = (byte) n;
    }

    /**
     * 
     * @param parseInt
     * @param tlvBuf
     * @param i
     */
    public static void int2byte3(int n, byte buf[], int offset) {

        buf[offset] = (byte) n;
    }

    /**
     * 
     * @param tlv
     * @param i
     * @return
     */
    public static short byte2short(byte b[], int offset) {
        return (short) (b[offset + 1] & 0xff | (b[offset] & 0xff) << 8);
    }

    /**
     * 
     * @param tlv
     * @param i
     * @param j
     * @param tlv_Length
     * @return
     */
    public static String getString(byte[] src, int srcPos, int destPos, int length) {
        byte[] tmp = new byte[length];
        System.arraycopy(src, srcPos, tmp, destPos, length);
        return (new String(tmp).trim());

    }

    /**
     * 
     * @param tlv
     * @param i
     * @return
     */
    public static short byte2tinyint(byte b[], int offset) {
        return (short) (b[offset] & 0xff);
    }

    public static String getHexString(byte[] src, int srcPos, int destPos, int length) {
        byte[] tmp = new byte[length];
        System.arraycopy(src, srcPos, tmp, destPos, length);
        return (Hex.rhex(tmp));

    }

    public static void main(String args[]) {
        byte[] a = long2byte(101001L);
        System.out.println(byteToLong(a));
    }

    public static long byteToLong(byte[] b) {
        long s = 0;
        long s0 = b[0] & 0xff;// 最低位      

        long s1 = b[1] & 0xff;
        long s2 = b[2] & 0xff;
        long s3 = b[3] & 0xff;
        long s4 = b[4] & 0xff;// 最低位      

        long s5 = b[5] & 0xff;
        long s6 = b[6] & 0xff;
        long s7 = b[7] & 0xff;

        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }

    public static byte[] longToByte(long number) {
        long temp = number;
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Long(temp & 0xff).byteValue();//        
            temp = temp >> 8;// 向右移8位      
        }
        return b;
    }

}
