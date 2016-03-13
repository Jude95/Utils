package com.jude.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Mr.Jude on 2015/4/3.
 */
public class JTimeTransform {
    public static final int SECOND = 60;
    public static final int HOUR = 3600;
    public static final int DAY = 86400;
    public static final int WEEK = 604800;


    Calendar currentTime;

    public JTimeTransform(){
        currentTime=new GregorianCalendar();
        currentTime.setTime(new Date());
    }
    public JTimeTransform(long timestamp){
        currentTime=new GregorianCalendar();
        currentTime.setTime(new Date(timestamp*1000));
    }
    public JTimeTransform(int year, int month, int day){
        currentTime=new GregorianCalendar(year,month,day);
    }

    public int getDay(){
        return currentTime.get(Calendar.DATE);
    }
    public int getMonth(){
        return currentTime.get(Calendar.MONTH)+1;
    }
    public int getYear(){
        return currentTime.get(Calendar.YEAR);
    }
    public long getTimestamp(){
        return currentTime.getTime().getTime()/1000;
    }

    /**
     * 格式化输出日期
     * 年:y		月:M		日:d		时:h(12制)/H(24值)	分:m		秒:s		毫秒:S
     * @param formatString
     */
    public String toString(String formatString){
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        String date = format.format(currentTime.getTime());
        return date;
    }


    /**
     * 格式化解析日期文本
     * 年:y		月:M		日:d		时:h(12制)/H(24值)	分:m		秒:s		毫秒:S
     * @param formatString
     */
    public JTimeTransform parse(String formatString,String content){
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        try {
            currentTime.setTime(format.parse(content));
            return this;
        } catch (ParseException e) {
            return null;
        }
    }

    public String toString(DateFormat format){
        long delta = (System.currentTimeMillis() - currentTime.getTime().getTime())/1000;
        return format.format(this,delta);
    }

    public interface DateFormat{
        String format(JTimeTransform date, long delta);
    }

    public static class RecentDateFormat implements DateFormat{
        private String lastFormat;

        public RecentDateFormat() {
            this("MM-dd");
        }

        public RecentDateFormat(String lastFormat) {
            this.lastFormat = lastFormat;
        }

        @Override
        public String format(JTimeTransform date, long delta) {
            if (delta>0){
                if (delta / JTimeTransform.SECOND < 1){
                    return delta +"秒前";
                }else if (delta / JTimeTransform.HOUR < 1){
                    return delta / JTimeTransform.SECOND+"分钟前";
                }else if (delta / JTimeTransform.DAY < 2 && new JTimeTransform().getDay() == date.getDay()){
                    return delta / JTimeTransform.HOUR+"小时前";
                }else if (delta / JTimeTransform.DAY < 3 && new JTimeTransform().getDay() == new JTimeTransform(date.getTimestamp()+ JTimeTransform.DAY).getDay()){
                    return "昨天"+date.toString("HH:mm");
                }else if (delta / JTimeTransform.DAY < 4 && new JTimeTransform().getDay() == new JTimeTransform(date.getTimestamp()+ JTimeTransform.DAY*2).getDay()){
                    return "前天"+date.toString("HH:mm");
                }else{
                    return date.toString(lastFormat);
                }
            }else{
                delta = -delta;
                if (delta / JTimeTransform.SECOND < 1){
                    return delta +"秒后";
                }else if (delta / JTimeTransform.HOUR < 1){
                    return delta / JTimeTransform.SECOND+"分钟后";
                }else if (delta / JTimeTransform.DAY > -2 && new JTimeTransform().getDay() == date.getDay()){
                    return delta / JTimeTransform.HOUR+"小时后";
                }else if (delta / JTimeTransform.DAY > -3 && new JTimeTransform().getDay() == new JTimeTransform(date.getTimestamp()- JTimeTransform.DAY).getDay()){
                    return "明天"+date.toString("HH:mm");
                }else if (delta / JTimeTransform.DAY > -4 && new JTimeTransform().getDay() == new JTimeTransform(date.getTimestamp()- JTimeTransform.DAY*2).getDay()){
                    return "后天"+date.toString("HH:mm");
                }else{
                    return date.toString(lastFormat);
                }
            }
        }
    }

}
