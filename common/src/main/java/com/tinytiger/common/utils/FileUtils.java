/*
 * Copyright © 2018 Yan Zhenjie.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tinytiger.common.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Yan Zhenjie on 2016/7/7.
 */
public class FileUtils {

    private static final String PIC_PATH = "/titi_pic_browser/";

    public static File getAppRootPath(Context context) {
        if (sdCardIsAvailable()) {
            return Environment.getExternalStorageDirectory();
        } else {
            return context.getFilesDir();
        }
    }

    public static String getSystemImagePath() {
        if (sdCardIsAvailable()) {
            if (Build.VERSION.SDK_INT > 7) {
                String picturePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                return picturePath + "/qiniu_titi/";
            } else {
                String picturePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
                return picturePath + "/qiniu_titi/";
            }
        }
        return new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + "qiniu_titi").getAbsolutePath();
    }

    public static boolean sdCardIsAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sd = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            return sd.canWrite();
        } else {
            return false;
        }
    }

    /**
     * Copy to clipboard.
     */
    public static boolean copyTextToClipboard(Context context, CharSequence content) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            ClipData clipData = ClipData.newPlainText(content, content);
            clipboardManager.setPrimaryClip(clipData);
            return true;
        }
        return false;
    }

    /**
     * 删除文件
     *
     * @param file
     */
    public static void DeleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                file.delete();
            }
        }
    }

    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            if (fileList == null) {
                return 0;
            }

            for (File value : fileList) {
                // 如果下面还有文件
                if (value.isDirectory()) {
                    size = size + getFolderSize(value);
                } else {
                    size = size + value.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static void writeFile(InputStream inputStream, File file) throws IOException {

        FileOutputStream fos = new FileOutputStream(file);
        byte[] b = new byte[1024];
        int len = inputStream.read(b);
        while (len != -1) {
            fos.write(b, 0, len);
            len = inputStream.read(b);
        }
        inputStream.close();
        fos.close();

    }

    /**
     * 保存bitmap到原路径
     *
     * @param mBitmap
     * @return
     */
    public static String saveBitmap(Bitmap mBitmap, String srcPath) {
//        File filePic;
//        filePic = new File(savePath );
//        if (!filePic.exists()) {
//            filePic.getParentFile().mkdirs();
//            filePic.createNewFile();
//        } else {
//            filePic.delete();
//        }
        FileOutputStream fos = null;
        try {
            //替换覆盖原路径
            fos = new FileOutputStream(new File(srcPath));
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        } catch (IOException e) {
            e.printStackTrace();
            return srcPath;
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return srcPath;
    }

    /**
     * 动态采样压缩法，防止图片OOM
     *
     * @param destWidth  显示图片的控件宽度
     * @param destHeight 显示图片的控件的高度
     * @return
     */
    public static Bitmap getCompressBitmap(String srcPath, int destWidth, int destHeight) {
        //第一次采样
        BitmapFactory.Options options = new BitmapFactory.Options();
        //该属性设置为true只会加载图片的边框进来，并不会加载图片具体的像素点
        options.inJustDecodeBounds = true;
        //第一次加载图片，这时只会加载图片的边框进来，并不会加载图片中的像素点
        BitmapFactory.decodeFile(srcPath, options);
        //获得原图的宽和高
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        /********************************************************************************************/
        //至此，第一次采样已经结束，我们已经成功的计算出了sampleSize的大小
        /********************************************************************************************/

        //二次采样开始
        //二次采样时我需要将图片加载出来显示，不能只加载图片的框架，因此inJustDecodeBounds属性要设置为false
        options.inJustDecodeBounds = false;
        //设置缩放比例
        options.inSampleSize = calculateInSampleSize(options, destWidth, destHeight);
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        //加载图片并返回
        return BitmapFactory.decodeFile(srcPath, options);
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            //计算图片高度和我们需要高度的最接近比例值
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            //宽度比例值
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            //取比例值中的较大值作为inSampleSize
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

}
