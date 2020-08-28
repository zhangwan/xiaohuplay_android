package com.tinytiger.titi.utils.image;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tinytiger.titi.R;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumLoader;

public class MediaLoader implements AlbumLoader {

    @Override
    public void load(ImageView imageView, AlbumFile albumFile) {
        load(imageView, albumFile.getPath());
    }

    @Override
    public void load(ImageView imageView, String url) {
        if (imageView.getContext() == null) {
            return;
        }
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.solid_rectangle_2_f3f3f3)
                .error(R.mipmap.icon_error_down3)
                .into(imageView);

//        GlideUtil.loadImg(imageView,url);

        /*Glide.with(imageView.getContext())
                .load(url)
                .error(com.tinytiger.common.R.mipmap.icon_error_down3)
                .placeholder(com.tinytiger.common.R.drawable.solid_rectangle_2_f3f3f3)
                .crossFade()
                .into(imageView);*/


    }
}

