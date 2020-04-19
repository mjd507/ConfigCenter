package com.configcenter.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mjd on 2020-01-13 15:16
 */
public class Md5Utils {
    public static void main(String[] args) {
        MD5_16("abc");
    }

    public static String MD5_16(String sourceStr) {

        return digist(sourceStr).substring(8, 24);
    }

    public static String digist(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (byte value : b) {
                i = value;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }
}
