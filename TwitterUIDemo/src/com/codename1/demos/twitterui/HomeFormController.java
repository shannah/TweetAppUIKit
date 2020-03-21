/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.twitterui;


import ca.weblite.shared.components.CollapsibleHeaderContainer;
import com.codename1.demos.twitterui.AppNavigationViewModel.NavSection;
import static com.codename1.demos.twitterui.AppNavigationViewModel.currentSection;
import static com.codename1.demos.twitterui.TwitterUIDemo.*;

import com.codename1.demos.twitterui.models.UserProfile;
import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.ControllerEvent;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.nodes.ListNode;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.schemas.Thing;
import com.codename1.rad.ui.UI;
import com.codename1.twitterui.views.TweetListView;
import com.codename1.twitterui.models.TWTAuthor;
import com.codename1.twitterui.models.TweetModel;
import com.codename1.twitterui.schemas.Tweet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static com.codename1.rad.ui.UI.*;
import com.codename1.rad.ui.entityviews.EntityListView;
import com.codename1.rad.ui.entityviews.ProfileAvatarView;

import com.codename1.twitterui.views.TWTSearchButton;

import com.codename1.twitterui.views.TWTTitleComponent;
import com.codename1.twitterui.views.TweetRowView;
import com.codename1.ui.Dialog;

/**
 *
 * @author shannah
 */
public class HomeFormController extends BaseFormController {
    
    
    
    
    
    
        
        
    
   
            
    
    
    private Map<String,Entity> authors = new HashMap<>();
    
    public HomeFormController(Controller parent) {
        super(parent);
        TweetListView view = new TweetListView(createDemoTweetList(), new ListNode(
                actions(TweetRowView.TWEET_MENU_ACTIONS, notInterested, unfollow, mute),
                actions(TweetRowView.TWEET_ACTIONS, reply, retweet, favorite, share),
                actions(ProfileAvatarView.PROFILE_AVATAR_CLICKED_MENU, unfollow),
                actions(TweetRowView.TWEET_CLICKED, tweetDetails),
                UI.param(EntityListView.SCROLLABLE_Y, true)
                
        ));
        
        ViewNode viewNode = getViewNode();
        
        UserProfile p = lookup(UserProfile.class);
        
        
        
        
        CollapsibleHeaderContainer mainFrame = new CollapsibleHeaderContainer(
                new TWTTitleComponent(p, viewNode, new TWTSearchButton(p, viewNode)), 
                view,
                view.getScrollWrapper());
        
        setView(mainFrame);
        AppNavigationViewModel navigationModel = lookup(AppNavigationViewModel.class);
        
        
        addActionListener(tweetDetails, evt->{
            //Dialog.show("Tweet Details", "The tweet was clicked", "OK", null);
            evt.consume();
            new TweetDetailsController(this, evt.getEntity()).getView().show();
        });
        
        addActionListener(home, evt -> {
            evt.consume();
            if (navigationModel.get(currentSection) == NavSection.Home) {
                // We are already home... so do nothing.
                // TODO:  This action should scroll to top, and refresh list
                return;
            }
            // If we are on another form, we should show "back"
            getView().showBack();
            
        });
        
    }
    
    
    
    private  EntityList createDemoTweetList() {
        String georgeThumb = "https://weblite.ca/cn1tests/radchat/george.jpg";
        String kramerThumb = "https://weblite.ca/cn1tests/radchat/kramer.jpg";
        Entity george = getOrCreateAuthor("George", "@kostanza", georgeThumb);
        Entity kramer = getOrCreateAuthor("Kramer", "@kramer", kramerThumb);
        long SECOND = 1000l;
        long MINUTE = SECOND * 60;
        long HOUR = MINUTE * 60;
        long DAY = HOUR * 24;
        long t = System.currentTimeMillis() - 2 * DAY;
        
        EntityList list = new EntityList();
        list.add(createTweet(george, "Just made my first sale", new Date(t), "https://optinmonster.com/wp-content/uploads/2018/05/sales-promotion-examples.jpg"));
        list.add(createTweet(george, "Just made my first sale", new Date(t), "https://optinmonster.com/wp-content/uploads/2018/05/sales-promotion-examples.jpg"));
        list.add(createTweet(george, "Just made my first sale", new Date(t), "https://optinmonster.com/wp-content/uploads/2018/05/sales-promotion-examples.jpg"));
        list.add(createTweet(george, "Just made my first sale", new Date(t), "https://optinmonster.com/wp-content/uploads/2018/05/sales-promotion-examples.jpg"));
        list.add(createTweet(george, "Just made my first sale", new Date(t), "https://optinmonster.com/wp-content/uploads/2018/05/sales-promotion-examples.jpg"));
        list.add(createTweet(george, "Just made my first sale", new Date(t), "https://optinmonster.com/wp-content/uploads/2018/05/sales-promotion-examples.jpg"));
        return list;
    }
    
    private Entity getOrCreateAuthor(String name, String id, String iconUrl) {
        if (authors.containsKey(id)) {
            return authors.get(id);
        }
        Entity author = new TWTAuthor();
        author.set(Thing.name, name);
        author.set(Thing.identifier, id);
        author.set(Thing.thumbnailUrl, iconUrl);
        authors.put(id, author);
        return author;
        
    }
    
    private  Entity createTweet(Entity author, String content, Date date, String imageUrl) {
        Entity tweet = new TweetModel();
        tweet.set(Tweet.author, author);
        tweet.setText(Tweet.text, content);
        tweet.setDate(Tweet.datePosted, date);
        tweet.setText(Tweet.image, imageUrl);
        return tweet;
    }

    

    @Override
    public void initController() {
        super.initController();
        lookup(AppNavigationViewModel.class).set(currentSection, NavSection.Home);
    }
    
    
    
}
