package com.example.administrator.quxiang.Utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2020/5/18.
 */

public class CalendarUtil {
    static Calendar calendar ;
    public static Date getToday(){
        calendar= Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    public static Date getBeforeYesterday(){
        calendar= Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE, -12);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    public static Date getNow(){
        calendar= Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.getTime();
    }
}
