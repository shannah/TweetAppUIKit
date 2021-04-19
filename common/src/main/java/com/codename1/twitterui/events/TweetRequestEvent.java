package com.codename1.twitterui.events;

import com.codename1.rad.events.EventContext;
import com.codename1.rad.models.EntityListProvider;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.ui.events.ActionEvent;

public class TweetRequestEvent extends DataRequestEvent {



    public TweetRequestEvent(ActionNode.ActionNodeEvent triggeredBy, EntityListProvider.Request request) {
        super(triggeredBy, request);
    }


}
