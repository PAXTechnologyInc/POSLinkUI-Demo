package com.paxus.pay.poslinkui.demo.entry.information;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.enumeration.CardType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.view.GifLoadOneTimeGif;

public class DisplayApproveMessageFragment extends DialogFragment {
    private String action;
    private String packageName;
    private long timeout;
    private String soundUri;
    private boolean soundEnabled;
    private boolean animationEnabled;
    private String cardType;

    private ImageView animView;

    public static DialogFragment newInstance(Intent intent){
        DisplayApproveMessageFragment dialogFragment = new DisplayApproveMessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());

        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_approve_msg, container, false);
        loadParameter(getArguments());
        loadView(view);

        Dialog dialog = getDialog();
        if(dialog!= null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        return view;
    }

    private void loadParameter(Bundle bundle){
        if(bundle == null){
            Log.e("DisplayApproveMessage","No arguments");
            return;
        }
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        timeout = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);
        soundUri = bundle.getString(EntryExtraData.PARAM_SOUND_URI);
        soundEnabled = bundle.getBoolean(EntryExtraData.PARAM_SOUND_SUPPORT);
        animationEnabled = bundle.getBoolean(EntryExtraData.PARAM_ANIMATION_SUPPORT);
        cardType = bundle.getString(EntryExtraData.PARAM_CARD_TYPE);
    }
    private void loadView(View view){
         animView = view.findViewById(R.id.image_view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(CardType.VISA.equals(cardType)) {
            if (animationEnabled) {
                animView.clearAnimation();
                GifLoadOneTimeGif.loadOneTimeGif(requireContext(), R.raw.visa_animation, animView, 1, new GifLoadOneTimeGif.GifListener() {
                    @Override
                    public void gifPlayComplete() {
                        sendNext();
                    }
                });
            }
            if (soundEnabled) {
                playSound(R.raw.visa_sound);
            }
        }else {
            if(soundEnabled){
                //TODO Bug fix, Grant Permission for sound uri
//                if(!TextUtils.isEmpty(soundUri)){
//                    playSound(soundUri);
//                }
                playSound(R.raw.boba);

            }else{
                sendNext();
            }
        }
    }

    private void playSound(int resourcesId) {
        //Play sound in background
        //Fix ANFDRC-977
        Intent intent = new Intent(requireContext(), PlayerService.class);
        intent.putExtra(PlayerService.PARAM_RESOURCE_ID,resourcesId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().startForegroundService(intent);
        } else {
            requireContext().startService(intent);
        }
        if(!animationEnabled || !CardType.VISA.equals(cardType)){
            sendNext();
        }
    }

    private void playSound(String soundUri) {
        //Fix ANFDRC-977
        Intent intent = new Intent(requireContext(),PlayerService.class);
        intent.putExtra(PlayerService.PARAM_APPROVAL_SOUND,soundUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().startForegroundService(intent);
        } else {
            requireContext().startService(intent);
        }

        if(!animationEnabled || !CardType.VISA.equals(cardType)){
            sendNext();
        }
    }


    private void sendNext(){
        dismiss();

        EntryRequestUtils.sendNext(requireContext(),packageName,action);
    }
}
