package com.xlong.libui.imagload

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.ImageView
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import java.io.File

/**
 * Create by xfchen
 * on 2019-10-23 09:53
 */
class ImageLoaderBuilder {

    private var requestManager: RequestManager? = null
    private var requestBuilder: RequestBuilder<Drawable>? = null
    private var requestBuilderBitmap: RequestBuilder<Bitmap>? = null
    private var requestBuilderGif: RequestBuilder<GifDrawable>? = null
    private var requestOptions: RequestOptions? = null
    private var mActivity: Activity? = null

    companion object {
        private val TAG: String = "ImageLoaderBuilder"
    }

    constructor(activity: Activity) {
        mActivity = activity
        if (isActivityAlive(activity)) {
            requestManager = Glide.with(activity)
        } else {
            Log.d(TAG, "activity is finish")
        }
    }

    constructor(context: Context) {
        mActivity = getActivityByContext(context)
        if (mActivity != null) {
            if (isActivityAlive(mActivity)) {
                requestManager = Glide.with(mActivity!!)
            } else {
                Log.d(TAG, "activity is finish")
            }
        }
    }

    fun load(url: String?): ImageLoaderBuilder {
        requestBuilder = requestManager?.load(url)
        listener(url)
        return this
    }

    fun loadAsGif(url: String?): ImageLoaderBuilder {
        requestBuilderGif = requestManager?.asGif()?.load(url)
        listenerAsGif(url)
        return this
    }

    fun loadAsBitmap(url: String?): ImageLoaderBuilder {
        requestBuilderBitmap = requestManager?.asBitmap()?.load(url)
        listenerAsBitmap(url)
        return this
    }

    fun loadAsBitmap(file: File?): ImageLoaderBuilder {
        requestBuilderBitmap = requestManager?.asBitmap()?.load(file)
        return this
    }

    fun loadAsBitmap(source: Int?): ImageLoaderBuilder {
        requestBuilderBitmap = requestManager?.asBitmap()?.load(source)
        return this
    }

    fun load(file: File?): ImageLoaderBuilder {
        requestBuilder = requestManager?.load(file)
        return this
    }

    fun load(url: Uri?): ImageLoaderBuilder {
        requestBuilder = requestManager?.load(url)
        return this
    }

    fun load(source: Int?): ImageLoaderBuilder {
        requestBuilder = requestManager?.load(source)
        return this
    }

    fun listener(listener: RequestListener<Drawable>): ImageLoaderBuilder {
        requestBuilder = requestBuilder?.listener(listener)
        return this
    }

    fun thumbnail(sizeMultiplier: Float?): ImageLoaderBuilder {
        if (requestBuilder == null) {
            return this
        }
        sizeMultiplier?.let { requestBuilder?.thumbnail(it) }
        return this
    }

    fun thumbnailAsBitmap(sizeMultiplier: Float?): ImageLoaderBuilder {
        if (requestBuilderBitmap == null) {
            return this
        }
        sizeMultiplier?.let { requestBuilderBitmap?.thumbnail(it) }
        return this
    }

    fun crossFade(): ImageLoaderBuilder {
        if (requestBuilder == null) {
            return this
        }
        requestBuilder?.transition(DrawableTransitionOptions().crossFade())
        return this
    }

    fun into(@NonNull target: SimpleTarget<Drawable>) {
        requestBuilder?.into(target)
    }

    fun into(iv: ImageView?) {
        if (requestBuilder == null) {
            return
        }
        createOptions()
        iv?.let { requestBuilder?.into(it) }
    }

    fun intoAsGif(iv: ImageView?): ImageLoaderBuilder {
        if (requestBuilderGif == null) {
            return this
        }
        createOptions()
        iv?.let { requestBuilderGif?.into(it) }
        return this
    }

    fun intoAsBitmap(iv: ImageView?): ImageLoaderBuilder {
        if (requestBuilderBitmap == null) {
            return this
        }
        createOptions()
        iv?.let { requestBuilderBitmap?.into(it) }
        return this
    }

