package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.content.Context;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pax.us.pay.ui.constant.entry.PoslinkEntry;

import java.util.ArrayList;
import java.util.List;


/**
 * Utils for POSLink Entry
 * {@value PoslinkEntry#ACTION_SHOW_ITEM}
 */
public class TextShowingUtils {
    public static final String BOLD = "\\B";
    public static final String LEFT_ALIGN = "\\L";
    public static final String RIGHT_ALIGN = "\\R";
    public static final String CENTER_ALIGN = "\\C";
    public static final String SMALL_FONT = "\\1";
    public static final String NORMAL_FONT = "\\2";
    public static final String BIG_FONT = "\\3";
    public static final String LINE = "\n";
    public static final String LINE_SEP = "\\n";


    public static final float FONT_SMALL_SP = 20;
    public static final float FONT_NORMAL_SP = 24;
    public static final float FONT_BIG_SP = 28;


    public static List<TextView> getTextViewList(Context context, String line) {
        line = line.replaceAll(LINE_SEP, LINE);

        List<TextView> list = new ArrayList<>();

        String[] lines = line.split(LINE);
        for(String l: lines){
            list.add(getTextView(context,l));
        }
        return list;
    }

    public static TextView getTextView(Context context, String line) {
        line = line.replaceAll(LINE_SEP, LINE);

        TextView textView = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_NORMAL_SP);

        StringBuilder text = new StringBuilder(line);
        while (text.length() > 0) {
            if (text.indexOf(LEFT_ALIGN) == 0) {
                text.delete(0, LEFT_ALIGN.length());

                layoutParams.gravity = Gravity.START;
                textView.setGravity(Gravity.START);
            } else if (text.indexOf(RIGHT_ALIGN) == 0) {
                text.delete(0, RIGHT_ALIGN.length());

                layoutParams.gravity = Gravity.END;
                textView.setGravity(Gravity.END);
            } else if (text.indexOf(CENTER_ALIGN) == 0) {
                text.delete(0, CENTER_ALIGN.length());

                layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
            } else if (text.indexOf(BOLD) == 0) {
                text.delete(0, BOLD.length());

                TextPaint textPaint = textView.getPaint();
                textPaint.setFakeBoldText(true);
            } else if (text.indexOf(SMALL_FONT) == 0) {
                text.delete(0, SMALL_FONT.length());

                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SMALL_SP);
            } else if (text.indexOf(BIG_FONT) == 0) {
                text.delete(0, BIG_FONT.length());

                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_BIG_SP);
            } else if (text.indexOf(NORMAL_FONT) == 0) {
                text.delete(0, NORMAL_FONT.length());

                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_NORMAL_SP);
            } else {
                break;
            }
        }

        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        return textView;
    }

}
