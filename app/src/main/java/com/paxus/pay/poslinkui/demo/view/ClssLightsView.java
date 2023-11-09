package com.paxus.pay.poslinkui.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;

import com.pax.us.pay.ui.constant.status.ClssLightStatus;
import com.paxus.pay.poslinkui.demo.R;

/**
 * Virtual Contactless Light View
 */
public class ClssLightsView extends LinearLayout {

    private final ClssLight[] lights = new ClssLight[4];

    private AlphaAnimation blinking;

    public ClssLightsView(Context context) {
        this(context, null);
    }

    public ClssLightsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClssLightsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater mInflater = LayoutInflater.from(context);
        View myView = mInflater.inflate(R.layout.layout_clss_light_default, null);
        LayoutParams parentParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        parentParams.setLayoutDirection(HORIZONTAL);
        addView(myView, parentParams);

        init();
    }

    private void init() {
        blinking = new AlphaAnimation(1, 0);
        blinking.setDuration(500);
        blinking.setRepeatCount(Animation.INFINITE);
        blinking.setRepeatMode(Animation.REVERSE);

        lights[0] = findViewById(R.id.light1);
        lights[1] = findViewById(R.id.light2);
        lights[2] = findViewById(R.id.light3);
        lights[3] = findViewById(R.id.light4);
    }

    private void setLights(final @IntRange(from = -1, to = 3) int index, @ClssLight.STATUS int status) {

        for (int i = 0; i < lights.length; ++i) {
            if (index == i) {
                lights[i].setStatus(status, blinking);
            } else {
                lights[i].setStatus(ClssLight.OFF, null);
            }
        }
    }

    public void setLight(final @IntRange(from = 0, to = 3) int index, @ClssLight.STATUS int status) {
        lights[index].setStatus(status, blinking);
    }

    public void updateStatus(String status) {
        if (getVisibility() != View.VISIBLE) return;

        switch (status) {
            case ClssLightStatus.CLSS_LIGHT_COMPLETED:
            case ClssLightStatus.CLSS_LIGHT_NOT_READY: //Fix ANBP-383, ANFDRC-319
                setLights(-1, ClssLight.OFF);
                return;
            case ClssLightStatus.CLSS_LIGHT_ERROR:
                setLight(0, ClssLight.OFF);
                setLight(1, ClssLight.OFF);
                setLight(2, ClssLight.OFF);
                setLight(3, ClssLight.ON);
                return;
            case ClssLightStatus.CLSS_LIGHT_IDLE:
                setLights(0, ClssLight.BLINK);
                return;
            case ClssLightStatus.CLSS_LIGHT_PROCESSING:
                setLight(0, ClssLight.ON);
                setLight(1, ClssLight.ON);
                setLight(2, ClssLight.OFF);
                setLight(3, ClssLight.OFF);
                return;
            case ClssLightStatus.CLSS_LIGHT_READY_FOR_TXN:
                setLight(0, ClssLight.ON);
                setLight(1, ClssLight.OFF);
                setLight(2, ClssLight.OFF);
                setLight(3, ClssLight.OFF);
                return;
            case ClssLightStatus.CLSS_LIGHT_REMOVE_CARD:
                setLight(0, ClssLight.ON);
                setLight(1, ClssLight.ON);
                setLight(2, ClssLight.ON);
                setLight(3, ClssLight.OFF);
                return;
            default:
                break;
        }
    }

}
