package com.paxus.pay.poslinkui.demo.entry;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;

public class EmptyFragment extends Fragment {

    private String senderPackage;
    private String action;

    public EmptyFragment() {
        // Required empty public constructor for newInstance
    }

    public static EmptyFragment newInstance(String senderPackage, String action) {
        EmptyFragment emptyFragment = new EmptyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("senderPackage", senderPackage);
        bundle.putString("action", action);
        emptyFragment.setArguments(bundle);
        return emptyFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            senderPackage = getArguments().getString("senderPackage");
            action = getArguments().getString("action");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_empty, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("keyCode", this, ((requestKey, result) -> {
            if(result.getInt("keyCode") == KeyEvent.KEYCODE_BACK) {
                EntryRequestUtils.sendAbort(requireContext(), senderPackage, action);
            }
        }));
    }

}
