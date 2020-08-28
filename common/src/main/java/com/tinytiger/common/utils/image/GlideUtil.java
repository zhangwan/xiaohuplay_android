package com.tinytiger.common.utils.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.orhanobut.logger.Logger;
import com.tinytiger.common.R;
import com.tinytiger.common.base.BaseApp;
import com.tinytiger.common.utils.FileUtils;
import com.tinytiger.common.utils.ScreenUtil;

import java.io.File;
import java.security.MessageDigest;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;


/**
 * @author zhw_luke
 * @date 2019/10/23 0023 下午 3:04
 * @Copyright 小虎互联科技
 * @doc glide图片加载工具类
 * @since 5.0.0
 */
public class GlideUtil {

    /**
     * 默认加载
     * 加载失败图为默认图
     *
     * @param imageView
     * @param url
     */
    public static void loadImg(ImageView imageView, String url) {
        if (imageView.getContext() == null) {
            return;
        }
        if(TextUtils.isEmpty(url)){
            return;
        }
        if (url.contains("https://cdn.tinytiger.cn") && !url.contains("?")){
            url=url+"?imageView2/0/w/500";
        }
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.solid_rectangle_2_f3f3f3)
                .error(R.mipmap.icon_error_down3)
                .into(imageView);
    }

    /**
     * 默认加载
     * 加载失败图为默认图
     * 带圆角
     */
    public static void loadImg(ImageView imageView, String url,Float circular) {
        if (imageView.getContext() == null) {
            return;
        }
        if(TextUtils.isEmpty(url)){
            return;
        }
        if (url.contains("https://cdn.tinytiger.cn") && !url.contains("?")){
            url=url+"?imageView2/0/w/500";
        }
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.solid_rectangle_2_f3f3f3)
                .error(R.mipmap.icon_error_down3)
                .apply(new RequestOptions().transform(
                               new RoundedCorners(ScreenUtil.dp2px(imageView.getContext(), circular))
                        )
                )
                .into(imageView);
    }


    /**
     * 头像加载控件
     * @param imageView
     * @param url
     */
    public static void loadLogo(ImageView imageView, String url) {
        if (imageView.getContext() == null) {
            return;
        }
        if (url.contains("https://cdn.tinytiger.cn") && !url.contains("?")){
            url=url+"?imageView2/1/w/200/h/200";
        }
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.solid_rectangle_2_f3f3f3)
                .error(R.mipmap.icon_error_down3)
                .into(imageView);
    }


    public static void loadImgs(ImageView imageView, String url) {
        if (imageView.getContext() == null) {
            return;
        }
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.solid_rectangle_2_f3f3f3)
                .error(R.mipmap.icon_error_down3)
                .override(650, 380)
                .into(imageView);
    }

    /**
     * 不压缩图片显示
     * @param imageView
     * @param url
     */
    public static void loadImgUrl(ImageView imageView, String url) {
        if (imageView.getContext() == null) {
            return;
        }

        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.solid_rectangle_2_f3f3f3)
                .error(R.mipmap.icon_error_down3)
                .into(imageView);


    }

    /**
     * 默认加载
     * 设置加载失败效果图
     *
     * @param url
     * @param imageView
     * @param error     加载失败效果图
     */
    public static void loadImg(ImageView imageView, String url, int error) {
        if (imageView.getContext() == null) {
            return;
        }
        if (url.contains("https://cdn.tinytiger.cn") && !url.contains("?")){
            url=url+"?imageView2/0/w/200/h/200";
        }
        Glide.with(imageView.getContext())
                .load(url)
                .apply(new RequestOptions()
                        .error(error)
                        .placeholder(R.drawable.solid_rectangle_2_f3f3f3)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .dontAnimate())
                .into(imageView);
    }

    /**
     * 加载本地图片资源
     * 加载失败图为默认图
     *
     * @param imageView
     * @param localPath 本地路径
     */
    public static void loadLocalImg(ImageView imageView, String localPath) {
        if (imageView.getContext() == null) {
            return;
        }
        Glide.with(imageView.getContext())
                .load(localPath)
                .placeholder(R.drawable.solid_rectangle_2_f3f3f3)
                .error(R.mipmap.icon_error_down3)
                .into(imageView);
    }


    /**
     * 清理内存缓存可以在UI主线程中进行
     */
    public static void GuideClearMemory(Context mContext) {
        Glide.get(mContext).clearMemory();
    }

    /**
     * 清除图片磁盘缓存
     * 清理磁盘缓存需要在子线程中执行
     */
    public void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public Long getCacheSize() {
        try {
            return FileUtils.getFolderSize(new File(BaseApp._instance.getCacheDir() + "/cache"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (long) 0;
    }




    /**
     * 获取视频幁
     *
     * @param uri             地址
     * @param imageView
     * @param frameTimeMicros 获取幁画面时间 ms
     */
    public static void loadVideoScreenshot(String uri, final ImageView imageView, long frameTimeMicros) {
        RequestOptions requestOptions = RequestOptions.frameOf(frameTimeMicros);
        requestOptions.set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST);
        requestOptions.transform(new BitmapTransformation() {
            @Override
            protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                return toTransform;
            }

            @Override
            public void updateDiskCacheKey(MessageDigest messageDigest) {
                try {
                    messageDigest.update((imageView.getContext().getPackageName() + "RotateTransform").getBytes("utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Glide.with(imageView.getContext()).load(uri).apply(requestOptions).into(imageView);
    }

}
