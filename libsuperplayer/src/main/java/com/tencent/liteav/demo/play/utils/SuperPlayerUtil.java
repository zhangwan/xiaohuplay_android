package com.tencent.liteav.demo.play.utils;


import com.tencent.liteav.demo.play.view.TCVideoQulity;
import com.tencent.rtmp.TXBitrateItem;


/**
 * Created by yuejiaoli on 2018/7/6.
 */

public class SuperPlayerUtil {

    /**
     * 清晰度转换
     *
     * @param bitrateItem
     * @return
     */
    public static TCVideoQulity convertToVideoQuality(TXBitrateItem bitrateItem, int index) {
        TCVideoQulity quality = new TCVideoQulity();
        quality.bitrate = bitrateItem.bitrate;
        quality.index = bitrateItem.index;
//320:240,640:480,1280:720,1920:1080
        switch (index) {
            case 0:
                quality.name = "1080p";
                quality.title = "原画";
                break;
            case 1:
                quality.name = "720p";
                quality.title = "高清";

                break;
            case 2:
                quality.name = "480p";
                quality.title = "标清";
                break;
            case 3:
                quality.name = "360p";
                quality.title = "流畅";
                break;
            default:
                quality.name = "2K";
                quality.title = "全高清";
        }
        return quality;
    }
}
