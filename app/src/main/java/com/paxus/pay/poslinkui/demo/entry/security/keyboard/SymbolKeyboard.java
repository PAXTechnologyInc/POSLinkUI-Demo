package com.paxus.pay.poslinkui.demo.entry.security.keyboard;

import com.paxus.pay.poslinkui.demo.R;

public class SymbolKeyboard extends SecureKeyboard{
    @Override
    protected int getLayoutResourceId() {
        return R.layout.secure_keyboard_view_alphanumeric_symbol;
    }
}
