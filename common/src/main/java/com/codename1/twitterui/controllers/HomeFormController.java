
package com.codename1.twitterui.controllers;


import ca.weblite.shared.components.CollapsibleHeaderContainer;
import com.codename1.rad.controllers.ApplicationController;
import com.codename1.rad.controllers.FormController;
import com.codename1.rad.models.EntityListProvider;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.ui.Actions;
import com.codename1.twitterui.models.*;

import static com.codename1.twitterui.controllers.TWTApplicationController.*;

import com.codename1.rad.controllers.Controller;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.nodes.ListNode;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.schemas.Thing;
import com.codename1.rad.ui.UI;
import com.codename1.twitterui.providers.ITweetProvider;
import com.codename1.twitterui.providers.TweetResponse;
import com.codename1.twitterui.views.TweetListView;
import com.codename1.twitterui.models.ITweet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static com.codename1.rad.ui.UI.*;
import static com.codename1.ui.ComponentSelector.$;

import com.codename1.rad.ui.entityviews.EntityListView;
import com.codename1.rad.ui.entityviews.ProfileAvatarView;

import com.codename1.twitterui.views.TWTSearchButton;

import com.codename1.twitterui.views.TWTTitleComponent;
import com.codename1.twitterui.views.TweetRowView;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;

/**
 *
 * @author shannah
 */
public class HomeFormController extends BaseFormController {

    private static TWTNavSection homeNavSection;

    public static TWTNavSection getHomeNavSection() {
        if (homeNavSection == null) {
            homeNavSection = new TWTNavSection(action(icon(FontImage.MATERIAL_HOME))) {
                @Override
                public FormController createFormController(Controller parent) {
                    TWTApplicationController applicationController = (TWTApplicationController)parent.getApplicationController();
                    FormController out = applicationController.createHomeFormController(parent);
                    if (out == null) {
                        out = new HomeFormController(parent);
                    }
                    return out;
                }
            };
        }
        return homeNavSection;
    }
    

    
    public HomeFormController(Controller parent) {
        super(parent);

        init();

    }



    private void init() {

        ListNode listNode = new ListNode(
                actions(TweetRowView.TWEET_MENU_ACTIONS, createTweetMenuActions()),
                actions(TweetRowView.TWEET_ACTIONS, createTweetActions()),
                actions(ProfileAvatarView.PROFILE_AVATAR_CLICKED_MENU, createTweetProfileAvatarClickedMenuActions()),
                actions(TweetRowView.TWEET_CLICKED, getTweetClickedAction()),
                UI.param(EntityListView.SCROLLABLE_Y, true),
                UI.providerLookup(ITweetProvider.class)

        );


        TweetListView view = new TweetListView(new EntityList(), listNode);

        
        ViewNode viewNode = getViewNode();
        Entity appModel = (Entity)lookup(IApplicationModel.class);


        CollapsibleHeaderContainer mainFrame = new CollapsibleHeaderContainer(
                new TWTTitleComponent(appModel, viewNode, new TWTSearchButton(appModel, viewNode)),
                view,
                view.getScrollWrapper());
        
        setView(mainFrame);
        Entity applicationModel = (Entity)lookup(IApplicationModel.class);
        
        if (getTweetClickedAction() != null) {
            addActionListener(getTweetClickedAction(), evt->{
                //Dialog.show("Tweet Details", "The tweet was clicked", "OK", null);
                evt.consume();
                createTweetDetailsController(this, (Entity & ITweet)evt.getEntity()).getView().show();
            });
        }

        
        addActionListener(getHomeNavSection().getAction(), evt -> {
            evt.consume();
            if (applicationModel.get(IApplicationModel.currentSection) == getHomeNavSection()) {
                // We are already home... so do nothing.
                // TODO:  This action should scroll to top, and refresh list
                return;
            }
            // If we are on another form, we should show "back"
            getView().showBack();
            
        });
        
    }


    @Override
    public void initController() {
        super.initController();
        lookupEntity(IApplicationModel.class).set(IApplicationModel.currentSection, getHomeNavSection());
    }
    
    protected Actions createTweetMenuActions() {
        TWTApplicationController appController = (TWTApplicationController)getApplicationController();
        if (appController.isDemoMode()) {
            return new Actions(notInterested, unfollow, mute);
        }
        return appController.createTweetMenuActions();
    }

    protected Actions createTweetActions() {
        TWTApplicationController appController = (TWTApplicationController)getApplicationController();
        if (appController.isDemoMode()) {
            return new Actions( reply, retweet, favorite, share);
        }
        return appController.createTweetActions();
    }

    protected Actions createTweetProfileAvatarClickedMenuActions() {
        TWTApplicationController appController = (TWTApplicationController)getApplicationController();
        if (appController.isDemoMode()) {
            return new Actions(unfollow);
        }
        return appController.createTweetProfileAvatarClickedMenuActions();
    }

    protected ActionNode createTweetClickedAction() {
        TWTApplicationController appController = (TWTApplicationController)getApplicationController();
        if (appController.isDemoMode()) {
            return tweetDetails;
        }
        return appController.createTweetClickedAction();
    }

    private ActionNode tweetClickedAction;
    private ActionNode getTweetClickedAction() {
        if (tweetClickedAction == null) {
            tweetClickedAction = createTweetClickedAction();
        }
        return tweetClickedAction;
    }


     protected <T extends Entity & ITweet> FormController createTweetDetailsController(Controller parent, T tweet) {
        TWTApplicationController applicationController = (TWTApplicationController)getApplicationController();
        FormController out = applicationController.createTweetDetailsController(parent, tweet);
        if (out == null) {
            out = new TweetDetailsController(parent, tweet);
        }
        return out;

    }


    
}
