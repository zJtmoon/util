package com.creditease.honeybot.utils;

import com.alibaba.fastjson.JSONObject;
import com.creditease.honeybot.entity.GatewayResponse;
import com.creditease.honeybot.entity.GatewayResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.*;

/**
 * date formatter with ThreadLocal
 *
 * @author <a href="mailto:weifeng14@creditease.cn">Feng Wei</a>
 * @version 1.00 2017/4/21
 */


public class DateUtil {
    private static ZoneOffset localZoneOffset = ZoneOffset.of("+8");
    private static SimpleDateFormat SDF_HM = new SimpleDateFormat("HH:mm");


    private static List<String> robotCodeList;

    public void init() {
        robotCodeList = new ArrayList<>();
        robotCodeList.add("yrd_M0_B5_0-001");
        robotCodeList.add("yrd_M0_B5_1-2-001");
        robotCodeList.add("yrd_M0_B12_0-001");
        robotCodeList.add("yrd_M0_B12_1-2-001");
        robotCodeList.add("yrd_M0_B6_0-001");
        robotCodeList.add("yrd_M0_B6_1-2-001");
    }
        //private static List<String> robotCodeList = asList("yrd_M0_B5_0-001", "yrd_M0_B5_1-2-001", "yrd_M0_B12_0-001",
          // "yrd_M0_B12_1-2-001", "yrd_M0_B6_0-001", "yrd_M0_B6_1-2-001", "yrd_M0_B0_1-2-001", "yrd_M0_B0_0-001");


    public static String getHMStr(Date date) {
        return SDF_HM.format(date);
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    public static Date toDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    public static int compareDay(LocalDateTime day1, LocalDateTime day2) {
        int y = day1.getYear() - day2.getYear();
        if (y != 0) {
            return y;
        }
        return day1.getDayOfYear() - day2.getDayOfYear();
    }

    public static String localDate2Str(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(fmt);
    }

    public static String localDateTime2Str(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(fmt);
    }

    public static String localDate2Str() {
        return localDate2Str(LocalDate.now());
    }

    public static String localDateTime2Str() {
        return localDateTime2Str(LocalDateTime.now());
    }

    public static LocalDateTime str2LocalDateTime(String dateTimeStr) {
        if (StringUtils.isEmpty(dateTimeStr)) {
            return null;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTimeStr, df);
    }

    public static Long localDateTime2Milli(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.toInstant(localZoneOffset).toEpochMilli();
    }

    public static Long str2DateTimeMilli(String dateTimeStr) {
        if (StringUtils.isEmpty(dateTimeStr)) {
            return null;
        }
        return localDateTime2Milli(str2LocalDateTime(dateTimeStr));
    }
    private static String parseDate(String strDate){
        String yearStr = null;
        String monthStr = null;
        String dayStr = null;

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(strDate);
            Calendar calendar = Calendar.getInstance();//日历对象
            calendar.setTime(date);//设置当前日期
            yearStr = calendar.get(Calendar.YEAR)+"";//获取年份
            int month = calendar.get(Calendar.MONTH) + 1;//获取月份
            monthStr = month < 10 ? "0" + String.valueOf(month) : String.valueOf(month)+ "";
            int day = calendar.get(Calendar.DATE);//获取日
            dayStr = day < 10 ? "0" + String.valueOf(day) : String.valueOf(day) + "";
        }catch (Exception ex){
            //log.info("发送短信时，处理日期失败");
        }

        return yearStr +"年" + monthStr + "月" + dayStr+ "日";
    }


    public static List<Date> getBetweenDates(String startDate, String endDate) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date begin = df.parse(startDate);
        Date end = df.parse(endDate);
        List<Date> result = new ArrayList<Date>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(begin);
            /* Calendar tempEnd = Calendar.getInstance();
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
            tempEnd.setTime(end);
            while (tempStart.before(tempEnd)) {
                result.add(tempStart.getTime());
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }*/
        while(begin.getTime()<= end.getTime()){
            result.add(tempStart.getTime());
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
            begin = tempStart.getTime();
        }
        return result;
    }


    public static void main(String[] args) {
        /*
        System.out.println(localDate2Str());
        System.out.println(str2LocalDateTime("2019-01-05 18:34:21"));

        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(1544371200000L);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        System.out.println(res);

        long a = str2LocalDateTime("2019-01-11 00:00:00").toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println(a);

        /**lt = str2DateTimeMilli("2019-05-06 00:00:00");
        System.out.println(lt == a);*/


        Map<String, String> callRountMap = new HashMap<>();
        callRountMap.put("callCount", String.valueOf("1"));
        callRountMap.put("callResult", "2");
        callRountMap.put("callStatus", "3");



        Map<String, Map<String, String>> callInfoMap = new HashMap<>();
        callInfoMap.put("firstCallResult",callRountMap);
        callInfoMap.put("secondCallResult", callRountMap);
        callInfoMap.put("thirdCallResult", callRountMap);


        String contentStr = JSONObject.toJSONString(callInfoMap);
        System.out.println(contentStr);
        if(Double.doubleToLongBits(0.1) < Double.doubleToLongBits(0.001)){
            System.out.println("mmmm");
        }


        try{
            GatewayResponse gatewayResponse = new GatewayResponse();
            GatewayUtil.setGatewayResponse(0, "mmmm", gatewayResponse);
            GatewayResult gatewayResult = new GatewayResult();
            gatewayResult.setAmount(10000.09);
            gatewayResponse.setCode(2);
            gatewayResponse.setData(gatewayResult);
            System.out.println(gatewayResponse.toString());
        }catch (Exception ex){
            System.out.println("dfgsfgsdgsdg");

        }


    }
}
