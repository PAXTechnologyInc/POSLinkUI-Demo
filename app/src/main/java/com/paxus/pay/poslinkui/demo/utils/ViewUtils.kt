package com.paxus.pay.poslinkui.demo.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import com.paxus.pay.poslinkui.demo.R
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sin

/**
 * Utils for handle water mark
 */
@Singleton
class ViewUtils @Inject constructor() {
    private val WATERMARK_TAG = "watermark"

    fun addWaterMarkView(activity: Activity?, content: String?) {
        if (activity != null) {
            val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
            if (null == rootView.findViewWithTag<View?>(WATERMARK_TAG)) {
                val waterMarkView =
                    LayoutInflater.from(activity).inflate(R.layout.layout_watermark, null)
                val bitmapDrawable: Drawable = genWaterMark(activity, content)
                waterMarkView.setBackground(bitmapDrawable)
                //avoid re-draw
                waterMarkView.setTag(WATERMARK_TAG)
                rootView.addView(waterMarkView)
            }
        }
    }

    fun removeWaterMarkView(activity: Activity?) {
        if (activity != null) {
            val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
            val watermark = rootView.findViewWithTag<View?>(WATERMARK_TAG)
            if (watermark != null) {
                rootView.removeView(watermark)
            }
        }
    }

    fun genWaterMark(activity: Activity, content: String?): BitmapDrawable {
        val paint = TextPaint()
        paint.setColor(activity.getBaseContext().getResources().getColor(R.color.pastel_secondary))
        paint.setAlpha(100)
        paint.setAntiAlias(true)
        paint.setTextAlign(Paint.Align.LEFT)
        paint.setTextSize(50f)
        val contentLayout = StaticLayout(
            content, paint, 600,
            Layout.Alignment.ALIGN_CENTER, 1f, 0f, true
        )

        val degrees = 45f
        val contentW = contentLayout.getWidth()
        val contentH = contentLayout.getHeight()
        val newH = (contentW * sin(Math.toRadians(degrees.toDouble())) + contentH).toInt()

        val bitmap = Bitmap.createBitmap(contentW, newH, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.TRANSPARENT)

        canvas.save()
        canvas.translate(0f, (newH - contentH).toFloat())
        canvas.rotate(-45f)
        contentLayout.draw(canvas)
        canvas.restore()

        val bitmapDrawable = BitmapDrawable(bitmap)
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        bitmapDrawable.setDither(true)
        return bitmapDrawable
    }

    fun setupAutoScaleTextView(
        textView: TextView,
        normalSize: Int,
        maxLines: Int,
        fontBold: Int,
        minSize: Int
    ) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, normalSize.toFloat())
        textView.setMaxLines(maxLines)
        textView.setEllipsize(null)
        textView.setTypeface(textView.getTypeface(), fontBold)

        // adjust font size to show StatusData.PARAM_MSG_PRIMARY.
        TextViewCompat.setAutoSizeTextTypeWithDefaults(
            textView,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
        )

        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
            textView,
            minSize,
            normalSize,
            1,
            TypedValue.COMPLEX_UNIT_SP
        )
    }
}
