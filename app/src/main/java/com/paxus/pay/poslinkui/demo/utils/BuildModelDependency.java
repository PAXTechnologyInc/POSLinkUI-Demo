package com.paxus.pay.poslinkui.demo.utils;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.HashMap;
import java.util.Map;

/**
 * To be used as a unified place to ask for any information related to specific devices.
 * Created by Dhrubo.Paul on 9/19/2022.
 */
public class BuildModelDependency {

    private static final int VERTICAL_KEYBOARD_SUPPORT_THRESHOLD = 360;

    private static class Dimension{
        private int width, height;
        Dimension(int width, int height){this.width=width; this.height=height;}
        public int getWidth(){return width;}
        public int getHeight(){return height;}
        @Override
        public String toString(){return "Dimension [width: "+getWidth()+" height: "+getHeight()+"]";}
    }

    private static Map<String, Dimension> deviceDimensionMap = new HashMap<>();
    static {
        deviceDimensionMap.put("A920", new Dimension(720,1280));
        deviceDimensionMap.put("A60", new Dimension(720,1280));
        deviceDimensionMap.put("A930", new Dimension(720,1280));
        deviceDimensionMap.put("A77", new Dimension(720,1440));
        deviceDimensionMap.put("A920Pro", new Dimension(720,1440));
        deviceDimensionMap.put("M50", new Dimension(720,1440));
        deviceDimensionMap.put("M8", new Dimension(800,1280));
        deviceDimensionMap.put("A80", new Dimension(480,800));
        deviceDimensionMap.put("A35", new Dimension(480,800));
        deviceDimensionMap.put("Aries6", new Dimension(1280,720));
        deviceDimensionMap.put("Aries8", new Dimension(1280,800));
        deviceDimensionMap.put("Elis", new Dimension(1920,1080));
        deviceDimensionMap.put("A30", new Dimension(1280,720));
        deviceDimensionMap.put("Q10A", new Dimension(480,279));
        deviceDimensionMap.put("A3700", new Dimension(1280,800));
    }

    private static String getBuildModel(){
        return Build.MODEL;
    }

    private static Dimension getDensityIndependentDimension(Context context){
        float density = context.getResources().getDisplayMetrics().density;
        return new Dimension((int)(deviceDimensionMap.get(getBuildModel()).getWidth()*density), (int)(deviceDimensionMap.get(getBuildModel()).getHeight()*density));
    }

    public static boolean noSpaceForKeyboardBelow(Context context){
        Logger.d(getDensityIndependentDimension(context).getHeight());
        return getDensityIndependentDimension(context).getHeight() < VERTICAL_KEYBOARD_SUPPORT_THRESHOLD;
    }
}
