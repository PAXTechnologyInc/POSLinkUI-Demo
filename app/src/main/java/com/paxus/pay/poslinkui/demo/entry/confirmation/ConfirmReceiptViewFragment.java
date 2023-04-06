package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;

import java.io.IOException;

/**
 * Implement confirmation entry action {@value ConfirmationEntry#ACTION_CONFIRM_RECEIPT_VIEW}
 * <p>
 * UI Tips:
 * If print button clicked, sendNext(true)
 * If receipt bitmap parse fail, sendAbort()
 * </p>
 */
public class ConfirmReceiptViewFragment extends BaseEntryFragment {
    private String transType;
    private long timeOut;
    private String transMode;

    private String receiptUri;

    private ImageView imageView;
    private Animation receiptOutAnim;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_receipt_view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (receiptOutAnim != null) {
            receiptOutAnim.cancel();
        }
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);

        receiptUri = bundle.getString(EntryExtraData.PARAM_RECEIPT_URI);
    }

    @Override
    protected void loadView(View rootView) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.receipt_preview));
        }

        receiptOutAnim = AnimationUtils.loadAnimation(requireActivity(), R.anim.receipt_out);
        imageView = rootView.findViewById(R.id.print_preview);
        Button printBtn = rootView.findViewById(R.id.print_button);
        printBtn.setOnClickListener(v->onPrintButtonClicked());

        try {
            Uri imageUri = Uri.parse(receiptUri);
            Bitmap bitmap = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
            } else {
                ImageDecoder.Source source = ImageDecoder.createSource(requireContext().getContentResolver(), imageUri);
                ImageDecoder.OnHeaderDecodedListener listener = new ImageDecoder.OnHeaderDecodedListener() {
                    @Override
                    public void onHeaderDecoded(@NonNull ImageDecoder decoder, @NonNull ImageDecoder.ImageInfo info, @NonNull ImageDecoder.Source source) {
                        decoder.setAllocator(ImageDecoder.ALLOCATOR_SOFTWARE);
                        decoder.setMutableRequired(true);
                    }
                };
                bitmap = ImageDecoder.decodeBitmap(source);
            }
            if (bitmap == null) {
                Toast.makeText(requireContext(), getString(R.string.receipt_image_too_larger),Toast.LENGTH_SHORT).show();
                sendAbort();
            } else {
                imageView.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            Logger.e(e);
            Toast.makeText(requireContext(), getString(R.string.receipt_image_too_long),Toast.LENGTH_SHORT).show();
            sendAbort();
        } catch (Exception e){
            Logger.e(e);
            Toast.makeText(requireContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            sendAbort();
        }
    }

    private void onPrintButtonClicked(){
        imageView.startAnimation(receiptOutAnim);
        Bundle bundle = new Bundle();
        bundle.putBoolean(EntryRequest.PARAM_CONFIRMED, true);
        sendNext(bundle);
    }
}
