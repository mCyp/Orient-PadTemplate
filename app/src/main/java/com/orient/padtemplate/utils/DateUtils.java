package com.orient.padtemplate.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间的辅助工具类
 * <p>
 * Author WangJie
 * Created on 2018/9/12.
 */
public class DateUtils {
    private static SimpleDateFormat normalFormat = new SimpleDateFormat("MM-dd HH:mm");
    private static SimpleDateFormat simpleDayFormat = new SimpleDateFormat("MM-dd");
    private static SimpleDateFormat detailFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat dateFormat_ch = new SimpleDateFormat("yyyy年MM月dd日");
    private static SimpleDateFormat detailFormat_ch = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");

    /*
        通常形式的时间 MM-dd HH:mm
     */
    public static String date2NormalStr(Date date) {
        return normalFormat.format(date);
    }

    /*
        通常形式的时间 MM_dd
     */
    public static String date2MM_dd(Date date) {
        return simpleDayFormat.format(date);
    }

    /*
        具体的日期 yyyy-MM-dd HH:mm:ss
     */
    public static String date2DetailStr(Date date) {
        return detailFormat.format(date);
    }

    /*
        日期格式 yyyy年MM月dd日HH时mm分ss秒
     */
    public static String date2CHDateStr(Date date) {
        return dateFormat_ch.format(date);
    }

    /*
        日期格式 yyyy-yyyy年MM月dd日-dd
     */
    public static String date2CHDetailStr(Date date) {
        return dateFormat_ch.format(date);
    }

    /*
        将yyyy-MM-dd的格式日期装换成Date
     */
    public static Date str2DayDate(String str) {
        try {
            return dayFormat.parse(str);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /*
        日期格式 yyyy-MM-dd
     */
    public static String date2dayStr(Date date) {
        return dayFormat.format(date);
    }

    /**
     * 数字转星期
     */
    public static String num2dayWeek(int num) {
        switch (num) {
            case 0:
                return "周一";
            case 1:
                return "周二";
            case 2:
                return "周三";
            case 3:
                return "周四";
            case 4:
                return "周五";
            case 5:
                return "周六";
            default:
                return "周一";
        }
    }


}
