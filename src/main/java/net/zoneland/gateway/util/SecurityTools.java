package net.zoneland.gateway.util;

import java.io.UnsupportedEncodingException;
import java.security.DigestException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

public class SecurityTools
{

    private static final byte salt[] = "webplat".getBytes();

    public SecurityTools()
    {
    }

    public static String digest(String str)
    {
        try
        {
            MessageDigest md5 = MessageDigest.getInstance("SHA");
            md5.update(salt);
            String s = Base64.encode(md5.digest(str.getBytes()));
            return s;
        }
        catch(NoSuchAlgorithmException ex)
        {
            throw new UnsupportedOperationException(ex.toString());
        }
    }

    public static void md5(byte data[], int offset, int length, byte digest[], int dOffset)
    {
        try
        {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(data, offset, length);
            md5.digest(digest, dOffset, 16);
        }
        catch(NoSuchAlgorithmException ex)
        {
            ex.printStackTrace();
        }
        catch(DigestException ex)
        {
            ex.printStackTrace();
        }
    }

    public static byte[] md5(byte data[], int offset, int length)
    {
        try
        {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(data, offset, length);
            byte abyte0[] = md5.digest();
            return abyte0;
        }
        catch(NoSuchAlgorithmException ex)
        {
            ex.printStackTrace();
        }
        byte abyte1[] = null;
        return abyte1;
    }

    public static byte[] encrypt(byte key[], byte src[])
    {
        try
        {
            byte abyte0[] = getCipher(key, 1).doFinal(src);
            return abyte0;
        }
        catch(BadPaddingException ex)
        {
            throw new UnsupportedOperationException(ex.toString());
        }
        catch(IllegalBlockSizeException ex)
        {
            throw new UnsupportedOperationException(ex.toString());
        }
    }

    public static String encrypt(String key, String src)
    {
        try
        {
            String s = Base64.encode(getCipher(key.getBytes("UTF8"), 1).doFinal(src.getBytes("UTF8")));
            return s;
        }
        catch(UnsupportedEncodingException ex)
        {
            throw new UnsupportedOperationException(ex.toString());
        }
        catch(BadPaddingException ex)
        {
            throw new UnsupportedOperationException(ex.toString());
        }
        catch(IllegalBlockSizeException ex)
        {
            throw new UnsupportedOperationException(ex.toString());
        }
    }

    public static byte[] decrypt(byte key[], byte src[])
    {
        try
        {
            byte abyte0[] = getCipher(key, 2).doFinal(src);
            return abyte0;
        }
        catch(IllegalBlockSizeException ex)
        {
            throw new UnsupportedOperationException(ex.toString());
        }
        catch(BadPaddingException ex)
        {
            throw new UnsupportedOperationException(ex.toString());
        }
    }

    public static String decrypt(String key, String src)
    {
        try
        {
            String s = new String(getCipher(key.getBytes("UTF8"), 2).doFinal(Base64.decode(src)), "UTF8");
            return s;
        }
        catch(UnsupportedEncodingException ex)
        {
            throw new UnsupportedOperationException(ex.toString());
        }
        catch(BadPaddingException ex)
        {
            throw new UnsupportedOperationException(ex.toString());
        }
        catch(IllegalBlockSizeException ex)
        {
            throw new UnsupportedOperationException(ex.toString());
        }
    }

    public static Cipher getCipher(byte key[], int mode)
    {
        try
        {
            if(key.length < 8)
            {
                byte oldkey[] = key;
                key = new byte[8];
                System.arraycopy(oldkey, 0, key, 0, oldkey.length);
            }
            SecretKeyFactory keyFactory;
            java.security.spec.KeySpec keySpec;
            Cipher c;
            if(key.length >= 24)
            {
                keyFactory = SecretKeyFactory.getInstance("DESede");
                keySpec = new DESedeKeySpec(key);
                c = Cipher.getInstance("DESede");
            } else
            {
                keyFactory = SecretKeyFactory.getInstance("DES");
                keySpec = new DESKeySpec(key);
                c = Cipher.getInstance("DES");
            }
            SecretKey k = keyFactory.generateSecret(keySpec);
            c.init(mode, k);
            Cipher cipher = c;
            return cipher;
        }
        catch(NoSuchAlgorithmException ex)
        {
            throw new UnsupportedOperationException(ex.toString());
        }
        catch(InvalidKeyException ex)
        {
            throw new UnsupportedOperationException(ex.toString());
        }
        catch(NoSuchPaddingException ex)
        {
            throw new UnsupportedOperationException(ex.toString());
        }
        catch(InvalidKeySpecException ex)
        {
            throw new UnsupportedOperationException(ex.toString());
        }
    }

    public static void main(String args[])
    {
        Debug.dump(digest("hello world"));
        for(int i = 0; i < 1000; i++)
        {
            decrypt("key", encrypt("key", "hello world"));
        }

        Debug.dump(digest("hello world"));
    }

}
