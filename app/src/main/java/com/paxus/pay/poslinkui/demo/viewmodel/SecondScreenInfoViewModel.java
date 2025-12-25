package com.paxus.pay.poslinkui.demo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Author: Elaine Xie
 * Date: 2025/12/8
 * Desc:
 */
public class SecondScreenInfoViewModel extends ViewModel {
    private final MutableLiveData<String> title = new MutableLiveData<>("");
    private final MutableLiveData<String> amount = new MutableLiveData<>("");
    private final MutableLiveData<String> msg = new MutableLiveData<>("");
    private final MutableLiveData<String> status = new MutableLiveData<>("");
    private final MutableLiveData<String> statusTitle = new MutableLiveData<>("");
    private final MutableLiveData<Integer> imageResourceId = new MutableLiveData<>();

    private final MediatorLiveData<ScreenInfo> screenInfo = new MediatorLiveData<>();


    public void updateAmount(String newContent) {
        amount.setValue(newContent);
    }

    public void updateTitle(String newContent) {
        title.setValue(newContent);
    }

    public void updateMessage(String newContent) {
        msg.setValue(newContent);
    }

    public SecondScreenInfoViewModel() {
        screenInfo.addSource(amount, amount -> combineUserData());
        screenInfo.addSource(msg, msg -> combineUserData());
        screenInfo.addSource(status, status -> combineUserData());
        screenInfo.addSource(imageResourceId, status -> combineUserData());
        screenInfo.addSource(title, status -> combineUserData());
        screenInfo.addSource(statusTitle, status -> combineUserData());
    }

    private void combineUserData() {
        String newAmount = amount.getValue();
        String newMsg = msg.getValue();
        String newStatus = status.getValue();
        Integer newImageResourceId = imageResourceId.getValue();
        String newTitle = title.getValue();
        String newStatusTitle = statusTitle.getValue();

        ScreenInfo info = new ScreenInfo(newAmount, newMsg, newStatus, newImageResourceId, newTitle, newStatusTitle);
        screenInfo.setValue(info);
    }

    public LiveData<ScreenInfo> getScreenInfo() {
        return screenInfo;
    }

    //  update second screen info
    public void updateAllData(String newAmount, String newMsg, String newStatus, Integer newImageResourceId, String newStatusTitle, String newTitle) {
        amount.setValue(newAmount);
        msg.setValue(newMsg);
        status.setValue(newStatus);
        imageResourceId.setValue(newImageResourceId);
        statusTitle.setValue(newStatusTitle);
        title.setValue(newTitle);
    }

    public static class ScreenInfo {
        public final String title;
        public final String amount;
        public final String msg;
        public final String status;
        public final String statusTitle;
        public final Integer imageResourceId;

        public ScreenInfo(String amount, String msg, String status, Integer imageResourceId, String title, String statusTitle) {
            this.amount = amount;
            this.msg = msg;
            this.status = status;
            this.imageResourceId = imageResourceId;
            this.title = title;
            this.statusTitle = statusTitle;
        }
    }
}