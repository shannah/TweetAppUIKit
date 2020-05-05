/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.twitterui.views;

import com.codename1.rad.models.DateFormatterAttribute;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ActionNode.Category;
import com.codename1.rad.nodes.ListNode;
import com.codename1.rad.ui.ActionViewFactory;
import com.codename1.rad.ui.UI;
import com.codename1.rad.ui.entityviews.EntityListView;
import com.codename1.twitterui.text.TweetDateFormatter;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import com.codename1.ui.Component;

/**
 *
 * A view for displaying a list of tweets.  This view in a CodeRAD EntityView instance, so it requires a
 * view model, and a UI descriptor to customize it.  Please read https://shannah.github.io/CodeRAD/manual/#truewhat-is-coderad[What is CodeRAD?]
 * for an introduction to CodeRAD and how to use CodeRAD components.
 * 
 * .Screenshot of the TweetListView with some sample data.
 * image::https://shannah.github.io/TweetAppUIKit/manual/images/Image-050520-094735.562.png[]
 * 
 * === View Model
 * 
 * The TweetListView, as a subclass of {@link EntityListView} requires an `EntityList` for a view model, and a {@link ListNode} 
 * as a UI descriptor.  The items/rows of the EntityList are expected to be {@link Entity} subclasses conforming to the {@link Tweet}
 * schema.  The {@link TweetModel} class is a reference implementation of such a view model.  You may use {@link TweetModel} instances
 * as the items of the `EntityList`, or you may define your own view model.
 * 
 * The rows of this view are {@link TweetRowView} instances, so you can refer to the {@link TweetRowView} docs for further information
 * about the view model requirements.
 * 
 * ==== Example View Model
 * 
 * [source,java]
 * ----
 * // Method the generates a view model of tweets to display in the TweetListView.
 * private  EntityList createDemoTweetList() {
        String georgeThumb = "https://weblite.ca/cn1tests/radchat/george.jpg";
        String kramerThumb = "https://weblite.ca/cn1tests/radchat/kramer.jpg";
        String jerryThumb = "https://weblite.ca/cn1tests/radchat/jerry.jpg";
        String elaineThumb = "https://weblite.ca/cn1tests/radchat/elaine.jpg";
        String newmanThumb = "https://weblite.ca/cn1tests/radchat/newman.jpg";
        Entity george = getOrCreateAuthor("George", "@kostanza", georgeThumb);
        Entity kramer = getOrCreateAuthor("Kramer", "@kramer", kramerThumb);
        Entity jerry = getOrCreateAuthor("Jerry", "@jerrys", jerryThumb);
        Entity elaine = getOrCreateAuthor("Elaine", "@elaine", elaineThumb);
        Entity newman = getOrCreateAuthor("Newman", "@newman", newmanThumb);
        long SECOND = 1000l;
        long MINUTE = SECOND * 60;
        long HOUR = MINUTE * 60;
        long DAY = HOUR * 24;
        long t = System.currentTimeMillis() - 2 * DAY;
        
        EntityList list = new EntityList();
        list.add(createTweet(george, "Just made my first sale", new Date(t), "https://optinmonster.com/wp-content/uploads/2018/05/sales-promotion-examples.jpg"));
        list.add(createTweet(kramer, "Check out these cool new features we added today.  I think you're going to like them.", new Date(t), "https://www.codenameone.com/img/blog/new-features.jpg"));
        list.add(createTweet(newman, "This is how I feel every time Jerry gets what he wants", new Date(t), "https://weblite.ca/cn1tests/radchat/damn-you-seinfeld.jpg"));
        list.add(createTweet(elaine, "These urban sombreros are going to be a huge hit.  Don't miss out.  Get them from the Peterman collection.", new Date(t), "https://weblite.ca/cn1tests/radchat/urban-sombrero.jpg"));
        return list;
    }
    
 * ----
 * 
 * === UI Descriptor
 * 
 * As a subclass of {@link EntityListView}, this class will obey all directives obeyed by the {@link EntityListView} class.
 * In addition, you should refer to the {@link TweetRowView} docs to see the supported UI descriptor attributes at a row level.
 * 
 * ==== Example UI Descriptor
 * 
 * [source,java]
 * ----
 * TweetListView view = new TweetListView(createDemoTweetList(), new ListNode(
                actions(TweetRowView.TWEET_MENU_ACTIONS, notInterested, unfollow, mute),
                actions(TweetRowView.TWEET_ACTIONS, reply, retweet, favorite, share),
                actions(ProfileAvatarView.PROFILE_AVATAR_CLICKED_MENU, unfollow),
                actions(TweetRowView.TWEET_CLICKED, tweetDetails),
                UI.param(EntityListView.SCROLLABLE_Y, true)
                
        ));
 * ----
 * 
 * 
 * === Full Sample
 * 
 * [source,java]
 * ----
 * 
package com.codename1.demos.twitterui;


import ca.weblite.shared.components.CollapsibleHeaderContainer;
import com.codename1.demos.twitterui.AppNavigationViewModel.NavSection;
import static com.codename1.demos.twitterui.AppNavigationViewModel.currentSection;
import static com.codename1.demos.twitterui.TwitterUIDemo.*;

import com.codename1.demos.twitterui.models.UserProfile;
import com.codename1.rad.controllers.Controller;
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

/**
 *
 * @author shannah
 *{slash}
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
        String jerryThumb = "https://weblite.ca/cn1tests/radchat/jerry.jpg";
        String elaineThumb = "https://weblite.ca/cn1tests/radchat/elaine.jpg";
        String newmanThumb = "https://weblite.ca/cn1tests/radchat/newman.jpg";
        Entity george = getOrCreateAuthor("George", "@kostanza", georgeThumb);
        Entity kramer = getOrCreateAuthor("Kramer", "@kramer", kramerThumb);
        Entity jerry = getOrCreateAuthor("Jerry", "@jerrys", jerryThumb);
        Entity elaine = getOrCreateAuthor("Elaine", "@elaine", elaineThumb);
        Entity newman = getOrCreateAuthor("Newman", "@newman", newmanThumb);
        long SECOND = 1000l;
        long MINUTE = SECOND * 60;
        long HOUR = MINUTE * 60;
        long DAY = HOUR * 24;
        long t = System.currentTimeMillis() - 2 * DAY;
        
        EntityList list = new EntityList();
        list.add(createTweet(george, "Just made my first sale", new Date(t), "https://optinmonster.com/wp-content/uploads/2018/05/sales-promotion-examples.jpg"));
        list.add(createTweet(kramer, "Check out these cool new features we added today.  I think you're going to like them.", new Date(t), "https://www.codenameone.com/img/blog/new-features.jpg"));
        list.add(createTweet(newman, "This is how I feel every time Jerry gets what he wants", new Date(t), "https://weblite.ca/cn1tests/radchat/damn-you-seinfeld.jpg"));
        list.add(createTweet(elaine, "These urban sombreros are going to be a huge hit.  Don't miss out.  Get them from the Peterman collection.", new Date(t), "https://weblite.ca/cn1tests/radchat/urban-sombrero.jpg"));
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
    
    
  
----
 * 
 * @author shannah
 */
