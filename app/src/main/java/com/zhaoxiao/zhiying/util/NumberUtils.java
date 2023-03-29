package com.zhaoxiao.zhiying.util;

import java.math.BigDecimal;

public class NumberUtils {
    public static final int ZERO = 0;
    public static final int TEN_THOUSAND = 10000;
    public static final int HUNDRED_MILLION = 100000000;

    public static String intChange2Str(int number) {
        String str = "";
        if (number <= ZERO) {
            str = "0";
        } else if (number < TEN_THOUSAND) {
            str = number + "";
        } else if (number > TEN_THOUSAND && number < HUNDRED_MILLION) {
            double num = (double) number / TEN_THOUSAND;//1.将数字转换成以万为单位的数字

            BigDecimal b = new BigDecimal(num);
            double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//2.转换后的数字四舍五入保留小数点后一位;
            str = f1 + "万";
        } else {
            double num = (double) number / HUNDRED_MILLION;//1.将数字转换成以亿为单位的数字

            BigDecimal b = new BigDecimal(num);
            double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//2.转换后的数字四舍五入保留小数点后一位;
            str = f1 + "亿";
        }
        return str;
    }
}