    fun intoAsBitmap(iAsBitmap: IAsBitmap?, width: Int, height: Int): ImageLoaderBuilder {
        if (requestBuilderBitmap == null) {
            return this
        }
        createOptions()
        requestBuilderBitmap?.into(object : SimpleTarget<Bitmap>(width, height) {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                iAsBitmap?.onResourceReady(resource)
            }
        })
        return this
    }

    fun intoAsDrawable(iAsDrawable: IAsDrawable?): ImageLoaderBuilder {
        if (requestBuilder == null) {
            return this
        }
        createOptions()
        requestBuilder?.listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                iAsDrawable?.onLoadFail()
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                iAsDrawable?.onResourceReady(resource)
                return false
            }
        })
        return this
    }

    //列表显示图片尽量别用asBitmap，容易造成内存溢出
    fun intoAsBitmap(iAsBitmap: IAsBitmap?): ImageLoaderBuilder {
        if (requestBuilderBitmap == null) {
            return this
        }
        createOptions()
        requestBuilderBitmap?.into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                iAsBitmap?.onResourceReady(resource)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                if (iAsBitmap is IAsBitmapWithError) {
                    iAsBitmap.onLoadFail()
                }
            }
        })
        return this
    }

    fun preload(): ImageLoaderBuilder {
        if (requestBuilder == null) {
            return this
        }
        requestBuilder?.preload()
        return this
    }

    fun dontAnimate(): ImageLoaderBuilder {
        if (requestOptions == null) {
            requestOptions = RequestOptions()
        }
        requestOptions?.dontAnimate()
        return this
    }

    fun overrideWH(width: Int, height: Int): ImageLoaderBuilder {
        if (requestOptions == null) {
            requestOptions = RequestOptions()
        }
        requestOptions?.override(width, height)
        return this
    }

    fun optionalCenterCrop(): ImageLoaderBuilder {
        if (requestOptions == null) {
            requestOptions = RequestOptions()
        }
        requestOptions?.optionalCenterCrop()
        return this
    }

    fun optionalFitCenter(): ImageLoaderBuilder {
        if (requestOptions == null) {
            requestOptions = RequestOptions()
        }
        requestOptions?.optionalFitCenter()
        return this
    }

    fun diskCacheStrategyNone(): ImageLoaderBuilder {
        if (requestOptions == null) {
            requestOptions = RequestOptions()
        }
        requestOptions?.diskCacheStrategy(DiskCacheStrategy.NONE)
        return this
    }

    fun diskCacheStrategyData(): ImageLoaderBuilder {
        if (requestOptions == null) {
            requestOptions = RequestOptions()
        }
        requestOptions?.diskCacheStrategy(DiskCacheStrategy.DATA)
        return this
    }

    fun placeholder(placeholderResID: Int): ImageLoaderBuilder {
        if (requestOptions == null) {
            requestOptions = RequestOptions()
        }
        requestOptions?.placeholder(placeholderResID)
        return this
    }

    fun error(errorResID: Int): ImageLoaderBuilder {
        if (requestOptions == null) {
            requestOptions = RequestOptions()
        }
        requestOptions?.error(errorResID)
        return this
    }

    fun circleTransform(): ImageLoaderBuilder {
        if (requestOptions == null) {
            requestOptions = RequestOptions()
        }
        if (isActivityAlive(mActivity)) {
            requestOptions?.transforms(GlideCircleTransform(mActivity))?.skipMemoryCache(true)
        }
        return this
    }

    fun circleTransform(borderWidth: Int, borderColor: Int): ImageLoaderBuilder {
        if (requestOptions == null) {
            requestOptions = RequestOptions()
        }
        if (isActivityAlive(mActivity)) {
            requestOptions?.transforms(GlideCircleWithBorderTransform(mActivity, borderWidth, borderColor))
        }
        return this
    }

    fun roundTransform(): ImageLoaderBuilder {
        if (requestOptions == null) {
            requestOptions = RequestOptions()
        }
        if (isActivityAlive(mActivity)) {
            requestOptions?.transforms(GlideRoundTransform(mActivity))
        }
        return this
    }

    fun roundTransform(round: Int): ImageLoaderBuilder {
        if (requestOptions == null) {
            requestOptions = RequestOptions()
        }
        if (isActivityAlive(mActivity)) {
            requestOptions?.transforms(GlideRoundTransform(mActivity, round))
        }
        return this
    }

    fun roundTransform(round: Int, cornerType: RoundedCornersTransformation.CornerType): ImageLoaderBuilder {
        if (requestOptions == null) {
            requestOptions = RequestOptions()
        }
        requestOptions?.transforms(RoundedCornersTransformation(round, 0, cornerType))
        return this
    }

    fun cornerTransform(cornerRadius: Float, corners: Int = Corners.CORNER_ALL): ImageLoaderBuilder {
        if (requestOptions == null) {
            requestOptions = RequestOptions()
        }
        requestOptions?.transforms(CustomRoundedCorners(cornerRadius, corners))
        return this
    }

    fun roundTopTransform(round: Int): ImageLoaderBuilder {
        if (requestOptions == null) {
            requestOptions = RequestOptions()
        }
        if (isActivityAlive(mActivity)) {
            requestOptions?.transforms(GlideRoundTopTransform(mActivity, round))
        }
        return this
    }

    private fun createOptions() {
        if (requestOptions == null) {
            return
        }
        if (requestBuilder != null) {
            requestOptions?.let { requestBuilder?.apply(it) }
        }
        if (requestBuilderBitmap != null) {
            requestOptions?.let { requestBuilderBitmap?.apply(it) }
        }
    }

    private fun listener(url: String?): ImageLoaderBuilder {
        if (requestBuilder == null) {
            return this
        }
        requestBuilder?.listener(requestListener(url))
        return this
    }

    private fun listenerAsGif(url: String?): ImageLoaderBuilder {
        if (requestBuilderGif == null) {
            return this
        }
        requestBuilderGif?.listener(requestListenerAsGif(url))
        return this
    }

    private fun requestListenerAsGif(url: String?): RequestListener<GifDrawable> {
        return object : RequestListener<GifDrawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                return false
            }
        }
    }

    private fun listenerAsBitmap(url: String?): ImageLoaderBuilder {
        if (requestBuilderBitmap == null) {
            return this
        }
        requestBuilderBitmap?.listener(requestListenerAsBitmap(url))
        return this
    }

    private fun requestListenerAsBitmap(url: String?): RequestListener<Bitmap> {
        return object : RequestListener<Bitmap> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                return false
            }
        }
    }

    private fun requestListener(url: String?): RequestListener<Drawable> {
        return object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                return false
            }
        }
    }

    interface IAsBitmap {
        fun onResourceReady(resource: Bitmap?)
    }

    interface IAsBitmapWithError : IAsBitmap {
        fun onLoadFail()
    }

    interface IAsDrawable {
        fun onResourceReady(resource: Drawable?)

        fun onLoadFail()
    }

    /**
     * Return whether the activity is alive.
     *
     * @param activity The activity.
     * @return `true`: yes<br></br>`false`: no
     */
    private fun isActivityAlive(activity: Activity?): Boolean {
        return (activity != null && !activity.isFinishing && (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 || !activity.isDestroyed))
    }

    private fun getActivityByContext(context: Context): Activity? {
        var context = context
        if (context is Activity) return context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }
}
