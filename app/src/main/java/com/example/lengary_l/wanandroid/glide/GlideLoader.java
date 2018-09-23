package com.example.lengary_l.wanandroid.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by CoderLengary
 */


public class GlideLoader extends com.example.lengary_l.wanandroid.CustomComponent.banner.interfaze.ImageLoader{
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }
}
