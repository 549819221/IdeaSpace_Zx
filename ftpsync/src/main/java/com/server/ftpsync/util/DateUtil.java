package com.server.ftpsync.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期格式化工具类
 * 2019-06-28
 * yc
 */
public class DateUtil {
    public static final String DATE_YEAR = "yyyy";
    public static final String DATE_SHORTS = "yyyyMMdd";
    public static final String DATE_SHORT = "yyyy-MM-dd";
    public static final String DATE_MONTH = "yyyy-MM";
    public static final String DATE_LONG = "yyyy-MM-dd HH:mm:ss";
    public static final String [] monthArr = {"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
    public static final String[] weeksI = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
    public static final String[] weeksII = {"日","一","二","三","四","五","六"};
    /************************************************日期计算开始************************************************/


    /**
     * @description  计算两个之时间的小时数  保留小数位
     * @param  starAmp  结束时间     时间格式HH:mm:ss
     * @param  endAmp  结束时间    时间格式HH:mm:ss
     * @param  scalse  小数位
     * @date  2019/10/18 16:19
     * @author  wanghb
     * @edit
     */
    public static BigDecimal countHour(String starAmp,String endAmp,Integer scalse){
        if("".equals(starAmp) || "".equals(endAmp)){
            return BigDecimal.ZERO;
        }
        String nowDate = toString(new Date(), DATE_SHORT);
        starAmp = nowDate + " " + starAmp;
        endAmp = nowDate + " " + endAmp;
        long diff = 0;
        try {
            diff = toDate(endAmp, DATE_LONG).getTime() - toDate(starAmp, DATE_LONG).getTime();
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
        BigDecimal hour = new BigDecimal(diff).divide(new BigDecimal(60 * 60 * 1000) ,scalse, BigDecimal.ROUND_CEILING);
        return hour;
    }


    /**
     * @description  计算两个日期的小时数  保留小数位
     * @param  startDate  结束时间     时间格式yyyy-MM-dd HH:mm:ss
     * @param  endDate  结束时间    时间格式yyyy-MM-dd HH:mm:ss
     * @param  scalse  小数位
     * @date  2019/10/18 16:19
     * @author  wanghb
     * @edit
     */
    public static BigDecimal countHour(Date startDate,Date endDate,Integer scalse){
        if(PowerUtil.isNull( startDate ) || PowerUtil.isNull( endDate ) ){
            return BigDecimal.ZERO;
        }
        long diff = 0;
        try {
            diff = endDate.getTime() - startDate.getTime();
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
        if(diff < 0){
            return BigDecimal.ZERO;
        }
        BigDecimal hour = new BigDecimal(diff).divide(new BigDecimal(60 * 60 * 1000) ,scalse, BigDecimal.ROUND_CEILING);
        return hour;
    }


    /**
     * @description  日期加减
     * @param  date  要计算的日期
     * @param  days  计算数量
     * @param  calendarType  Calendar类的常量   Calendar.YEAR 年  Calendar.DATE 天 Calendar.MONTH 月
     * @return  计算结果
     * @date  20/07/03 16:49
     * @author  wanghb
     * @edit
     */
    public static Date countDate(Date date, Integer days,int calendarType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendarType, days);
        return calendar.getTime();
    }


    /**
     * @description 计算2个日期之间相差的  以年、月、日为单位，各自计算结果是多少
     * 比如：2011-02-02 到  2017-03-02
     * 以年为单位相差为：6年
     * 以月为单位相差为：73个月
     * 以日为单位相差为：2220天
     * @param  startDate  开始时间
     * @param  endDate  结束时间
     * @param  type  计算类型  year 年 ,month  月,day  天
     * @return  返回结果
     * @date  20/07/03 17:14
     * @author  wanghb
     * @edit
     */
    public static int countCompare(Date startDate,Date endDate,String type){
        Calendar  from  =  Calendar.getInstance();
        from.setTime(startDate);
        Calendar  to  =  Calendar.getInstance();
        to.setTime(endDate);
        int fromYear = from.get(Calendar.YEAR);
        int fromMonth = from.get(Calendar.MONTH);
        int toYear = to.get(Calendar.YEAR);
        int toMonth = to.get(Calendar.MONTH);
        int year = toYear  -  fromYear;
        int month = toYear *  12  + toMonth  -  (fromYear  *  12  +  fromMonth);
        int day = (int) ((to.getTimeInMillis()  -  from.getTimeInMillis())  /  (24  *  3600  *  1000));
        if("year".equals(type)) {
            return day;
        }
        if("month".equals(type)) {
            return month;
        }
        if("day".equals(type)) {
            return day;
        }
        return  0;
    }


    /**
     * @description  计算两个时间相差的分钟数
     * @param  startTime  开始时间
     * @param  endTime  结束时间
     * @param  scale  保留几位小数
     * @return  返回计算结果
     * @date  20/07/03 16:57
     * @author  wanghb
     * @edit
     */
    public static BigDecimal countMinute(String startTime, String endTime,Integer scale){
        SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        // 获得两个时间的毫秒时间差异
        try {
            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天
            hour = diff % nd / nh + day * 24;// 计算差多少小时
            min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟
            sec = diff % nd % nh % nm / ns;// 计算差多少秒
            long s = (hour - day * 24)*60*60;
            long s1 = (min - day * 24 * 60)*60 + s+sec;
            BigDecimal bigDecimal2 = new BigDecimal(s1);
            bigDecimal2=bigDecimal2.divide(new BigDecimal(60),scale,BigDecimal.ROUND_HALF_UP);
            return bigDecimal2;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
    /************************************************日期计算结束************************************************/


    /************************************************日期转化开始************************************************/


    /**
     * @description  通过日期获取星期
     * @param  date  日期
     * @param  weeks  星期类型
     * @return  星期几
     * @date  20/07/03 16:39
     * @author  wanghb
     * @edit
     */
    public static String toWeek(Date date ,String[] weeks){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(week_index<0){
            week_index = 0;
        }
        return PowerUtil.getString(weeks[week_index]);
    }


    /**
     * @description  将Date日期转换为String
     * @param  date  时间
     * @param  formatStr  转化的格式
     * @return  返回的结果
     * @date  20/07/03 16:51
     * @author  wanghb
     * @edit
     */
    public static String toString(Date date, String formatStr) {
        if (null == date || null == formatStr) {
            return "";
        }
        SimpleDateFormat df = new SimpleDateFormat(formatStr);
        return df.format(date);
    }


    /**
     * @description  时间戳 转 String
     * @param  date  时间戳
     * @param  format  日期格式
     * @return  返回结果
     * @date  20/07/03 16:52
     * @author  wanghb
     * @edit
     */
    public static String toDate(long date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }


    /**
     * @description  String 转 date
     * @param  str  字符串
     * @param  format  转化格式
     * @return  返回结果
     * @date  20/07/03 17:02
     * @author  wanghb
     * @edit
     */
    public static Date toDate(String str,String format){
        if("".equals(PowerUtil.getString(str))){
            return null;
        }
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        try {
            return format1.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /************************************************日期转化结束************************************************/
    /************************************************获取日期开始************************************************/


    /**
     * @description  获取当年年份
     * @return  年份
     * @date  20/07/03 17:54
     * @author  wanghb
     * @edit
     */
    public static String getNowYear() {
        return toString(new Date(), DATE_YEAR);
    }


    /**
     * 获取当前,下个月,上个月 的的第一天
     * @return index 0 为当前月  1为下个月  -1为上个月  以此类推
     * @author wanghb
     * @date 2019-06-25
     */
    public static Date getMonthFirstDay(Integer index){
        if(index == null){
            return null;
        }
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, index);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        return cale.getTime();
    }


    /**
     * 获取中间日期
     * @return start 开始日期
     * @return end 结束日期
     * @author wanghb
     * @date 2019-06-25
     */
    public static List<String> getMiddleDate(Date startDate, Date endDate) {
        List<String> list = new ArrayList<>();
        long s = startDate.getTime();
        long e = endDate.getTime();
        Long oneDay = 1000 * 60 * 60 * 24l;
        while (s <= e) {
            startDate = new Date(s);
            list.add(new SimpleDateFormat("yyyy-MM-dd").format(startDate));
            s += oneDay;
        }
        return list;
    }


    /**
     * @description  获取某个月有多少天
     * @param  date  日期
     * @return  返回结果
     * @date  20/07/03 17:19
     * @author  wanghb
     * @edit
     */
    public static int getMonthCountDays(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());//设置时间
        int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH);//获取月份
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH, month - 1);//Java月份才0开始算
        return cal.getActualMaximum(Calendar.DATE);
    }


    /**
     * @description  获取某一年一共多少天
     * @param  year  年份
     * @return  返回结果
     * @date  20/07/03 17:23
     * @author  wanghb
     * @edit
     */
    public static int getYearCountDays(Integer year){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        int actualMaximum = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
        return actualMaximum;
    }


    /**
     * @description  获取某一年的最后一天
     * @param  year  某一年
     * @return  返回结果
     * @date  20/07/03 17:22
     * @author  wanghb
     * @edit
     */
    public static Date getYearLast(Integer year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
        return currYearLast;
    }
    /************************************************获取日期结束************************************************/


    /************************************************日期比较开始************************************************/
    /**
     * @description  两个时间的大小比较
     * @param  time1  开始时间   时间格式HH:mm:ss
     * @param  time2  结束时间   时间格式HH:mm:ss
     * @date  2019/10/18 16:19
     * @author  wanghb
     * @edit
     */
    public static Boolean compareHour(String time1,String time2){
        if("".equals(time1) || "".equals(time2)){
            return false;
        }
        String nowDate = toString(new Date(), DATE_SHORT);
        time1 = nowDate + " " + time1;
        time2 = nowDate + " " + time2;
        return toDate(time1, DATE_LONG).getTime() >= toDate(time2, DATE_LONG).getTime();
    }
    /************************************************日期比较结束************************************************/


    /************************************************日期判断开始************************************************/


    /**
     * @description  判断一个时间是上午还是下午  0：上午  1：下午  -1 无法识别
     * @param  aLong  时间戳
     * @param  format  日期格式
     * @return  返回结果
     * @date  20/07/03 16:54
     * @author  wanghb
     * @edit
     */
    public static int amEnum = 0;
    public static int pmEnum = 1;
    public static int isAMPM(long timestamp) {
        try {
            String s = toDate(timestamp, "yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(s);
            GregorianCalendar ca = new GregorianCalendar();
            ca.setTime(date);
            return ca.get(GregorianCalendar.AM_PM);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * @description  判断当前时间是否在 startTime ~ endTime  区间，注意时间格式要一致
     * @param  nowTime  当前时间
     * @param  startTime  开始时间
     * @param  endTime  结束时间
     * @date  2019/10/21 11:32
     * @author  wanghb
     * @edit
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
    /************************************************日期判断结束************************************************/



}
