package com.woniu.yago.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Description: java类作用描述
 * @Author: 路边
 * @time: 2019/4/16 23:59
 */
public class Md5Util {
    /**
     * 方法实现说明 将源字符串使用MD5加密为字节数组
     * @author      lxy
     * @Param:      source
     * @return      
     * @exception   
     * @date        2019/4/17 0:01
     */
    public static byte[] encode2bytes(String source) {
        byte[] result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(source.getBytes("UTF-8"));
            result = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }
    /**
     * 方法实现说明 将源字符串使用MD5加密为32位16进制数
     * @author      lxy
     * @Param:      source
     * @return
     * @exception
     * @date        2019/4/17 0:02
     */
    public static String encode2hex(String source) {
        byte[] data = encode2bytes(source);
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            String hex = Integer.toHexString(0xff & data[i]);

            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }
    /**
     * 方法实现说明 验证字符串是否匹配
     * @author      lxy
     * @Param:      unknown 待验证的字符串
     * @Param:      okHex 使用MD5加密过的16进制字符串
     * @return      匹配返回true，不匹配返回false
     * @exception
     * @date        2019/4/17 0:02
     */
    public static boolean validate(String unknown , String okHex) {
        return okHex.equals(encode2hex(unknown));
    }
}
