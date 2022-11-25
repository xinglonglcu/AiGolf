package com.xlong.libui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.Checkable
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import net.soulwolf.widget.ratiolayout.RatioDatumMode
import net.soulwolf.widget.ratiolayout.RatioLayoutDelegate
import net.soulwolf.widget.ratiolayout.RatioMeasureDelegate

class TDFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) : FrameLayout(context, attrs, defStyleAttr), RatioMeasureDelegate, Checkable,
    RCAttrs {

    val shapeMaker = ViewShapeMaker(this, attrs)
    private var mRatioLayoutDelegate: RatioLayoutDelegate<*>? = null
    private var mRCHelper: RCHelper? = null

    init {
        mRatioLayoutDelegate = RatioLayoutDelegate.obtain(this, attrs, defStyleAttr)
        mRCHelper = RCHelper()
        mRCHelper?.initAttrs(context, attrs)
    }

    fun setSolidAndStrokeColor(solidColor: Int, storkeColor: Int) {
        shapeMaker.setSolidColor(solidColor)
            .setStrokeColor(storkeColor)
            .apply()
    }

    fun setRippleColor(@ColorInt color: Int) {
        shapeMaker.setRippleColor(color)
            .apply()
    }

    fun setBackgroundSource(backgroundSource: Int) {
        shapeMaker.setTDBackground(backgroundSource)
            .apply()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRCHelper?.onSizeChanged(this, w, h)
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.saveLayer(mRCHelper?.mLayer, null, Canvas.ALL_SAVE_FLAG)
        super.dispatchDraw(canvas)
        mRCHelper?.onClipDraw(canvas)
        canvas.restore()
    }

    override fun draw(canvas: Canvas) {
        if (mRCHelper?.mClipBackground == true) {
            canvas.save()
            canvas.clipPath(mRCHelper?.mClipPath!!)
            super.draw(canvas)
            canvas.restore()
        } else {
            super.draw(canvas)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        if (action == MotionEvent.ACTION_DOWN && mRCHelper?.mAreaRegion?.contains(
                ev.x
                    .toInt(), ev.y.toInt()
            ) == false
        ) {
            return false
        }
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_UP) {
            refreshDrawableState()
        } else if (action == MotionEvent.ACTION_CANCEL) {
            isPressed = false
            refreshDrawableState()
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var wSpec = widthMeasureSpec
        var hSpec = heightMeasureSpec
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate?.onMeasure(widthMeasureSpec, heightMeasureSpec)
            wSpec = mRatioLayoutDelegate?.getWidthMeasureSpec()!!
            hSpec = mRatioLayoutDelegate?.getHeightMeasureSpec()!!
        }
        super.onMeasure(wSpec, hSpec)
    }

    override fun setDelegateMeasuredDimension(measuredWidth: Int, measuredHeight: Int) {
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun setRatio(mode: RatioDatumMode?, datumWidth: Float, datumHeight: Float) {
        if (mRatioLayoutDelegate != null) {
            mRatioLayoutDelegate?.setRatio(mode, datumWidth, datumHeight)
        }
    }

    //--- 公开接口 ----------------------------------------------------------------------------------
    override fun setClipBackground(clipBackground: Boolean) {
        mRCHelper?.mClipBackground = clipBackground
        invalidate()
    }

    override fun setRoundAsCircle(roundAsCircle: Boolean) {
        mRCHelper?.mRoundAsCircle = roundAsCircle
        invalidate()
    }

    override fun setRadius(radius: Int) {
        for (i in mRCHelper?.radii?.indices!!) {
            mRCHelper?.radii?.set(i, radius.toFloat())
        }
        invalidate()
    }

    override fun setTopLeftRadius(topLeftRadius: Int) {
        mRCHelper?.radii?.set(0, topLeftRadius.toFloat())
        mRCHelper?.radii?.set(1, topLeftRadius.toFloat())
        invalidate()
    }

    override fun setTopRightRadius(topRightRadius: Int) {
        mRCHelper?.radii?.set(2, topRightRadius.toFloat())
        mRCHelper?.radii?.set(3, topRightRadius.toFloat())
        invalidate()
    }

    override fun setBottomLeftRadius(bottomLeftRadius: Int) {
        mRCHelper?.radii?.set(6, bottomLeftRadius.toFloat())
        mRCHelper?.radii?.set(7, bottomLeftRadius.toFloat())
        invalidate()
    }

    override fun setBottomRightRadius(bottomRightRadius: Int) {
        mRCHelper?.radii?.set(4, bottomRightRadius.toFloat())
        mRCHelper?.radii?.set(5, bottomRightRadius.toFloat())
        invalidate()
    }

    override fun setStrokeWidth(strokeWidth: Int) {
        mRCHelper?.mStrokeWidth = strokeWidth
        invalidate()
    }

    override fun setStrokeColor(strokeColor: Int) {
        mRCHelper?.mStrokeColor = strokeColor
        invalidate()
    }

    override fun invalidate() {
        if (null != mRCHelper) mRCHelper?.refreshRegion(this)
        super.invalidate()
    }

    override fun isClipBackground(): Boolean {
        return mRCHelper?.mClipBackground!!
    }

    override fun isRoundAsCircle(): Boolean {
        return mRCHelper?.mRoundAsCircle!!
    }

    override fun getTopLeftRadius(): Float {
        return mRCHelper?.radii!![0]
    }

    override fun getTopRightRadius(): Float {
        return mRCHelper?.radii!![2]
    }

    override fun getBottomLeftRadius(): Float {
        return mRCHelper?.radii!![4]
    }

    override fun getBottomRightRadius(): Float {
        return mRCHelper?.radii!![6]
    }

    override fun getStrokeWidth(): Int {
        return mRCHelper?.mStrokeWidth!!
    }

    override fun getStrokeColor(): Int {
        return mRCHelper?.mStrokeColor!!
    }


    //--- Selector 支持 ----------------------------------------------------------------------------

    //--- Selector 支持 ----------------------------------------------------------------------------
    override fun drawableStateChanged() {
        super.drawableStateChanged()
        mRCHelper?.drawableStateChanged(this)
    }

    override fun setChecked(checked: Boolean) {
        if (mRCHelper?.mChecked != checked) {
            mRCHelper?.mChecked = checked
            refreshDrawableState()
            if (mRCHelper?.mOnCheckedChangeListener != null) {
                mRCHelper?.mOnCheckedChangeListener?.onCheckedChanged(this, mRCHelper?.mChecked!!)
            }
        }
    }

    override fun isChecked(): Boolean {
        return mRCHelper?.mChecked == true
    }

    override fun toggle() {
        isChecked = mRCHelper?.mChecked == false
    }

    fun setOnCheckedChangeListener(listener: RCHelper.OnCheckedChangeListener?) {
        mRCHelper?.mOnCheckedChangeListener = listener
    }
}