package com.paxus.pay.poslinkui.demo.entry;

import static com.paxus.pay.poslinkui.demo.utils.ViewUtils.setupAutoScaleTextView;

import android.app.Presentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;
import androidx.lifecycle.Observer;

import com.pax.us.pay.ui.constant.entry.enumeration.TransactionStatus;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.viewmodel.SecondScreenInfoViewModel;

import coil.Coil;
import coil.ComponentRegistry;
import coil.ImageLoader;
import coil.decode.GifDecoder;
import coil.decode.ImageDecoderDecoder;
import coil.request.ImageRequest;

/**
 * Author: Elaine Xie
 * Date: 2025/11/25
 * Desc:
 */
public class TransactionPresentation extends Presentation {
    Intent mintent;
    AppCompatActivity hostActivity;
    private TextView titleTextView;
    private TextView amountTextView;
    private TextView messageTextView;
    private TextView statusMessageTextView;

    private ImageView imageTapView;
    private View backgroundView;

    private final SecondScreenInfoViewModel viewModel;
    private ImageView imageView;

    public TransactionPresentation(AppCompatActivity outerContext, Display display, Intent intent, SecondScreenInfoViewModel viewModel) {
        super(outerContext, display);
        hostActivity = outerContext;
        mintent = intent;
        this.viewModel = viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_screen_layout);

        titleTextView = findViewById(R.id.tv_title);
        amountTextView = findViewById(R.id.tv_amount);
        messageTextView = findViewById(R.id.tv_desc);
        statusMessageTextView = findViewById(R.id.tv_status_msg);
        backgroundView = findViewById(R.id.tv_blue_bg);
        imageTapView = findViewById(R.id.tv_tap_image_view);

        // load gif
        imageView = findViewById(R.id.gif_image_view);
        ImageLoader imageLoader = createDynamicImageLoader(hostActivity);
        ImageRequest request = new ImageRequest.Builder(hostActivity)
                .data(R.raw.border_animated)
                .target(imageView)
                .build();
        imageLoader.enqueue(request);
        Coil.setImageLoader(imageLoader);

        // Observer ViewModel
        Observer<SecondScreenInfoViewModel.ScreenInfo> contentObserver = this::updateContent;
        viewModel.getScreenInfo().observeForever(contentObserver);
    }

    public static ImageLoader createDynamicImageLoader(Context context) {
        ComponentRegistry componentRegistry;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            componentRegistry = new ComponentRegistry().newBuilder().add(new ImageDecoderDecoder.Factory()).build();
        } else {
            componentRegistry = new ComponentRegistry().newBuilder().add(new GifDecoder.Factory()).build();
        }
        return new ImageLoader.Builder(context)
                .components(componentRegistry)
                .build();
    }

    private void updateContent(SecondScreenInfoViewModel.ScreenInfo info) {
        if (info.status.equals(TransactionStatus.APPROVED.name())){
            setStatusCase(getResources().getColor(R.color.success),
                    info.statusTitle.isEmpty() ?  getResources().getString(R.string.status_msg_approved) : info.statusTitle);
        } else if (info.status.equals(TransactionStatus.DECLINED.name())){
            setStatusCase(getResources().getColor(R.color.fail),
                    info.statusTitle.isEmpty() ?  getResources().getString(R.string.status_msg_declined) : info.statusTitle);
        } else if (info.status.equals(TransactionStatus.PARTIALLY_APPROVED.name())){
            setStatusCase(getResources().getColor(R.color.partially_success),
                    info.statusTitle.isEmpty() ?  getResources().getString(R.string.status_msg_partially_approved) : info.statusTitle);
        } else {
            // default status should show gif in background
            imageView.setVisibility(View.VISIBLE);
            backgroundView.setBackgroundColor(getResources().getColor(R.color.normal_background));
            messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            //[POSUI-359] Limit the title line to display at most one line.
            setupAutoScaleTextView(titleTextView, 26, 1, Typeface.NORMAL, 20);
            showContent(info.title, info.amount, info.msg, info.imageResourceId, "");
        }
    }

    private void setStatusCase(@ColorInt int color, String statusMsg) {
        //show trans status
        imageView.setVisibility(View.GONE);
        backgroundView.setBackgroundColor(color);
        setupAutoScaleTextView(statusMessageTextView, 40, 3, Typeface.BOLD, 26);
        showContent("", "", "", null, statusMsg);
    }

    public void showContent(String title,
                            String amount,
                            String message, Integer imageId,
                            String statusMessage) {
        // there maybe show info below in second screen.
        titleTextView.setVisibility(!title.isEmpty() ? View.VISIBLE : View.GONE);
        amountTextView.setVisibility(!amount.isEmpty() ? View.VISIBLE : View.GONE);
        messageTextView.setVisibility(!message.isEmpty() ? View.VISIBLE : View.GONE);
        imageTapView.setVisibility(imageId != null ? View.VISIBLE: View.GONE);
        statusMessageTextView.setVisibility(!statusMessage.isEmpty() ? View.VISIBLE: View.GONE);

        if (!title.isEmpty()) titleTextView.setText(title);
        if (!amount.isEmpty()) amountTextView.setText(amount);
        if (!message.isEmpty())messageTextView.setText(message);
        if (!statusMessage.isEmpty())statusMessageTextView.setText(statusMessage);
        if (imageId != null) imageTapView.setImageResource(imageId);
    }
}
