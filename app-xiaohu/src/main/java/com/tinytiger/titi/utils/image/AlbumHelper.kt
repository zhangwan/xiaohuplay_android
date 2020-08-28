package com.tinytiger.titi.utils.image

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.tinytiger.common.utils.permission.DefaultRationale
import com.tinytiger.common.utils.permission.PermissionSetting
import com.tinytiger.titi.R
import com.yanzhenjie.album.Action
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.api.widget.Widget
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission

/**
 * @author lmq001
 * @date 2020/06/19 18:58
 * @copyright 小虎互联科技
 * @doc
 */
class AlbumHelper {

    /**
     * 获取本地图片,或者视频
     *
     * @param openType 0-选择图片，1-选择视频，2-更换封面
     * @param picSize 图片张数
     */
    fun openGallery(
        context: Context, openType: Int, picSize: Int, videoResult: Action<ArrayList<AlbumFile>>,
        picResult: Action<ArrayList<AlbumFile>>
    ) {
        AndPermission.with(context)
            .runtime()
            .permission(Permission.Group.STORAGE)
            .rationale(DefaultRationale())
            .onGranted {
                if (openType == 1) {
                    Album.video(context)
                        .multipleChoice()
                        .camera(true)
                        .selectCount(1)
                        .columnCount(3)
                        .checkedList(ArrayList())
                        .widget(
                            Widget.newLightBuilder(context)
                                .statusBarColor(Color.WHITE)
                                .title("选择视频")
                                .toolBarColor(Color.WHITE)
                                .mediaItemCheckSelector(
                                    Color.WHITE,
                                    ContextCompat.getColor(context, R.color.color_ffcc03)
                                )
                                .bucketItemCheckSelector(
                                    Color.GRAY,
                                    ContextCompat.getColor(context, R.color.color_ffcc03)
                                )
                                .build()
                        )
                        .filterSize { it > 500 * 1024 * 1024 }
                        //.filterMimeType() // Filter file format.
                        //.afterFilterVisibility() // Show the filtered files, but they are not available.
                        .onResult(videoResult)
                        .onCancel { }
                        .start()
                } else {
                    Album.image(context)
                        .multipleChoice()
                        .camera(true)
                        .columnCount(3)
                        .selectCount(if (openType == 0) picSize else 1)
                        .checkedList(ArrayList())
                        .widget(
                            Widget.newLightBuilder(context)
                                .statusBarColor(Color.WHITE)
                                .title("选择图片")
                                .toolBarColor(Color.WHITE)
                                .mediaItemCheckSelector(
                                    Color.WHITE,
                                    ContextCompat.getColor(context, R.color.color_ffcc03)
                                )
                                .bucketItemCheckSelector(
                                    Color.GRAY,
                                    ContextCompat.getColor(context, R.color.color_ffcc03)
                                )
                                .build()
                        )
                        .filterSize { it > 10 * 1024 * 1024 }
                        //.filterMimeType() // Filter file format.
                        //.afterFilterVisibility() // Show the filtered files, but they are not available.
                        .onResult(picResult)
                        .onCancel { }
                        .start()
                }
            }
            .onDenied { permissions ->
                if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                    PermissionSetting().showSettingDialog(context, permissions)
                }
            }
            .start()
    }
}