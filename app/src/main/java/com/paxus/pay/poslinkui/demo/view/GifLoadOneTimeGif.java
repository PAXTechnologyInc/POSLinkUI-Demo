package com.paxus.pay.poslinkui.demo.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 */
public class GifLoadOneTimeGif {
    /**
     * Gif load
     *
     * @param context
     * @param model       gif path
     * @param imageView
     * @param loopCount   display times
     * @param gifListener Gif callback listener
     */
    public static void loadOneTimeGif(Context context, Object model, final ImageView imageView, int loopCount, final GifListener gifListener) {
        Glide.get(context).clearMemory();
        Glide.with(context).asGif().load(model).fitCenter().addListener(new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {

                resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                    @Override
                    public void onAnimationEnd(Drawable drawable) {
                        resource.stop();
                        //super.onAnimationEnd(drawable);
                        gifListener.gifPlayComplete();
                        resource.unregisterAnimationCallback(this);
                    }
                });
                resource.setLoopCount(loopCount);
                resource.start();
                return false;
            }
        }).into(imageView);
    }

    /**
     * Gif callback Listener
     */
    public interface GifListener {
        void gifPlayComplete();

        //void gifPlayDuration(int duration);
    }

}
