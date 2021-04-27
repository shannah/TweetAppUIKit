
package com.codename1.twitterui.controllers;


import ca.weblite.shared.components.CollapsibleHeaderContainer;
import com.codename1.rad.controllers.FormController;
import com.codename1.rad.controllers.ViewController;
import com.codename1.rad.models.Tag;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.ui.ActionCategories;
import com.codename1.rad.ui.Actions;
import com.codename1.rad.ui.Slot;
import com.codename1.twitterui.models.TWTApplicationModel;
import com.codename1.twitterui.models.Tweet;
import com.codename1.twitterui.schemas.TWTApplicationSchema;


import static com.codename1.twitterui.controllers.TWTApplicationController.*;

import com.codename1.rad.controllers.Controller;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.nodes.ListNode;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.ui.UI;
import com.codename1.twitterui.providers.DemoTweetProvider;
import com.codename1.twitterui.providers.ITweetProvider;
import com.codename1.twitterui.views.TweetListView;

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
public class TweetListFormController extends TWTFormController{

    public static final Tag northSlot = new Tag("TWTHomeFormController.northSlot");
    public static final Tag southSlot = new Tag("TWTHomeFormController.southSlot");
    private EntityList<Tweet> feed;


    public TweetListFormController(Controller parent) {
        this(parent, null);
    }
    
    public TweetListFormController(Controller parent, EntityList<Tweet> feed) {
        super(parent);
        this.feed = feed;
        setPathName("home");


    }



    protected void init() {
        TWTApplicationController applicationController = getTWTApplicationController();
        if (feed == null) {
            feed = applicationController.getApplicationModel().getFeed();
            if (feed == null) {
                feed = new EntityList();
                applicationController.getApplicationModel().setFeed(feed);
            }
        }

        ListNode listNode = new ListNode(
                actions(TweetRowView.TWEET_MENU_ACTIONS, applicationController.getTweetMenuActions()),
                actions(TweetRowView.TWEET_ACTIONS, applicationController.getTweetActions()),
                actions(ProfileAvatarView.PROFILE_AVATAR_CLICKED_MENU, applicationController.getTweetProfileAvatarClickedMenuActions()),
                actions(TweetRowView.TWEET_CLICKED, applicationController.getTweetDetailsAction()),
                actions(ActionCategories.LIST_REFRESH_ACTION, getRefreshTweetsAction()),
                actions(ActionCategories.LIST_LOAD_MORE_ACTION, getLoadMoreTweetsAction()),
                UI.param(EntityListView.SCROLLABLE_Y, true),
                UI.providerLookup(ITweetProvider.class)

        );



        TweetListView view = new TweetListView(feed, listNode);

        
        ViewNode viewNode = getViewNode();

        TWTApplicationModel appModel = applicationController.getApplicationModel();
        Slot northSlotImpl = new Slot(northSlot, appModel, viewNode);
        Slot southSlotImpl = new Slot(southSlot, appModel, viewNode);

        Container contentWrap = new Container(new BorderLayout());
        $(contentWrap).selectAllStyles().stripMarginAndPadding();
        contentWrap.add(BorderLayout.CENTER, view)
                .add(BorderLayout.NORTH, northSlotImpl)
                .add(BorderLayout.SOUTH, southSlotImpl);

        CollapsibleHeaderContainer mainFrame = new CollapsibleHeaderContainer(
                new TWTTitleComponent(appModel, viewNode, new TWTSearchButton(appModel, viewNode)),
                contentWrap,
                view.getScrollWrapper());
        
        setView(mainFrame);



        
    }

    public ActionNode getRefreshTweetsAction() {
        return getTWTApplicationController().getRefreshTweetsAction();
    }

    public ActionNode getLoadMoreTweetsAction() {
        return getTWTApplicationController().getLoadMoreTweetsAction();
    }




}
