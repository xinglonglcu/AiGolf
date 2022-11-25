package com.xlong.libui

import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import androidx.collection.ArrayMap

class ViewShapeMaker(val view: View, attrs: AttributeSet?) {
    private val context = view.context
    private val shapeHelper: ShapeHelper

    init {
        var idWrapper = sIdCache[view::class.java.simpleName]
        if (idWrapper == null) {
            idWrapper = IdWrapper(view)
            sIdCache[view::class.java.simpleName] = idWrapper
        }
        val ta = context.obtainStyledAttributes(attrs, idWrapper.styles)
        shapeHelper = ShapeHelper(ta, idWrapper)
        ta.recycle()
        shapeHelper.applyTo(view)
    }

    fun setRadius(radius: Float): ViewShapeMaker {
        shapeHelper.radius = radius
        return this
    }

    fun setStroke(stroke: Int): ViewShapeMaker {
        shapeHelper.stroke = stroke
        return this
    }

    fun setDashWidth(dashWidth: Float): ViewShapeMaker {
        shapeHelper.dashWidth = dashWidth
        return this
    }

    fun setDashGap(dashGap: Float): ViewShapeMaker {
        shapeHelper.dashGap = dashGap
        return this
    }

    fun setSolidColor(@ColorInt color: Int): ViewShapeMaker {
        shapeHelper.solidColor = color
        return this
    }

    fun setStrokeColor(@ColorInt color: Int): ViewShapeMaker {
        shapeHelper.strokeColor = color
        return this
    }

    fun setGradientStartColor(@ColorInt color: Int): ViewShapeMaker {
        shapeHelper.gradientStartColor = color
        return this
    }

    fun setGradientCenterColor(@ColorInt color: Int): ViewShapeMaker {
        shapeHelper.gradientCenterColor = color
        return this
    }

    fun setGradientEndColor(@ColorInt color: Int): ViewShapeMaker {
        shapeHelper.gradientEndColor = color
        return this
    }

    fun setGradientOrientation(orientation: GradientDrawable.Orientation): ViewShapeMaker {
        shapeHelper.gradientOrientation = orientation.ordinal
        return this
    }

    fun setShape(@Shape shape: Int): ViewShapeMaker {
        shapeHelper.shape = shape
        return this
    }

    fun setTopLeftRadius(radius: Float): ViewShapeMaker {
        shapeHelper.topLeftRadius = radius
        return this
    }

    fun setTopRightRadius(radius: Float): ViewShapeMaker {
        shapeHelper.topRightRadius = radius
        return this
    }

    fun setBottomRightRadius(radius: Float): ViewShapeMaker {
        shapeHelper.bottomRightRadius = radius
        return this
    }

    fun setBottomLeftRadius(radius: Float): ViewShapeMaker {
        shapeHelper.bottomLeftRadius = radius
        return this
    }

    fun setRippleColor(@ColorInt color: Int): ViewShapeMaker {
        shapeHelper.rippleColor = color
        return this
    }

    fun setTDBackground(background: Int): ViewShapeMaker {
        shapeHelper.background = background
        return this
    }

    fun apply() {
        shapeHelper.applyTo(view)
    }

    @IntDef(GradientDrawable.RECTANGLE, GradientDrawable.OVAL)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Shape

    private class IdWrapper(view: View) {
        val td_dashWidth: Int
        val td_dashGap: Int
        val id_td_radius: Int
        val id_td_stroke: Int
        val id_td_solid_color: Int
        val id_td_stroke_color: Int
        val id_td_gradient_startColor: Int
        val id_td_gradient_centerColor: Int
        val id_td_gradient_endColor: Int
        val id_td_gradient_orientation: Int
        val id_td_shape: Int
        val id_td_topLeftRadius: Int
        val id_td_topRightRadius: Int
        val id_td_bottomRightRadius: Int
        val id_td_bottomLeftRadius: Int
        val id_td_rippleColor: Int
        val id_td_background: Int
        val styles: IntArray
        val packageName = view.context.packageName

        init {
            styles = getStyleableAttrId(view::class.java.simpleName) as? IntArray ?: intArrayOf()
            td_dashWidth = getStyleableAttrId(view::class.java.simpleName + "_td_dashWidth") as Int
            td_dashGap = getStyleableAttrId(view::class.java.simpleName + "_td_dashGap") as Int
            id_td_radius = getStyleableAttrId(view::class.java.simpleName + "_td_radius") as Int
            id_td_stroke = getStyleableAttrId(view::class.java.simpleName + "_td_stroke") as Int
            id_td_solid_color = getStyleableAttrId(view::class.java.simpleName + "_td_solid_color") as Int
            id_td_stroke_color = getStyleableAttrId(view::class.java.simpleName + "_td_stroke_color") as Int
            id_td_gradient_startColor = getStyleableAttrId(view::class.java.simpleName + "_td_gradient_startColor") as Int
            id_td_gradient_centerColor = getStyleableAttrId(view::class.java.simpleName + "_td_gradient_centerColor") as Int
            id_td_gradient_endColor = getStyleableAttrId(view::class.java.simpleName + "_td_gradient_endColor") as Int
            id_td_gradient_orientation = getStyleableAttrId(view::class.java.simpleName + "_td_gradient_orientation") as Int
            id_td_shape = getStyleableAttrId(view::class.java.simpleName + "_td_shape") as Int
            id_td_topLeftRadius = getStyleableAttrId(view::class.java.simpleName + "_td_topLeftRadius") as Int
            id_td_topRightRadius = getStyleableAttrId(view::class.java.simpleName + "_td_topRightRadius") as Int
            id_td_bottomRightRadius = getStyleableAttrId(view::class.java.simpleName + "_td_bottomRightRadius") as Int
            id_td_bottomLeftRadius = getStyleableAttrId(view::class.java.simpleName + "_td_bottomLeftRadius") as Int
            id_td_rippleColor = getStyleableAttrId(view::class.java.simpleName + "_td_rippleColor") as Int
            id_td_background = getStyleableAttrId(view::class.java.simpleName + "_td_background") as Int
        }

