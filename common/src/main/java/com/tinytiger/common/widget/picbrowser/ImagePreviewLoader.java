package com.tinytiger.common.widget.picbrowser;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.tinytiger.common.R;
import com.tinytiger.common.widget.picbrowser.view.listener.OnBigImageClickListener;

import java.util.List;

/**
 * @author lmq001
 * @date 2020/06/08 11:43
 * @copyright 小虎互联科技
 * @doc
 */
public class ImagePreviewLoader {


    public static void showImagePreview(Context context, int position, List<String> imageList) {
        ImagePreview.getInstance()
                // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好
                .setContext(context)
                // 从第几张图片开始，索引从0开始哦~
                .setIndex(position)
                //=================================================================================================
                // 有三种设置数据集合的方式，根据自己的需求进行三选一：
                // 1：第一步生成的imageInfo List
//                .setImageInfoList(imageInfoList)
                // 2：直接传url List
                .setImageList(imageList)
                // 3：只有一张图片的情况，可以直接传入这张图片的url
                //.setImage(String image)
                //=================================================================================================
                // 加载策略，默认为手动模式
                .setLoadStrategy(ImagePreview.LoadStrategy.AlwaysOrigin)
                // 保存的文件夹名称，会在Picture目录进行文件夹的新建。比如："BigImageView"，会在Picture目录新建BigImageView文件夹)
                .setFolderName("titiImageView")
                // 缩放动画时长，单位ms
                .setZoomTransitionDuration(300)
                // 是否显示加载失败的Toast
                .setShowErrorToast(true)
                // 是否启用点击图片关闭。默认启用
                .setEnableClickClose(true)
                // 是否启用下拉关闭。默认不启用
                .setEnableDragClose(true)
                // 是否启用上拉关闭。默认不启用
                .setEnableUpDragClose(true)
                // 是否显示关闭页面按钮，在页面左下角。默认不显示
//                .setShowCloseButton(false)
//                // 设置关闭按钮图片资源，可不填，默认为库中自带：R.drawable.ic_action_close
//                .setCloseIconResId(R.mipmap.ic_action_close)
//                // 是否显示下载按钮，在页面右下角。默认显示
//                .setShowDownButton(true)
//                // 设置下载按钮图片资源，可不填，默认为库中自带：R.drawable.icon_download_new
//                .setDownIconResId(R.mipmap.icon_download_new)
//                // 设置是否显示顶部的指示器（1/9）默认显示
//                .setShowIndicator(true)
                // 设置顶部指示器背景shape，默认自带灰色圆角shape，设置为 0 时不显示
                .setIndicatorShapeResId(0)
                // 设置失败时的占位图，默认为库中自带R.drawable.load_failed，设置为 0 时不显示
                .setErrorPlaceHolder(R.mipmap.icon_error_down3)
                // 点击回调
                .setBigImageClickListener(new OnBigImageClickListener() {
                    @Override
                    public void onClick(Activity activity, View view, int position) {

                    }
                })
                //=================================================================================================
//                // 设置查看原图时的百分比样式：库中带有一个样式：ImagePreview.PROGRESS_THEME_CIRCLE_TEXT，使用如下：
//                .setProgressLayoutId(ImagePreview.PROGRESS_THEME_CIRCLE_TEXT, new OnOriginProgressListener() {
//                    @Override
//                    public void progress(View parentView, int progress) {
//                        // 需要找到进度控件并设置百分比，回调中的parentView即传入的布局的根View，可通过parentView找到控件：
//                        ProgressBar progressBar = parentView.findViewById(R.id.sh_progress_view);
//                        TextView textView = parentView.findViewById(R.id.sh_progress_text);
//                        progressBar.setProgress(progress);
//                        String progressText = progress + "%";
//                        textView.setText(progressText);
//                    }
//
//                    @Override
//                    public void finish(View parentView) {
//                    }
//                })
                // 开启预览
                .start();
    }


}
