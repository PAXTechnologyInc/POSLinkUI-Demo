package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.PoslinkEntry;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.EntryRequestUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Implement text entry actions:<br>
 * {@value PoslinkEntry#ACTION_SHOW_ITEM}
 *
 * <p>
 *     UI Tips:
 * </p>
 */
public class ShowItemFragment  extends BaseEntryFragment {

    private String transMode;
    private String title;
    private String taxLine;
    private String totalLine;
    private String currencySymbol;

    RecyclerView recyclerViewShowItem;
    TextView tvTotalLineShowItem;
    TextView tvTaxLineShowItem;
    LinearLayout titleLayout;

    private List<ItemDetailWrapper> itemWrapperList = new ArrayList<>();
    private RecyclerView.Adapter itemListAdapter;

    public static Fragment newInstance(Intent intent) {
        Fragment fragment = new ShowItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_ACTION, intent.getAction());
        bundle.putAll(intent.getExtras());

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_show_item;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        action = bundle.getString(EntryRequest.PARAM_ACTION);
        packageName = bundle.getString(EntryExtraData.PARAM_PACKAGE);
        transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);

        title = bundle.getString(EntryExtraData.PARAM_TITLE);
        taxLine = bundle.getString(EntryExtraData.PARAM_TAX_LINE);
        totalLine = bundle.getString(EntryExtraData.PARAM_TOTAL_LINE);
        currencySymbol = bundle.getString(EntryExtraData.PARAM_CURRENCY_SYMBOL);

        itemWrapperList = parseItemList(bundle.getString(EntryExtraData.PARAM_MESSAGE_LIST));

    }

    @Override
    protected void loadView(View rootView) {
        titleLayout = rootView.findViewById(R.id.tv_title_show_item);
        tvTotalLineShowItem = rootView.findViewById(R.id.tv_total_line_show_item);
        tvTaxLineShowItem = rootView.findViewById(R.id.tv_tax_line_show_item);
        recyclerViewShowItem = rootView.findViewById(R.id.recycler_View_show_item);


        if(title == null || title.isEmpty()){
            titleLayout.setVisibility(View.GONE);
        }else {
            for(TextView textView: TextShowingUtils.getTextViewList(requireContext(),title)){
                titleLayout.addView(textView);
            }
            titleLayout.setVisibility(View.VISIBLE);
        }
        tvTotalLineShowItem.setText(totalLine);
        tvTaxLineShowItem.setText(taxLine);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewShowItem.setLayoutManager(linearLayoutManager);
        recyclerViewShowItem.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        if(itemWrapperList != null) {
            itemListAdapter = new ItemListAdapter(requireContext(), itemWrapperList, currencySymbol);
            recyclerViewShowItem.setAdapter(itemListAdapter);
        }

        sendNext();
    }

    private void sendNext() {
        EntryRequestUtils.sendNext(requireContext(), packageName, action);
    }

    private List<ItemDetailWrapper> parseItemList(String jsonString){
        try {
            List<ItemDetailWrapper> list = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                ItemDetailWrapper item = new ItemDetailWrapper();

                JSONObject obj = jsonArray.getJSONObject(i);
                String index = obj.optString("index");
                item.setIndex(index);

                JSONObject msgObj = obj.getJSONObject("ItemDetail");
                item.setItemDetail(new ItemDetailWrapper.ItemDetail(
                        msgObj.optString("productName"),
                        msgObj.optString("plUcode"),
                        msgObj.optDouble("price"),
                        msgObj.optString("unit"),
                        msgObj.optDouble("unitPrice"),
                        msgObj.optString("tax"),
                        msgObj.optString("quantity"),
                        msgObj.optString("productImgUri"),
                        msgObj.optString("productImgDesc")
                        ));
                list.add(item);
            }

            return list;
        } catch (JSONException e) {
            Logger.e(e);
        }

        return null;
    }

}
