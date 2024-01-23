package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TextWithControlChar extends ConstraintLayout{

    private static final String Line_Feed_Delimiter = "\\n";
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

    public TextWithControlChar(@NonNull Context context) {
        super(context);
    }

    public TextWithControlChar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextWithControlChar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TextWithControlChar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void loadText(Context context, String text) {
        List<Line> lines = parseLines(text);

        for(int i=0; i<lines.size(); i++) {
            Line line = lines.get(i);

            ConstraintLayout lineLayout = new ConstraintLayout(context);
            lineLayout.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_dark));

            ConstraintLayout.LayoutParams lineLayoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lineLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            lineLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            lineLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            lineLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            lineLayout.setLayoutParams(lineLayoutParams);

            TextView left = mergePartialLinesToTextView(new TextView(context), line.leftPart);
            left.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_dark));
            TextView center = mergePartialLinesToTextView(new TextView(context), line.centerPart);
            center.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_dark));
            TextView right = mergePartialLinesToTextView(new TextView(context), line.rightPart);
            right.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_dark));

            ConstraintLayout.LayoutParams leftLayoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
            leftLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            leftLayoutParams.endToStart = center.getId();
            leftLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            leftLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            left.setLayoutParams(leftLayoutParams);

            ConstraintLayout.LayoutParams centerLayoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
            centerLayoutParams.startToEnd = left.getId();
            centerLayoutParams.endToStart = right.getId();
            centerLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            centerLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            center.setLayoutParams(centerLayoutParams);

            ConstraintLayout.LayoutParams rightLayoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
            rightLayoutParams.startToEnd = center.getId();
            rightLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            rightLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            rightLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            right.setLayoutParams(rightLayoutParams);

            lineLayout.addView(left);
            lineLayout.addView(center);
            lineLayout.addView(right);
            addView(lineLayout);

        }
    }

    private TextView mergePartialLinesToTextView(TextView textView, List<PartialLine> partialLines) {
        if(partialLines == null || partialLines.isEmpty()) return textView;

        for(PartialLine partialLine : partialLines) {
            textView.append(partialLine.text);
        }
        textView.setId(View.generateViewId());
        return textView;
    }

    private List<Line> parseLines(String text) {
        List<Line> lines = new ArrayList<>();

        // \n defines new lines
        String[] splitByLineFeed = text.split(Line_Feed_Delimiter);
        for(String line: splitByLineFeed) {
            // \B also creates new lines
            String[] separatedByBold = line.split(Bold_Delimiter);
            for(int i=0; i<separatedByBold.length; i++) {
                if(!separatedByBold[i].isEmpty()) {
                    lines.add(new Line(separatedByBold[i], i==0 ? false : true));
                }
            }
        }
        return lines;
    }

    private class Line {
        private List<PartialLine> leftPart;
        private List<PartialLine> centerPart;
        private List<PartialLine> rightPart;

        Line(String line, boolean bold) {
            if (line.indexOf(Left_Align_Delimiter) > 0) {
                String left = line.substring(line.indexOf(Left_Align_Delimiter) + Left_Align_Delimiter.length(), getEndOfSameAlign(line, Left_Align_Delimiter, line.indexOf(Left_Align_Delimiter)));
                leftPart = parsePartialLine(left, bold);
            }
            if (line.indexOf(Center_Align_Delimiter) > 0) {
                String center = line.substring(line.indexOf(Center_Align_Delimiter) + Center_Align_Delimiter.length(), getEndOfSameAlign(line, Center_Align_Delimiter, line.indexOf(Center_Align_Delimiter)));
                centerPart = parsePartialLine(center, bold);
            }
            if (line.indexOf(Right_Align_Delimiter) > 0) {
                String right = line.substring(line.indexOf(Right_Align_Delimiter) + Right_Align_Delimiter.length(), getEndOfSameAlign(line, Right_Align_Delimiter, line.indexOf(Right_Align_Delimiter)));
                rightPart = parsePartialLine(right, bold);
            }
        }

        private List<PartialLine> parsePartialLine(String text, boolean bold) {
            List<PartialLine> partialLines = new ArrayList<>();
            String[] splitByEscape = text.split("\\\\");
            for(int i=0; i<splitByEscape.length; i++) {
                String partial = splitByEscape[i];
                if(i == 0 && !partial.isEmpty()) {
                    partialLines.add(new PartialLine(partial, bold, Fontsize_Normal));
                }
                else if(partial.startsWith("1")) {
                    partialLines.add(new PartialLine(partial.substring(1), bold, Fontsize_Small));
                }
                else if(partial.startsWith("2")) {
                    partialLines.add(new PartialLine(partial.substring(1), bold, Fontsize_Normal));
                }
                else if (partial.startsWith("3")) {
                    partialLines.add(new PartialLine(partial.substring(1), bold, Fontsize_Big));
                }
            }
            return partialLines;
        }

        private int getEndOfSameAlign(String line, String thisDelimiter, int thisDelimiterIndex) {
            int endIndex = line.length();
            for(String delimiter : Alignment_Delimiters) {
                if(line.indexOf(delimiter) > thisDelimiterIndex) {
                    endIndex = line.indexOf(delimiter);
                    break;
                }
            }
            return endIndex;
        }
    }
    private class PartialLine {
        private String text;
        private boolean bold;
        private int fontSize;

        public PartialLine(String text, boolean bold, int fontsize) {
            this.text = text;
            this.bold = bold;
            this.fontSize = fontsize;
        }
    }

}
