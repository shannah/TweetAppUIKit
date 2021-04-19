package com.codename1.twitterui.events;

import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.ControllerEvent;
import com.codename1.twitterui.schemas.TWTApplicationSchema;

public class AppNavigationEvent extends ControllerEvent {
    private TWTApplicationSchema.Section section;

    public AppNavigationEvent(Controller source, TWTApplicationSchema.Section section) {
        super(source);
        this.section = section;
    }

    public TWTApplicationSchema.Section getSection() {
        return section;
    }
}
