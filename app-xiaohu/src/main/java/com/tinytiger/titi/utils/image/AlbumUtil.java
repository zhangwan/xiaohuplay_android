package com.tinytiger.titi.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.webkit.URLUtil;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.yanzhenjie.album.util.AlbumUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * @author lmq001
 * @date 2020/06/17 14:55
 * @copyright 小虎互联科技
 * @doc
 */
public class AlbumUtil {

    /**
     * 获取视频的第一帧路径
     *
     * @param videoPath video path.
     * @return thumbnail path.
     */
    @WorkerThread
    @Nullable
    public static String createThumbnailForVideo(Context context, String videoPath) {
        if (TextUtils.isEmpty(videoPath)) return null;

        File thumbnailFile = randomPath(context, videoPath);
        if (thumbnailFile.exists()) return thumbnailFile.getAbsolutePath();

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (URLUtil.isNetworkUrl(videoPath)) {
                retriever.setDataSource(videoPath, new HashMap<String, String>());
            } else {
                retriever.setDataSource(videoPath);
            }
            Bitmap bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            thumbnailFile.createNewFile();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(thumbnailFile));
            return thumbnailFile.getAbsolutePath();
        } catch (Exception ignored) {
            return null;
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static File randomPath(Context context, String filePath) {
        File mCacheDir = AlbumUtils.getAlbumRootPath(context);
        if (mCacheDir.exists() && mCacheDir.isFile()) mCacheDir.delete();
        if (!mCacheDir.exists()) mCacheDir.mkdirs();
        String outFilePath = AlbumUtils.getMD5ForString(filePath) + ".png";
        return new File(mCacheDir, outFilePath);
    }



}
