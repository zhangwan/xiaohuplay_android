package com.tinytiger.common.utils;


import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author luke
 * @date 2018/9/3 17:58
 * @doc
 */
public class TimeZoneUtil {


    /**
     * 将字符串转为时间戳
     *
     * @param dateString
     * @param pattern
     * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        if (dateString == null) {
            return 0;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


    public static long getStringToDate(String dateString) {
        return getStringToDate(dateString, "yyyy-MM-dd HH:mm:ss");
    }

    public static long getToDate(String dateString) {
        return getStringToDate(dateString, "yyyy-MM-dd");
    }


    /**
     * 时间戳转换成字符窜
     *
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }


    public static String formatTimemS(long time) {
        SimpleDateFormat cdate = new SimpleDateFormat("mm:ss");
        Date date = new Date(time);
        return cdate.format(date);
    }

    public static String formatTime2(Long time) {
        SimpleDateFormat cdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return cdate.format(date);
    }

    public static String formatTimeM(long time) {
        SimpleDateFormat cdate = new SimpleDateFormat("MM月dd日 HH:mm");
        Date date = new Date(time);
        return cdate.format(date);
    }


    /**
     * 时间格式化
     *
     * @param time 标准时间 yyyy-MM-dd HH:mm
     * @return
     */
    public static String getShortTimeShowString(String time) {
        return getTimeShowString(getStringToDate(time));
    }


    /**
     * 时间格式化
     *
     * @param milliseconds 时间戳
     * @return
     */
    public static String getTimeShowString(long milliseconds) {
        String dataString;

        Date currentTime = new Date(milliseconds);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(currentTime);

        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);

        //获取当前时间
        Date todaybegin = todayStart.getTime();
        Date yesterdaybegin = new Date(todaybegin.getTime() - 3600 * 24 * 1000);

        boolean type = true;
        if (!currentTime.before(todaybegin)) {
            long ti = System.currentTimeMillis() - milliseconds;
            if (ti < 600000) {
                dataString = "刚刚";
                type = false;
            } else if (ti < 3600000) {
                dataString = ti / 60000 + "分钟前";
                type = false;
            } else {
                dataString = "今天";
            }
        } else if (!currentTime.before(yesterdaybegin)) {
            dataString = "昨天";
        } else if (todayStart.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
            //同年
            dataString = new SimpleDateFormat("MM月dd日", Locale.getDefault()).format(currentTime);
        } else {
            //不同年
            type = false;
            dataString = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault()).format(currentTime);
        }

        if (type) {
            return dataString + " " + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(currentTime);
        } else {
            return dataString;
        }

    }

    public static String formatTimeMS(int time) {
        int mm = time / 60;
        int ss = time % 60;

        String ssString = "" + ss;
        String mmString = "" + mm;

        if (ss < 10 && ss >= 0) {
            ssString = "0" + ss;
        }
        if (mm < 10) {
            mmString = "0" + mm;
        }

        return mmString + ":" + ssString;
    }

    /**
     * 秒转成时分秒
     *
     * @param second
     * @param type   true 返回 时分秒  false 14:30
     * @return
     */
    public static String convertTime(Integer second, Boolean type) {

        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        String s1 = "";
        if (s == 0) {
            s1 = "00";
        } else if (s < 10) {
            s1 = "0" + s;
        } else {
            s1 = s + "";
        }
        if (h > 0) {
            if (type) {
                return h + "时" + d + "分" + s1 + "秒";
            } else {
                return h + ":" + d + ":" + s1;
            }

        } else {
            if (type) {
                return d + "分" + s1 + "秒";
            } else {
                return d + ":" + s1;
            }
        }

    }


}
