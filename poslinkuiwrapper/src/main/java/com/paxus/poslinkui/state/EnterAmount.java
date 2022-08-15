package com.paxus.poslinkui.state;

import android.os.Parcel;

/**
 * Created by Kim.L 8/15/22
 */
public class EnterAmount extends PageState {
    
    private final String title;
    private final String transMode;
    private final long timeOut;
    private final String currency;
    private final String valuePatten;
    
    private long amount = 0;
    
    EnterAmount(String action,
                String packageName,
                String title,
                String transMode,
                long timeOut,
                String currency,
                String valuePatten) {
        super(action, packageName);
        this.title = title;
        this.transMode = transMode;
        this.timeOut = timeOut;
        this.currency = currency;
        this.valuePatten = valuePatten;
    }
    
    protected EnterAmount(Parcel in) {
        super(in);
        title = in.readString();
        transMode = in.readString();
        timeOut = in.readLong();
        currency = in.readString();
        valuePatten = in.readString();
        amount = in.readLong();
    }
    
    public static final Creator<EnterAmount> CREATOR = new Creator<EnterAmount>() {
        @Override
        public EnterAmount createFromParcel(Parcel in) {
            return new EnterAmount(in);
        }
        
        @Override
        public EnterAmount[] newArray(int size) {
            return new EnterAmount[size];
        }
    };
    
    public String getTitle() {
        return title;
    }
    
    public String getTransMode() {
        return transMode;
    }
    
    public long getTimeOut() {
        return timeOut;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public String getValuePatten() {
        return valuePatten;
    }
    
    public long getAmount() {
        return amount;
    }
    
    public void setAmount(long amount) {
        this.amount = amount;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(title);
        dest.writeString(transMode);
        dest.writeLong(timeOut);
        dest.writeString(currency);
        dest.writeString(valuePatten);
        dest.writeLong(amount);
    }
}
