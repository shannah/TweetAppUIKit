package com.codename1.demos.twitterui;


import com.codename1.demos.twitterui.models.UserProfile;
import com.codename1.rad.controllers.ApplicationController;
import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.ControllerEvent;
import com.codename1.rad.controllers.ViewController;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.schemas.Thing;
import static com.codename1.rad.ui.UI.action;
import static com.codename1.rad.ui.UI.actions;
import static com.codename1.rad.ui.UI.badge;
import static com.codename1.rad.ui.UI.icon;
import static com.codename1.rad.ui.UI.label;
import static com.codename1.rad.ui.UI.selectedCondition;
import com.codename1.twitterui.views.TWTGlobalTabs;
import com.codename1.twitterui.views.TWTSideBarView;
import com.codename1.twitterui.schemas.Tweet;
import com.codename1.twitterui.views.TWTTitleComponent;
import com.codename1.ui.FontImage;

/**
 * This file was generated by <a href="https://www.codenameone.com/">Codename One</a> for the purpose 
 * of building native mobile applications using Java.
 */
public class TwitterUIDemo extends ApplicationController {
    private AppNavigationViewModel navigationModel = new AppNavigationViewModel();
   
    
    
    public static class AppNavigationEvent extends ControllerEvent {
        private AppNavigationViewModel.NavSection section;
        
        public AppNavigationEvent(Controller source, AppNavigationViewModel.NavSection section) {
            super(source);
            this.section = section;
        }
        
        public AppNavigationViewModel.NavSection getSection() {
            return section;
        }
    }
    
    static final ActionNode notInterested = action(
            label("Not interested in this"),
            icon(FontImage.MATERIAL_MOOD_BAD)
        ),
            
        unfollow = action(
            icon(FontImage.MATERIAL_REMOVE),
            label(tweet->{
                if (tweet.isEntity(Tweet.author)) {
                    return "Unfollow "+tweet.getEntity(Tweet.author).getText(Thing.identifier);
                } else if (!tweet.isEmpty(Tweet.authorId)){
                    return "Unfollow "+tweet.getText(Tweet.authorId);
                }
                return "Unfollow this user";
            })
        ),
            
        mute = action(
            icon(FontImage.MATERIAL_VOLUME_OFF),
            label(tweet->{
                if (tweet.isEntity(Tweet.author)) {
                    return "Mute "+tweet.getEntity(Tweet.author).getText(Thing.identifier);
                } else if (!tweet.isEmpty(Tweet.authorId)){
                    return "Mute "+tweet.getText(Tweet.authorId);
                }
                return "Mute this user";
            })
        ),
            
        reply = action(
            icon(FontImage.MATERIAL_CHAT_BUBBLE_OUTLINE)
        ),
        retweet = action(
            icon(FontImage.MATERIAL_FORWARD)
        ),
        favorite = action(
            icon(FontImage.MATERIAL_FAVORITE_OUTLINE)
        ),
        share = action(
            icon(FontImage.MATERIAL_SHARE)
        ),
            
