package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;

import java.util.Hashtable;

/**
 * Implement confirmation entry action {@value ConfirmationEntry#ACTION_DISPLAY_QR_CODE_RECEIPT}
 * <p>
 * UI Tips:
 * 1.If click BACK, sendNext()
 * </p>
 */
public class DisplayQRCodeReceiptFragment extends BaseEntryFragment {
    private String action;
    private String packageName;
    private String qrCodeContent;
    private long timeout;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_display_qr_receipt;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        timeout = bundle.getLong(EntryExtraData.PARAM_TIMEOUT);
        qrCodeContent = bundle.getString(EntryExtraData.PARAM_QR_CODE_CONTENT);
    }

    @Override
    protected void loadView(View rootView) {
        if (qrCodeContent != null && !qrCodeContent.isEmpty()) {
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.qr_code_logo);
            Bitmap bitmap = createQRCode(qrCodeContent, 600, logo);
            if (bitmap == null) {
                Logger.e(getString(R.string.receipt_image_too_larger));
                sendAbort();
            } else {
                ImageView imageView = rootView.findViewById(R.id.qrcode_view);
                imageView.setImageBitmap(bitmap);
            }
        } else {
            Logger.e("NO QR Code Content.");
            sendAbort();
        }
    }

    @Override
    protected String getSenderPackageName() {
        return packageName;
    }

    @Override
    protected String getEntryAction() {
        return action;
    }

    private Bitmap createQRCode(String content, int qrCodeSize, Bitmap logo) {
        Matrix m = new Matrix();
        int imageWidth = 80;
        int imageHeight = 80;
        m.setScale((float) imageWidth / logo.getWidth(), (float) imageHeight / logo.getHeight());
        logo = Bitmap.createBitmap(logo, 0, 0,
                logo.getWidth(), logo.getHeight(), m, false);
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable<EncodeHintType, Object> hst = new Hashtable<>();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix matrix = null;
        try {
            matrix = writer.encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hst);
        } catch (Exception e) {
            Logger.e(e);
            return null;
        }
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];
        Logger.d("BitMatrix Size:" + width + "x" + height + ",content:" + content);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x > halfW - imageWidth / 2 && x < halfW + imageWidth / 2
                        && y > halfH - imageHeight / 2 && y < halfH + imageHeight / 2) {
                    pixels[y * width + x] = logo.getPixel(x - halfW
                            + imageWidth / 2, y - halfH + imageHeight / 2);
                } else {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    }
                }

            }
        }
        logo.recycle();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    @Override
    protected void executeBackPressEvent() {
        EntryRequestUtils.sendNext(requireContext(), getSenderPackageName(), getEntryAction());
    }
}