public class TweetListView extends EntityListView {
    
    
    private static class TweetListActionViewFactory implements ActionViewFactory {

        private final ActionViewFactory parent;
        
        TweetListActionViewFactory(ActionViewFactory parent) {
            this.parent = parent;
        }
        
        @Override
        public Component createActionView(Entity entity, ActionNode action) {
            
            Category cat = action.getCategory();
            if (cat == TweetRowView.TWEET_MENU_ACTIONS) {
                action.setAttributes(UI.uiid("TweetMenuAction"));
            } else if (cat == TweetRowView.TWEET_ACTIONS) {
                action.setAttributes(UI.uiid("TweetAction"));
            }
            Component out = parent.createActionView(entity, action);
            if (action.getCategory() == TweetRowView.TWEET_MENU_ACTIONS) {
                if (out instanceof Button) {
                    Button btn = (Button)out;
                    btn.setGap(CN.convertToPixels(3f));
                }
            }
            return out;
        }
        
    }
    
    private static ListNode decorate(ListNode node) {
        if (node == null) {
            node = new ListNode();
        }
        if (node.findAttribute(DateFormatterAttribute.class) == null) {
            node.setAttributes(new DateFormatterAttribute(new TweetDateFormatter()));
        }
        ActionNode dummyAction = new ActionNode();
        dummyAction.setParent(node);
        node.setAttributes(UI.viewFactory(new TweetListActionViewFactory(dummyAction.getViewFactory())));
        node.setAttributes(UI.cellRenderer(new TweetRowView.TweetRowCellRenderer()));
        return node;
    }
    
   
    /**
     * Creates a new tweet list view.
     * @param tweets List of tweets.  Rows of this list should conform to the {@link com.codename1.twitterui.schemas.Tweet} schema.  
     * @see com.codename1.twitterui.models.TweetModel A reference implementation conforming to the {@link Tweet} schema.
     * @see TweetRowView For component used to render each individual row.
     */
    public TweetListView(EntityList tweets) {
        this(tweets, null);
    }
    
    /**
     * Creates a new tweet list view.
     * @param tweets List of tweets.  Rows of this list should conform to the {@link com.codename1.twitterui.schemas.Tweet} schema.
     * @param node UI descriptor.  Use this to pass parameters, attributes, actions, etc.. to the component.
     * @see com.codename1.twitterui.models.TweetModel A reference implementation conforming to the {@link Tweet} schema.
     * @see TweetRowView For component used to render each individual row.
     */
    public TweetListView(EntityList tweets, ListNode node) {
        super(tweets, decorate(node));
    }

    @Override
    public void update() {
        
        super.update();
    }
    
    

   
    
}
