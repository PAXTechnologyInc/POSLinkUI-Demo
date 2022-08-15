package com.paxus.poslinkui;

import com.paxus.poslinkui.state.IPOSLinkUIState;

/**
 * Created by Kim.L 8/15/22
 */
public interface IPOSLinkUICallback {
    
    void onStateChanged(IPOSLinkUIState state);
}
