package com.example.bluerain.carok.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by bluerain on 17-2-8.
 */

public class DateUtils {


    private static final int LIMIT = 4;

    /**
     * 将2013-12-01 转换为long 方便对比
     *
     * @param date
     * @return
     */
    public static long parseDate2Long(String date) {
        try {
            String fomate = "yyyy-MM-dd-HH-mm-ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fomate);
            return simpleDateFormat.parse(date).getTime();
        } catch (Exception e) {
            return -1;
        }
    }

    public static boolean isXianXing() {
        boolean limit = false;
        Date date = new Date(System.currentTimeMillis());
        final Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        int w = ca.get(Calendar.DAY_OF_WEEK) - 1;
        if (w == LIMIT && date.getHours() >= 7 && date.getHours() < 20) limit = true;
        return limit;
    }

    public static boolean isBusyTime() {
        boolean limit = false;
        Date date = new Date(System.currentTimeMillis());
        final Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        int w = ca.get(Calendar.DAY_OF_WEEK) - 1;
        if (w >= 1 && w <= 5) {//周一到周五
            if ((date.getHours() >= 7 && date.getHours() < 9) ||
                    (date.getHours() >= 17 && date.getHours() < 20)) limit = true;
        }

        return limit;
    }

}
