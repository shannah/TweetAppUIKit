
package com.codename1.twitterui.controllers;


import ca.weblite.shared.components.CollapsibleHeaderContainer;
import com.codename1.rad.controllers.FormController;
import com.codename1.rad.controllers.ViewController;
import com.codename1.rad.models.EntityListProvider;
import com.codename1.rad.models.Tag;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.ui.ActionCategories;
import com.codename1.rad.ui.Actions;
import com.codename1.rad.ui.Slot;
import com.codename1.twitterui.models.TWTApplicationModel;
import com.codename1.twitterui.schemas.TWTApplicationSchema;
import com.codename1.twitterui.spi.ITweetListController;
import com.codename1.twitterui.events.TweetRequestEvent;

import static com.codename1.twitterui.controllers.TWTApplicationController.*;

import com.codename1.rad.controllers.Controller;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.nodes.ListNode;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.ui.UI;
import com.codename1.twitterui.providers.DemoTweetProvider;
import com.codename1.twitterui.providers.ITweetProvider;
import com.codename1.twitterui.views.TweetListView;
import com.codename1.twitterui.schemas.TweetSchema;

import static com.codename1.rad.ui.UI.*;
import static com.codename1.ui.ComponentSelector.$;

import com.codename1.rad.ui.entityviews.EntityListView;
import com.codename1.rad.ui.entityviews.ProfileAvatarView;

import com.codename1.twitterui.views.TWTSearchButton;

import com.codename1.twitterui.views.TWTTitleComponent;
import com.codename1.twitterui.views.TweetRowView;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.layouts.BorderLayout;

/**
 *
 * @author shannah
 */
public class HomeFormController extends TWTFormController implements ITweetListController {

    public static final Tag northSlot = new Tag("TWTHomeFormController.northSlot");
    public static final Tag southSlot = new Tag("TWTHomeFormController.southSlot");

    public static interface Factory {
        public HomeFormController newInstance(Controller parent);
    }

    public static final TWTApplicationSchema.Section section = new TWTApplicationSchema.Section(action(icon(FontImage.MATERIAL_HOME)), evt->{
        ViewController vc = evt.getViewController();
        Factory factory = vc.lookup(Factory.class);
        if (factory == null) {
            new HomeFormController(vc.getApplicationController()).show();
            return;
        }
        factory.newInstance(vc.getApplicationController()).show();

    });
    private ActionNode loadMoreAction = UI.action();
    private ActionNode refreshAction = UI.action();
    private ITweetProvider tweetProvider;



    
    public HomeFormController(Controller parent) {
        super(parent);
        setPathName("home");
        init();

    }



