package com.codename1.twitterui.controllers;

import com.codename1.rad.controllers.ActionSupport;
import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.FormController;
import com.codename1.rad.controllers.ViewController;
import com.codename1.twitterui.events.AppNavigationEvent;
import com.codename1.twitterui.schemas.TWTApplicationSchema;
import com.codename1.ui.FontImage;

import static com.codename1.rad.ui.UI.*;

public class AlertsFormController extends TWTFormController {
    public static interface Factory {
        public HomeFormController newInstance(Controller parent);
    }

    public static final TWTApplicationSchema.Section section = new TWTApplicationSchema.Section(action(icon(FontImage.MATERIAL_ALARM)), evt->{
        ViewController vc = evt.getViewController();
        Factory factory = vc.lookup(Factory.class);
        if (factory == null) {
            new AlertsFormController(vc.getApplicationController()).show();
        }
        factory.newInstance(vc.getApplicationController()).show();

    });

    public AlertsFormController(Controller parent) {
        super(parent);
    }

    @Override
    public void initController() {
        super.initController();
        ActionSupport.dispatchEvent(new AppNavigationEvent(this, section));
    }
}
