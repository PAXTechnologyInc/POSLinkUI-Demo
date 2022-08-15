package com.paxus.poslinkui.state;

import android.os.Parcelable;

/**
 * Created by Kim.L 8/15/22
 */
public interface IPOSLinkUIState extends Parcelable {
    
    String getActionName();
    
    String getTargetPackageName();
    
}
