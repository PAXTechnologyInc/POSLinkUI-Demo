package com.paxus.pay.poslinkui.demo.entry.text;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.view.AmountTextWatcher;

/**
 * Fragment which used to input fsa amount
 * See {@link FSAFragment}
 */
public class FSAAmountFragment extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_MIN_LENGTH = "min_length";
    private static final String ARG_MAX_LENGTH = "max_length";
    private static final String ARG_TOTAL_AMOUNT = "total_amount";
    private static final String ARG_CURRENCY = "currency";

    public static final String RESULT = "result";
    public static final String VALUE = "value";

    private int minLength;
    private int maxLength;
    private String message = "";
    private String currency = "";
    private long totalAmount = 0;

    private EditText editText;




    public static Fragment newInstance(String title, int minLength, int maxLength, String currency, long totalAmount){
        FSAAmountFragment fragment = new FSAAmountFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        bundle.putInt(ARG_MIN_LENGTH, minLength);
        bundle.putInt(ARG_MAX_LENGTH, maxLength);
        bundle.putString(ARG_CURRENCY,currency);
        bundle.putLong(ARG_TOTAL_AMOUNT, totalAmount);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d(this.getClass().getSimpleName()+" onCreateView");

        //1. Load layout in onCreateView (getLayoutResourceId, loadParameter, loadView)
        Bundle bundle = getArguments();
        if(bundle != null) {
            loadArgument(bundle);
        }else {
            Logger.e(this.getClass().getSimpleName()+" arguments missing!!!");
        }

        View view = inflater.inflate(R.layout.fragment_fsa_amount, container, false);
        loadView(view);
        return view;
    }

    protected void loadArgument(@NonNull Bundle bundle) {
        message = bundle.getString(ARG_TITLE);
        currency =  bundle.getString(ARG_CURRENCY, CurrencyType.USD);
        minLength = bundle.getInt(ARG_MIN_LENGTH,0);
        maxLength = bundle.getInt(ARG_MAX_LENGTH,9);
        totalAmount = bundle.getLong(ARG_TOTAL_AMOUNT,0L);
    }

    protected void loadView(View rootView) {
        TextView totalAmtView = rootView.findViewById(R.id.total_amount);
        if(totalAmount>0) {
            totalAmtView.setText(CurrencyUtils.convert(totalAmount,currency));
        }else {
            rootView.findViewById(R.id.amount_layout).setVisibility(View.GONE);
        }
        TextView textView = rootView.findViewById(R.id.message);
        textView.setText(message);

        editText = rootView.findViewById(R.id.edit_amount);
        editText.setSelected(false);
        editText.setText(CurrencyUtils.convert(0,currency));
        editText.setSelection(editText.getEditableText().length());

        editText.addTextChangedListener(new AmountTextWatcher(maxLength, currency));
        editText.requestFocus();

        Button confirmBtn = rootView.findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(v -> onConfirmButtonClicked());

    }

    private void onConfirmButtonClicked(){
        long value = CurrencyUtils.parse(editText.getText().toString());
        if(String.valueOf(value).length() < minLength){
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }else {
            Bundle bundle = new Bundle();
            bundle.putLong(VALUE, value);
            getParentFragmentManager().setFragmentResult(RESULT, bundle);
        }
    }

}
