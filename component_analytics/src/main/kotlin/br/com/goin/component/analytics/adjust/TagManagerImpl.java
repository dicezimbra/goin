package br.com.goin.component.analytics.adjust;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;

public class TagManagerImpl implements TagManager {

    @Override
    public void trackAdjustEvent(String token) {
        AdjustEvent event = new AdjustEvent(token);
        Adjust.trackEvent(event);
    }
}
