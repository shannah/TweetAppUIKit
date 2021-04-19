package com.codename1.twitterui.controllers;

import com.codename1.rad.controllers.ActionSupport;
import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.FormController;
import com.codename1.rad.controllers.ViewController;
import com.codename1.twitterui.events.AppNavigationEvent;
import com.codename1.twitterui.schemas.TWTApplicationSchema;
import com.codename1.ui.FontImage;

import static com.codename1.rad.ui.UI.*;

public class InboxFormController extends TWTFormController {
    public static interface Factory {
        public HomeFormController newInstance(Controller parent);
    }

    public static final TWTApplicationSchema.Section section = new TWTApplicationSchema.Section(action(
            icon(FontImage.MATERIAL_INBOX),
            badge("9")
        ), evt->{
            ViewController vc = evt.getViewController();
            InboxFormController.Factory factory = vc.lookup(InboxFormController.Factory.class);
            if (factory == null) {
                new InboxFormController(vc.getApplicationController()).show();
            }
            factory.newInstance(vc.getApplicationController()).show();

    });

    public InboxFormController(Controller parent) {
        super(parent);
    }

    @Override
    public void initController() {
        super.initController();
        ActionSupport.dispatchEvent(new AppNavigationEvent(this, section));
    }
}
