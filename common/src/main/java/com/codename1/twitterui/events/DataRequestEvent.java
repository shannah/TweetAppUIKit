package com.codename1.twitterui.events;

import com.codename1.rad.controllers.ControllerEvent;
import com.codename1.rad.events.EventContext;
import com.codename1.rad.models.EntityListProvider;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.ui.events.ActionEvent;
import com.codename1.util.AsyncResource;

public class DataRequestEvent extends ActionNode.ActionNodeEvent {
    private ActionNode.ActionNodeEvent triggeredBy;
    private EntityListProvider.Request request;

    public DataRequestEvent(ActionNode.ActionNodeEvent triggeredBy, EntityListProvider.Request request) {
        super(triggeredBy.getContext());
        this.triggeredBy = triggeredBy;
        this.request = request;
        setAsyncResource(request);
    }

    @Override
    public void consume() {
        super.consume();
        triggeredBy.consume();
    }

    public EntityListProvider.Request getRequest() {
        return request;
    }

    @Override
    public void setAsyncResource(AsyncResource task) {
        triggeredBy.setAsyncResource(task);
    }
}
