package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.PoslinkEntry;
import com.pax.us.pay.ui.constant.status.POSLinkStatus;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.Logger;

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

    private void clearMessage() {
        clearTitle(); // BPOSANDJAX-1283
        clearTaxandTotal(); // POSUI-294
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
        TextView textView = rootView.findViewById(R.id.title_view);
        textView.setText(title);

        ListView listView = rootView.findViewById(R.id.list_view);
        ArrayAdapter<MsgInfoWrapper> adapter = new ArrayAdapter<MsgInfoWrapper>(requireContext(),
                android.R.layout.simple_list_item_2, android.R.id.text1, messages) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(messages.get(position).getMsgInfo().msg1);
                text2.setText(messages.get(position).getMsgInfo().msg2);
                return view;
            }
        };
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
                wrapper.setMsgInfo(new MsgInfo(msgObj.getString("msg1"), msgObj.getString("msg2")));

                list.add(wrapper);
            }

            return list;
        } catch (JSONException e) {
            Logger.e(e);
        }

        return null;
    }

    //[{"index":null,"MsgInfo":{"msg1":"","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"976&","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"976&","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"976&","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"976&","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"976&","msg2":"8654"}},{"index":null,"MsgInfo":{"msg1":"976&","msg2":"8654"}}]
    private static class MsgInfoWrapper {
        private String index;
        private MsgInfo msgInfo;

        public MsgInfoWrapper() {
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public MsgInfo getMsgInfo() {
            return msgInfo;
        }

        public void setMsgInfo(MsgInfo msgInfo) {
            this.msgInfo = msgInfo;
        }
    }

    private static class MsgInfo{
        public String msg1;
        public String msg2;
        public MsgInfo(String msg1,String msg2){
            this.msg1 = msg1;
            this.msg2 = msg2;
        }
    }
}
