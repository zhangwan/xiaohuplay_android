/*
 *
 *  * Copyright © Yan Zhenjie
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package com.tinytiger.common.utils.permission;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;


/**
 *
 * @author zhw_luke
 * @date 2019/10/21 0021 下午 3:29
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 系统权限,获取弹框
 */
public final class NotifyRationale implements Rationale<Void> {

    @Override
    public void showRationale(Context context, Void data, final RequestExecutor executor) {
        new AlertDialog.Builder(context).setCancelable(false)
                .setTitle("提示")
                .setMessage("您的设备不允许我们弹出通知")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executor.execute();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executor.cancel();
                    }
                })
                .show();
    }

}