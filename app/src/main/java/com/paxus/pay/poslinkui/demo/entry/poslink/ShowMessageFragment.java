package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.PoslinkEntry;
import com.pax.us.pay.ui.constant.status.POSLinkStatus;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.view.TextField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Implement POSLink Entry actions:<br>
 * {@value PoslinkEntry#ACTION_SHOW_MESSAGE}
 */
public class ShowMessageFragment extends BaseEntryFragment {
    private String title;
    private String tax;
    private String total;
    private String imgUrl;
    private String imgDesc;
    private List<MsgInfoWrapper> messages;

    //Interfaces of POSLink Category may need to listen to POSLinkStatus Broadcasts
    private POSLinkStatusManager posLinkStatusManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        posLinkStatusManager = new POSLinkStatusManager(getContext(), getViewLifecycleOwner());
        posLinkStatusManager.registerHandler(POSLinkStatus.CLEAR_MESSAGE, this::clearMessage);
    }

    private void clearTitle() {
        title = null;
    }

    private void clearTaxandTotal() {
        tax = null;
        total = null;
    }
    private void clearImageAndDesc() {
        imgUrl = null;
        imgDesc = null;
    }

    private void clearMessage() {
        clearTitle(); // BPOSANDJAX-1283
        clearTaxandTotal(); // POSUI-294
        clearImageAndDesc();
        messages.clear();
        loadView(getView());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_show_message;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        title = bundle.getString(EntryExtraData.PARAM_TITLE,"");
        tax = bundle.getString(EntryExtraData.PARAM_TAX_LINE,"");
        total = bundle.getString(EntryExtraData.PARAM_TOTAL_LINE,"");
        imgUrl = bundle.getString(EntryExtraData.PARAM_IMAGE_URL,"");
        imgDesc = bundle.getString(EntryExtraData.PARAM_IMAGE_DESC,"");

        String messageList = bundle.getString(EntryExtraData.PARAM_MESSAGE_LIST);
        if(messageList != null) {
            messages = parseMessageList(messageList);
        }
        else {
            messages = new ArrayList<>();  // POSUI-300
        }
    }

    @Override
    protected void loadView(View rootView) {
        LinearLayout titleLayout = rootView.findViewById(R.id.title_layout_show_message);
        titleLayout.removeAllViews();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        TextShowingUtils.getTitleViewListAsync(
                requireContext(),
                title,
                lp,
                Color.WHITE,
                requireContext().getResources().getDimension(R.dimen.text_size_subtitle),
                true,
                titleLayout
        );

        ListView listView = rootView.findViewById(R.id.list_view);

        MessageItemAdapter adapter = new MessageItemAdapter(requireContext(), R.layout.item_layout_msg_list, messages);
        listView.setAdapter(adapter);
        TextView taxLine = rootView.findViewById(R.id.tax_line);
        if(!TextUtils.isEmpty(tax)) {
            taxLine.setText(tax);
        }else {
            rootView.findViewById(R.id.tax_layout).setVisibility(View.INVISIBLE);
        }

        TextView totalLine = rootView.findViewById(R.id.total_line);
        if(!TextUtils.isEmpty(total)) {
            totalLine.setText(total);
        }else {
            rootView.findViewById(R.id.total_layout).setVisibility(View.INVISIBLE);
        }


        ImageView msgImgView = rootView.findViewById(R.id.img_view_show_message);
        LinearLayout llDescMsgLayout = rootView.findViewById(R.id.ll_desc_list_show_message);

        if (!TextUtils.isEmpty(imgUrl)) {
            msgImgView.setVisibility(View.VISIBLE);
            Glide.with(this).load(imgUrl).into(msgImgView);
            if (!TextUtils.isEmpty(imgDesc)) {
                llDescMsgLayout.setVisibility(View.VISIBLE);
                new Handler(Looper.getMainLooper()).post(()->{
                    for (TextView textView: TextShowingUtils.getTitleViewList(requireContext(), imgDesc, lp, Color.WHITE, requireContext().getResources().getDimension(R.dimen.text_size_subtitle),true)) {
                        llDescMsgLayout.addView(textView);
                    }
                });
            } else {
                llDescMsgLayout.setVisibility(View.GONE);
            }
        } else {
            llDescMsgLayout.setVisibility(View.GONE);
            msgImgView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isActive()) {
            sendNext(null);
        }
    }



    private List<MsgInfoWrapper> parseMessageList(String jsonString) {
        try {
            List<MsgInfoWrapper> list = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                MsgInfoWrapper wrapper = new MsgInfoWrapper();
                JSONObject obj = jsonArray.getJSONObject(i);
                String index = obj.getString("index");
                wrapper.setIndex(index);

                JSONObject msgObj = obj.getJSONObject("MsgInfo");
                wrapper.setMsgInfo(new MsgInfoWrapper.MsgInfo(msgObj.getString("msg1"), msgObj.getString("msg2")));

                list.add(wrapper);
            }

            return list;
        } catch (JSONException e) {
            Logger.e(e);
        }

        return null;
    }


    @Override
    protected TextField[] focusableTextFields() {
        return null;
    }
}
