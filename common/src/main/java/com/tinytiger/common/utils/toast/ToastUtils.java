package com.tinytiger.common.utils.toast;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;
import com.tinytiger.common.utils.Dp2PxUtils;


public class ToastUtils {

    /**
     * 将最长使用的显示方法单独提出来，方便使用。
     * 屏幕中心位置短时间显示Toast。
     *
     * context
     * @param message
     */
    public static void show(Context context, String message) {
        toshort(context,message);
    }


    /**
     * 屏幕底部中间位置显示短时间Toast
     *
     * context
     * @param message
     */
    public static void ToastShortBottomCenter(Context context, String message) {
        if (context != null) {
            MyToast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 屏幕中心位置短时间显示Toast
     * @param context
     * @param message
     */
    public static void toshort(Context context, String message) {
        if (context != null) {
            MyToast toast = MyToast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }


    /**
     * 屏幕顶部中心位置短时间显示Toast
     *
     * @param message
     */
    public static void ToastShortTopCenter(Context context, String message) {
        if (context != null) {

            MyToast toast = MyToast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
    }


    /**
     * 屏幕中心位置长时间显示Toast
     *
     * @param message
     */
    public static void ToastLongCenter(Context context, String message) {
        if (context != null) {
            MyToast toast = MyToast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    /**
     * 屏幕中心位置长时间显示Toast
     *
     * @param message
     */
    public static void ToastTop(Context context, String message,int dp) {
        if (context != null) {
            MyToast toast = MyToast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, Dp2PxUtils.dip2px(context,dp));
            toast.show();
        }
    }


}
