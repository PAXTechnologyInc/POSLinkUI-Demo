package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Collections;
import java.util.List;

/**
 * Implement text entry actions:<br>
 * {@value PoslinkEntry#ACTION_SHOW_ITEM}
 *
 * <p>
 * UI Tips:
 * </p>
 */
public class ShowItemFragment extends BaseEntryFragment {
    private String title;
    private String taxLine;
    private String totalLine;
    private String currencySymbol;
    private boolean topDown;
    private List<ItemDetailWrapper> itemWrapperList = new ArrayList<>();

    private RecyclerView.Adapter itemListAdapter;

    //Interfaces of POSLink Category may need to listen to POSLinkStatus Broadcasts
    private POSLinkStatusManager posLinkStatusManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        posLinkStatusManager = new POSLinkStatusManager(getContext(), getViewLifecycleOwner());
        posLinkStatusManager.registerHandler(POSLinkStatus.CLEAR_MESSAGE, this::clearMessage);
        sendNext(null);
    }

    private void clearMessage() {
        int itemCount = itemWrapperList.size();
        itemWrapperList.clear();
        itemListAdapter.notifyItemRangeRemoved(0, itemCount);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_show_item;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        title = bundle.getString(EntryExtraData.PARAM_TITLE);
        taxLine = bundle.getString(EntryExtraData.PARAM_TAX_LINE);
        totalLine = bundle.getString(EntryExtraData.PARAM_TOTAL_LINE);
        currencySymbol = bundle.getString(EntryExtraData.PARAM_CURRENCY_SYMBOL);
        topDown = bundle.getBoolean(EntryExtraData.PARAM_TOP_DOWN, true);

        itemWrapperList = parseItemList(bundle.getString(EntryExtraData.PARAM_MESSAGE_LIST));
    }

    @Override
    protected void loadView(View rootView) {
        LinearLayout titleLayout = rootView.findViewById(R.id.tv_title_show_item);
        TextView tvTotalLineShowItem = rootView.findViewById(R.id.tv_total_line_show_item);
        TextView tvTaxLineShowItem = rootView.findViewById(R.id.tv_tax_line_show_item);
        TextView tvTotalLineTitle = rootView.findViewById(R.id.total_line_title);
        TextView tvTaxLineTitle = rootView.findViewById(R.id.tax_line_title);
        RecyclerView recyclerViewShowItem = rootView.findViewById(R.id.recycler_View_show_item);

        if (!TextUtils.isEmpty(taxLine)) {
            tvTaxLineTitle.setVisibility(View.VISIBLE);
            tvTaxLineShowItem.setVisibility(View.VISIBLE);
        } else {
            tvTaxLineTitle.setVisibility(View.GONE);
            tvTaxLineShowItem.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(totalLine)) {
            tvTotalLineTitle.setVisibility(View.VISIBLE);
            tvTotalLineShowItem.setVisibility(View.VISIBLE);
        } else {
            tvTotalLineTitle.setVisibility(View.GONE);
            tvTotalLineShowItem.setVisibility(View.GONE);
        }

        if (title == null || title.isEmpty()) {
            titleLayout.setVisibility(View.GONE);
        } else {
            for (TextView textView : TextShowingUtils.getTextViewList(requireContext(), title)) {
                titleLayout.addView(textView);
            }
            titleLayout.setVisibility(View.VISIBLE);
        }
        tvTotalLineShowItem.setText(totalLine);
        tvTaxLineShowItem.setText(taxLine);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewShowItem.setLayoutManager(linearLayoutManager);
        recyclerViewShowItem.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        if (itemWrapperList != null) {
            itemListAdapter = new ItemListAdapter(requireContext(), topDown ? itemWrapperList : reverseList(itemWrapperList), currencySymbol);
            recyclerViewShowItem.setAdapter(itemListAdapter);
            recyclerViewShowItem.scrollToPosition(topDown ? itemWrapperList.size() - 1 : 0);
        }

        ConstraintLayout.LayoutParams recyclerViewLayoutParams = (ConstraintLayout.LayoutParams) recyclerViewShowItem.getLayoutParams();
        recyclerViewLayoutParams.verticalBias = topDown ? 0.0f : 1.0f;
        recyclerViewShowItem.setLayoutParams(recyclerViewLayoutParams);
    }

    private List<ItemDetailWrapper> reverseList(List<ItemDetailWrapper> itemWrapperList) {
        List<ItemDetailWrapper> reversedList = new ArrayList<>(itemWrapperList);
        Collections.reverse(reversedList);
        return reversedList;
    }

    private List<ItemDetailWrapper> parseItemList(String jsonString) {
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
        } catch (JSONException | NullPointerException e) {
            Logger.e(e);
        }

        return null;
    }

}
