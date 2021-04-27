/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.twitterui.controllers;

import ca.weblite.shared.components.CollapsibleHeaderContainer;
import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.ViewController;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.models.PropertySelector;
import com.codename1.rad.nodes.Node;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.ui.EntityListCellRenderer;
import com.codename1.rad.ui.entityviews.TabsEntityView;
import com.codename1.twitterui.models.TWTApplicationModel;
import com.codename1.twitterui.models.TWTNewsCategories;
import com.codename1.twitterui.models.TWTNewsCategory;
import com.codename1.twitterui.providers.INewsProvider;
import com.codename1.twitterui.views.TWTNewsList;
import com.codename1.twitterui.views.TWTSearchButton;
import com.codename1.twitterui.views.TWTTabsView;
import com.codename1.twitterui.views.TWTTitleComponent;

import static com.codename1.rad.ui.UI.*;
import static com.codename1.rad.util.NonNull.with;

/**
 * A controller for the "News" form, which displays a list of news items.
 * @author shannah
 */
public class NewsFormController extends TWTFormController {

    private EntityList<TWTNewsCategory> newsCategories;

    public NewsFormController(Controller parent) {
        this(parent, null);
    }

    public NewsFormController(Controller parent, EntityList<TWTNewsCategory> newsCategories) {
        super(parent);
        this.newsCategories = newsCategories;

    }

    @Override
    protected void initControllerActions() {
        super.initControllerActions();
        extendAction(TWTSearchButton.SEARCH_ACTION, action -> {
            // Customize search action here as ncessary.
        });
        extendAction(LIST_REFRESH_ACTION, action -> {
            //
        });
        extendAction(LIST_LOAD_MORE_ACTION, action -> {

        });


    }

    @Override
    protected void onStartController() {
        super.onStartController();
        TWTApplicationController applicationController = getTWTApplicationController();
        TWTApplicationModel model = applicationController.getApplicationModel();
        if (this.newsCategories == null) {
            this.newsCategories = model.getNewsCategories();
            if (this.newsCategories == null) {
                this.newsCategories = new TWTNewsCategories();
                model.setNewsCategories(this.newsCategories);
            }
        }

        setPathName("news");


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

        // Pipe the LIST_LOAD_MORE_ACTION and LIST_REFRESH_ACTION events through
        // to a registered news provider.  Generally the news provider would be registered
        // in the Application controller as a lookup.
        with(getNewsProvider(), provider -> {
            addActionListener(getAction(LIST_LOAD_MORE_ACTION), provider);
            addActionListener(getAction(LIST_REFRESH_ACTION), provider);
        });
    }

    @Override
    protected ViewNode createViewNode() {
        TWTApplicationController applicationController = getTWTApplicationController();
        TWTApplicationModel appModel = applicationController.getApplicationModel();

        ViewNode vn = super.createViewNode();
        for (TWTNewsCategory cat : appModel.getNewsCategories()) {
            if (cat.getFeed() == null) {
                cat.setFeed(new EntityList());
            }
            vn.setAttributes(createNewsCategoryNode(cat));
        }


        return vn;

    }

    protected EntityListCellRenderer createNewsListCellRenderer(TWTNewsCategory cat) {
        return new TWTNewsList.TWTNewsListCellRenderer();
    }

    /**
     * Creates the node to use for a category tab.  Typically this will return a ListNode that is configured
     * with the label, cell renderer, category controller, and the property from which the entity list
     * should be pulled from.
     *
     * @param cat The news category.
     * @return
     */
    protected Node createNewsCategoryNode(TWTNewsCategory cat) {
        ViewController categoryController = getTWTApplicationController().createNewsCategoryController(this, cat);


        return list(
                property(new PropertySelector(cat, TWTNewsCategory.feed)),
                label(cat.getName()),
                controller(categoryController),
                cellRenderer(createNewsListCellRenderer(cat))
        );
    }


    /**
     * Sets the news provider to use with this form controller.  Note that you can also add a news provider
     * on any parent controller (e.g. the application controller via `addLookup(NewsProvider.class, myNewsProvider)`
     *
     * @param provider
     */
    public void setNewsProvider(INewsProvider provider) {
        addLookup(INewsProvider.class, provider);
    }

    /**
     * Gets the news provider to use in the news list.  This will crawl up the controller hierarchy until it
     * finds a provider.
     *
     * @return
     */
    public INewsProvider getNewsProvider() {
        return lookup(INewsProvider.class);
    }



}
