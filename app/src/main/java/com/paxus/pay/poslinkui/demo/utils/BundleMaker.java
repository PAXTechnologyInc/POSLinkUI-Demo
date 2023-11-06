package com.paxus.pay.poslinkui.demo.utils;

import android.os.Bundle;

public class BundleMaker {
    private Bundle bundle;

    public BundleMaker(){
        bundle = new Bundle();
    }

    public BundleMaker addLong(String key, long value){
        bundle.putLong(key, value);
        return this;
    }

    public BundleMaker addString(String key, String value){
        bundle.putString(key, value);
        return this;
    }

    public BundleMaker addBundle(Bundle bundle) {
        if(bundle != null) this.bundle.putAll(bundle);
        return this;
    }

    public Bundle get(){
        return bundle;
    }
}