        tweetDetails = action(
                   
        ),
        profile = action(
                label("Profile"),
                icon(FontImage.MATERIAL_ACCOUNT_CIRCLE)
        ),
        lists = action(label("Lists"), icon(FontImage.MATERIAL_LIST)),
        topics = action(label("Topics"), icon(FontImage.MATERIAL_CATEGORY)),
        bookmarks = action(label("Bookmarks"), icon(FontImage.MATERIAL_BOOKMARKS)),
        moments = action(label("Moments"), icon(FontImage.MATERIAL_BOLT)),
        newAccount = action(label("Create new account")),
        addExistingAccount = action(label("Add Existing Account")),
        settingsAndPrivacy = action(label("Settings and privacy")),
        helpCenter = action(label("Help Center")),
        darkMode = action(icon(FontImage.MATERIAL_LIGHTBULB_OUTLINE)),
        qrCode = action(icon(FontImage.MATERIAL_SCANNER)),
        switchProfile = action(icon(FontImage.MATERIAL_ACCOUNT_CIRCLE)),
        switchProfile2 = action(icon(FontImage.MATERIAL_ACCOUNT_BALANCE_WALLET)),
        followers = action(
                icon("Followers"),
                label("344")
        ),
        following = action(
            icon("Following"),
            label("311")
        ),
        home = action(
            selectedCondition(entity -> {
                return entity.get(AppNavigationViewModel.currentSection) == AppNavigationViewModel.NavSection.Home;
            }),
            icon(FontImage.MATERIAL_HOME)
        ),
        search = action(
            selectedCondition(entity -> {
                return entity.get(AppNavigationViewModel.currentSection) == AppNavigationViewModel.NavSection.Search;
            }),
            icon(FontImage.MATERIAL_SEARCH)
        ),
        alerts = action(
            selectedCondition(entity -> {
                return entity.get(AppNavigationViewModel.currentSection) == AppNavigationViewModel.NavSection.Alerts;
            }),
            icon(FontImage.MATERIAL_ALARM)
        ),
        messages = action(
            selectedCondition(entity -> {
                return entity.get(AppNavigationViewModel.currentSection) == AppNavigationViewModel.NavSection.Inbox;
            }),
            icon(FontImage.MATERIAL_INBOX),
            badge("9")
        ),
        settings = action(
            icon(FontImage.MATERIAL_SETTINGS)
        )
    
        ;
    
    
    public TwitterUIDemo() {

    }

    @Override
    public void init(Object context) {
        super.init(context);
        TWTGlobalTabs menuBar = new TWTGlobalTabs(navigationModel, getViewNode());
        addLookup(menuBar);
        
        UserProfile p = createDemoProfile();
        addLookup(p);
        addLookup(navigationModel);
        
        
        addActionListener(home, evt -> {
            evt.consume();
            new HomeFormController(this).getView().show();
            
        });
        
        addActionListener(search, evt->{
            evt.consume();
            new SearchTabsController(this).getView().show();
        });
        
        addActionListener(settings, evt-> {
            evt.consume();
            com.codename1.ui.Component c = evt.getContext().getEventSource();
            ViewController vc = ViewController.getViewController(c);
            Controller parent = this;
            if (vc != null && vc.getFormController() != null) {
                parent = vc.getFormController();
            }
            new SettingsFormController(parent).getView().show();
        });
    }
    
    
    
    private static UserProfile createDemoProfile() {
        UserProfile p = new UserProfile();
        p.set(Thing.name, "Steve Hannah");
        p.set(Thing.identifier, "@shannah78");
        p.set(Thing.thumbnailUrl, "https://www.codenameone.com/img/steve.jpg");
        return p;
    }

    @Override
    public void actionPerformed(ControllerEvent evt) {
        if (evt instanceof StartEvent) {
            evt.consume();
            new HomeFormController(this).getView().show();
            return;
        }
       if (evt instanceof AppNavigationEvent) {
            // When the app navigates to a new section, it should fire this event, which we use
            // to update the global navigation state - which will update the globals tabs
            // state.
            evt.consume();
            navigationModel.set(AppNavigationViewModel.currentSection, evt.as(AppNavigationEvent.class).getSection());
        }
        super.actionPerformed(evt);
    }
    
    
    
    public ViewNode createViewNode() {
        ViewNode viewNode = new ViewNode( 
            actions(TWTSideBarView.SIDEBAR_ACTIONS, profile, lists, topics, bookmarks, moments),
            actions(TWTSideBarView.SIDEBAR_TOP_OVERFLOW_MENU, newAccount, addExistingAccount),
            actions(TWTSideBarView.SIDEBAR_SETTINGS_ACTIONS, settingsAndPrivacy, helpCenter),
            actions(TWTSideBarView.SIDEBAR_BOTTOM_LEFT_ACTIONS, darkMode),
            actions(TWTSideBarView.SIDEBAR_BOTTOM_RIGHT_ACTIONS, qrCode),
            actions(TWTSideBarView.SIDEBAR_TOP_ACTIONS, switchProfile, switchProfile2),
            actions(TWTSideBarView.SIDEBAR_STATS, following, followers),
            actions(TWTGlobalTabs.GLOBAL_TABS, home, search, alerts, messages),
            actions(TWTTitleComponent.TITLE_ACTIONS, settings)
                
        );
        return viewNode;
    }
    
}