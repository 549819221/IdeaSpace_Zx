package com.server.ftpsynchostel.util;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PowerUtil {
    public PowerUtil() {
    }

    public static void throwException(String message) throws Exception {
        throw new Exception(message);
    }

    public static Object getObject(int value) {
        return getInt(value);
    }

    public static Object getObject(double value) {
        return getDouble(value);
    }

    public static Object getObject(boolean value) {
        return getBoolean(value);
    }

    public static Object getObject(Object value) {
        return value;
    }

    public static int getIntValue(Object obj) {
        if (obj instanceof String) {
            return getIntValue((String)obj);
        } else if (obj instanceof Integer) {
            return getIntValue((Integer)obj);
        } else if (obj instanceof BigDecimal) {
            return getIntValue((BigDecimal)obj);
        } else if (obj instanceof Double) {
            return getIntValue((Double)obj);
        } else {
            return obj instanceof Long ? getIntValue((Long)obj) : 0;
        }
    }

    public static int getIntValue(String obj) {
        return getInt(obj);
    }

    public static int getIntValue(Integer obj) {
        return obj;
    }

    public static int getIntValue(BigDecimal obj) {
        return obj.intValue();
    }

    public static int getIntValue(Double obj) {
        return obj.intValue();
    }

    public static int getIntValue(Long obj) {
        return obj.intValue();
    }

    public static int getIntValue(int value) {
        return value;
    }

    public static int getIntValue(double value) {
        return (int)value;
    }

    public static boolean getBooleanValue(Object obj) {
        return obj instanceof Boolean ? getBooleanValue((Boolean)obj) : false;
    }

    public static boolean getBooleanValue(Boolean obj) {
        return obj;
    }

    public static Integer getInt(Object obj) {
        if (obj == null) {
            return new Integer(0);
        } else if (obj instanceof String) {
            return getInt((String)obj);
        } else if (obj instanceof Integer) {
            return (Integer)obj;
        } else if (obj instanceof BigDecimal) {
            return getInt((BigDecimal)obj);
        } else if (obj instanceof Double) {
            return getInt((Double)obj);
        } else {
            return obj instanceof Long ? getInt((Long)obj) : new Integer(0);
        }
    }

    public static Integer getInt(String value) {
        return isNull(value) ? new Integer(0) : Integer.valueOf(value);
    }

    public static Integer getInt(Long value) {
        return value == null ? new Integer(0) : new Integer(value.intValue());
    }

    public static Integer getInt(double value) {
        return new Integer((new Double(value)).intValue());
    }

    public static Integer getInt(int value) {
        return new Integer(value);
    }

    public static Integer getInt(BigDecimal value) {
        return value == null ? new Integer(0) : new Integer(value.intValue());
    }

    public static Integer getInt(Double value) {
        return value == null ? new Integer(0) : new Integer(value.intValue());
    }

    public static Boolean getBoolean(Object obj) {
        return obj instanceof Boolean ? (Boolean)obj : null;
    }

    public static Boolean getBoolean(boolean obj) {
        return new Boolean(obj);
    }

    public static int abs(int value) {
        return value >= 0 ? value : -1 * value;
    }

    public static double abs(double value) {
        return value >= 0.0D ? value : -1.0D * value;
    }

    public static int abs(Integer value) {
        if (value == null) {
            return 0;
        } else {
            return value >= 0 ? value : -1 * value;
        }
    }

    public static double abs(Double value) {
        if (value == null) {
            return 0.0D;
        } else {
            return value >= 0.0D ? value : -1.0D * value;
        }
    }

    public static double abs(BigDecimal value) {
        if (value == null) {
            return 0.0D;
        } else {
            return value.doubleValue() >= 0.0D ? value.doubleValue() : -1.0D * value.doubleValue();
        }
    }

    public static long abs(Long value) {
        if (value == null) {
            return 0L;
        } else {
            return value.doubleValue() >= 0.0D ? value : -1L * value;
        }
    }

    public static double abs(Object value) {
        if (value == null) {
            return 0.0D;
        } else if (value instanceof Integer) {
            return new Double((double)abs((Integer)value));
        } else if (value instanceof Double) {
            return abs((Double)value);
        } else if (value instanceof BigDecimal) {
            return abs((BigDecimal)value);
        } else {
            return value instanceof Long ? new Double((double)abs((Long)value)) : 0.0D;
        }
    }

    public static int len(Object value) {
        return len((String)value);
    }

    public static int len(String value) {
        return value.length();
    }

    public static String left(Object value, int length) {
        return left((String)value, length);
    }

    public static String left(String value, int length) {
        if (length > 0 && length <= value.length()) {
            return value.substring(0, length);
        } else {
            return length > 0 ? value : "";
        }
    }

    public static String right(Object value, int length) {
        return right((String)value, length);
    }

    public static String right(String value, int length) {
        if (length > 0 && length <= value.length()) {
            return value.substring(value.length() - length);
        } else {
            return length > 0 ? value : "";
        }
    }

    public static String upper(Object value) {
        return upper((String)value);
    }

    public static String upper(String value) {
        return value.toUpperCase();
    }

    public static String lower(Object value) {
        return lower((String)value);
    }

    public static String lower(String value) {
        return value.toLowerCase();
    }

    public static String trim(Object value) {
        return trim((String)value);
    }

    public static String trim(String value) {
        return value.trim();
    }

    public static double getBigDecimalValue(Object obj) {
        if (obj instanceof String) {
            return getBigDecimalValue((String)obj);
        } else if (obj instanceof BigDecimal) {
            return getBigDecimalValue((BigDecimal)obj);
        } else if (obj instanceof Double) {
            return getBigDecimalValue((Double)obj);
        } else {
            return obj instanceof Integer ? getBigDecimalValue((Integer)obj) : 0.0D;
        }
    }

    public static double getBigDecimalValue(String obj) {
        return getBigDecimal(obj).doubleValue();
    }

    public static double getBigDecimalValue(Double obj) {
        return obj;
    }

    public static double getBigDecimalValue(BigDecimal obj) {
        return obj.doubleValue();
    }

    public static double getBigDecimalValue(Integer obj) {
        return getBigDecimal(obj).doubleValue();
    }

    public static double getBigDecimalValue(int value) {
        return (double)value;
    }

    public static double getBigDecimalValue(double value) {
        return value;
    }

    public static BigDecimal getBigDecimal(Object obj) {
        if (obj == null) {
            return new BigDecimal(0.0D);
        } else if (obj instanceof String) {
            return getBigDecimal((String)obj);
        } else if (obj instanceof BigDecimal) {
            return (BigDecimal)obj;
        } else if (obj instanceof Double) {
            return getBigDecimal((Double)obj);
        } else {
            return obj instanceof Integer ? getBigDecimal((Integer)obj) : new BigDecimal(0.0D);
        }
    }

    public static BigDecimal getBigDecimal(Double value) {
        return value == null ? new BigDecimal(0.0D) : new BigDecimal(value);
    }

    public static BigDecimal getBigDecimal(Integer value) {
        return value == null ? new BigDecimal(0.0D) : new BigDecimal(value.doubleValue());
    }

    public static BigDecimal getBigDecimal(String value) {
        return value != null && !isNull(value) ? new BigDecimal(value) : new BigDecimal(0.0D);
    }

    public static BigDecimal getBigDecimal(double value) {
        return new BigDecimal(value);
    }

    public static BigDecimal getBigDecimal(int value) {
        return BigDecimal.valueOf((long)value);
    }

    public static double getDouble(Object obj) {
        return 0.0D;
    }

    public static double getDouble(String text) {
        if (StringUtils.isEmpty(text)) {
            return 0.0D;
        } else {
            try {
                double value = Double.parseDouble(text);
                return value;
            } catch (Exception var3) {
                return 0.0D;
            }
        }
    }

    public static double getDoubleValue(Double obj) {
        return obj;
    }

    public static Double getDouble(double value) {
        return new Double(value);
    }

    public static String getString(int value) {
        return String.valueOf(value);
    }

    public static String getString(double value) {
        return String.valueOf(value);
    }

    public static String getString(Object value) {
        return value == null ? "" : String.valueOf(value);
    }


    public static Date now() {
        return new Date();
    }

    public static int year(Object date) {
        if (date == null) {
            return 0;
        } else if (date instanceof String) {
            return year((String)date);
        } else {
            return date instanceof Date ? year((Date)date) : 0;
        }
    }

    public static int year(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(1);
    }

    public static int year(String date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(date));
        return calendar.get(1);
    }

    public static int month(Object date) {
        if (date == null) {
            return 0;
        } else if (date instanceof String) {
            return month((String)date);
        } else {
            return date instanceof Date ? month((Date)date) : 0;
        }
    }

    public static int month(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(2) + 1;
    }

    public static int month(String date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(date));
        return calendar.get(2) + 1;
    }

    public static int day(Object date) {
        if (date == null) {
            return 0;
        } else if (date instanceof String) {
            return day((String)date);
        } else {
            return date instanceof Date ? day((Date)date) : 0;
        }
    }

    public static int day(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(5);
    }

    public static int day(String date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(date));
        return calendar.get(5);
    }

    public static int hour(Object date) {
        if (date == null) {
            return 0;
        } else if (date instanceof String) {
            return hour((String)date);
        } else {
            return date instanceof Date ? hour((Date)date) : 0;
        }
    }

    public static int hour(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(10);
    }

    public static int hour(String date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(date));
        return calendar.get(10);
    }

    public static int minute(Object date) {
        if (date == null) {
            return 0;
        } else if (date instanceof String) {
            return minute((String)date);
        } else {
            return date instanceof Date ? minute((Date)date) : 0;
        }
    }

    public static int minute(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(12);
    }

    public static int minute(String date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(date));
        return calendar.get(12);
    }

    public static int second(Object date) {
        if (date == null) {
            return 0;
        } else if (date instanceof String) {
            return second((String)date);
        } else {
            return date instanceof Date ? second((Date)date) : 0;
        }
    }

    public static int second(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(13);
    }

    public static int second(String date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(date));
        return calendar.get(13);
    }

    public static Date dateAdd(Object date, Object offSet) {
        if (date == null) {
            return null;
        } else if (offSet == null) {
            if (date instanceof String) {
                return new Date((String)date);
            } else {
                return date instanceof Date ? (Date)date : null;
            }
        } else if (date instanceof String) {
            if (offSet instanceof String) {
                return dateAdd(new Date((String)date), new Date((String)offSet));
            } else if (offSet instanceof Date) {
                return dateAdd(new Date((String)date), (Date)offSet);
            } else if (offSet instanceof Integer) {
                return dateAdd(new Date((String)date), (Integer)offSet);
            } else if (offSet instanceof Double) {
                return dateAdd(new Date((String)date), ((Double)offSet).intValue());
            } else if (offSet instanceof Long) {
                return dateAdd(new Date((String)date), ((Long)offSet).intValue());
            } else {
                return offSet instanceof BigDecimal ? dateAdd(new Date((String)date), ((BigDecimal)offSet).intValue()) : new Date((String)date);
            }
        } else if (date instanceof Date) {
            if (offSet instanceof String) {
                return dateAdd((Date)date, new Date((String)offSet));
            } else if (offSet instanceof Date) {
                return dateAdd((Date)date, (Date)offSet);
            } else if (offSet instanceof Integer) {
                return dateAdd((Date)date, (Integer)offSet);
            } else if (offSet instanceof Double) {
                return dateAdd((Date)date, ((Double)offSet).intValue());
            } else if (offSet instanceof Long) {
                return dateAdd((Date)date, ((Long)offSet).intValue());
            } else {
                return offSet instanceof BigDecimal ? dateAdd((Date)date, ((BigDecimal)offSet).intValue()) : (Date)date;
            }
        } else {
            return null;
        }
    }

    public static Date dateAdd(Date date, Date offSet) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(1, year(offSet));
        calendar.add(2, month(offSet));
        calendar.add(5, day(offSet));
        calendar.add(10, hour(offSet));
        calendar.add(12, minute(offSet));
        calendar.add(13, second(offSet));
        return calendar.getTime();
    }

    public static Date dateAdd(Object date, int offSet) {
        if (date == null) {
            return null;
        } else if (date instanceof String) {
            return dateAdd(new Date((String)date), offSet);
        } else {
            return date instanceof Date ? dateAdd((Date)date, offSet) : null;
        }
    }

    public static Date dateDiff(Object date, int offSet) {
        if (date == null) {
            return null;
        } else if (date instanceof String) {
            return dateDiff(new Date((String)date), offSet);
        } else {
            return date instanceof Date ? dateDiff((Date)date, offSet) : null;
        }
    }

    public static Date dateAdd(Date date, int offSet) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(5, offSet);
        return calendar.getTime();
    }

    public static Date dateDiff(Date date, int offSet) {
        return dateAdd(date, -1 * offSet);
    }

    public static String getNumberFormat(Object precision) {
        return getNumberFormat(getIntValue(precision));
    }

    public static String getNumberFormat(int precision) {
        String format = "%r{0.";

        for(int i = 0; i < precision; ++i) {
            format = format + "#";
        }

        format = format + "}f";
        return format;
    }

    public static int getLength(String text) {
        return StringUtils.isEmpty(text) ? 0 : text.length();
    }

    public static boolean isNull(Object value) {
        if (value instanceof String) {
            return isNull((String)value);
        } else if (value instanceof Integer) {
            return isNull((Integer)value);
        } else if (value instanceof Float) {
            return isNull((Float)value);
        } else if (value instanceof Double) {
            return isNull((Double)value);
        } else if (value instanceof BigDecimal) {
            return isNull((BigDecimal)value);
        } else if (value instanceof Date) {
            return isNull((Date)value);
        } else {
            return value == null;
        }
    }

    public static boolean isNull(String value) {
        return StringUtils.isEmpty(value);
    }

    public static boolean isNull(int value) {
        return value == 0 || value == -1;
    }

    public static boolean isNull(float value) {
        return value == 0.0F;
    }

    public static boolean isNull(double value) {
        return value == 0.0D;
    }

    public static boolean isNull(Integer value) {
        return value == null || value == 0;
    }

    public static boolean isNull(Float value) {
        return value == null || isNull(value);
    }

    public static boolean isNull(Double value) {
        return value == null || isNull(value);
    }

    public static boolean isNull(BigDecimal value) {
        return value == null || isNull(value.doubleValue());
    }

    public static boolean isNull(Date value) {
        return value == null;
    }


    public static boolean isNotNull(Object value) {
        if (value instanceof Integer) {
            return isNotNull((Integer)value);
        } else if (value instanceof Float) {
            return isNotNull((Float)value);
        } else if (value instanceof Double) {
            return isNotNull((Double)value);
        } else if (value instanceof BigDecimal) {
            return isNotNull((BigDecimal)value);
        } else if (value instanceof Date) {
            return isNotNull((Date)value);
        } else {
            return value != null;
        }
    }

    public static boolean isNotNull(String value) {
        return !isNull(value);
    }

    public static boolean isNotNull(int value) {
        return !isNull(value);
    }

    public static boolean isNotNull(float value) {
        return !isNull(value);
    }

    public static boolean isNotNull(double value) {
        return !isNull(value);
    }

    public static boolean isNotNull(Integer value) {
        return !isNull(value);
    }

    public static boolean isNotNull(Float value) {
        return !isNull(value);
    }

    public static boolean isNotNull(Double value) {
        return !isNull(value);
    }

    public static boolean isNotNull(BigDecimal value) {
        return !isNull(value);
    }

    public static boolean isNotNull(Date value) {
        return !isNull(value);
    }

    public static boolean isNumeric(Object value) {
        if (isNull(value)) {
            return true;
        } else {
            String str = value.toString();
            return str.matches("\\d+(.\\d+)?");
        }
    }

    public static boolean isValidDate(Object str) {
        if (isNull(str)) {
            return true;
        } else {
            boolean convertSuccess = true;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            try {
                format.setLenient(false);
                format.parse(str.toString());
            } catch (ParseException var4) {
                convertSuccess = false;
            }

            return convertSuccess;
        }
    }

    public static String format(Object value, String format) {
        if (value == null) {
            return "";
        } else {
            return value instanceof Date ? format((Date)value, format) : value.toString();
        }
    }

    public static String format(Date value, String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(value);
    }


    /**
     * @description  将命名改为驼峰的方法
     * @param  map  转换的对象
     * @return  转换的结果
     * @date  20/07/06 9:12
     * @author  wanghb
     * @edit
     */
    public static Map<String, Object> toHump(Map<String, Object> map) {
        Map<String,Object> humpMap = new HashMap<>();
        humpMap.clear();
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        Iterator<Map.Entry<String, Object>> iter = entrySet.iterator();
        while (iter.hasNext())
        {
            Map.Entry<String, Object> entry = iter.next();
            String keyStr = entry.getKey();
            Object valueStr = entry.getValue();
            humpMap.put( toHump(keyStr),valueStr);
        }
        return humpMap;
    }

    public static final char UNDERLINE = '_';

    /**
     * @description  下划线格式字符串转换为驼峰格式字符串
     * @param  param  参数
     * @return  转换的结果
     * @date  20/07/06 9:12
     * @author  wanghb
     * @edit
     */
    public static String toHump(String param) {

        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰法转下划线 方法格式
     * @param line String类型 要处理的字符串
     * @return String类型
     * @author wanghb
     * @date 2017-11-13
     * @version V1.0
     */
    private static Pattern humpPattern = Pattern.compile("[A-Z]");
    public static String toUnderline(String line) {
        if (line == null){return "";}
        Matcher matcher = humpPattern.matcher(line);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 首字母小写
     * @param str String类型 要处理的字符串
     * @return String类型
     * @author wanghb
     * @date 2017-11-13
     * @version V1.0
     */
    public static String firstLowerCase(String str) {
        if ("".equals( getString( str ) )){return "";};
        if(Character.isLowerCase(str.charAt(0))) {
            return str;
        }else {
            return (new StringBuilder()).append( Character.toLowerCase( str.charAt( 0 ) ) ).append( str.substring( 1 ) ).toString();
        }
    }

    /**
     * 首字母小写
     * @param str String类型 要处理的字符串
     * @return String类型
     * @author wanghb
     * @date 2017-11-13
     * @version V1.0
     */
    public static String firstUpperCase(String str) {
        if ("".equals( getString( str ) )){return "";};
        if(Character.isUpperCase(str.charAt(0))) {
            return str;
        }else {
            return (new StringBuilder()).append( Character.toUpperCase( str.charAt( 0 ) ) ).append( str.substring( 1 ) ).toString();
        }
    }


}