    private void init() {

        tweetProvider = createTweetProvider();

        ListNode listNode = new ListNode(
                actions(TweetRowView.TWEET_MENU_ACTIONS, createTweetMenuActions()),
                actions(TweetRowView.TWEET_ACTIONS, createTweetActions()),
                actions(ProfileAvatarView.PROFILE_AVATAR_CLICKED_MENU, createTweetProfileAvatarClickedMenuActions()),
                actions(TweetRowView.TWEET_CLICKED, getTweetClickedAction()),
                actions(ActionCategories.LIST_REFRESH_ACTION, refreshAction),
                actions(ActionCategories.LIST_LOAD_MORE_ACTION, loadMoreAction),
                UI.param(EntityListView.SCROLLABLE_Y, true),
                UI.providerLookup(ITweetProvider.class)

        );

        TWTApplicationModel appModel = lookup(TWTApplicationModel.class);
        EntityList feed = null;
        if (appModel != null) {
            if (appModel.getFeed() == null) {
                appModel.setFeed(new EntityList());
            }
            feed = appModel.getFeed();
        } else {
            feed = new EntityList();
        }


        TweetListView view = new TweetListView(feed, listNode);

        
        ViewNode viewNode = getViewNode();


        Slot northSlotImpl = new Slot(northSlot, appModel.getEntity(), viewNode);
        Slot southSlotImpl = new Slot(southSlot, appModel.getEntity(), viewNode);

        Container contentWrap = new Container(new BorderLayout());
        $(contentWrap).selectAllStyles().setPadding(0).setMargin(0).setBgTransparency(0);
        contentWrap.add(BorderLayout.CENTER, view).add(BorderLayout.NORTH, northSlotImpl).add(BorderLayout.SOUTH, southSlotImpl);

        CollapsibleHeaderContainer mainFrame = new CollapsibleHeaderContainer(
                new TWTTitleComponent(appModel.getEntity(), viewNode, new TWTSearchButton(appModel.getEntity(), viewNode)),
                contentWrap,
                view.getScrollWrapper());
        
        setView(mainFrame);
        TWTApplicationModel applicationModel = lookup(TWTApplicationModel.class);
        
        if (getTweetClickedAction() != null) {
            addActionListener(getTweetClickedAction(), evt->{
                //Dialog.show("Tweet Details", "The tweet was clicked", "OK", null);
                evt.consume();
                createTweetDetailsController(this, (Entity & TweetSchema)evt.getEntity()).getView().show();
            });
        }

        
        addActionListener(section.getAction(), evt -> {
            evt.consume();
            if (applicationModel.getCurrentSection() == section) {
                // We are already home... so do nothing.
                // TODO:  This action should scroll to top, and refresh list
                return;
            }
            // If we are on another form, we should show "back"
            getView().showBack();
            
        });

        addActionListener(loadMoreAction, tweetProvider);
        addActionListener(refreshAction, tweetProvider);


        
    }


    @Override
    public void initController() {
        super.initController();
        withLookup(TWTApplicationModel.class, model -> model.setCurrentSection(section));
    }
    
    public Actions createTweetMenuActions() {
        if (isDemoMode()) {
            return new Actions(notInterested, unfollow, mute);
        }
        ITweetListController actionProvider = parentLookup(ITweetListController.class);
        if (actionProvider == null) {
            return new Actions();
        }

        return actionProvider.createTweetMenuActions();
    }

    public Actions createTweetActions() {
        if (isDemoMode()) {
            return new Actions( reply, retweet, favorite, share);
        }
        ITweetListController actionProvider = parentLookup(ITweetListController.class);
        if (actionProvider == null) {
            return new Actions();
        }


        return actionProvider.createTweetActions();
    }

    public Actions createTweetProfileAvatarClickedMenuActions() {
        if (isDemoMode()) {
            return new Actions(unfollow);
        }
        ITweetListController actionProvider = parentLookup(ITweetListController.class);
        if (actionProvider == null) {
            return new Actions();
        }

        return actionProvider.createTweetProfileAvatarClickedMenuActions();
    }

    public ActionNode createTweetClickedAction() {
        if (isDemoMode()) {
            return tweetDetails;
        }
        ITweetListController actionProvider = parentLookup(ITweetListController.class);
        if (actionProvider == null) {
            return new ActionNode();
        }

        return actionProvider.createTweetClickedAction();
    }

    @Override
    public ITweetProvider createTweetProvider() {
        if (isDemoMode()) {
            return new DemoTweetProvider();
        }
        ITweetListController parentController = parentLookup(ITweetListController.class);
        if (parentController != null) {
            return parentController.createTweetProvider();
        }

        return null;
    }



    private ActionNode tweetClickedAction;
    private ActionNode getTweetClickedAction() {
        if (tweetClickedAction == null) {
            tweetClickedAction = createTweetClickedAction();
        }
        return tweetClickedAction;
    }


     protected <T extends Entity & TweetSchema> FormController createTweetDetailsController(Controller parent, T tweet) {
        TWTControllerFactory factory = parentLookup(TWTControllerFactory.class);
        if (factory == null) {
            return new TweetDetailsController(parent, tweet);
        }
        FormController out = factory.createTweetDetailsController(parent, tweet);
        if (out == null) {
            out = new TweetDetailsController(parent, tweet);
        }
        return out;

    }



}
