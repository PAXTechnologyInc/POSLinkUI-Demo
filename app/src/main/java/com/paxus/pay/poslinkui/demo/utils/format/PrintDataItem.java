/*
 * ============================================================================
 * = COPYRIGHT
 *          PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or nondisclosure
 *   agreement with PAX Computer Technology(Shenzhen) CO., LTD and may not be copied or
 *   disclosed except in accordance with the terms in that agreement.
 *     Copyright (C) 2018-? PAX Computer Technology(Shenzhen) CO., LTD All rights reserved.
 *
 * Module Date: 2019/7/30
 * Module Auth: Frank.W
 * Description:
 *
 * Revision History:
 * Date                   Author                       Action
 * 2019/7/30              Frank.W                       Create
 * ============================================================================
 */

package com.paxus.pay.poslinkui.demo.utils.format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * For internal use
 */
public class PrintDataItem {

    public static final String BOLD = "\\B";
    public static final String LEFT_ALIGN = "\\L";
    public static final String RIGHT_ALIGN = "\\R";
    public static final String CENTER_ALIGN = "\\C";
    //    public static final String INVERT = "\\I";
    public static final String SMALL_FONT = "\\1";
    public static final String NORMAL_FONT = "\\2";
    public static final String BIG_FONT = "\\3";

    public static final String LINE = "\n";
    public static final String LINE_SEP = "\\n";

    public static final List<String> TEXT_SHOWING_LIST =  Arrays.asList(
            BOLD, LEFT_ALIGN, RIGHT_ALIGN, CENTER_ALIGN, LINE, SMALL_FONT, NORMAL_FONT, BIG_FONT
    );

    private final List<String> cmds = new ArrayList<>(3);
    private String content = "";

    public PrintDataItem(String content, List<String> cmds) {
        this.content = content;
        if (cmds != null) {
            this.cmds.addAll(cmds);
        }
    }


    public List<String> getCmds() {
        return cmds;
    }



    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String cmd : cmds) {
            stringBuilder.append(cmd).append(" ");
        }
        stringBuilder.append(" ").append(content);
        return stringBuilder.toString();
    }

//    public static class ViewItem {
//        private boolean isLineSeparate = false;
//        private View view;
//        public static final int ALIGN_LEFT = -1;
//        public static final int ALIGN_CENTER = 0;
//        public static final int ALIGN_RIGHT = 1;
//        private int align;
//        private String content;
//
//        public ViewItem(boolean isLineSeparate, View view) {
//            this.isLineSeparate = isLineSeparate;
//            this.view = view;
//        }
//
//        @NonNull
//        public static Comparator<View> getComparator() {
//            return new Comparator<View>() {
//                @Override
//                public int compare(View l, View r) {
//                    ViewItem leftViewItem = (ViewItem) l.getTag();
//                    ViewItem rightViewItem = (ViewItem) r.getTag();
//                    return Integer.compare(leftViewItem.getAlign(), rightViewItem.getAlign());
//                }
//            };
//        }
//
//        public boolean isLineSeparate() {
//            return isLineSeparate;
//        }
//
//        public View getView() {
//            return view;
//        }
//
//
//        public int getAlign() {
//            return align;
//        }
//
//        public void setAlign(int align) {
//            this.align = align;
//        }
//
//        public String getContent() {
//            return content;
//        }
//
//        public void setContent(String content) {
//            this.content = content;
//        }
//    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public boolean isLineSep() {
//        return !Collections.disjoint(cmds, SINGLE_ONLY_CONTROLLER_LIST) || cmds.contains(LOGO) || cmds.contains(BARCODE);
//    }

//    @NonNull
//    public static Comparator<PrintDataItem> getAlignmentComparator() {
//        return new Comparator<PrintDataItem>() {
//            @Override
//            public int compare(PrintDataItem l, PrintDataItem r) {
//                List<String> leftCmds = l.getCmds();
//                List<String> rightCmds = r.getCmds();
//                Set<String> leftCenterRight = new HashSet<>(3);
//                leftCenterRight.add(LEFT_ALIGN);
//                leftCenterRight.add(CENTER_ALIGN);
//                leftCenterRight.add(RIGHT_ALIGN);
//                //leftCenterRight.add(SMALL_FONT);
//                //leftCenterRight.add(NORMAL_FONT);
//                //leftCenterRight.add(BIG_FONT);
//
//                String lastAlignCmdOfLeft = getLastAlignCmd(leftCmds, leftCenterRight);
//                String lastAlignCmdOfRight = getLastAlignCmd(rightCmds, leftCenterRight);
//                int leftAlignNum = getAlignNum(lastAlignCmdOfLeft);
//                int rightAlignNum = getAlignNum(lastAlignCmdOfRight);
//
//                return Integer.compare(leftAlignNum, rightAlignNum);
//            }
//
//            private int getAlignNum(String align) {
//                List<String> list = new ArrayList<String>() {
//                    {
//                        add(LEFT_ALIGN);
//                        add(CENTER_ALIGN);
//                        add(RIGHT_ALIGN);
//                        //add(SMALL_FONT);
//                        //add(NORMAL_FONT);
//                        //add(BIG_FONT);
//                    }
//                };
//                if (list.contains(align)) {
//                    return list.indexOf(align);
//                } else {
//                    return -1;
//                }
////                return RIGHT_ALIGN.equals(align) ? 1 :
////                        CENTER_ALIGN.equals(align) ? 0 : -1;
//            }
//
//            private String getLastAlignCmd(List<String> cmds, Set<String> leftCenterRight) {
//                for (int i = cmds.size()-1; i >= 0; i--) {
//                    String cmd = cmds.get(i);
//                    if (leftCenterRight.contains(cmd)) {
//                        return cmd;
//                    }
//                }
//                return null;
//            }
//        };
//    }
}
