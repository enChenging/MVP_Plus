package com.release.base.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Created by long on 2016/8/23.
 * 图片加载帮助类
 * 不加 dontAnimate()，有的机型会出现图片变形的情况，先记下找到更好的方法再处理
 */
public final class ImageLoader {


    private ImageLoader() {
        throw new RuntimeException("ImageLoader cannot be initialized!");
    }


    public static void loadFit(Context context, String url, ImageView view, int defaultResId) {
        Glide.with(context).load(url).fitCenter().dontAnimate().placeholder(defaultResId).into(view);
    }

    public static void loadCenterCrop(Context context, String url, ImageView view, int defaultResId) {
        Glide.with(context).load(url).centerCrop().dontAnimate().placeholder(defaultResId).into(view);
    }

    public static void loadFitCenter(Context context, String url, ImageView view, int defaultResId) {
        Glide.with(context).load(url).fitCenter().dontAnimate().placeholder(defaultResId).into(view);
    }

    public static void loadCircleCrop(Context context, String url, ImageView view, int defaultResId) {
        Glide.with(context).load(url).circleCrop().dontAnimate().placeholder(defaultResId).into(view);
    }

    //设置跳过内存缓存
    public static void loadImageViewCache(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).skipMemoryCache(true).into(mImageView);
    }

    //设置缩略图支持
    public static void loadImageViewThumbnail(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).thumbnail(0.1f).into(mImageView);
    }


    /**
     * 带监听处理
     *
     * @param context
     * @param url
     * @param view
     * @param listener
     */
    public static void loadFitCenter(Context context, String url, ImageView view, RequestListener listener) {
        Glide.with(context).load(url).fitCenter().dontAnimate().listener(listener).into(view);
    }

    public static void loadCenterCrop(Context context, String url, ImageView view, RequestListener listener) {
        Glide.with(context).load(url).centerCrop().dontAnimate().listener(listener).into(view);
    }

    /**
     * 设置图片大小处理
     *
     * @param context
     * @param url
     * @param view
     * @param defaultResId
     * @param width
     * @param height
     */
    public static void loadFitOverride(Context context, String url, ImageView view, int defaultResId, int width, int height) {
        Glide.with(context).load(url).fitCenter().dontAnimate().override(width, height).placeholder(defaultResId).into(view);
    }

    /**
     * 计算图片分辨率
     *
     * @param context
     * @param url
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String calePhotoSize(Context context, String url) throws ExecutionException, InterruptedException {
        File file = Glide.with(context).load(url)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        return options.outWidth + "*" + options.outHeight;
    }

    //清理磁盘缓存
    public static void GuideClearDiskCache(Context mContext) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(mContext).clearDiskCache();
    }

    //清理内存缓存
    public static void GuideClearMemory(Context mContext) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(mContext).clearMemory();
    }

}
