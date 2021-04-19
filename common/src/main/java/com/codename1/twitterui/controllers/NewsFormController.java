/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.twitterui.controllers;

import ca.weblite.shared.components.CollapsibleHeaderContainer;
import com.codename1.rad.controllers.FormController;
import com.codename1.rad.controllers.ViewController;
import com.codename1.rad.models.PropertySelector;
import com.codename1.rad.ui.UI;
import com.codename1.twitterui.providers.DemoNewsProvider;
import com.codename1.twitterui.providers.INewsProvider;
import com.codename1.twitterui.schemas.TWTApplicationSchema;
import com.codename1.twitterui.spi.INewsFormController;
import com.codename1.twitterui.models.*;
import com.codename1.rad.controllers.Controller;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ViewNode;

import static com.codename1.rad.ui.UI.*;
import com.codename1.rad.ui.entityviews.TabsEntityView;
import com.codename1.twitterui.views.TWTNewsList;
import com.codename1.twitterui.views.TWTSearchButton;
import com.codename1.twitterui.views.TWTTabsView;
import com.codename1.twitterui.views.TWTTitleComponent;
import com.codename1.ui.FontImage;

/**
 *
 * @author shannah
 */
public class NewsFormController extends TWTFormController implements INewsFormController {

    private static final ActionNode refreshAction = UI.action();
    private static final ActionNode loadMoreAction = UI.action();

    public static interface Factory {
        public NewsFormController newController(Controller parent);
    }

    /**
     * Creates a new instance of NewsFormController at the given context.  This will do a
     * lookup for {@link Factory} and use that factory to create the instance if found.
     *
     * It will fall back to creating a new {@link NewsFormController}.  This implementation
     * allows you to override the {@link Factory} by adding a lookup to the application controller.
     *
     * @param context The context from where the lookup is made to find a {@link Factory}.
     * @param parent The parent controller passed to the new form controller to be its parent.
     * @return A NewsFormController.
     */
    public static NewsFormController newInstance(Controller context, Controller parent) {
        Factory factory = context.lookup(Factory.class);
        NewsFormController out = null;
        if (factory != null) {
            out = factory.newController(parent);
            if (out != null) return out;
        }
        return new NewsFormController(parent);
    }

    public static final TWTApplicationSchema.Section section = new TWTApplicationSchema.Section(action(icon(FontImage.MATERIAL_SEARCH)), evt->{
        ViewController vc = evt.getViewController();
        newInstance(vc, vc.getApplicationController()).show();
    });


    public static final ActionNode search = action(
            
    );


    private TWTApplicationModel appModel;
    
    public NewsFormController(Controller parent) {
        super(parent);
        setPathName("news");
        TWTApplicationModel model = parent.lookup(TWTApplicationModel.class);
        appModel = model;
        TabsEntityView view = new TWTTabsView(model.getEntity(), getViewNode());
       
        CollapsibleHeaderContainer wrapper = new CollapsibleHeaderContainer(
                new TWTTitleComponent(
                        model.getEntity(),
                        getViewNode(),
                        new TWTSearchButton(model.getEntity(), getViewNode())
                ),
                view, 
                view
        );
        setView(wrapper);
        
        addActionListener(search, evt->{
            evt.consume();
            createSearchFormController(this).show();

        });

        // We need to bind the news provider to the load and refresh actions to give it
        // a chance to respond to events in the news lists.
        addActionListener(refreshAction, getNewsProvider());
        addActionListener(loadMoreAction, getNewsProvider());


        
    }

    @Override
    public void initController() {
        super.initController();
        withLookup(TWTApplicationModel.class, model -> model.setCurrentSection(section));
    }

    private FormController createSearchFormController(Controller parent) {
        TWTControllerFactory factory = parentLookup(TWTControllerFactory.class);
        if (factory != null) {
            FormController out = factory.createSearchFormController(parent);
            if (out != null) {
                return out;
            }
        }
        return new SearchFormController(parent);
    }

    @Override
    protected ViewNode createViewNode() {

        ViewNode vn = new ViewNode(
                actions(TWTSearchButton.SEARCH_ACTION, search)

        );
        appModel.setNewsCategories(getNewsCategories());

        for (TWTNewsCategory cat : getNewsCategories()) {
            if (cat.getFeed() == null) {
                cat.setFeed(new EntityList());
            }
            NewsCategoryController categoryController = NewsCategoryController.newInstance(this, this, cat);
            vn.setAttributes(list(
                    property(new PropertySelector(cat, TWTNewsCategory.feed)),
                    label(cat.getName()),
                    controller(categoryController),
                    cellRenderer(new TWTNewsList.TWTNewsListCellRenderer()),
                    actions(LIST_REFRESH_ACTION, refreshAction),
                    actions(LIST_LOAD_MORE_ACTION, loadMoreAction)
            ));


        }


        return vn;

    }

    private TWTNewsCategories newsCategories;

    public TWTNewsCategories<?> getNewsCategories() {
        if (newsCategories == null) {
            withParentLookup(INewsFormController.class, ctl -> {
                newsCategories = ctl.getNewsCategories();
            });
            if (newsCategories != null) {
                return newsCategories;
            }
            withLookup(TWTApplicationModel.class, appModel -> {
                EntityList el = appModel.getNewsCategories();
                if (el != null) {
                    newsCategories = el.wrap(TWTNewsCategories.class);
                }
            });
            if (newsCategories != null) return newsCategories;
            newsCategories = new TWTNewsCategories();
            newsCategories.add((Entity)new TWTNewsCategoryImpl(DemoNewsProvider.ID_FOR_YOU, "For You" ));
            newsCategories.add((Entity)new TWTNewsCategoryImpl(DemoNewsProvider.ID_TRENDING, "Trending"));
        }
        return newsCategories;

    }

    private INewsProvider newsProvider;

    @Override
    public INewsProvider getNewsProvider() {
        if (newsProvider == null) {
            withParentLookup(INewsFormController.class, ctl -> {
                 newsProvider = ctl.getNewsProvider();
            });
            if (newsProvider == null) {
                if (isDemoMode()) {
                    newsProvider = new DemoNewsProvider();
                } else {

                }
            }

        }
        return newsProvider;
    }


}
