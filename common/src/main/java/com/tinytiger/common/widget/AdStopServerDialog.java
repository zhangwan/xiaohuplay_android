package com.tinytiger.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tinytiger.common.R;


/**
 * @author zhw_luke
 * @date 2018/10/6 0006 下午 5:08
 * @doc 全局停服通知
 */
public class AdStopServerDialog {
    private Dialog dialog;

    private static AdStopServerDialog instance;

    private AdStopServerDialog() {

    }

    public static AdStopServerDialog getInstance() {
        if (instance == null) {
            synchronized (AdStopServerDialog.class) {
                if (instance == null) {
                    instance = new AdStopServerDialog();
                }
            }
        }
        return instance;
    }


    public AdStopServerDialog builder(Context context, String msg) {
        if (isShowing()) {
            return this;
        }

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_stop_server, null);
        TextView tv_message = view.findViewById(R.id.tv_message);
        tv_message.setText(msg);

        dialog = new Dialog(context, R.style.DialogStyle);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        return this;
    }

    public void show() {
        try {
            if (isShowing()) {
                return;
            }
            if (dialog != null) {
                dialog.show();
            }
        } catch (Exception e) {

        }
    }


    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        } else {
            return false;
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
