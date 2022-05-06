package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.enumeration.TransMode;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.event.EntryAbortEvent;
import com.paxus.pay.poslinkui.demo.event.EntryAcceptedEvent;
import com.paxus.pay.poslinkui.demo.event.EntryDeclinedEvent;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

//TODO Yanina: Grant Permission for RECEIPT_URI
public class ConfirmReceiptViewFragment extends Fragment {
    private String action;
    private String packageName;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadArgument(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadView(view);
        EventBus.getDefault().register(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
        if (receiptOutAnim != null) {
            receiptOutAnim.cancel();
        }
    }

    private void loadArgument(Bundle bundle){
        if(bundle == null){
            return;
        }
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT,30000);

        receiptUri = bundle.getString(EntryExtraData.PARAM_RECEIPT_URI);
    }

    private void loadView(View view){
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
        imageView = view.findViewById(R.id.print_preview);
        Button confirmBtn = view.findViewById(R.id.confirm_button);
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
            e.printStackTrace();
//            tickTimerStop();
            Toast.makeText(requireContext(), getString(R.string.receipt_image_too_long),Toast.LENGTH_SHORT).show();
            sendAbort();
        }


    }

    private void sendNext(boolean confirm){
        EntryRequestUtils.sendNext(requireContext(), packageName, action,EntryRequest.PARAM_CONFIRMED,confirm);
    }

    private void sendAbort(){
        EntryRequestUtils.sendAbort(requireContext(), packageName, action);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEntryAbort(EntryAbortEvent event) {
        // Do something
        sendAbort();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEntryAccepted(EntryAcceptedEvent event) {
        // Do something
        Toast.makeText(requireActivity(),"Accepted",Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEntryDeclined(EntryDeclinedEvent event) {
        // Do something

        Toast.makeText(requireActivity(),event.code+"-"+event.message,Toast.LENGTH_SHORT).show();
    }



}
