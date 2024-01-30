package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;

import com.paxus.pay.poslinkui.demo.R;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TextWithControlChar extends ConstraintLayout {

    //Constants
    private static final String Line_Feed_Delimiter = "\\\\n";
    private static final String Bold_Delimiter = "\\\\B";
    private static final String Left_Align_Delimiter = "\\L";
    private static final String Right_Align_Delimiter = "\\R";
    private static final String Center_Align_Delimiter = "\\C";
    private static final String Small_Font_Prefix = "1";
    private static final String Normal_Font_Prefix = "2";
    private static final String Big_Font_Prefix = "3";
    private enum FontSize {SMALL, NORMAL, BIG}
    private EnumMap<FontSize, Integer> FontSizeMap = new EnumMap<FontSize, Integer>(FontSize.class){{
        put(FontSize.SMALL, (int) getResources().getDimension(R.dimen.text_size_hint));
        put(FontSize.NORMAL, (int) getResources().getDimension(R.dimen.text_size_normal));
        put(FontSize.BIG, (int) getResources().getDimension(R.dimen.text_size_subtitle));
    }};
    private static final Set<String> Alignment_Delimiters = new HashSet<String>(){{
        add(Left_Align_Delimiter);
        add(Right_Align_Delimiter);
        add(Center_Align_Delimiter);
    }};

    //Use as button
    private static final int[] STATE_CHECKED = {android.R.attr.state_checked};
    private boolean checked = false;
    private boolean checkable = false;

    public TextWithControlChar(@NonNull Context context) { this(context, null); }

    public TextWithControlChar(@NonNull Context context, @Nullable AttributeSet attrs) { this(context, attrs, 0); }

    public TextWithControlChar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) { this(context, attrs, defStyleAttr, 0); }

    public TextWithControlChar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TextWithControlChar setText(String text) {
        Context context = this.getContext();
        if(text == null || text.isEmpty()) return this;

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

        return this;
    }

    public TextWithControlChar setCheckable(boolean checkable) {
        setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.option_item_button_background, null));
        this.checkable = checkable;
        return this;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        if(l != null) {
            super.setOnClickListener(v -> {
                l.onClick(v);
                if(checkable) setChecked(!checked);
            });
        }
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        refreshDrawableState();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if(checked) mergeDrawableStates(drawableState, STATE_CHECKED);
        return drawableState;
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
        public Line(@NonNull Context context) { this(context, null); }
        public Line(@NonNull Context context, @Nullable AttributeSet attrs) { this(context, attrs, 0); }
        public Line(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) { this(context, attrs, defStyleAttr, 0); }

        public Line(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            setLayoutParams(new ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            setId(generateViewId());
        }

        Line(Context context, String line, boolean bold) {
            this(context);
            List<PartialLine> partialLines = new ArrayList<>();

            //Will not allow multiple parts of same alignment. Will only take the first one.
            //TODO - Parse in less passes.

            if (line.contains(Left_Align_Delimiter)) {
                String left = line.substring(line.indexOf(Left_Align_Delimiter) + Left_Align_Delimiter.length(), getEndOfSameAlign(line, line.indexOf(Left_Align_Delimiter)));
                partialLines.add(new PartialLine(context, left, bold, PartialLine.LEFT_ALIGN));
            }
            if (line.contains(Center_Align_Delimiter)) {
                String center = line.substring(line.indexOf(Center_Align_Delimiter) + Center_Align_Delimiter.length(), getEndOfSameAlign(line, line.indexOf(Center_Align_Delimiter)));
                partialLines.add(new PartialLine(context, center, bold, PartialLine.CENTER_ALIGN));
            }
            if (line.contains(Right_Align_Delimiter)) {
                String right = line.substring(line.indexOf(Right_Align_Delimiter) + Right_Align_Delimiter.length(), getEndOfSameAlign(line, line.indexOf(Right_Align_Delimiter)));
                partialLines.add(new PartialLine(context, right, bold, PartialLine.RIGHT_ALIGN));
            }

            if( line.indexOf(Left_Align_Delimiter) !=0 && line.indexOf(Center_Align_Delimiter) !=0 && line.indexOf(Right_Align_Delimiter) !=0 ) {
                String left = line.substring(0, getEndOfSameAlign(line, 0));
                partialLines.add(new PartialLine(context, left, bold, PartialLine.LEFT_ALIGN));
            }

            for(PartialLine partialLine : partialLines) {
                addView(partialLine);
            }
        }

        private int getEndOfSameAlign(String line, int startIndex) {
            int endIndex = line.length();
            for(String delimiter : Alignment_Delimiters) {
                if(line.indexOf(delimiter, startIndex) > startIndex){
                    endIndex = Math.min(endIndex, line.indexOf(delimiter, startIndex));
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
            SpannableStringBuilder builder = new SpannableStringBuilder();

            String[] splitByEscape = input.split("\\\\");
            for(int i=0; i<splitByEscape.length; i++) {
                String partial = splitByEscape[i];
                if(i == 0 && !partial.isEmpty()) {
                    builder.append(generateSpannableString(partial, bold, FontSizeMap.get(FontSize.NORMAL)));
                }
                else if(partial.startsWith(Small_Font_Prefix)) {
                    builder.append(generateSpannableString(partial.substring(1), bold, FontSizeMap.get(FontSize.SMALL)));
                }
                else if(partial.startsWith(Normal_Font_Prefix)) {
                    builder.append(generateSpannableString(partial.substring(1), bold, FontSizeMap.get(FontSize.NORMAL)));
                }
                else if (partial.startsWith(Big_Font_Prefix)) {
                    builder.append(generateSpannableString(partial.substring(1), bold, FontSizeMap.get(FontSize.BIG)));
                }
            }

            super.setText(builder, BufferType.SPANNABLE);
        }

        private SpannableString generateSpannableString(String input, boolean bold, int textSize) {
            SpannableString spannableString = new SpannableString(input);
            spannableString.setSpan(new AbsoluteSizeSpan(textSize), 0, input.length(), 0);
            spannableString.setSpan(new StyleSpan(bold ? Typeface.BOLD : Typeface.NORMAL), 0, input.length(), 0);
            return spannableString;
        }

        public PartialLine(@NonNull Context context) { this(context, null); }

        public PartialLine(@NonNull Context context, @Nullable AttributeSet attrs) { this(context, attrs, 0); }

        public PartialLine(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); }
    }

}
