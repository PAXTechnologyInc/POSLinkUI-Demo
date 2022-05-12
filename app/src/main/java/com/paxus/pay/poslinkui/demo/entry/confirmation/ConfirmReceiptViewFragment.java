package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.enumeration.TransMode;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

//TODO Yanina: Grant Permission for RECEIPT_URI
public class ConfirmReceiptViewFragment extends BaseEntryFragment {
    private String transType;
    private long timeOut;
    private String transMode;

    private String receiptUri;

    private ImageView imageView;
    private Animation receiptOutAnim;
    public static ConfirmReceiptViewFragment newInstance(Intent intent){
        ConfirmReceiptViewFragment numFragment = new ConfirmReceiptViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());
        numFragment.setArguments(bundle);
        return numFragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_receipt_view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("POSLinkUIDemo","ConfirmReceiptView onDestroy");

        EventBus.getDefault().unregister(this);
        if (receiptOutAnim != null) {
            receiptOutAnim.cancel();
        }
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);

        receiptUri = bundle.getString(EntryExtraData.PARAM_RECEIPT_URI);
    }

    @Override
    protected void loadView(View rootView) {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(getString(R.string.receipt_preview));
        }

        String mode = null;
        if(!TextUtils.isEmpty(transMode)){
            if(TransMode.DEMO.equals(transMode)){
                mode = getString(R.string.demo_only);
            }else if(TransMode.TEST.equals(transMode)){
                mode = getString(R.string.test_only);
            }else if(TransMode.TEST_AND_DEMO.equals(transMode)){
                mode = getString(R.string.test_and_demo);
            }else {
                mode = "";
            }
        }
        if(!TextUtils.isEmpty(mode)){
            ViewUtils.addWaterMarkView(requireActivity(),mode);
        }else{
            ViewUtils.removeWaterMarkView(requireActivity());
        }

        receiptOutAnim = AnimationUtils.loadAnimation(requireActivity(), R.anim.receipt_out);
        imageView = rootView.findViewById(R.id.print_preview);
        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.startAnimation(receiptOutAnim);
                sendNext(true);
            }
        });

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
        }


    }

    private void sendNext(boolean confirm){
        EntryRequestUtils.sendNext(requireContext(), packageName, action,EntryRequest.PARAM_CONFIRMED,confirm);
    }

}
