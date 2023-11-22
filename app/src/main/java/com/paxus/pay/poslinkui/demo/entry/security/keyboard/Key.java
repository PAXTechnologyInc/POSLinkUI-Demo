package com.paxus.pay.poslinkui.demo.entry.security.keyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.TextViewCompat;

import com.paxus.pay.poslinkui.demo.R;

public class Key extends androidx.appcompat.widget.AppCompatTextView {

    private String code;
    private String payload;

    public Key(@NonNull Context context) {
        super(context);
        init(context, null, R.style.POSLinkUISecureKeyboardKey);
    }

    public Key(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, R.style.POSLinkUISecureKeyboardKey);
    }

    public Key(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.Key, defStyleAttr, 0);

        this.code = styledAttributes.getString(R.styleable.Key_code);
        this.payload = styledAttributes.getString(R.styleable.Key_payload);

        styledAttributes.recycle();

        TextViewCompat.setAutoSizeTextTypeWithDefaults(this, TextViewCompat.AUTO_SIZE_TEXT_TYPE_NONE);
    }

    public String getCode() {
        return code;
    }

    public String getPayload() {
        return payload;
    }
}
