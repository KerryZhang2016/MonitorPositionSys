package com.bupt.model.utils;

import java.util.Calendar;

/**
 * Created by Kerry on 15/11/13.
 *
 * 时间管理工具类
 */
public class TimeUtil {

    /**
     * 获取今日日期
     *
     * return yyyy-mm-dd
     * */
    public static String getToday(){
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH)+1;
        int day = ca.get(Calendar.DATE);

        StringBuilder date = new StringBuilder();
        date.append(year);
        date.append("-");
        if(month < 10){
            date.append("0").append(month);
        }else{
            date.append(month);
        }
        date.append("-");
        if(day < 10){
            date.append("0").append(day);
        }else{
            date.append(day);
        }
        return date.toString();
    }

    /**
     * 获取当前24小时制的时间
     * @return HH:MM
     * */
    public static String getSysTime(){
        String result;

        Calendar ca = Calendar.getInstance();

        int minute = ca.get(Calendar.MINUTE);		// 分
        int hour = ca.get(Calendar.HOUR_OF_DAY);	// 小时

        if(hour < 10){
            result = "0"+hour;
        }else{
            result = "" +hour;
        }

        result = result+":";

        if(minute < 10){
            result = result + "0"+minute;
        }else{
            result = result +minute;
        }
        return result;
    }
}
