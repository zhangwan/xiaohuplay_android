package com.tinytiger.common.event;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yvan on 2018/3/19.
 */
public class ShareEvent implements Parcelable {

    /**
     * 页面分享标识
     */
    public String class_name;

    public String share_url;
    public String share_title;
    public String share_desc;
    public String share_image;
    public Bitmap shareBitmapImage;

    /**
     * 分享路径
     * WEIXIN 微信网页
     * WEIXIN_CIRCLE 朋友圈
     * QQ qq
     * QQZONE qq空间
     * Copy复制
     * Callback 分享回调
     */
    public String type;

    /**
     * 内容id
     */
    public String contentId;

    public ShareEvent(Bitmap bitmap,String type){
        this.shareBitmapImage=bitmap;
        this.type=type;
    }

    public ShareEvent(String share_url, String share_title, String share_desc, String share_image) {
        this.share_url = share_url;
        this.share_title = share_title;
        this.share_desc = share_desc;
        this.share_image = share_image;
    }
    public ShareEvent(String class_name, String share_url, String share_title, String share_desc, String share_image, String type) {
        this.class_name = class_name;

        this.share_url = share_url;
        this.share_title = share_title;
        this.share_desc = share_desc;
        this.share_image = share_image;
        this.type = type;
    }
    public ShareEvent(String class_name, String share_url, String share_title, String share_desc, String share_image,String contentId,  String type) {
        this.class_name = class_name;

        this.share_url = share_url;
        this.share_title = share_title;
        this.share_desc = share_desc;
        this.share_image = share_image;
        this.contentId = contentId;
        this.type = type;
    }

    protected ShareEvent(Parcel in) {
        class_name = in.readString();
        share_url = in.readString();
        share_title = in.readString();
        share_desc = in.readString();
        share_image = in.readString();
        type = in.readString();
        contentId = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(class_name);
        dest.writeString(share_url);
        dest.writeString(share_title);
        dest.writeString(share_desc);
        dest.writeString(share_image);
        dest.writeString(type);
        dest.writeString(contentId);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShareEvent> CREATOR = new Creator<ShareEvent>() {
        @Override
        public ShareEvent createFromParcel(Parcel in) {
            return new ShareEvent(in);
        }

        @Override
        public ShareEvent[] newArray(int size) {
            return new ShareEvent[size];
        }
    };
}
