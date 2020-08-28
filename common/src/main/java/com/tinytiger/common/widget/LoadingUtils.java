package com.tinytiger.common.widget;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.tinytiger.common.R;


/**
 * @author luke
 * @date 2018/8/30 20:19
 * @doc 加载框
 */
public class LoadingUtils {

    private static Dialog mProgressDialog = null;
    private static LoadingUtils mInstance = null;

    /**
     * 显示时长
     */
    private int mTimeOut = 6000;

    public static LoadingUtils getInstance() {
        if (mInstance == null) {
            mInstance = new LoadingUtils();
        }
        return mInstance;
    }

    /**
     * 设置显示时长
     *
     * @param time
     */
    public LoadingUtils setTimeOut(int time) {
        this.mTimeOut = time;
        return this;
    }


    /**
     * 加载显示loding
     *
     * @param activity
     */
    public void show(Activity activity) {
        try {
            // 开始请求，显示请求对话框
            if (mProgressDialog == null) {
                mProgressDialog = new Dialog(activity, R.style.LodingDialog);
                setStatusBar();
                View contentView = View.inflate(activity, R.layout.dialog_loding, null);
                mProgressDialog.setContentView(contentView);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setCancelable(true);
                Window window = mProgressDialog.getWindow();
                window.setDimAmount(0f);
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);
                window.getDecorView().setPadding(0, 0, 0, 0);
            }

            if (!activity.isFinishing()) {
                mProgressDialog.show();
                mHandler.sendEmptyMessageDelayed(10, mTimeOut);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void show(Activity activity, int millis) {
        show(activity);
        mHandler.sendEmptyMessageDelayed(10, millis);
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mHandler.removeMessages(10);
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }

        }
    };

    public void dismiss() {
        mHandler.sendEmptyMessage(10);
    }


    /**
     * Android 6.0 以上设置状态栏颜色
     */
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 设置状态栏底色颜色
            mProgressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            mProgressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mProgressDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

}
