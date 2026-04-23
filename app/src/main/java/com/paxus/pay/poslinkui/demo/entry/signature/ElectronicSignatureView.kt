/*
 * ============================================================================
 * COPYRIGHT
 *              Pax CORPORATION PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or
 *   nondisclosure agreement with Pax Corporation and may not be copied
 *   or disclosed except in accordance with the terms in that agreement.
 *      Copyright (C) 2016 - ? Pax Corporation. All rights reserved.
 * Module Date: 2016-12-1
 * Module Author: Steven.W
 * Description:
 *
 * ============================================================================
 */
package com.paxus.pay.poslinkui.demo.entry.signature

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PorterDuff
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.utils.Logger
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Signature board used by Compose `AndroidView` bridge.
 */
class ElectronicSignatureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var x0 = 0f
    private var y0 = 0f
    private val gesturePaint = Paint()
    private val path = Path()
    private var cacheCanvas: Canvas? = null
    private var cacheBitmap: Bitmap? = null
    private var touched = false
    private var paintWidth = 0
    private var penColor = Color.BLACK
    private var backColor = Color.WHITE
    private var textColor = Color.BLACK
    private var text = ""
    private var rect = Rect(0, 0, 500, 200)
    private var paddingPx = 0
    private var backgroundColor = Color.WHITE
    private var firstDraw = true
    private val pathPos = mutableListOf<FloatArray>()
    private var sampleRate = 3

    init {
        initializeView(context)
    }

    private fun initializeView(context: Context) {
        contentDescription = "ElectronicSignatureView"
        paintWidth = context.resources.getDimension(R.dimen.paint_width).toInt()
        gesturePaint.isAntiAlias = true
        gesturePaint.style = Paint.Style.STROKE
        gesturePaint.strokeWidth = paintWidth.toFloat()
        gesturePaint.color = penColor
        gesturePaint.strokeJoin = Paint.Join.ROUND
        gesturePaint.strokeCap = Paint.Cap.ROUND
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val bitmapWidth = w.coerceAtLeast(1)
        val bitmapHeight = h.coerceAtLeast(1)
        // Use RGB_565 to reduce bitmap memory overhead in security bitmap checks.
        val createdBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.RGB_565)
        cacheBitmap = createdBitmap
        val createdCanvas = Canvas(createdBitmap)
        createdCanvas.drawColor(resolveCanvasBackgroundColor())
        cacheCanvas = createdCanvas
        touched = false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchDown(event)
            MotionEvent.ACTION_MOVE -> {
                touched = true
                touchMove(event)
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                performClick()
            }
        }
        invalidate()
        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        cacheBitmap?.let { canvas.drawBitmap(it, 0f, 0f, gesturePaint) }
        canvas.drawPath(path, gesturePaint)

        if (firstDraw) {
            val paint = Paint().apply {
                color = textColor
                flags = Paint.ANTI_ALIAS_FLAG
            }
            val bounds = Rect()
            paint.getTextBounds(text, 0, text.length, bounds)
            cacheCanvas?.drawText(
                text,
                measuredWidth / 2f - bounds.width() / 2f,
                measuredHeight / 2f + bounds.height() / 2f,
                paint
            )
            firstDraw = false
        }
    }

    fun setText(textColor: Int, text: String) {
        this.textColor = textColor
        this.text = text
    }

    fun setBitmap(rect: Rect, padding: Int, background: Int) {
        this.backgroundColor = background
        this.rect = rect
        this.paddingPx = padding
    }

    private fun touchDown(event: MotionEvent) {
        path.reset()
        x0 = event.x
        y0 = event.y
        path.moveTo(x0, y0)
    }

    private fun touchMove(event: MotionEvent) {
        val x = event.x
        val y = event.y
        val dx = kotlin.math.abs(x - x0)
        val dy = kotlin.math.abs(y - y0)

        if (dx >= 3 || dy >= 3) {
            val cx = (x + x0) / 2f
            val cy = (y + y0) / 2f
            path.quadTo(x0, y0, cx, cy)
            x0 = x
            y0 = y
        }
    }

    private fun touchUp() {
        cacheCanvas?.drawPath(path, gesturePaint)
        val pathMeasure = PathMeasure(path, false)
        var distance = 0
        while (distance < pathMeasure.length) {
            val pos = floatArrayOf(0f, 0f)
            pathMeasure.getPosTan(distance.toFloat(), pos, null)
            pathPos.add(pos)
            distance += sampleRate
        }
        pathPos.add(floatArrayOf(-1f, -1f))
        path.reset()
    }

    fun clear() {
        firstDraw = true
        cacheCanvas?.let { canvas ->
            touched = false
            gesturePaint.color = penColor
            // RGB_565 has no alpha channel; always repaint with an opaque background.
            canvas.drawColor(resolveCanvasBackgroundColor(), PorterDuff.Mode.SRC)
            gesturePaint.color = penColor
            invalidate()
            pathPos.clear()
        }
    }

    @Throws(IOException::class)
    fun save(path: String) {
        save(path, false, 0)
    }

    fun save(clearBlank: Boolean, blank: Int): Bitmap {
        var bitmap = cacheBitmap ?: Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
        if (clearBlank) {
            bitmap = clearBlank(bitmap, blank, backColor)
        }
        return placeBitmapIntoRect(bitmap, rect, paddingPx, backgroundColor)
    }

    fun save(path: String, clearBlank: Boolean, blank: Int) {
        Thread { saveBitmapToPath(path, clearBlank, blank) }.start()
    }

    private fun saveBitmapToPath(path: String, clearBlank: Boolean, blank: Int) {
        var bitmap = cacheBitmap ?: Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
        if (clearBlank) {
            bitmap = clearBlank(bitmap, blank, backColor)
        }
        bitmap = placeBitmapIntoRect(bitmap, rect, paddingPx, backgroundColor)

        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val buffer = bos.toByteArray()
        val file = File(path)
        if (!file.delete() && file.exists()) {
            Logger.d("$file can not be deleted")
        }
        try {
            FileOutputStream(file).use { outputStream -> outputStream.write(buffer) }
        } catch (e: Exception) {
            Logger.e(e)
        }
    }

    fun getBitMap(): Bitmap? {
        isDrawingCacheEnabled = true
        buildDrawingCache()
        val bitmap = drawingCache
        isDrawingCacheEnabled = false
        return bitmap
    }

    fun setPaintWidth(paintWidth: Int) {
        this.paintWidth = if (paintWidth > 0) paintWidth else 5
        gesturePaint.strokeWidth = this.paintWidth.toFloat()
    }

    fun setBackColor(backColor: Int) {
        // RGB_565 cannot preserve transparent background; transparent falls back to white.
        this.backColor = if (backColor == Color.TRANSPARENT) Color.WHITE else backColor
        cacheCanvas?.drawColor(resolveCanvasBackgroundColor(), PorterDuff.Mode.SRC)
        invalidate()
    }

    fun setPenColor(penColor: Int) {
        this.penColor = penColor
        gesturePaint.color = penColor
    }

    fun getTouched(): Boolean = touched

    fun getPathPos(): List<FloatArray> = pathPos

    fun getSampleRate(): Int = sampleRate

    fun setSampleRate(sampleRate: Int) {
        if (sampleRate >= 1) {
            this.sampleRate = sampleRate
        }
    }

    companion object {
        private fun getTopBorder(bp: Bitmap, width: Int, height: Int, blank: Int, backColor: Int): Int {
            val pixels = IntArray(width)
            for (y in 0 until height) {
                bp.getPixels(pixels, 0, width, 0, y, width, 1)
                if (pixels.any { it != backColor }) {
                    return (y - blank).coerceAtLeast(0)
                }
            }
            return 0
        }

        private fun getBottomBorder(bp: Bitmap, width: Int, height: Int, blank: Int, backColor: Int): Int {
            val pixels = IntArray(width)
            for (y in height - 1 downTo 0) {
                bp.getPixels(pixels, 0, width, 0, y, width, 1)
                if (pixels.any { it != backColor }) {
                    return (y + blank).coerceAtMost(height - 1)
                }
            }
            return (height - 1).coerceAtLeast(0)
        }

        private fun getLeftBorder(bp: Bitmap, width: Int, height: Int, blank: Int, backColor: Int): Int {
            val pixels = IntArray(height)
            for (x in 0 until width) {
                bp.getPixels(pixels, 0, 1, x, 0, 1, height)
                if (pixels.any { it != backColor }) {
                    return (x - blank).coerceAtLeast(0)
                }
            }
            return 0
        }

        private fun getRightBorder(bp: Bitmap, width: Int, height: Int, blank: Int, backColor: Int): Int {
            val pixels = IntArray(height)
            for (x in width - 1 downTo 0) {
                bp.getPixels(pixels, 0, 1, x, 0, 1, height)
                if (pixels.any { it != backColor }) {
                    return (x + blank).coerceAtMost(width - 1)
                }
            }
            return (width - 1).coerceAtLeast(0)
        }

        private fun clearBlank(bp: Bitmap, blank: Int, backColor: Int): Bitmap {
            val height = bp.height
            val width = bp.width
            if (height <= 0 || width <= 0) {
                return Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
            }

            val safeBlank = blank.coerceAtLeast(0)
            val top = getTopBorder(bp, width, height, safeBlank, backColor)
            val bottom = getBottomBorder(bp, width, height, safeBlank, backColor)
            val left = getLeftBorder(bp, width, height, safeBlank, backColor)
            val right = getRightBorder(bp, width, height, safeBlank, backColor)

            val safeLeft = left.coerceIn(0, width - 1)
            val safeTop = top.coerceIn(0, height - 1)
            val safeRight = right.coerceIn(safeLeft, width - 1)
            val safeBottom = bottom.coerceIn(safeTop, height - 1)
            val cropWidth = (safeRight - safeLeft + 1).coerceAtLeast(1)
            val cropHeight = (safeBottom - safeTop + 1).coerceAtLeast(1)

            return Bitmap.createBitmap(bp, safeLeft, safeTop, cropWidth, cropHeight)
        }

        @JvmStatic
        fun placeBitmapIntoRect(bitmap: Bitmap, rect: Rect, padding: Int, background: Int): Bitmap {
            if (rect.width() <= 0 || rect.height() <= 0) {
                return Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
            }
            if (bitmap.width <= 0 || bitmap.height <= 0) {
                return Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.RGB_565).apply {
                    eraseColor(background)
                }
            }

            val safePadding = if (padding * 2 >= rect.height() || padding * 2 >= rect.width() || padding < 0) {
                0
            } else {
                padding
            }
            val targetHeight = (rect.height() - safePadding * 2).coerceAtLeast(1)
            val targetWidth = (rect.width() - safePadding * 2).coerceAtLeast(1)
            val h = targetHeight.toFloat() / bitmap.height.toFloat()
            val w = targetWidth.toFloat() / bitmap.width.toFloat()
            val size = minOf(h, w).coerceAtLeast(0.01f).coerceAtMost(0.5f)

            val matrix = Matrix().apply { postScale(size, size) }
            val scaledBitmap = Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )

            return try {
                Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.RGB_565).apply {
                    val canvas = Canvas(this)
                    val paint = Paint().apply { color = background }
                    canvas.drawRect(Rect(0, 0, rect.width(), rect.height()), paint)
                    val centerX = rect.width() / 2f - scaledBitmap.width / 2f
                    val topOffset = if (minOf(h, w) > 0.5f) {
                        rect.height() / 2f - scaledBitmap.height / 2f
                    } else {
                        safePadding.toFloat()
                    }
                    canvas.drawBitmap(scaledBitmap, centerX, topOffset, null)
                }
            } finally {
                if (scaledBitmap !== bitmap && !scaledBitmap.isRecycled) {
                    scaledBitmap.recycle()
                }
            }
        }
    }

    private fun resolveCanvasBackgroundColor(): Int {
        return if (backColor == Color.TRANSPARENT) Color.WHITE else backColor
    }
}
