package com.creditease.honeybot.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by donghuicun on 2018/11/14.
 */
public class DateUtil {

    /**
     * 当前时区的时间（LocalDateTime）
     * @param date
     * @return
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * 当前时区的日期（Date）
     * @param localDateTime
     * @return
     */
    public static Date toDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 获取今天开始时间
     * @return
     */
    public static Date getTodayStart(){
        return getDayStart(Calendar.getInstance());
    }

    /**
     * 获取今天结束时间
     * @return
     */
    public static Date getTodayEnd(){
        return getDayEnd(Calendar.getInstance());
    }

    /**
     * 获取某天开始时间
     * @return
     */
    public static Date getDayStart(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取某天结束时间
     * @return
     */
    public static Date getDayEnd(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 格式化当前时间(String)
     * yyyy-MM-dd
     * @return
     */
    public static String localDate2Str(){
        Date date =new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(date);
    }

    /**
     * 格式化昨天时间(String)
     * yyyy-MM-dd
     * @return
     */
    public static String beforeDate2Str(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1); //昨天
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 判断是否是过去的日期
     * @param  str
     * @return
     */
    public static boolean isPastDate(String str) throws ParseException{
        boolean flag = false;
        Date nowDate = new Date();
        Date pastDate = null;
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //将字符串转为日期格式，如果此处字符串为非合法日期就会抛出异常。
        pastDate = sdf.parse(str);
        //调用Date里面的before方法来做判断
        flag = pastDate.before(nowDate);
        return flag;
    }

    /**
     * 日期向后增加小时
     * @param date
     * @param hour
     * @return
     */
    public static Date addDateHour(Date date, int hour){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);// 24小时制
        date = cal.getTime();
        return date;
    }

    /**
     * 比较两个日期Date2比Date1(年月日)多的天数,(只考虑天数不考虑时间)
     * 例如:2017-01-25 23:59:59与 2017-01-26 00:00:00   返回1
     * 2017-01-25 与 2017-01-25   返回0
     * 2017-01-28 与 2017-01-25   返回-3
     * @param Date1
     * @param Date2
     * @return
     */
    public static int differDayQty(Date Date1, Date Date2){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        calendar.setTime(Date1);
        int day1 = calendar.get(Calendar.DAY_OF_YEAR);
        int year1 = calendar.get(Calendar.YEAR);

        calendar.setTime(Date2);
        int day2 = calendar.get(Calendar.DAY_OF_YEAR);
        int year2 = calendar.get(Calendar.YEAR);

        int sign = 0;
        int days = 0;
        if(year1 != year2){
            sign = year1<year2 ? 1 : -1;
            int minYear = min(year1, year2);
            int maxYear = max(year1, year2);
            for (int i = minYear; i < maxYear; i++) {
                days += (i%4 == 0 && i%100 != 0 || i%400 == 0) ? 366 : 365;
            }
        }
        return day2 - day1 + sign*days;
    }


    /**
     * 获取日期几号 dateStr:2019-06-06    返回6
     * @param dateStr
     * @return
     */
    public static String getDayStr(String dateStr){
        String dayStr = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date date = df.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            dayStr = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return dayStr;
    }
}
