package com.tinytiger.common.utils.image;


import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.bumptech.glide.Glide;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author zhw_luke
 * @date 2019/12/26 0026 上午 11:53
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 下载图片,保存到图库
 */
public class DonwloadSaveImg {

    private static Context context;
    private static String filePath;
    private static Uri fileUri;
    public static void donwloadImg(Context contexts, String filePaths) {
        context = contexts;
        filePath = filePaths;
        new Thread(saveFileRunnable).start();
    }

    public static void donwloadImg(Context contexts, Uri filePaths) {
        context = contexts;
        fileUri = filePaths;
        new Thread(saveFileRunnable).start();
    }

    private static Runnable saveFileRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Bitmap bit =null;
                if (TextUtils.isEmpty(filePath)){
                     bit = Glide.with(context).asBitmap().load(fileUri).submit()
                            .get(500, TimeUnit.MILLISECONDS);
                }else {
                     bit = Glide.with(context).asBitmap().load(filePath).submit()
                            .get(500, TimeUnit.MILLISECONDS);
                }

                saveFile(bit);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 保存图片到相册
     * @param bm
     * @throws IOException
     */
    public static void saveFile(Bitmap bm ) throws IOException {
        String fileName = System.currentTimeMillis() + ".jpg";
        File myCaptureFile = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + fileName);

        /*String myCaptureFile = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator+fileName;*/


        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        //把图片保存后声明这个广播事件通知系统相册有新图片到来
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(myCaptureFile);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }


}
