package com.github.binarywang.demo.wechat.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataUtils {
    /**
     * 判断time是否在now的n天之内
     * @param time
     * @param now
     * @param n    正数表示在条件时间n天之后，负数表示在条件时间n天之前
     * @return
     */
    public static boolean belongDate(Date time, Date now, int n) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();  //得到日历
        calendar.setTime(time);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, n);
        Date beforedays = calendar.getTime();   //得到n前的时间
        if (beforedays.getTime() < now.getTime()) {
            return true;
        } else {
            return false;
        }
    }
}
