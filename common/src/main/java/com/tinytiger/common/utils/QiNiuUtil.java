package com.tinytiger.common.utils;


import com.orhanobut.logger.Logger;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.tinytiger.common.R;
import com.tinytiger.common.utils.preference.SpUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * @author zhw_luke
 * @date 2018/8/29 0029 下午 4:25
 * @Copyright 小虎互联科技
 * @doc 七牛图片上传工具类
 * @since 2.1.0
 */
public class QiNiuUtil {


    /**
     * @param token 七牛tocken
     * @param key   文件标记,指定七牛服务上的文件名
     * @param data  File对象、或 文件路径、或 字节数组
     */
    public static void sUploadManager(String token, String key, String data, final OnQiNiuListener mListener) {

        Zone zone = new FixedZone(new String[]{"up-z2.qiniup.com"});
        Configuration configuration = new Configuration.Builder().chunkSize(256 * 1024)
                .putThreshhold(512 * 1024)
                .connectTimeout(10)
                .zone(zone)
                .useHttps(false)
                .responseTimeout(10).build();

        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        new UploadManager(configuration).put(data, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        mListener.onType(info.isOK());
                        if (!info.isOK()) {
                            Logger.d(info.error);
                        }
                    }
                }, null);
    }


    public interface OnQiNiuListener {
        /**
         * @param type false 上传失败 true上传成功
         */
        void onType(Boolean type);
    }


    public static void uploadImages(String token, final List<String> images, final OnQiniuImagesListener listener) {
        if (token == null || images == null || images.size() == 0) {
            return;
        }

        Zone zone = new FixedZone(new String[]{"up-z2.qiniup.com"});
        Configuration config = new Configuration.Builder().chunkSize(256 * 1024)
                .putThreshhold(512 * 1024)
                .connectTimeout(10)
                .zone(zone)
                .useHttps(false)
                .responseTimeout(60).build();

        for (int i = 0; i < images.size(); i++) {
            String key = "img" + System.currentTimeMillis();
            new UploadManager(config).put(images.get(i), key, token,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            if (info.isOK()) {
                                listener.onResult(key);
                            }

                        }
                    }, null);
        }

    }


    public interface OnQiniuImagesListener {

        void onResult(String result);
    }


    public interface OnQiniuFileListener {
        void onResult(String result, Boolean type);
    }

    /**
     * @param token 七牛tocken
     * @param key   文件标记,指定七牛服务上的文件名
     * @param data  File对象、或 文件路径、或 字节数组
     */
    public static void uploadManager(String token, String key, String data, final OnQiniuFileListener mListener) {
        key = key + System.currentTimeMillis() + SpUtils.getString(R.string.user_id, "");

        int last = data.lastIndexOf(".");
        if (last > 0) {
            key += data.substring(last);
        }
        Zone zone = new FixedZone(new String[]{"up-z2.qiniup.com"});
        Configuration configuration = new Configuration.Builder().chunkSize(256 * 1024)
                .putThreshhold(512 * 1024)
                .connectTimeout(10)
                .zone(zone)
                .useHttps(false)
                .responseTimeout(10).build();

        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        new UploadManager(configuration).put(data, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        mListener.onResult("https://cdn.tinytiger.cn/" + key, info.isOK());
                        if (!info.isOK()) {
                            Logger.d(info.error);
                        }
                    }
                }, null);
    }
}
