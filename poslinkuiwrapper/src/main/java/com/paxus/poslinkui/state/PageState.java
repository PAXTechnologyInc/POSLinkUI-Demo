package com.paxus.poslinkui.state;

import android.os.Parcel;

/**
 * Created by Kim.L 8/15/22
 */
class PageState implements IPOSLinkUIState {
    
    final String action;
    final String packageName;
    
    PageState(String action, String packageName) {
        this.action = action;
        this.packageName = packageName;
    }
    
    protected PageState(Parcel in) {
        action = in.readString();
        packageName = in.readString();
    }
    
    public static final Creator<PageState> CREATOR = new Creator<PageState>() {
        @Override
        public PageState createFromParcel(Parcel in) {
            return new PageState(in);
        }
        
        @Override
        public PageState[] newArray(int size) {
            return new PageState[size];
        }
    };
    
    @Override
    public String getActionName() {
        return action;
    }
    
    @Override
    public String getTargetPackageName() {
        return packageName;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(action);
        dest.writeString(packageName);
    }
}
