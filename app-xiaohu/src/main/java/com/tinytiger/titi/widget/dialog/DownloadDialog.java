package com.tinytiger.titi.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tinytiger.common.R;
import com.tinytiger.common.utils.preference.SpUtils;
import com.xwdz.download.core.QuietDownloader;


/**
 *
 * @author zhw_luke
 * @date 2020/4/15 0015 下午 4:49
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 下载服务网络切换提示
 */
public class DownloadDialog {
    private Dialog dialog;

    private static DownloadDialog instance;

    private DownloadDialog() {

    }

    public static DownloadDialog getInstance() {
        if (instance == null) {
            synchronized (DownloadDialog.class) {
                if (instance == null) {
                    instance = new DownloadDialog();
                }
            }
        }
        return instance;
    }


    public DownloadDialog builder(Context context) {
        if (isShowing()) {
            return this;
        }

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_text, null);
        TextView tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText("流量下载提醒");
        tv_title.setVisibility(View.VISIBLE);
        TextView tv_message = view.findViewById(R.id.tv_message);
        tv_message.setText("本次下载是否使用手机流量下载");

        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtils.saveSP(R.string.network_download, false);
                dismiss();
            }
        });

        view.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtils.saveSP(R.string.network_download, true);
                QuietDownloader.recoverAll();
                dismiss();
            }
        });


        dialog = new Dialog(context, R.style.DialogStyle);
        dialog.setContentView(view);
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
