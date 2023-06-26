package com.paxus.pay.poslinkui.demo.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectOptionsView extends RecyclerView {
    private boolean isInitialized = false, isModified = false;
    private OptionAdapter adapter;
    private Set<SelectOptionsView> siblings = new HashSet<>();
    private Activity context;

    public SelectOptionsView(@NonNull Context context) {
        super(context);
    }

    public SelectOptionsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectOptionsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initialize(Activity context, int columnCount, List<Option> options, OptionSelectListener listener) {
        this.adapter = new OptionAdapter(context, options, listener);
        setLayoutManager(new GridLayoutManager(context, columnCount));
        this.context = context;
        this.isInitialized = true;
        this.setAdapter(this.adapter);
    }

    public void clear(){
        if(!isInitialized){
            Logger.e(getClass().getSimpleName() + " not yet initialized. " + new Object(){}.getClass().getEnclosingMethod().getName() + " cannot be executed.");
            return;
        }

        if(isModified){
            this.adapter.unselectAllOptions((ViewGroup) this.getRootView());
            this.isModified = false;
        }

        if(!siblings.isEmpty()){
            for(SelectOptionsView sibling : siblings) {
                if(sibling.isModified) sibling.clear();
            }
        }
    }

    public void append(SelectOptionsView selectOptionsView) {
        if(!selectOptionsView.isInitialized) {
            Logger.e(selectOptionsView.getClass().getSimpleName() + " not yet initialized. " + new Object(){}.getClass().getEnclosingMethod().getName() + " cannot be executed.");
            return;
        }

        for(SelectOptionsView sibling : this.siblings) {
            sibling.addSibling(selectOptionsView);
            selectOptionsView.addSibling(sibling);
        }
        this.siblings.add(selectOptionsView);
        selectOptionsView.addSibling(this);
    }

    private void addSibling(SelectOptionsView selectOptionsView) {
        this.siblings.add(selectOptionsView);
    }

    public static class Option {
        Integer index;
        String title, subtitle;
        Object value;

        public Option(Integer index, String title, String subtitle, Object value) {
            this.index = index;
            this.title = title;
            this.subtitle = subtitle;
            this.value = value;
        }

        public Object getValue() {
            return value;
        }
    }

    @FunctionalInterface
    public interface OptionSelectListener {
        void onSelect(Option option);
    }

    private class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.OptionHolder> {
        private List<Option> options;
        private LayoutInflater layoutInflater;
        private OptionSelectListener optionSelectListener;
        private boolean isDoubleHeightRequired = false;

        OptionAdapter(Context context, List<Option> options, OptionSelectListener optionSelectListener) {
            this.options = options;
            this.layoutInflater = LayoutInflater.from(context);
            this.optionSelectListener = optionSelectListener;
            for(Option option : this.options) {
                if(option.title != null && option.subtitle != null) {
                    isDoubleHeightRequired = true;
                    break;
                }
            }
        }

        @NonNull
        @Override
        public OptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View holder = layoutInflater.inflate(R.layout.layout_select_option_item, parent, false);
            if(isDoubleHeightRequired){
                ViewGroup.LayoutParams layoutParams = holder.getLayoutParams();
                layoutParams.height = (int) getResources().getDimension(R.dimen.button_height_double_line);
                holder.setLayoutParams(layoutParams);
            }
            return new OptionHolder(holder);
        }

        @Override @SuppressLint("SetTextI18n")
        public void onBindViewHolder(@NonNull OptionHolder holder, int position) {
            Option option = options.get(position);
            SpannableStringBuilder text = new SpannableStringBuilder();

            if (option.title != null) {
                text.append(option.title);
                text.setSpan(new AbsoluteSizeSpan((int) getResources().getDimension(R.dimen.text_size_title)), 0, option.title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
            if (option.subtitle != null) {
                text.append(option.title != null ? "\n" : "").append(option.subtitle);
                text.setSpan(new AbsoluteSizeSpan((int) (option.title != null ? getResources().getDimension(R.dimen.text_size_hint) : getResources().getDimension(R.dimen.text_size_title))),
                        text.length() - option.subtitle.length(), text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                text.setSpan(new StyleSpan(Typeface.BOLD), 0, option.title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            holder.text.setText(text);
            holder.bindListener(option, optionSelectListener);
        }

        @Override
        public int getItemCount() {
            return options.size();
        }

        private void unselectAllOptions(ViewGroup parent) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                if (parent.getChildAt(i) instanceof CheckedTextView)
                    ((CheckedTextView) parent.getChildAt(i)).setChecked(false);
                else if (parent.getChildAt(i) instanceof ViewGroup)
                    unselectAllOptions((ViewGroup) parent.getChildAt(i));
            }
        }

        private class OptionHolder extends RecyclerView.ViewHolder {
            public TextView text;

            public OptionHolder(@NonNull View itemView) {
                super(itemView);
                text = itemView.findViewById(R.id.layout_select_option_item_text);
                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }

            public void bindListener(final Option option, final OptionSelectListener listener) {
                itemView.setOnClickListener(view -> {
                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    listener.onSelect(option);
                    clear();
                    ((CheckedTextView) text).setChecked(true);
                    isModified = true;
                });
            }
        }

    }
}
