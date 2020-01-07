
import com.alibaba.fastjson.JSONObject;
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

public class DateUtil {
    private static ZoneOffset localZoneOffset = ZoneOffset.of("+8");
    private static SimpleDateFormat SDF_HM = new SimpleDateFormat("HH:mm");


    private static List<String> robotCodeList;

    public void init() {
    
    }
        

    public static String getHMStr(Date date) {
        return SDF_HM.format(date);
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        
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
日期失败");
        }

        return yearStr +"年" + monthStr + "月" + dayStr+ "日";
    }


    public static List<Date> getBetweenDates(String startDate, String endDate) throws Exception {
        Si
        return result;
    }


    public static void main(String[] args) {
        /*
      
    
}
