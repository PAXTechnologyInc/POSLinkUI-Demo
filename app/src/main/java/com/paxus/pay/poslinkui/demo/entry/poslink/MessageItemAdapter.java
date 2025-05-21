package com.paxus.pay.poslinkui.demo.entry.poslink;


import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.StringUtils;

import java.util.List;

public class MessageItemAdapter extends ArrayAdapter<ShowMessageFragment.MsgInfoWrapper> {

    private final Context context;
    private final int layoutResourceId;
    private List<ShowMessageFragment.MsgInfoWrapper> dataList;

    public MessageItemAdapter(Context context, int layoutResourceId, List<ShowMessageFragment.MsgInfoWrapper> dataList) {
        super(context, layoutResourceId, dataList);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.dataList = dataList;
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
            // 移除之前动态添加的 TextView，避免重复添加
            holder.llMsg1.removeAllViews();
            holder.llMsg2.removeAllViews();
        }
        ShowMessageFragment.MsgInfoWrapper currentItem = getItem(position);
        String msg1 = currentItem.getMsgInfo().msg1;
        String msg2 = currentItem.getMsgInfo().msg2;
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
