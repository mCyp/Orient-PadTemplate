package com.orient.padtemplate.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Glide工具类
 * <p>
 * Author WangJie
 * Created on 2019/7/26.
 */
public class GlideUtils {

    public static void loadUrl(Context context, String url, final ImageView imageView) {
        Glide.with(context).load(url)
                .asBitmap()
                .centerCrop()
                .into(imageView);
    }

    public static void loadResource(Context context, int resource, final ImageView imageView) {
        Glide.with(context).load(resource)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(imageView);
    }

}
