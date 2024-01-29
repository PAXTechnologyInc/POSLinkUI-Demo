package com.paxus.pay.poslinkui.demo.status;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.paxus.pay.poslinkui.demo.R;

public class ToastFragment extends Fragment {
    public ToastFragment() {
        // Default initialization code here
    }
    public ToastFragment newInstance(String message, int colorResID) {
        ToastFragment fragment = new ToastFragment();
        Bundle args = new Bundle();
        args.putString("message", message);
        args.putInt("color", colorResID);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String message = getArguments().getString("message");
        int colorResID = getArguments().getInt("color");
        return new ToastView(getContext(), message, colorResID);
    }

    private class ToastView extends ConstraintLayout {
        public ToastView(@NonNull Context context, String message, int colorResID) {
            this(context);

            //Add a View to contain Toast message
            ConstraintLayout messageContainer = new ConstraintLayout(context);
            ConstraintLayout.LayoutParams messageContainerLayoutParams = new ConstraintLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_CONSTRAINT);
            messageContainerLayoutParams.topToTop = LayoutParams.PARENT_ID;
            messageContainerLayoutParams.bottomToBottom = LayoutParams.PARENT_ID;
            messageContainerLayoutParams.endToEnd = LayoutParams.PARENT_ID;
            messageContainer.setLayoutParams(messageContainerLayoutParams);
            messageContainer.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.background_toast, null));
            messageContainer.setBackgroundTintList(AppCompatResources.getColorStateList(context, colorResID));
            addView(messageContainer);

            //Add Toast message to the Container
            TextView messageView = new TextView(context);
            messageView.setText(message);
            messageView.setTextColor(getResources().getColor(R.color.white));
            ConstraintLayout.LayoutParams messageLayoutParams = new ConstraintLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            messageLayoutParams.topToTop = LayoutParams.PARENT_ID;
            messageLayoutParams.bottomToBottom = LayoutParams.PARENT_ID;
            messageLayoutParams.startToStart = LayoutParams.PARENT_ID;
            messageLayoutParams.endToEnd = LayoutParams.PARENT_ID;
            messageView.setLayoutParams(messageLayoutParams);
            messageView.setPadding(30, 0, 30, 0);
            messageContainer.addView(messageView);
        }

        public ToastView(@NonNull Context context) {
            this(context, null);
        }

        public ToastView(@NonNull Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public ToastView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            this(context, attrs, defStyleAttr, 0);
        }

        public ToastView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            setLayoutParams(new ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
    }
}
