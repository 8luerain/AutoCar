package com.example.bluerain.carok.utils;

import java.util.Date;

/**
 * Created by bluerain on 17-2-9.
 */

public class CommonUtil {

    public static char getLastCarNum(String s) {
        char c = s.charAt(s.length() - 1);
        if (c >= 'A' && c <= 'Z') c = '0'; //字母尾号按照0计算
        return c;
    }

    public static int isXianXing(String carNum, String limitNum) {
        Date date = new Date(System.currentTimeMillis());
        if (null == limitNum || limitNum.split("\\d").length == 0) return -1;
        if (limitNum.contains(getLastCarNum(carNum) + "") &&
                (date.getHours() >= 7 && date.getHours() < 20) ) return 1;
        return 0;
    }

    public static boolean isSetAlarm() {


        return false;
    }
}
