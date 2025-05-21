package com.paxus.pay.poslinkui.demo.entry.poslink;

import static com.paxus.pay.poslinkui.demo.entry.poslink.ShowDialogFormCheckBoxFragment.ARG_OPTIONS_CHECKED;
import static com.paxus.pay.poslinkui.demo.entry.poslink.ShowDialogFormRadioFragment.ARG_OPTIONS;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.enumeration.ManageUIConst;
import com.paxus.pay.poslinkui.demo.R;

import java.util.ArrayList;
import java.util.List;

public class LabelAdapter extends RecyclerView.Adapter<LabelAdapter.LabelItemHolder> {
    private final Bundle bundle;
    private final ArrayList<MyItem> labelList;
    private final ColorStateList colorStateList;

    private Context context;
    private SelectCallback callback;

    private int lastCheckedPosition = -1;

    LabelAdapter(Context context, Bundle bundle, SelectCallback selectCallback) {
        this.context = context;
        this.callback = selectCallback;
        this.bundle = bundle;
        labelList = new ArrayList<MyItem>();
        addLabel(bundle.getStringArray(ARG_OPTIONS), bundle.getStringArray(ARG_OPTIONS_CHECKED));
        int colorPrimaryDark = ContextCompat.getColor(context, R.color.white);
        colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        colorPrimaryDark,
                        colorPrimaryDark
                }
        );
    }

    private void addLabel(String[] labels, String[] labelProperties) {
        for (int i = 0; i < 4; i++) {
            MyItem myItem = null;
            if (labels != null && i < labels.length) {
                if (!TextUtils.isEmpty(labels[i])) {
                    myItem = new MyItem();
                    myItem.setText(labels[i]);
                    myItem.setLabelId(i + 1);
                }
            }
            if (labelProperties != null) {
                if (i < labelProperties.length) {
                    if (myItem != null) {
                        myItem.setSelected(!ManageUIConst.LabelProperty.UNCHECKED.equals(labelProperties[i]));
                    }
                } else {
                    if (myItem != null) {
                        myItem.setSelected(false);
                    }
                }
            }
            if (myItem != null) {
                labelList.add(myItem);
            }
        }
    }

    private LinearLayout createItemView(ViewGroup parent, int viewType) {
        LinearLayout ll = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_show_dialog_form_item, parent, false);
        CompoundButton button = createButton(parent.getContext(), viewType);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ll.addView(button, 0, lp);
        return ll;
    }

    @Nullable
    private CompoundButton createButton(Context c, int viewType) {
        CompoundButton b;
        if (viewType == 1) {
            b = new AppCompatRadioButton(c);
        } else if (viewType == 2) {
            b = new CheckBox(c);
        } else {
            return null;
        }

        CompoundButtonCompat.setButtonTintList(b, colorStateList);
        b.setPadding(0, 0, c.getResources().getDimensionPixelSize(R.dimen.default_gap), 0);
        return b;
    }


    @Override
    public LabelItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LabelItemHolder vh = null;

        LinearLayout itemView = createItemView(parent, viewType);

        if (itemView != null) {
            vh = new LabelItemHolder(itemView);
        }

        if (vh != null) {
            vh.itemView.setPadding(parent.getResources().getDimensionPixelSize(R.dimen.default_gap), 0, 0, 0);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(LabelItemHolder holder, int position) {
        LinearLayout linearLayout = holder.linearLayout;
        CompoundButton compoundButton = (CompoundButton) linearLayout.getChildAt(0);
        String labelData = labelList.get(position).getText();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        LinearLayout llTexts = linearLayout.findViewById(R.id.item_texts);
        llTexts.removeAllViews();
        ShowTextBoxController.fromContext(linearLayout.getContext()).defaultColor(Color.WHITE).showText(llTexts, labelData, lp);
        if (compoundButton instanceof CheckBox) {
            compoundButton.setChecked(labelList.get(position).isSelected());
            compoundButton.setOnCheckedChangeListener(null);
            compoundButton.setChecked(labelList.get(position).isSelected());
            compoundButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    labelList.get(clickedPosition).setSelected(isChecked);
                    handleCheckBox(clickedPosition, isChecked);
                }
            });

            holder.itemView.setOnClickListener(v -> {
                compoundButton.toggle();
            });
        } else if (compoundButton instanceof RadioButton) {
            compoundButton.setChecked(labelList.get(position).isSelected());
            compoundButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = holder.getAdapterPosition();
                    if (clickedPosition != RecyclerView.NO_POSITION) {
                        if (clickedPosition != lastCheckedPosition) {
                            if (lastCheckedPosition != -1) {
                                labelList.get(lastCheckedPosition).setSelected(false);
                                notifyItemChanged(lastCheckedPosition);
                            }

                            labelList.get(clickedPosition).setSelected(true);
                            lastCheckedPosition = clickedPosition;
                            notifyItemChanged(clickedPosition);
                            handleRadioButton(clickedPosition, true);
                        }
                    }
                }
            });
            linearLayout.setOnClickListener(v -> {
                compoundButton.performClick();
            });
        }

    }


    public int getSelectedPosition() {
        return lastCheckedPosition;
    }

    public MyItem getSelectedItem() {
        if (lastCheckedPosition != -1 && lastCheckedPosition < labelList.size()) {
            return labelList.get(lastCheckedPosition);
        }
        return null;
    }

    private void handleRadioButton(int position, boolean isChecked) {
        if (isChecked) {
            if (callback != null) {
                callback.onItemSelect(getSelectedItem().labelId);
            }
        }
    }

    private void handleCheckBox(int position, boolean isChecked) {
        labelList.get(position).setSelected(isChecked);
    }

    public void clearCheckedItems() {
        for (MyItem item : labelList) {
            item.setSelected(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        String buttonType = bundle.getString(EntryExtraData.PARAM_BUTTON_TYPE);
        return Integer.parseInt(buttonType);
    }

    @Override
    public int getItemCount() {
        return labelList.size();
    }

    public List<MyItem> getAllSelectedItems() {
        List<MyItem> selectedItems = new ArrayList<>();
        for (MyItem item : labelList) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    class LabelItemHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;

        LabelItemHolder(LinearLayout v) {
            super(v);
            linearLayout = v;
        }
    }

    public interface SelectCallback {
        void onItemSelect(int position);
    }


    public static class MyItem {
        private String text;
        private boolean isSelected;

        private int labelId;

        public MyItem(String text, boolean isSelected, int labelId) {
            this.text = text;
            this.isSelected = isSelected;
            this.labelId = labelId;
        }

        public MyItem() {

        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public int getLabelId() {
            return labelId;
        }

        public void setLabelId(int labelId) {
            this.labelId = labelId;
        }
    }
}
