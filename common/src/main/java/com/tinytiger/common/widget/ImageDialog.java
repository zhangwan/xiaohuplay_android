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
public class ImageDialog {
    private Dialog dialog;

    private static ImageDialog instance;

    private ImageDialog() {

    }

    public static ImageDialog getInstance() {
        if (instance == null) {
            synchronized (ImageDialog.class) {
                if (instance == null) {
                    instance = new ImageDialog();
                }
            }
        }
        return instance;
    }


    public ImageDialog builder(Context context, String msg) {
        if (isShowing()) {
            return this;
        }

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_image, null);


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
