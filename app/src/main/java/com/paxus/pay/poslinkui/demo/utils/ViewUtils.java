package com.paxus.pay.poslinkui.demo.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.paxus.pay.poslinkui.demo.R;

/**
 * Utils for handle water mark
 */
public class ViewUtils {
    private static final String WATERMARK_TAG = "watermark";

    public static void addWaterMarkView(Activity activity, String content) {
        if (activity != null) {
            ViewGroup rootView = activity.findViewById(android.R.id.content);
            if (null == rootView.findViewWithTag(WATERMARK_TAG)) {
                View waterMarkView = LayoutInflater.from(activity).inflate(R.layout.layout_watermark, null);
                Drawable bitmapDrawable = genWaterMark(activity, content);
                waterMarkView.setBackground(bitmapDrawable);
                //avoid re-draw
                waterMarkView.setTag(WATERMARK_TAG);
                rootView.addView(waterMarkView);
            }
        }
    }

    public static void removeWaterMarkView(Activity activity) {
        if (activity != null) {
            ViewGroup rootView = activity.findViewById(android.R.id.content);
            View watermark = rootView.findViewWithTag(WATERMARK_TAG);
            if (watermark != null) {
                rootView.removeView(watermark);
            }
        }
    }

    public static BitmapDrawable genWaterMark(Activity activity, String content) {
        TextPaint paint = new TextPaint();
        paint.setColor(activity.getBaseContext().getResources().getColor(R.color.pastel_secondary));
        paint.setAlpha(100);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(50);
        StaticLayout contentLayout = new StaticLayout(content, paint, 600,
                Layout.Alignment.ALIGN_CENTER, 1, 0, true);

        //new height, anticlockwise
        //[x0,y0] = [0,contentH], [x1, y1]=[contentW, contentH], [x2,y2]=[newW,newH]
        //y2 = (y1 - y0) * cos(a) + (x1 - x0) * sin(a) + y0
        float degrees = 45;
        int contentW = contentLayout.getWidth();
        int contentH = contentLayout.getHeight();

        int newH = (int) (contentW * Math.sin(Math.toRadians(degrees)) + contentH);

        Bitmap bitmap = Bitmap.createBitmap(contentW, newH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);

        canvas.save();
        canvas.translate(0, newH - contentH); // move to the start point to the old position
        canvas.rotate(-45); //anticlockwise rotation
        contentLayout.draw(canvas);
        canvas.restore();

        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        bitmapDrawable.setDither(true);
        return bitmapDrawable;
    }

    public static int getBarHeight(Activity activity){
        int barHeight = 0;
        boolean immersiveSticky = (activity.getWindow().getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) > 0;
        if (!immersiveSticky) {
            Rect rect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            barHeight = rect.top;
        }
        return barHeight;
    }

    public static Bundle generateInputTextAreaExtras(Activity hostActivity, TextView inputTextView, String hint){
        Bundle bundle = new Bundle();

        if(inputTextView == null) return bundle;

        int[] location = new int[2];
        inputTextView.getLocationInWindow(location);

        int fontSize = (int) (inputTextView.getPaint().getTextSize() / inputTextView.getPaint().density);

        bundle.putInt(EntryRequest.PARAM_X, location[0]);
        bundle.putInt(EntryRequest.PARAM_Y, location[1] - getBarHeight(hostActivity));
        bundle.putInt(EntryRequest.PARAM_WIDTH, inputTextView.getWidth());
        bundle.putInt(EntryRequest.PARAM_HEIGHT, inputTextView.getHeight());
        bundle.putInt(EntryRequest.PARAM_FONT_SIZE, fontSize);
        bundle.putString(EntryRequest.PARAM_HINT, (hint != null && !hint.isEmpty()) ? hint : "");
        bundle.putString(EntryRequest.PARAM_COLOR, String.format("%X", inputTextView.getCurrentTextColor()));

        return bundle;
    }
}
