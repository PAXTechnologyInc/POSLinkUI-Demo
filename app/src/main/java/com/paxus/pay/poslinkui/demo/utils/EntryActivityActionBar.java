package com.paxus.pay.poslinkui.demo.utils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.paxus.pay.poslinkui.demo.R;

public class EntryActivityActionBar {
    private ActionBar actionBar;
    private Activity activityContext;
    public enum Status { IDLE, PROCESSING, SUCCESS, FAIL }

    public EntryActivityActionBar(Activity context, ActionBar actionBar){
        this.activityContext = context;
        this.actionBar = actionBar;
        this.initialize();
    }

    private void initialize() {
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(0);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        ConstraintLayout actionBarView = (ConstraintLayout) activityContext.getLayoutInflater().inflate(R.layout.action_bar, null);
        actionBar.setCustomView(actionBarView, new ActionBar.LayoutParams(MATCH_PARENT, (int) activityContext.getResources().getDimension(R.dimen.toolbar_height)));

        ((ViewGroup)actionBar.getCustomView().getParent()).setPadding(0,0,0,0);
    }

    public EntryActivityActionBar setTitle(String title){
        ((TextView) actionBar.getCustomView().findViewById(R.id.action_bar_title)).setText(title);
        return this;
    }

    public EntryActivityActionBar setStatus(Status status, boolean loop) {
        ImageView icon = ((ImageView) actionBar.getCustomView().findViewById(R.id.action_bar_icon));

        switch (status) {
            case PROCESSING:
                break;

            case SUCCESS:
                break;

            case FAIL:
                break;

            case IDLE:
            default:
                break;

        }
        icon.setVisibility(View.GONE);
        return this;
    }

    public EntryActivityActionBar swapActionBar(ActionBar actionBar){
        this.actionBar = actionBar;
        this.initialize();
        return this;
    }

}
