package com.codename1.twitterui.controllers;

import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.ControllerEvent;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityListProvider;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.ui.UI;
import com.codename1.twitterui.models.TWTNewsCategory;
import com.codename1.twitterui.providers.INewsProvider;
import com.codename1.twitterui.spi.INewsCategoryController;
import com.codename1.twitterui.schemas.TWTNewsCategorySchema;

import static com.codename1.rad.util.NonNull.with;

public class NewsCategoryController extends TWTViewController implements INewsCategoryController {

    public static interface Factory {
        public NewsCategoryController newController(Controller parent, TWTNewsCategory category);
    }

    public static NewsCategoryController newInstance(Controller context, Controller parent, TWTNewsCategory category) {
        Factory factory = context.lookup(Factory.class);
        if (factory != null) {
            NewsCategoryController out = factory.newController(parent, category);
            if (out != null) return out;
        }
        return new NewsCategoryController(parent, category);
    }

    private TWTNewsCategory newsCategory;

    private ActionNode refreshAction = UI.action(), loadMoreAction = UI.action();
    public NewsCategoryController(Controller parent, TWTNewsCategory newsCategory) {
        super(parent);
        this.newsCategory = newsCategory;
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
