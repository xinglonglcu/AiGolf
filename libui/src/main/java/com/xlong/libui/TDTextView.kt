package com.xlong.libui

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView

class TDTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = -1) : AppCompatTextView(context, attrs, defStyleAttr) {
    private var isBold = false
    private fun getAttrs(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TDTextView)
        isBold = ta.getBoolean(R.styleable.TDTextView_tv_bold, false)
        ta.recycle()
        if (isBold) {
            paint.isAntiAlias = true
            paint.isFakeBoldText = true
        }
        includeFontPadding = false
    }

    fun setShape(shape: Int) {
        this.shapeMaker.setShape(shape).apply()
    }

    fun setStroke(stroke: Int) {
        this.shapeMaker.setStroke(stroke).apply()
    }

    fun setBold(isBold: Boolean) {
        if (isBold) {
            paint.isAntiAlias = true
        }
        paint.isFakeBoldText = isBold
        postInvalidate()
    }

    fun setRadius(radius: Float) {
        shapeMaker.setRadius(radius).apply()
    }

    fun setSolidColor(solidColor: Int) {
        shapeMaker.setSolidColor(solidColor).apply()
    }

    fun setSolidAndStrokeColor(solidColor: Int, storkeColor: Int) {
        shapeMaker.setSolidColor(solidColor).setStrokeColor(storkeColor).apply()
    }

    fun setStrokeColor(storkeColor: Int) {
        shapeMaker.setStrokeColor(storkeColor).apply()
    }

    fun setRippleColor(@ColorInt color: Int) {
        shapeMaker.setRippleColor(color).apply()
    }

    fun setBackgroundSource(backgroundSource: Int) {
        shapeMaker.setTDBackground(backgroundSource).apply()
    }

    fun setGradientColor(@ColorInt startColor: Int, @ColorInt endColor: Int, orientation: GradientDrawable.Orientation) {
        shapeMaker.setGradientStartColor(startColor)
        shapeMaker.setGradientEndColor(endColor)
        shapeMaker.setGradientOrientation(orientation)
        shapeMaker.apply()
    }

    val shapeMaker = ViewShapeMaker(this, attrs)

    init {
        attrs?.let { getAttrs(context, it) }
    }
}