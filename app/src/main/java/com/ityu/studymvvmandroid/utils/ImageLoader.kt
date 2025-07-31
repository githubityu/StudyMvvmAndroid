package com.ityu.studymvvmandroid.utils



import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ityu.studymvvmandroid.R

object ImageLoader {
    /**
     * 加载普通图片
     */
    fun load(context: Context, url: Any?, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.accept_call)
            .error(R.drawable.accept_call)
            .transition(DrawableTransitionOptions.withCrossFade()) // 淡入淡出动画
            .into(imageView)
    }

    /**
     * 加载圆形图片
     */
    fun loadCircle(context: Context, url: Any?, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.accept_call)
            .error(R.drawable.accept_call)
            .circleCrop() // 应用圆形裁剪
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    /**
     * 加载圆角图片
     * @param radius 圆角半径 (单位：dp, Glide内部会自动转换)
     */
    fun loadRoundedCorners(context: Context, url: Any?, imageView: ImageView, radius: Int) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.accept_call)
            .error(R.drawable.accept_call)
            .transform(RoundedCorners(radius)) // 应用圆角裁剪
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
}