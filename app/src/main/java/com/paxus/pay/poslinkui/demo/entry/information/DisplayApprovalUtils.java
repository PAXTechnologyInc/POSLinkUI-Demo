package com.paxus.pay.poslinkui.demo.entry.information;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.pax.us.pay.ui.constant.entry.enumeration.CardType;
import com.paxus.pay.poslinkui.demo.R;

import java.util.Map;

public class DisplayApprovalUtils {

    private Map<String, Integer> cardTypeToImage = Map.of(
            CardType.MASTER_CARD, R.drawable.mastercard_approval
    );

    public ApprovalStrategy getApprovalStrategy(String cardType) {
        return cardTypeToImage.containsKey(cardType) ? new AnimationApproval() : new DefaultApproval();
    }


    public interface ApprovalStrategy {
        void displayApproval(Context context, ConstraintLayout parent, String cardType, Runnable onDisplayCompleted);
    }

    private class AnimationApproval implements ApprovalStrategy {
        @Override
        public void displayApproval(Context context, ConstraintLayout parent, String cardType, Runnable onDisplayCompleted) {
            ImageView animation = new ImageView(context);

            ConstraintLayout.LayoutParams animationParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            animationParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            animationParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            animationParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            animationParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            animation.setLayoutParams(animationParams);

            parent.addView(animation);

            Glide.with(context)
                    .load(cardTypeToImage.get(cardType))
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            new Handler(Looper.getMainLooper()).post(onDisplayCompleted);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if(resource instanceof GifDrawable) {
                                GifDrawable gifDrawable = (GifDrawable) resource;
                                gifDrawable.setLoopCount(1);
                                gifDrawable.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                    @Override
                                    public void onAnimationEnd(Drawable drawable) {
                                        gifDrawable.stop();
                                        onDisplayCompleted.run();
                                        gifDrawable.unregisterAnimationCallback(this);
                                    }
                                });
                                gifDrawable.start();
                            }
                            return false;
                        }
                    })
                    .into(new DrawableImageViewTarget(animation));
        }
    }

    private class DefaultApproval implements ApprovalStrategy {
        @Override
        public void displayApproval(Context context, ConstraintLayout parent, String cardType, Runnable onDisplayCompleted) {
            String line1 = "Approved By", line2 = cardType;
            SpannableString spannable = new SpannableString(line1 + "\n" + line2);
            spannable.setSpan(new AbsoluteSizeSpan((int) context.getResources().getDimension(R.dimen.text_size_subtitle)), 0, line1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, line1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new AbsoluteSizeSpan((int) context.getResources().getDimension(R.dimen.text_size_title)), line1.length() + 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            TextView textView = new TextView(context);
            textView.setText(spannable);
            textView.setTextColor(ContextCompat.getColor(context, R.color.pastel_text_color));
            textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            textView.setGravity(Gravity.CENTER);

            ConstraintLayout.LayoutParams textParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            textParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            textParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            textParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            textParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            textView.setLayoutParams(textParams);

            parent.addView(textView);

            new Handler(Looper.getMainLooper()).postDelayed(onDisplayCompleted, 3000);
        }
    }

}
