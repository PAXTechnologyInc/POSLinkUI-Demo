package com.paxus.pay.poslinkui.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;

import androidx.annotation.IntDef;
import androidx.appcompat.widget.AppCompatImageView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ClssLight extends AppCompatImageView {

    public static final int OFF = 0;
    public static final int ON = 1;
    public static final int BLINK = 2;

    @IntDef({OFF, ON, BLINK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface STATUS {
    }

    public ClssLight(Context context) {
        this(context, null);
    }

    public ClssLight(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClssLight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setEnabled(false);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearAnimation();
    }

    public void setStatus(@STATUS int status, final Animation blinking) {
        if (status == BLINK) {
            setEnabled(true);
            startAnimation(blinking);
        } else {
            clearAnimation();
            setEnabled(status == ON);
        }
    }
}
