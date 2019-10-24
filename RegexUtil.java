package com.creditease.honeybot.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 */
public class RegexUtil {

	public static boolean isMatch(String str, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 是否匹配日期格式 yyyy-mm-dd
     *
     * @param str
     * @return
     */
    public static boolean isDate1(String str) {
        String regex = "^20\\d{2}-\\d{1,2}-\\d{1,2}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }
	
    /**
     * 	默认匹配第一个值
     *
     * @param str 需要匹配的字符串
     * @param rex 需要匹配的正则表达式
     * @return
     */
    public static String matchRex(String str, String rex) {
        return matchRex(str, rex, 1);
    }

    /**
     * 	根据goup索引位置匹配
     *
     * @param str        需要匹配的字符串
     * @param rex        需要匹配的正则表达式
     * @param groupIndex group的索引值，注：初始值是从1开始
     * @return
     */
    public static String matchRex(String str, String rex, int groupIndex) {
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return m.group(groupIndex);
        }
        return null;
    }

    /**
     * 	匹配多条记录，且每条记录只取一个值
     *
     * @param str 需要匹配的字符串
     * @param rex 需要匹配的正则表达式
     * @return
     */
    public static List<String> matchRex4List(String str, String rex) {
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(str);
        List<String> resultList = new ArrayList<String>();
        while (m.find()) {
            resultList.add(m.group(1));
        }
        if (resultList.size() == 0) {
            return null;
        }
        return resultList;
    }


    /**
     * 	匹配多条记录，且每条记录取多个值
     *
     * @param str   需要匹配的字符串
     * @param rex   需要匹配的正则表达式
     * @param count 每条记录需要取值的个数
     * @return
     */
    public static List<String[]> matchRex4List(String str, String rex, Integer count) {
        if (count < 1) {
            return null;
        }
        Pattern p = Pattern.compile(rex);
        Matcher m = p.matcher(str);
        List<String[]> resultList = new ArrayList<String[]>();
        String[] row;
        while (m.find()) {
            row = new String[count];
            for (int i = 0; i < count; i++) {
                row[i] = m.group(i + 1);
            }
            resultList.add(row);
        }
        if (resultList.size() == 0) {
            return null;
        }
        return resultList;
    }

    
    /**
	 * 	获取匹配
	 *
	 * @param input
	 * @param inputPattern
	 * @return
	 */
	public static Matcher getMatcher(String input, String inputPattern) {
		Matcher matcher = null;
		try {
			Pattern pattern = Pattern.compile(inputPattern, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matcher;
	}

}