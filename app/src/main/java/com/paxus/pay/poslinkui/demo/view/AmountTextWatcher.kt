package com.paxus.pay.poslinkui.demo.view

import android.text.Editable
import android.text.TextWatcher
import com.paxus.pay.poslinkui.demo.utils.CurrencyUtils
import com.paxus.pay.poslinkui.demo.utils.Logger

/**
 * TextWatcher for input format
 * Example: <br></br>
 * if currency is {@value CurrencyType#USD}<br></br>
 * "1" --> "$1.00" <br></br>
 * if currency is {@value CurrencyType#POINT}<br></br>
 * "1" --> "$1.00" <br></br>
 */
open class AmountTextWatcher(private val maxLength: Int, private val currency: String?) :
    TextWatcher {
    protected var mEditing: Boolean = false
    protected var mPreStr: String? = null
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        if (!mEditing) {
            mPreStr = s.toString()
        }
        Logger.d("AmountTextWatcher beforeTextChanged:" + mPreStr)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Logger.d("AmountTextWatcher onTextChanged:" + mPreStr)
    }

    override fun afterTextChanged(s: Editable) {
        if (!mEditing) {
            mEditing = true
            var value = s.toString().replace("[^0-9]".toRegex(), "")
            if (value.isEmpty()) {
                value = "0"
            }
            if (value.length > maxLength) {
                s.replace(0, s.length, mPreStr)
            } else {
                val formatted = CurrencyUtils.convert(value.toLong(), currency)
                s.replace(0, s.length, formatted)
                Logger.d("AmountTextWatcher afterTextChanged:" + formatted)
            }
            mEditing = false
        }
    }
}
