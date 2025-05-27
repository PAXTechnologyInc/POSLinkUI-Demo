package com.paxus.pay.poslinkui.demo.entry.poslink;


import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.StringUtils;

import java.util.List;

public class MessageItemAdapter extends ArrayAdapter<MsgInfoWrapper> {

    private final Context context;
    private final int layoutResourceId;
    public MessageItemAdapter(Context context, int layoutResourceId, List<MsgInfoWrapper> dataList) {
        super(context, layoutResourceId, dataList);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.llMsg1 = convertView.findViewById(R.id.ll_msg_1);
            holder.llMsg2 = convertView.findViewById(R.id.ll_msg_2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            //  Remove previously added TextViews to avoid duplication."
            holder.llMsg1.removeAllViews();
            holder.llMsg2.removeAllViews();
        }
        MsgInfoWrapper currentItem = getItem(position);
        String msg1 = currentItem.getMsgInfo().getMsg1();
        String msg2 = currentItem.getMsgInfo().getMsg2();
        LinearLayout.LayoutParams msgLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        if (TextUtils.isEmpty(msg1) && TextUtils.isEmpty(msg2)) {
            setMsgView(TextShowingUtils.getViewList(context, " ", msgLp, Color.WHITE, R.dimen.text_size_normal), holder.llMsg1);
        } else {
            setMsgView(TextShowingUtils.getViewList(context, StringUtils.isEmpty(msg1) ? " " : msg1, msgLp, Color.WHITE, R.dimen.text_size_normal), holder.llMsg1);
            if (!StringUtils.isEmpty(msg2)) {
                setMsgView(TextShowingUtils.getViewList(context, StringUtils.isEmpty(msg2) ? " " : msg2, msgLp, Color.WHITE, R.dimen.text_size_normal), holder.llMsg2);
            }
        }

        return convertView;
    }

    private void setMsgView(List<TextView> viewList, LinearLayout msgLayout) {
        for (TextView textView : viewList) {
            textView.setMaxLines(1);
            msgLayout.addView(textView);
        }
    }

    static class ViewHolder {
        LinearLayout llMsg1;
        LinearLayout llMsg2;
    }
}
