package com.codename1.twitterui.controllers;

import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.ControllerEvent;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityListProvider;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.ui.UI;
import com.codename1.twitterui.models.TWTNewsCategory;
import com.codename1.twitterui.providers.INewsProvider;
import com.codename1.twitterui.schemas.TWTNewsCategorySchema;

import static com.codename1.rad.util.NonNull.with;

public class NewsCategoryController extends TWTViewController {


    private TWTNewsCategory newsCategory;


    public NewsCategoryController(Controller parent, TWTNewsCategory newsCategory) {
        super(parent);
        this.newsCategory = newsCategory;




    }

    @Override
    protected void onStartController() {
        super.onStartController();
        addLookup(TWTNewsCategorySchema.class, newsCategory);
    }

    @Override
    public void actionPerformed(ControllerEvent evt) {
        super.actionPerformed(evt);
        evt.as(EntityListProvider.UpdateProviderRequestEvent.class, requestEvent -> {
            // This event is fired by EventListProvider when request is being made
            // It allows us to modify the request.  In this case we are adding
            // the news category to the request.
            with(requestEvent.getRequest(), INewsProvider.NewsRequest.class, newsRequest -> {
                newsRequest.setNewsCategory(newsCategory);
            });
        });
    }
}
