package com.leyou.common.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**
 * @Description : 对密码使用MD5算法加密，获取salt密码添加盐的工具类
 * @Author santu
 * @Date 2018 - 10 - 21 21:03
 **/
public class CodeUtils {

    private static String[] hex = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};

    /**
     * 将明文密码转成MD5密码
     */
    public static String encodeByMd5(String password,String salt){
        byte[] byteArray = new byte[0];
        try {
            //Java中MessageDigest类封装了MD5和SHA算法，今天我们只要MD5算法
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //调用MD5算法，即返回16个byte类型的值
            byteArray = md5.digest((password + salt).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //注意：MessageDigest只能将String转成byte[]，接下来的事情，由我们程序员来完成
        return byteArrayToHexString(byteArray);
    }
    /**
     * 将byte[]转在16进制字符串
     */
    private static String byteArrayToHexString(byte[] byteArray) {
        StringBuffer sb = new StringBuffer();
        //遍历
        for(byte b : byteArray){//16次
            //取出每一个byte类型，进行转换
            String hex = byteToHexString(b);
            //将转换后的值放入StringBuffer中
            sb.append(hex);
        }
        return sb.toString();
    }
    /**
     * 将byte转在16进制字符串
     */
    private static String byteToHexString(byte b) {//-31转成e1，10转成0a，。。。
        //将byte类型赋给int类型
        int n = b;
        //如果n是负数
        if(n < 0){
            //转正数
            //-31的16进制数，等价于求225的16进制数
            n = 256 + n;
        }
        //商(14)，数组的下标
        int d1 = n / 16;
        //余(1)，数组的下标
        int d2 = n % 16;
        //通过下标取值
        return hex[d1] + hex[d2];
    }

    public static String generatorSalt(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return Base64.encodeBase64String(bytes);
    }


    public static void main(String[] args) throws Exception {
        /*String salt = CodeUtils.generatorSalt();
        System.out.println(salt);
        String password = "123456";
        String str = CodeUtils.encodeByMd5(password, salt);
        System.out.println(str);*/

        String str2 = "947ee36204fdeecebf61579decb2ffe4";
        String salt2 = "DRJONmOj285ZCNXyYI9gXQ==";
        System.out.println(str2.equals(CodeUtils.encodeByMd5("123456",salt2)));
    }

}
