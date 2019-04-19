package com.lib.core.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Description : 图片加载工具类 使用glide框架封装
 */
public class ImageLoaderUtils {


    public static void display(Context context, ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    /**
     * 不使用缓存
     *
     * @param context
     * @param imageView
     * @param url
     */
    public static void displaysNoCache(Context context, ImageView imageView, String url) {
        RequestOptions options = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context).asBitmap()
                .apply(options)
                .load(url)
                .into(imageView);
    }

    public static void display(Context context, ImageView imageView, File url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    public static void display(Context context, ImageView imageView, int url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    public static void display(Context context, ImageView imageView, byte[] byteArr) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context)
                .load(byteArr)
                .into(imageView);
    }

    public static void displayGif(Context context, ImageView imageView, int url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).asGif()
                .load(url)
                .into(imageView);
    }

    public static void displayRound(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }

    public static void displayRound(Context context, ImageView imageView, int resId) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context)
                .load(resId)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }

    public static void displayCorners(Context context, int radius, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(radius, 0,
                        RoundedCornersTransformation.CornerType.ALL)))
                .into(imageView);
    }

    public static void displayCorners(Context context, int radius, ImageView imageView, int resId) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context)
                .load(resId)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(radius, 0,
                        RoundedCornersTransformation.CornerType.ALL)))
                .into(imageView);
    }

}
