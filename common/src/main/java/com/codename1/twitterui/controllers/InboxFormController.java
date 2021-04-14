package com.codename1.twitterui.controllers;

import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.FormController;
import com.codename1.twitterui.models.TWTNavSection;
import com.codename1.ui.FontImage;

import static com.codename1.rad.ui.UI.*;

public class InboxFormController extends BaseFormController {
    private static TWTNavSection inboxNavSection;
    public static TWTNavSection getInboxNavSection() {
        if (inboxNavSection == null) {
            inboxNavSection = new TWTNavSection(
                    action(
                            icon(FontImage.MATERIAL_INBOX),
                            badge("9")
                    )
            ) {
                @Override
                public FormController createFormController(Controller parent) {
                    return new InboxFormController(parent);
                }
            };
        }
        return inboxNavSection;
    }
    public InboxFormController(Controller parent) {
        super(parent);
    }
}
