package com.codename1.twitterui.controllers;

import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.FormController;
import com.codename1.twitterui.models.TWTNavSection;
import com.codename1.ui.FontImage;

import static com.codename1.rad.ui.UI.*;

public class AlertsFormController extends BaseFormController {
    private static TWTNavSection alertsNavSection;
    public static TWTNavSection getAlertsNavSection() {
        if (alertsNavSection == null) {
            alertsNavSection = new TWTNavSection(action(icon(FontImage.MATERIAL_ALARM))) {
                @Override
                public FormController createFormController(Controller parent) {
                    return new AlertsFormController(parent);
                }
            };
        }
        return alertsNavSection;
    }


    public AlertsFormController(Controller parent) {
        super(parent);
    }
}
