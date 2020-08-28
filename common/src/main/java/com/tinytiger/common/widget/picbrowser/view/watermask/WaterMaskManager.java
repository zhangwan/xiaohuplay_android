package com.tinytiger.common.widget.picbrowser.view.watermask;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RelativeLayout;

import com.tinytiger.common.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lmq001
 * @date 2020/07/27 11:07
 * @copyright 小虎互联科技
 * @doc 水印管理类
 */
public class WaterMaskManager {
    private final static int LEFT_TOP = 0;
    private final static int RIGHT_TOP = 1;
    private final static int RIGHT_BOTTOM = 2;
    private final static int LEFT_BOTTOM = 3;

    private static Bitmap watermarkBitmap;
    private static Bitmap sourBitmap;
    private static Bitmap waterBitmap;
    private static List<String> savePathList = new ArrayList<>();

    /**
     * 给图片加水印效果
     */
    public static String getWaterMask(Context context, String srcPath) {
        return getWaterMask(context, RIGHT_BOTTOM, srcPath);
    }

    /**
     * 给图片加水印效果
     *
     * @param position 左上为0，顺时针算起
     */
    public static String getWaterMask(Context context, int position, String srcPath) {
        try {
            //图片太大，需要进行采样压缩
            sourBitmap = FileUtils.getCompressBitmap(srcPath, 1080, 1920);
        } catch (Exception e) {
            e.printStackTrace();
            return srcPath;
        }

        WaterMaskView waterMaskView = new WaterMaskView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        waterMaskView.setLayoutParams(params);

        waterBitmap = WaterMaskUtil.convertViewToBitmap(waterMaskView);

        //根据原图处理要生成的水印的宽高
        int width = sourBitmap.getWidth() * 115 / 375;
        int height = (int) (width * 46 / 115);
        waterBitmap = WaterMaskUtil.zoomBitmap(waterBitmap, width, height);

        //根据原图处理要生成的水印的宽高 (原方法)
//        float width = sourBitmap.getWidth();
//        float height = sourBitmap.getHeight();
//        float be = width / height;
//        if ((float) 16 / 9 >= be && be >= (float) 4 / 3) {
//            //在图片比例区间内16;9~4：3内，将生成的水印bitmap设置为原图宽高各自的1/5
//            waterBitmap = WaterMaskUtil.zoomBitmap(waterBitmap, (int) width / 5, (int) height / 5);
//        } else if (be > (float) 16 / 9) {
//            //生成4：3的水印
//            waterBitmap = WaterMaskUtil.zoomBitmap(waterBitmap, (int) width / 5, (int) width * 3/ 20);
//        } else if (be < (float) 4 / 3) {
//            //生成4：3的水印
//            waterBitmap = WaterMaskUtil.zoomBitmap(waterBitmap, (int) height * 4 / 15, (int) height / 5);
//        }

        switch (position) {
            case LEFT_TOP:
                watermarkBitmap = WaterMaskUtil.createWaterMaskLeftTop(context, sourBitmap, waterBitmap, 0, 0);
                break;
            case RIGHT_TOP:
                watermarkBitmap = WaterMaskUtil.createWaterMaskRightTop(context, sourBitmap, waterBitmap, 0, 0);
                break;
            case RIGHT_BOTTOM:
                watermarkBitmap = WaterMaskUtil.createWaterMaskRightBottom(context, sourBitmap, waterBitmap, 0, 0);
                break;
            case LEFT_BOTTOM:
                watermarkBitmap = WaterMaskUtil.createWaterMaskLeftBottom(context, sourBitmap, waterBitmap, 0, 0);
                break;
        }

        //缓存的新路径用于保存水印图
        String startPath = srcPath.substring(0, srcPath.lastIndexOf("/") + 1);
        String endPath = srcPath.substring(srcPath.lastIndexOf("/") + 1);
        String savePath = startPath + "titi" + endPath;
        savePathList.add(savePath);

        return FileUtils.saveBitmap(watermarkBitmap, savePath);
    }

    /**
     * 资源回收
     */
    public static void recycle() {
        if (sourBitmap != null) {
            sourBitmap.recycle();
            sourBitmap = null;
        }
        if (waterBitmap != null) {
            waterBitmap.recycle();
            waterBitmap = null;
        }
        if (savePathList != null && savePathList.size() > 0) {
            for (int i = 0; i < savePathList.size(); i++) {
                if (savePathList.get(i) != null) {
                    //删除水印图片，减少项目无用图片大小
                    FileUtils.DeleteFile(new File(savePathList.get(i)));
                }
            }
            //清空
            savePathList.clear();
        }

    }

}
