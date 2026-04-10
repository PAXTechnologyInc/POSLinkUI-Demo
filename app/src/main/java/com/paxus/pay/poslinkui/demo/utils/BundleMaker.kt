package com.paxus.pay.poslinkui.demo.utils

import android.os.Bundle

class BundleMaker {
    private val bundle: Bundle

    init {
        bundle = Bundle()
    }

    fun addLong(key: String?, value: Long): BundleMaker {
        bundle.putLong(key, value)
        return this
    }

    fun addString(key: String?, value: String?): BundleMaker {
        bundle.putString(key, value)
        return this
    }

    fun addBundle(bundle: Bundle?): BundleMaker {
        if (bundle != null) this.bundle.putAll(bundle)
        return this
    }

    fun get(): Bundle {
        return bundle
    }
}
