package com.paxus.poslinkui.state;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Kim.L 8/15/22
 */
public interface IPOSLinkUIHelper {
    
    Bundle packResponse();
    
    void unpackRequest(Intent intent);
    
    IPOSLinkUIState getState();
}
