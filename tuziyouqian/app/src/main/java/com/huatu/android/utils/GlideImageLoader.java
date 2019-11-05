package com.huatu.android.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.huatu.android.R;
import com.youth.banner.loader.ImageLoader;

/**
 * @author 周竹
 * @file GlideImageLoader
 * @brief
 * @date 2018/4/30 下午6:26
 * Copyright (c) 2017
 * All rights reserved.
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        RequestOptions myOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_banner)
                .error(R.mipmap.ic_banner)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(context)
                .load(path)
                .apply(myOptions)
//                .transition(new DrawableTransitionOptions().crossFade(2000))
//                .thumbnail(Glide.with(context)
//                        .load(path))
                .into(imageView);
    }
}