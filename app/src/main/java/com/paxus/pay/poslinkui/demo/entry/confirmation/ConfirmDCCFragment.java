package com.paxus.pay.poslinkui.demo.entry.confirmation;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.pax.us.pay.ui.constant.entry.ConfirmationEntry;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;

/**
 * Implement confirmation entry action {@link ConfirmationEntry#ACTION_CONFIRM_DCC} <br>
 * <p>
 * UI Tips:
 * 1.If {@link EntryExtraData#PARAM_CONFIRM_WITH_CURRENCY} is true,
 * if click USD, sendNext(true)
 * if click {@link EntryExtraData#PARAM_CURRENCY_ALPHA_CODE}, sendNext(false)
 * 2.If {@link EntryExtraData#PARAM_CONFIRM_WITH_CURRENCY} is false,
 * if click Agree, sendNext(true)
 * if click Cancel, sendNext(false)
 * </p>
 */
public class ConfirmDCCFragment extends AConfirmationFragment {
    private String amountMessage;
    private String exchangeRate;
    private String currencyAlfCode;
    private String margin;
    private String foreignAmountMessage;
    private boolean confirmWithCurrency;

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        amountMessage = bundle.getString(EntryExtraData.PARAM_AMOUNT_MESSAGE);
        exchangeRate = bundle.getString(EntryExtraData.PARAM_EXCHANGE_RATE);
        currencyAlfCode = bundle.getString(EntryExtraData.PARAM_CURRENCY_ALPHA_CODE);
        foreignAmountMessage = bundle.getString(EntryExtraData.PARAM_FOREIGN_AMOUNT_MESSAGE);
        confirmWithCurrency = bundle.getBoolean(EntryExtraData.PARAM_CONFIRM_WITH_CURRENCY);
        margin = bundle.getString(EntryExtraData.PARAM_MARGIN);

    }

    @Override
    protected String formatMessage(String message) {
        StringBuilder contentMsg = new StringBuilder();
        if (!TextUtils.isEmpty(amountMessage)) {
            contentMsg.append("USD ").append(amountMessage).append("\n");
        }
        if (!TextUtils.isEmpty(exchangeRate) && !TextUtils.isEmpty(currencyAlfCode)) {
            contentMsg.append("1 USD = ").append(exchangeRate).append(" ").append(currencyAlfCode).append("\n");
        }
        if (!TextUtils.isEmpty(margin)) {
            contentMsg.append("Int'l Margin ").append(margin).append("%\n");
        }
        if (!TextUtils.isEmpty(foreignAmountMessage) && !TextUtils.isEmpty(currencyAlfCode)) {
            contentMsg.append(currencyAlfCode).append(" ").append(foreignAmountMessage);
        }
        return contentMsg.toString();
    }

    @Override
    @NonNull protected String getPositiveText() {
        if (confirmWithCurrency && !TextUtils.isEmpty(currencyAlfCode)) {
            return "USD";
        } else {
            return "Agree";
        }
    }

    @Override
    protected String getNegativeText() {
        if (confirmWithCurrency && !TextUtils.isEmpty(currencyAlfCode)) {
            return currencyAlfCode;
        } else {
            return "Cancel";
        }
    }
}
