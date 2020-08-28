package com.tinytiger.common.utils;


/**
 * @author luke
 * @date 2018/9/11 22:01
 * @doc 清除重复点击事件
 */
public class FastClickUtil {


    /**
     * 两次点击间隔不能少于1100ms
     */
    private static final int MIN_DELAY_TIME = 1100;
    public static long lastClickTime;


    /**
     * 重复点击,两次有效点击间隔
     * 非重复计时,有效点击更新时间
     * @return true 无效点击,false有效点击
     */
    public static boolean isFastClickTiming() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
            lastClickTime = currentClickTime;
        }
        return flag;
    }

    /**
     * 重复点击,两次点击间隔,
     * 连续计时,每次点击更新时间
     * @return true 无效点击,false有效点击
     */
    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    /**
     * 重复点击,两次有效点击间隔
     * @param time 有效点击间隔时间
     * @return true 无效点击,false有效点击
     */
    public static boolean isFastClick(int time) {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= time) {
            flag = false;
            lastClickTime = currentClickTime;
        }
        return flag;
    }

}
