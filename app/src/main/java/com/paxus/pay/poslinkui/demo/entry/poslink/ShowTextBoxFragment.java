package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.pax.us.pay.ui.constant.entry.EntryExtraData;
import com.pax.us.pay.ui.constant.entry.EntryRequest;
import com.pax.us.pay.ui.constant.entry.PoslinkEntry;
import com.pax.us.pay.ui.constant.entry.enumeration.ManageUIConst;
import com.pax.us.pay.ui.constant.status.POSLinkStatus;
import com.paxus.pay.poslinkui.demo.R;
import com.paxus.pay.poslinkui.demo.entry.BaseEntryFragment;
import com.paxus.pay.poslinkui.demo.utils.Logger;
import com.paxus.pay.poslinkui.demo.utils.TaskScheduler;
import com.paxus.pay.poslinkui.demo.view.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Implement text entry actions:<br>
 * {@value PoslinkEntry#ACTION_SHOW_TEXT_BOX}
 */
public class ShowTextBoxFragment extends BaseEntryFragment {

    private static final String BARCODE_QR_CODE = "7";

    private long timeOut;
    private String title;
    private String text;
    private boolean continuousScreen;
    private String button1Name;
    private String button1Color;
    private String button1Key;
    private String button2Name;
    private String button2Color;
    private String button2Key;
    private String button3Name;
    private String button3Color;
    private String button3Key;
    private boolean enableHardKey;
    private List<String> hardKeyList;
    private String barcodeType;
    private String barcodeData;

    private LinearLayout textLayout;

    //Interfaces of POSLink Category may need to listen to POSLinkStatus Broadcasts
    private POSLinkStatusManager posLinkStatusManager;

    private LinearLayout llButton1;
    private LinearLayout llButton2;
    private LinearLayout llButton3;
    private ArrayList<ButtonInfo> buttonInfos = new ArrayList<>();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        posLinkStatusManager = new POSLinkStatusManager(getContext(), getViewLifecycleOwner());
        posLinkStatusManager.registerHandler(POSLinkStatus.CLEAR_MESSAGE, this::clearMessage);
    }

    private void clearMessage() {
        textLayout.removeAllViews();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_show_text_box;
    }

    @Override
    protected void loadArgument(@NonNull Bundle bundle) {
        timeOut = bundle.getLong(EntryExtraData.PARAM_TIMEOUT, 30000);
        text = bundle.getString(EntryExtraData.PARAM_TEXT);
        title = bundle.getString(EntryExtraData.PARAM_TITLE);
        continuousScreen = ManageUIConst.ContinuousScreen.DO_NOT_GO_TO_IDLE.equals(
                bundle.getString(EntryExtraData.PARAM_CONTINUE_SCREEN, ""));
        button1Name = bundle.getString(EntryExtraData.PARAM_BUTTON_1_NAME);
        button1Color = bundle.getString(EntryExtraData.PARAM_BUTTON_1_COLOR);
        button1Key = bundle.getString(EntryExtraData.PARAM_BUTTON_1_KEY);
        button2Name = bundle.getString(EntryExtraData.PARAM_BUTTON_2_NAME);
        button2Color = bundle.getString(EntryExtraData.PARAM_BUTTON_2_COLOR);
        button2Key = bundle.getString(EntryExtraData.PARAM_BUTTON_2_KEY);
        button3Name = bundle.getString(EntryExtraData.PARAM_BUTTON_3_NAME);
        button3Color = bundle.getString(EntryExtraData.PARAM_BUTTON_3_COLOR);
        button3Key = bundle.getString(EntryExtraData.PARAM_BUTTON_3_KEY);
        buttonInfos.add(new ButtonInfo(button1Name,button1Color));
        buttonInfos.add(new ButtonInfo(button2Name,button2Color));
        buttonInfos.add(new ButtonInfo(button3Name,button3Color));
        enableHardKey = bundle.getString(EntryExtraData.PARAM_ENABLE_HARD_KEY, "0").equals("1");
        if(enableHardKey) {
            String keyList = bundle.getString(EntryExtraData.PARAM_HARD_KEY_LIST);
            if (!TextUtils.isEmpty(keyList)) {
                hardKeyList = Arrays.asList(keyList.split(" "));
            }
        }
        barcodeType = bundle.getString(EntryExtraData.PARAM_BARCODE_TYPE);
        if(BARCODE_QR_CODE.equals(barcodeType)) {
            barcodeData = bundle.getString(EntryExtraData.PARAM_BARCODE_DATA);
        }
    }

    @Override
    protected void loadView(View rootView) {
        LinearLayout titleLayout = rootView.findViewById(R.id.title_layout_show_text_box);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        for (TextView textView: TextShowingUtils.getTitleViewList(requireContext(), title, lp, Color.WHITE, requireContext().getResources().getDimension(R.dimen.text_size_title))) {
            textView.setElegantTextHeight(true);
            titleLayout.addView(textView);
        }

        textLayout = rootView.findViewById(R.id.text_view);

        //POSUI-245
        boolean noText = text == null || text.isEmpty();
        boolean noBard = barcodeData == null || barcodeData.isEmpty();
        if (noText && noBard) {
            textLayout.setVisibility(View.GONE);
        } else {
            if (!noText) { // text

                LinearLayout tempTextLayout = new LinearLayout(requireContext());
                tempTextLayout.setOrientation(LinearLayout.HORIZONTAL);

                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                tempTextLayout.setLayoutParams(layoutParams);

                for(TextView tv: TextShowingUtils.getTitleViewList(requireContext(), text, lp, Color.WHITE, requireContext().getResources().getDimension(R.dimen.text_size_subtitle))){
                    tv.setElegantTextHeight(true);
                    tempTextLayout.addView(tv);
                }

                if (tempTextLayout.getChildCount() > 0) {
                    textLayout.addView(tempTextLayout);
                }
            }
            if (!noBard) { // qr code
                ImageView imageView = new ImageView(requireContext());
                imageView.setImageBitmap(createQRCode(barcodeData, 500,null));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setColorFilter(Color.WHITE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(layoutParams);
                textLayout.addView(imageView);
            }
            // output
            textLayout.setVisibility(View.VISIBLE);
        }

        llButton1 = rootView.findViewById(R.id.button1);
        llButton2 = rootView.findViewById(R.id.button2);
        llButton3 = rootView.findViewById(R.id.button3);
        showButtons();
        if(timeOut > 0 ) {
            getParentFragmentManager().setFragmentResult(TaskScheduler.SCHEDULE, TaskScheduler.generateTaskRequestBundle(TaskScheduler.TASK.TIMEOUT, timeOut));
        }

        if(enableHardKey) {
            rootView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(event.getAction() == KeyEvent.ACTION_DOWN){
                        return onKeyDown(keyCode);
                    }
                    return false;
                }
            });
        }
    }

    private boolean onKeyDown(int keyCode){
        Map<Integer, String> keyMap = new HashMap<Integer, String>(){
            {
                put(KeyEvent.KEYCODE_0,"KEY0");
                put(KeyEvent.KEYCODE_1,"KEY1");
                put(KeyEvent.KEYCODE_2,"KEY2");
                put(KeyEvent.KEYCODE_3,"KEY3");
                put(KeyEvent.KEYCODE_4,"KEY4");
                put(KeyEvent.KEYCODE_5,"KEY5");
                put(KeyEvent.KEYCODE_6,"KEY6");
                put(KeyEvent.KEYCODE_7,"KEY7");
                put(KeyEvent.KEYCODE_8,"KEY80");
                put(KeyEvent.KEYCODE_9,"KEY9");
                put(KeyEvent.KEYCODE_ENTER,"KEYENTER");
                put(KeyEvent.KEYCODE_BACK,"KEYCANCEL");
                put(KeyEvent.KEYCODE_DEL,"KEYCLEAR");
            }
        };

        String keyName = keyMap.get(keyCode);
        if(keyName != null
                && (hardKeyList == null || hardKeyList.isEmpty() || hardKeyList.contains(keyName))){
            if(keyName.equals(button1Key)){
                sendNext("1");
                return true;
            }else if(keyName.equals(button2Key)){
                sendNext("2");
                return true;
            }else if(keyName.equals(button3Key)){
                sendNext("3");
                return true;
            }
        }
        return false;
    }


    private void showButtons(){
        List<LinearLayout> disPlayButtons = Arrays.asList(llButton1, llButton2, llButton3);
        for (int i = 0; i < buttonInfos.size(); i++) {
            if(!TextUtils.isEmpty(buttonInfos.get(i).buttonName)){
                disPlayButtons.get(i).setVisibility(View.VISIBLE);
                setButtonView(disPlayButtons.get(i), buttonInfos.get(i).buttonName, buttonInfos.get(i).buttonColor);
                int index = i + 1;
                disPlayButtons.get(i).setOnClickListener(v -> {
                    sendNext(String.valueOf(index));
                });
            }else{
                disPlayButtons.get(i).setVisibility(View.GONE);
            }
        }

    }

    private void setButtonView(LinearLayout layoutBtn, String btnName, String btnColor) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        List<TextView> viewList = TextShowingUtils.getViewList(requireContext(), btnName, lp,Color.WHITE, requireContext().getResources().getDimension(R.dimen.text_size_subtitle));
        for (TextView view : viewList) {
            if (!TextUtils.isEmpty(btnColor) && getColor(btnColor)) {
                view.setTextColor(Color.WHITE);
            }
            LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) view.getLayoutParams();
            lps.gravity = Gravity.CENTER;
            view.setGravity(lps.gravity);
            view.setLayoutParams(lps);
            layoutBtn.addView(view);

        }
        if (!TextUtils.isEmpty(btnColor)) {
            try {
                layoutBtn.setBackgroundColor(Color.parseColor("#" + btnColor));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean getColor(String color) {
        int r = Integer.valueOf(color.substring(0, 2),16);
        int g = Integer.valueOf(color.substring(2, 4),16);
        int b = Integer.valueOf(color.substring(4),16);
        //浅色
        //深色
        return !(r * 0.299 + g * 0.578 + b * 0.114 >= 192);
    }

    private void sendNext(String index){
        Bundle bundle = new Bundle();
        bundle.putString(EntryRequest.PARAM_BUTTON_NUMBER, index);
        sendNext(bundle);
    }

    private Bitmap createQRCode(String content, int qrCodeSize, Bitmap logo){
        Matrix m = new Matrix();
        int imageWidth = 80;
        int imageHeight = 80;
        if(logo != null) {
            m.setScale((float) imageWidth / logo.getWidth(), (float) imageHeight / logo.getHeight());
            logo = Bitmap.createBitmap(logo, 0, 0,
                    logo.getWidth(), logo.getHeight(), m, false);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable<EncodeHintType, Object> hst = new Hashtable<>();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix matrix = null;
        try {
            matrix = writer.encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hst);
        } catch (Exception e) {
            Logger.e(e);
            return null;
        }
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];
        Logger.d("BitMatrix Size:"+width+"x"+height+",content:"+content);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x > halfW - imageWidth/2 && x < halfW + imageWidth/2
                        && y > halfH - imageHeight/2 && y < halfH + imageHeight/2 && logo != null) {
                    pixels[y * width + x] = logo.getPixel(x - halfW
                            + imageWidth/2, y - halfH + imageHeight/2);
                } else {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    }
                }

            }
        }
        if(logo != null) {
            logo.recycle();
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    @Override
    protected TextField[] focusableTextFields() {
        return null;
    }
    public static class ButtonInfo{
        public String buttonName;
        public String buttonColor;
        public ButtonInfo(String buttonName,String buttonColor){
            this.buttonName = buttonName;
            this.buttonColor = buttonColor;
        }
    }
}
