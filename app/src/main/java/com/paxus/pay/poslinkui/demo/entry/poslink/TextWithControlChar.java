package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.paxus.pay.poslinkui.demo.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TextWithControlChar extends ConstraintLayout {

    private static final String Line_Feed_Delimiter = "\\\\n";
    private static final String Left_Align_Delimiter = "\\L";
    private static final String Right_Align_Delimiter = "\\R";
    private static final String Center_Align_Delimiter = "\\C";
    private static final String Small_Font_Delimiter = "\\1";
    private static final String Normal_Font_Delimiter = "\\2";
    private static final String Big_Font_Delimiter = "\\3";
    private static final String Bold_Delimiter = "\\\\B";

    private static final int Fontsize_Small = 16;
    private static final int Fontsize_Normal = 24;
    private static final int Fontsize_Big = 36;
    private static final Set<String> Alignment_Delimiters = new HashSet<String>(){{
        add(Left_Align_Delimiter);
        add(Right_Align_Delimiter);
        add(Center_Align_Delimiter);
    }};

    public TextWithControlChar(@NonNull Context context) { this(context, null); }

    public TextWithControlChar(@NonNull Context context, @Nullable AttributeSet attrs) { this(context, attrs, 0); }

    public TextWithControlChar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) { this(context, attrs, defStyleAttr, 0); }

    public TextWithControlChar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setBackgroundColor(getResources().getColor(R.color.clss_red));
    }

    public void setText(String text) {
        Context context = this.getContext();

        List<Line> lines = parseLines(context, text);

        for(int i=0; i<lines.size(); i++) {
            Line line = lines.get(i);

            LayoutParams lineLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            lineLayoutParams.startToStart = ConstraintSet.PARENT_ID;
            lineLayoutParams.endToEnd = ConstraintSet.PARENT_ID;

            lineLayoutParams.topToTop = (i==0) ? ConstraintSet.PARENT_ID : ConstraintSet.UNSET;
            lineLayoutParams.topToBottom = (i==0) ? ConstraintSet.UNSET : lines.get(i-1).getId();
            lineLayoutParams.bottomToBottom = (i==lines.size()-1) ? ConstraintSet.PARENT_ID : ConstraintSet.UNSET;
            lineLayoutParams.bottomToTop = (i==lines.size()-1) ? ConstraintSet.UNSET : lines.get(i+1).getId();

            lineLayoutParams.setMargins(0, 2, 0, 2);
            lineLayoutParams.verticalChainStyle = ConstraintSet.CHAIN_PACKED;

            line.setLayoutParams(lineLayoutParams);

            addView(line);
        }
    }

    private List<Line> parseLines(Context context, String text) {
        List<Line> lines = new ArrayList<>();

        // \n defines new lines
        String[] splitByLineFeed = text.split(Line_Feed_Delimiter);
        for(String line: splitByLineFeed) {
            // \B also creates new lines
            String[] separatedByBold = line.split(Bold_Delimiter);
            for(int i=0; i<separatedByBold.length; i++) {
                if(!separatedByBold[i].isEmpty()) {
                    lines.add(new Line(context, separatedByBold[i], i==0 ? false : true));
                }
            }
        }
        return lines;
    }

    private class Line extends ConstraintLayout {
        private PartialLine leftPart;
        private PartialLine centerPart;
        private PartialLine rightPart;

        public Line(@NonNull Context context) { this(context, null); }
        public Line(@NonNull Context context, @Nullable AttributeSet attrs) { this(context, attrs, 0); }
        public Line(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) { this(context, attrs, defStyleAttr, 0); }

        public Line(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            setLayoutParams(new ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            setBackgroundColor(getResources().getColor(R.color.clss_yellow));
            setId(generateViewId());
        }

        Line(Context context, String line, boolean bold) {
            this(context);

            //Will not allow multiple parts of same alignment. Will only take the first one.

            if (line.indexOf(Left_Align_Delimiter) >= 0) {
                String left = line.substring(line.indexOf(Left_Align_Delimiter) + Left_Align_Delimiter.length(), getEndOfSameAlign(line, Left_Align_Delimiter, line.indexOf(Left_Align_Delimiter)));
                leftPart = new PartialLine(context, left, bold, PartialLine.LEFT_ALIGN);
            }
            if (line.indexOf(Center_Align_Delimiter) >= 0) {
                String center = line.substring(line.indexOf(Center_Align_Delimiter) + Center_Align_Delimiter.length(), getEndOfSameAlign(line, Center_Align_Delimiter, line.indexOf(Center_Align_Delimiter)));
                centerPart = new PartialLine(context, center, bold, PartialLine.CENTER_ALIGN);
            }
            if (line.indexOf(Right_Align_Delimiter) >= 0) {
                String right = line.substring(line.indexOf(Right_Align_Delimiter) + Right_Align_Delimiter.length(), getEndOfSameAlign(line, Right_Align_Delimiter, line.indexOf(Right_Align_Delimiter)));
                rightPart = new PartialLine(context, right, bold, PartialLine.RIGHT_ALIGN);
            }

            if(leftPart == null && centerPart == null && rightPart == null) {
                leftPart = new PartialLine(context, line, bold, PartialLine.LEFT_ALIGN);
            }

            if(leftPart != null) addView(leftPart);
            if(centerPart != null) addView(centerPart);
            if(rightPart != null) addView(rightPart);
        }

        private int getEndOfSameAlign(String line, String thisDelimiter, int thisDelimiterIndex) {
            int endIndex = line.length();
            for(String delimiter : Alignment_Delimiters) {
                if(line.indexOf(delimiter, thisDelimiterIndex) > thisDelimiterIndex){
                    endIndex = Math.min(endIndex, line.indexOf(delimiter, thisDelimiterIndex));
                }
            }
            return endIndex;
        }
    }
    private class PartialLine extends androidx.appcompat.widget.AppCompatTextView {
        private static final float LEFT_ALIGN = 0.0f;
        private static final float CENTER_ALIGN = 0.5f;
        private static final float RIGHT_ALIGN = 1.0f;

        public PartialLine(Context context, String input, boolean bold, float align) {
            this(context);
            setText(input, bold);
            setLayoutParams(align);
        }

        private void setLayoutParams(float align) {
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            params.topToTop = ConstraintSet.PARENT_ID;
            params.bottomToBottom = ConstraintSet.PARENT_ID;
            params.startToStart = ConstraintSet.PARENT_ID;
            params.endToEnd = ConstraintSet.PARENT_ID;
            params.horizontalBias = align;

            setLayoutParams(params);
        }

        private void setText(String input, boolean bold) {
            String text = "";
            String[] splitByEscape = input.split("\\\\");
            for(int i=0; i<splitByEscape.length; i++) {
                String partial = splitByEscape[i];
                if(i == 0 && !partial.isEmpty()) {
                    text += partial;
                }
                else if(partial.startsWith("1")) {
                    text += partial;
                }
                else if(partial.startsWith("2")) {
                    text += partial;
                }
                else if (partial.startsWith("3")) {
                    text += partial;
                }
            }

            setText(input);
        }

        public PartialLine(@NonNull Context context) { this(context, null); }

        public PartialLine(@NonNull Context context, @Nullable AttributeSet attrs) { this(context, attrs, 0); }

        public PartialLine(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); }
    }

}
