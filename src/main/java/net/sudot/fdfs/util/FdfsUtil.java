package net.sudot.fdfs.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by chulung on 2016/10/28.
 */
public class FdfsUtil {

    /**
     * get token for file URL
     * @param remote_filename the filename return by FastDFS server
     * @param ts              unix timestamp, unit: second
     * @param secret_key      the secret key
     * @return token string
     */
    public static String getToken(String remote_filename, int ts, String secret_key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bsFilename = remote_filename.getBytes();
        byte[] bsKey = secret_key.getBytes();
        byte[] bsTimestamp = (new Integer(ts)).toString().getBytes();
        byte[] buff = new byte[bsFilename.length + bsKey.length + bsTimestamp.length];
        System.arraycopy(bsFilename, 0, buff, 0, bsFilename.length);
        System.arraycopy(bsKey, 0, buff, bsFilename.length, bsKey.length);
        System.arraycopy(bsTimestamp, 0, buff, bsFilename.length + bsKey.length, bsTimestamp.length);
        return md5(buff);
    }

    /**
     * md5 function
     * @param source the input buffer
     * @return md5 string
     */
    public static String md5(byte[] source) throws NoSuchAlgorithmException {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        md.update(source);
        byte tmp[] = md.digest();
        char str[] = new char[32];
        int k = 0;
        for (int i = 0; i < 16; i++) {
            str[k++] = hexDigits[tmp[i] >>> 4 & 0xf];
            str[k++] = hexDigits[tmp[i] & 0xf];
        }
        return new String(str);
    }
}