        private fun getStyleableAttrId(name: String?): Any? {
            try {
                val clazz = Class.forName("$packageName.R\$styleable")
                return name?.let { clazz.getField(it).get(clazz) }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
            return 0
        }
    }

    private class ShapeHelper(typedArray: TypedArray, idWrapper: IdWrapper) {
        var dashWidth: Float = typedArray.getDimension(idWrapper.td_dashWidth, 0f)
        var dashGap: Float = typedArray.getDimension(idWrapper.td_dashGap, 0f)
        var radius: Float = typedArray.getDimension(idWrapper.id_td_radius, 0f)
        var stroke: Int = typedArray.getDimensionPixelSize(idWrapper.id_td_stroke, 0)

        /**
         * view 背景图 设置了边框或者填充色时不生效
         */
        var background: Int = typedArray.getResourceId(idWrapper.id_td_background, 0)

        @ColorInt
        var solidColor: Int = typedArray.getColor(idWrapper.id_td_solid_color, Color.TRANSPARENT)

        @ColorInt
        var strokeColor: Int = typedArray.getColor(idWrapper.id_td_stroke_color, Color.TRANSPARENT)

        @ColorInt
        var gradientStartColor: Int = typedArray.getColor(idWrapper.id_td_gradient_startColor, Color.TRANSPARENT)

        @ColorInt
        var gradientCenterColor: Int = typedArray.getColor(idWrapper.id_td_gradient_centerColor, Color.TRANSPARENT)

        @ColorInt
        var gradientEndColor: Int = typedArray.getColor(idWrapper.id_td_gradient_endColor, Color.TRANSPARENT)
        var gradientOrientation: Int = typedArray.getInt(idWrapper.id_td_gradient_orientation, GradientDrawable.Orientation.TOP_BOTTOM.ordinal)

        @Shape
        var shape: Int = typedArray.getInt(idWrapper.id_td_shape, 0)
        var topLeftRadius: Float = typedArray.getDimension(idWrapper.id_td_topLeftRadius, 0f)
        var topRightRadius: Float = typedArray.getDimension(idWrapper.id_td_topRightRadius, 0f)
        var bottomRightRadius: Float = typedArray.getDimension(idWrapper.id_td_bottomRightRadius, 0f)
        var bottomLeftRadius: Float = typedArray.getDimension(idWrapper.id_td_bottomLeftRadius, 0f)

        /**
         * 水波纹效果的颜色值，只有设置了有边框或者填充颜色才会生效
         */
        @ColorInt
        var rippleColor: Int =  typedArray.getColor(idWrapper.id_td_rippleColor, Color.parseColor("#33000000"))

        fun applyTo(view: View) {
            if (stroke > 0 || solidColor != Color.TRANSPARENT
                || gradientStartColor != Color.TRANSPARENT
                || gradientEndColor != Color.TRANSPARENT
                || gradientCenterColor != Color.TRANSPARENT
            ) {
                val gd = GradientDrawable()
                gd.shape = shape
                if (dashWidth > 0 && dashGap > 0) {
                    gd.setStroke(stroke, strokeColor, dashWidth, dashGap)
                } else {
                    gd.setStroke(stroke, strokeColor)
                }
                if (solidColor != Color.TRANSPARENT) {
                    gd.setColor(solidColor)
                } else if (gradientStartColor != Color.TRANSPARENT
                    || gradientEndColor != Color.TRANSPARENT
                    || gradientCenterColor != Color.TRANSPARENT
                ) {
                    if (gradientCenterColor != Color.TRANSPARENT) {
                        gd.colors =
                            intArrayOf(gradientStartColor, gradientCenterColor, gradientEndColor)
                    } else {
                        gd.colors = intArrayOf(gradientStartColor, gradientEndColor)
                    }
                    gd.orientation = GradientDrawable.Orientation.values()[gradientOrientation]
                }
                gd.cornerRadius = radius
                if ((topLeftRadius > 0f || topRightRadius > 0f || bottomLeftRadius > 0f || bottomRightRadius > 0f) &&
                    (topLeftRadius != radius || topRightRadius != radius || bottomLeftRadius != radius || bottomRightRadius != radius)
                ) {
                    gd.cornerRadii = floatArrayOf(
                        topLeftRadius, topLeftRadius,
                        topRightRadius, topRightRadius,
                        bottomRightRadius, bottomRightRadius,
                        bottomLeftRadius, bottomLeftRadius
                    )
                }
                if (Build.VERSION.SDK_INT >= 23) {
                    val drawable = RippleDrawable(
                        getPressedColorSelector(Color.TRANSPARENT, rippleColor),
                        gd,
                        null
                    )
                    view.background = drawable
                } else {
                    view.background = gd
                }
            } else if (background != 0) {
                val drawable = view.resources.getDrawable(background)
                view.background = drawable
            } else {
                view.background = null
            }
        }

        private fun getPressedColorSelector(normalColor: Int, pressedColor: Int): ColorStateList {
            return ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_pressed),
                    intArrayOf(android.R.attr.state_focused),
                    intArrayOf(android.R.attr.state_activated),
                    intArrayOf()
                ), intArrayOf(
                    pressedColor,
                    pressedColor,
                    pressedColor,
                    normalColor
                )
            )
        }
    }


    companion object {
        private val sIdCache = ArrayMap<String, IdWrapper>()
    }
}