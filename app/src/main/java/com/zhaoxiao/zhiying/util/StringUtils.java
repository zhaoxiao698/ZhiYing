package com.zhaoxiao.zhiying.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.length() <= 0;
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String formatDateTime(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(date);
    }

    public static String formatDate2English(Date date) {
        return new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH).format(date);
    }

    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0|5|6|7|9])|(15[0-3])|(15[5-9])|(16[6|7])|(17[2|3|5|6|7|8])|(18[0-9])|(19[1|8|9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static String randomCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    public static String randomCode1() {
        long codeL = System.nanoTime();
        String codeStr = Long.toString(codeL);
        return codeStr.substring(codeStr.length() - 6);
    }

    public static String foldString(String str, int num) {
        if (str.length()>num){
            return str.substring(0,num)+"...";
        }
        return str;
    }

    public static String hidePhone(String phoneNumber) {
//        String phoneNumber = "15567893456";
        String resultPhone1= phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        String resultPhone= phoneNumber.replaceAll("(\\d{3})\\d{6}(\\d{2})","$1******$2");
//        System.out.println("隐藏后的手机号：" + resultPhone);
        return resultPhone;
    }
}
