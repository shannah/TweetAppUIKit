package com.codename1.twitterui.controllers;

import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.FormController;
import com.codename1.rad.controllers.ViewController;
import com.codename1.rad.models.Entity;
import com.codename1.twitterui.schemas.TweetSchema;

public interface TWTControllerFactory {
    <T extends Entity & TweetSchema> FormController createTweetDetailsController(Controller parent, T tweet);

    FormController createHomeFormController(Controller parent);
    SettingsFormController createSettingsFormController(Controller parent);

    FormController createNewsFormController(Controller parent);

    FormController createSearchFormController(Controller parent);

    ViewController createNavigationController(Controller parent);
}
