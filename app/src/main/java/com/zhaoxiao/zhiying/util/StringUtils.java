package com.zhaoxiao.zhiying.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.length() <= 0;
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String formatDate2English(Date date) {
        return new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH).format(date);
    }
}
