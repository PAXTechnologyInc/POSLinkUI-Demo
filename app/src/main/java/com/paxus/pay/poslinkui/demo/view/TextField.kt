package com.paxus.pay.poslinkui.demo.view

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.paxus.pay.poslinkui.demo.utils.MainThreadRunner
import android.os.ResultReceiver
import android.text.InputType
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.utils.DeviceUtils
import com.paxus.pay.poslinkui.demo.utils.Logger

/**
 * Created by Dhrubo Paul
 */

class TextField : AppCompatEditText {

    private var keyboardOpened : Boolean = false
    private var pressedIntentionally : Boolean = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        //Clickability
        isFocusable = true
        isFocusableInTouchMode = true
        isClickable = true

        //Accessibility
        hint = ""
        contentDescription = ""

        //Input Method
        showSoftInputOnFocus = true

        //Styling
        isCursorVisible = true
        background = ResourcesCompat.getDrawable(resources, R.drawable.rounded_corner_on_background, null)
        setTextColor(ResourcesCompat.getColor(resources, R.color.pastel_text_color_on_light, null))
        setHintTextColor(ResourcesCompat.getColor(resources, R.color.pastel_text_color_on_light, null))
        imeOptions = android.view.inputmethod.EditorInfo.IME_FLAG_NO_EXTRACT_UI
        gravity = android.view.Gravity.CENTER
        minHeight = resources.getDimensionPixelSize(R.dimen.button_height)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        imm().hideSoftInputFromWindow(windowToken, 0)
        viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
    }

    override fun performClick(): Boolean {
        pressedIntentionally = true
        if(hasFocus() && softKeyboardNeeded() && !keyboardOpened) {
            ensureSoftKeyboardWhenReady()
        }
        return super.performClick()
    }



    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if(focused) {
            if(softKeyboardNeeded()) showSoftKeyboard() else hideSoftKeyboard()
        } else {
            hideSoftKeyboard()
        }
    }

    private fun imm(): InputMethodManager {
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private fun softKeyboardNeeded(): Boolean {
        return !(DeviceUtils.hasPhysicalKeyboard() && !requiresAlphanumericKeyboard(inputType)) || pressedIntentionally
    }

    private fun requiresAlphanumericKeyboard(inputType: Int): Boolean {
        return when (inputType and InputType.TYPE_MASK_CLASS) {
            InputType.TYPE_CLASS_TEXT -> true
            InputType.TYPE_CLASS_NUMBER -> false
            InputType.TYPE_CLASS_PHONE -> false
            InputType.TYPE_CLASS_DATETIME -> false
            else -> true
        }
    }

    private val immResultReceiver = object : ResultReceiver(Handler(Looper.getMainLooper())) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            super.onReceiveResult(resultCode, resultData)

            keyboardOpened = resultCode == InputMethodManager.RESULT_SHOWN || resultCode == InputMethodManager.RESULT_UNCHANGED_SHOWN

            Logger.i(
                when (resultCode) {
                    InputMethodManager.RESULT_UNCHANGED_SHOWN -> "IMM: Soft keyboard already shown and unchanged"
                    InputMethodManager.RESULT_UNCHANGED_HIDDEN -> "IMM: Soft keyboard already hidden and unchanged"
                    InputMethodManager.RESULT_SHOWN -> "IMM: Soft keyboard shown successfully"
                    InputMethodManager.RESULT_HIDDEN -> "IMM: Soft keyboard hidden successfully"
                    else -> "IMM: Unknown result code ($resultCode)"
                }
            )
            Logger.i("Keyboard Opened: $keyboardOpened")
        }
    }

    fun attachToLifecycle(lifecycle: Lifecycle) {
        val lifecycleObserver = object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        if(softKeyboardNeeded()) MainThreadRunner.post { requestFocus() }
                    }
                    Lifecycle.Event.ON_PAUSE -> {
                        hideSoftKeyboard()
                    }
                    Lifecycle.Event.ON_DESTROY -> {
                        lifecycle.removeObserver(this) // Remove the observer explicitly
                    }
                    else -> {
                    }
                }
            }
        }
        lifecycle.addObserver(lifecycleObserver)
    }

    private val globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            showSoftKeyboard()
            if (keyboardOpened) viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    }

    private fun ensureSoftKeyboardWhenReady() {
        if(keyboardOpened) return
        if(isLaidOut) showSoftKeyboard()
        while (!isAttachedToWindow){}
        viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    private fun showSoftKeyboard() {
        if(!softKeyboardNeeded() || keyboardOpened) return

        if (hasFocus() && imm().isActive(this) && isAttachedToWindow) {
            MainThreadRunner.postDelayed(200, Runnable {
                imm().showSoftInput(this, InputMethodManager.SHOW_IMPLICIT, immResultReceiver)
                requestLayout()
            })
        }

    }

    private fun hideSoftKeyboard() {
        MainThreadRunner.post {
            imm().hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY, immResultReceiver)
        }
    }
}