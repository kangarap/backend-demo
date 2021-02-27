package com.demo.backend.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {

    public static long getTimeStamp()
    {
        return System.currentTimeMillis()/1000;
    }


    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.parseLong(seconds+"000")));
    }


    public static int date2TimeStamp(String date_str,String format){

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);

            return (int)(sdf.parse(date_str).getTime()/1000);

        } catch (Exception e) {

            e.printStackTrace();
        }
        return 0;
    }


    public static String NowTime() {

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }


    public static long TodayTimeStamp(String action){

        int hour = "end".equals(action) ? 24 : 0;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        return calendar.getTime().getTime()/1000;
    }

    public static boolean isDone(Long beginTime, Long endTime) {
        long nowTime = System.currentTimeMillis() / 1000L;
        if (beginTime != 0L || endTime != 0L && endTime <= nowTime) {
            return beginTime < nowTime && (endTime > nowTime || endTime == 0L);
        } else {
            return true;
        }
    }


    /**
     * 获取当年的开始时间戳
     *
     * @param timeStamp 毫秒级时间戳
     * @param timeZone  如 GMT+8:00
     * @return
     */
    public static Long getYearStartTime(Long timeStamp, String timeZone) {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTimeInMillis(timeStamp);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.DATE, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * 获取当年的最后时间戳
     *
     * @param timeStamp 毫秒级时间戳
     * @param timeZone  如 GMT+8:00
     * @return
     */
    public static Long getYearEndTime(Long timeStamp, String timeZone) {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTimeInMillis(timeStamp);
        int year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTimeInMillis() / 1000;
    }
}
