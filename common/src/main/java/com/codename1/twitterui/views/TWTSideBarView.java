/*
 * Copyright 2020 Codename One.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codename1.twitterui.views;

import com.codename1.compat.java.util.Objects;
import com.codename1.io.Log;
import com.codename1.io.Util;
import com.codename1.rad.attributes.IconUIID;
import com.codename1.rad.attributes.UIID;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityType;
import com.codename1.rad.models.PropertySelector;
import com.codename1.rad.nodes.ActionNode.Category;
import com.codename1.rad.nodes.ComponentDecoratorNode;
import com.codename1.rad.nodes.Node;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.schemas.Thing;
import com.codename1.rad.ui.AbstractEntityView;
import com.codename1.rad.ui.ActionStyle;
import com.codename1.rad.ui.Actions;
import com.codename1.rad.ui.UI;
import com.codename1.rad.ui.entityviews.ProfileAvatarView;
import com.codename1.rad.ui.menus.ActionSheet;
import com.codename1.twitterui.models.TWTUserProfileImpl;
import com.codename1.twitterui.schemas.TWTApplicationSchema;
import com.codename1.twitterui.schemas.TWTUserProfileSchema;
import com.codename1.twitterui.models.TWTUserProfile;
import com.codename1.ui.*;

import static com.codename1.ui.ComponentSelector.$;

import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Border;
import java.io.IOException;
import com.codename1.twitterui.controllers.TWTFormController;
import com.codename1.rad.models.Entity;


/**
 * A component inspired by the Twitter mobile app's sidebar menu.  
 * 
 * image::https://shannah.github.io/TweetAppUIKit/manual/images/Image-050520-093228.115.png[]
 * 
 * This is designed to work seamlessly with the 
 * {@link TWTFormController} and the {@link ca.weblite.shared.components.CollapsibleSideBarContainer} to provide a sidebar menu for
 * an app that uses the Tweet App UI Kit.  It is the default component returned by TWTFormController#createSideBarView(), and this controller
 * uses the {@link ca.weblite.shared.components.CollapsibleSideBarContainer} view to wrap its content by default.
 * 
 * However, this component can be used on its own as well.
 * 
 * === View Model
 * 
 * There are no special requirements for the view model.  If the view model implements {@link Thing#name} and/or {@link Thing#thumbnailUrl},
 * these will be used to generate the user avatar.   Customization of the content of this view is primarily handled
 * by registering actions.
 * 
 * === Actions
 * 
 * . {@link #SIDEBAR_TOP_ACTIONS} - Actions displayed at the top of the sidebar.
 * . {@link #SIDEBAR_TOP_OVERFLOW_MENU} - Actions displayed in the overflow menu at the top of the sidebar.
 * . {@link #SIDEBAR_STATS} - Actions meant to display user stats (e.g. 2034 Posts, etc..).
 * . {@link #SIDEBAR_ACTIONS} - The primary menu items in the sidebar.  These will render icon and text.
 * . {@link #SIDEBAR_SETTINGS_ACTIONS} - Additional settings/related actions that are rendered below the {@link #SIDEBAR_ACTIONS} menu.  These will be rendered 
 * as text only.  No icons.
 * . {@link #SIDEBAR_BOTTOM_LEFT_ACTIONS} - Actions rendered in the bottom left.
 * . {@link #SIDEBAR_BOTTOM_RIGHT_ACTIONS} - Actions rendered in the bottom right.
 * 
 * === Styles
 * 
 * . `TWTSideBar` - UIID for the component.
 * . `TWTSideBarTopAction` - UIID used for buttons in the {@link #SIDEBAR_TOP_ACTIONS} category.
 * . `TWTSideBarAction` - UIID used by main actions.  i.e. {@link #SIDEBAR_ACTIONS} category.
 * . `TWTSideBarActionIcon` - UIID used for icons of main actions.  I.e. icons of {@link #SIDEBAR_ACTIONS} category.
 * . `TWTSideBarStatsAction` - UIID used by stats actions.  I.e. {@link #SIDEBAR_STATS} category.
 * . `TWTSideBarStatsActionIcon` - UIID used for icons of stats actions.
 * . `TWTSideBarBottomLeftAction` - UIID used for bottom left actions. I.e. {@link #SIDEBAR_BOTTOM_LEFT_ACTIONS} category.
 * . `TWTSideBarBottomLeftActionsWrapper` - UIID used for wrapper container of all of the bottom left actions.
 * . `TWTSideBarBottomRightAction` - UIID used for bottom right actions. I.e. {@link #SIDEBAR_BOTTOM_RIGHT_ACTIONS} category.
 * . `TWTSideBarBottomRightActionsWrapper` - UIID used for wrapper container of all of the bottom right actions.
 * . `TWTSideBarWrapper` - Wrapper component inside `TWTSideBar` but wrapping all of the content.
 * . `TWTSideBarNorth` - North container in the sidebar.  This wraps top actions, avatar/username, and stats actions.
 * . `TWTSideBarOverflowMenuButton` - UIID of the overflow menu button.
 * . `TWTSideBarNameLabel` - UIID for the label with the profile user name.
 * . `TWTSideBarIDLabel` - UIID for the label with the user ID.
 * . `TWTSideBarStatsActionsWrapper` - UIID for the wrapper container of all of the stats actions.
 * . `TWTSideBarCenter` - UIID for the wrapper container of the CENTER slot in the TWTSideBarWrapper.  This wraps the SIDEBAR_ACTIONS and SIDEBAR_SETTINGS_ACTIONS.
 * . `TWTSideBarActionsWrapper` - UIID for the sidebar actions wrapper container.
 * . `TWTSideBarSettingsActionsWrapper` - UIID for the container that wraps the settings actions.
 * . `TWTSideBarSouth` - UIID for the south container.  This wraps the bottom left and bottom right actions.
 * 
 * 
 * === Examples
 * 
 * .Some basic example usage.  If you use the {@link TWTFormController} class for your form controller, then you wouldn't actually create the TWTSideBar view directly.  The controller will do that for you.  See next example for such a case.
 * [source,java]
 * ----
 * public static final ActionNode profile = action(
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
   ...
  
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
 
 TWTSideBarView view = new TWTSideBarView(myViewModel, viewNode)
 * ----
 * 
 * === Example using TWTFormController
 * 
 * This component is designed to work seamlessly with the other components of the TweetAppUIKit.  If you use the `TWTFormController` as a base for your
 * form controllers, it will automatically create the TWTSideBarView as your app's sidebar, using the controller's {@link ViewNode} as the UI
 * descriptor for the side view, and the registered {@link TWTApplicationSchema} (which you would implement) as the view model for the side bar.
 * 
 * The following is a simple example.
 * 
 * .Application controller.  Since the sidebar is generally the same, app-wide, it makes sense to configure the TWTSideBar view using the UI descriptor of the appliation controller so that the settings will be inherited by all forms on the app. Complete sample can be seen https://github.com/shannah/TweetAppUIKit/blob/master/TwitterUIDemo/src/com/codename1/demos/twitterui/TwitterUIDemo.java[here].
 * [source,java]
 * ----
 * package com.codename1.demos.twitterui;


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
import com.codename1.twitterui.models.Tweet;
import com.codename1.twitterui.views.TWTTitleComponent;
import com.codename1.ui.FontImage;


public class TwitterUIDemo extends ApplicationController {
    
    // ... code redacted for sample
    
    // Define all of the actions for the app <1>
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
        
        // ... code redacted for sample ...
        
        // Create a UserProfile which will serve as the view model for many views
        // in this app.
        UserProfile p = createDemoProfile(); <2>
        addLookup(p);   // Add lookup so profile will be accessible by all controllers in app
        
        // ... code redacted for sample ...
        
    }
    
    
    
    private static UserProfile createDemoProfile() {
        UserProfile p = new UserProfile();
        p.set(Thing.name, "Steve Hannah");
        p.set(Thing.identifier, "@shannah78");
        p.set(Thing.thumbnailUrl, "https://www.codenameone.com/img/steve.jpg");
        return p;
    }

    // ... code redacted for sample ...
    
    public ViewNode createViewNode() {
        ViewNode viewNode = new ViewNode( <3>
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
 * ----
 * <1> We define a all of the actions in the app.
 * <2> Create the view model, which will be used for the sidebar and manu other views.
 * <3> Create the UI descriptor via the `createViewNode()` method.  This will serve as the root view node
 * for all UI descriptors in the app, so that its attributes will be accessible to all sub-nodes.
 * 
 * .A Form controller.  This implements the `getSideBarViewModel()` method which defines the view model that should be used for the sidebar in this form.
 * [source,java]
 * ----

package com.codename1.demos.twitterui;

import com.codename1.demos.twitterui.models.UserProfile;
import com.codename1.rad.controllers.Controller;
import com.codename1.rad.models.Entity;
import com.codename1.twitterui.controllers.TWTFormController;


public class MyFormController extends TWTFormController {
    public TweetDetailsController(Controller parent, Entity tweet) {
        super(parent);
        
        TweetDetailView view = new TweetDetailView(tweet, getViewNode());
        CollapsibleHeaderContainer wrapper = new CollapsibleHeaderContainer(
                new TWTTitleComponent(tweet, getViewNode(), new Label("Tweet")), 
                view, 
        view);
        setView(wrapper); <1>
        
    }

    @Override
    protected Entity getSideBarViewModel() {
        return lookup(UserProfile.class); <2>
    }
  
}
 * ----
 * <1> Since we only pass a Container to the `setView()` method, and not a Form, the form controller will automatically wrap the contain in a CollapsibleSideBarContainer and create the form.
 * <2> In the `getSideBarViewModel()` method we return the UserProfile class that was created and registered in the Application controller.
 * 
 * @author shannah
 */
public class TWTSideBarView extends AbstractEntityView {
    /**
     * Actions rendered at the top of the sidebar.
     */
    public static final Category SIDEBAR_TOP_ACTIONS = new Category(),
            
            /**
             * Actions displayed in the overflow menu at the top of the sidebar.
             */
            SIDEBAR_TOP_OVERFLOW_MENU = new Category(),
            
            /**
             * Actions meant to display user stats (e.g. 2034 Posts, etc..).
             */
            SIDEBAR_STATS = new Category(),
            
            /**
             * The primary menu items in the sidebar.  These will render icon and text.
             */
            SIDEBAR_ACTIONS = new Category(),
            
            /**
             * Additional settings/related actions that are rendered below the {@link #SIDEBAR_ACTIONS} menu.  These will be rendered 
 * as text only.  No icons.
             */
            SIDEBAR_SETTINGS_ACTIONS = new Category(),
            
            /**
             * Actions rendered in the bottom left.
             */
            SIDEBAR_BOTTOM_LEFT_ACTIONS = new Category(),
            
            /**
             * Actions rendered in the bottom right.
             */
            SIDEBAR_BOTTOM_RIGHT_ACTIONS = new Category();
            
            
    
    
    private ViewNode node;
    private PropertySelector nameProp, idProp, avatarProp;
    private Button nameLabel = new Button(), idLabel = new Button(), overflowMenu = new Button();
    private ProfileAvatarView avatar;
    private String avatarUrl = null;
    private static String fragmentTemplate;
    //private Container wrapper = new Container(new BorderLayout());
    private UIFragment fragment;

    private static String loadFragmentTemplate() {
        if (fragmentTemplate == null) {
            try {
                fragmentTemplate = Util.readToString(CN.getResourceAsStream("/com.codename1.twitterui.TWTSideBar.xml"));
            } catch (IOException ex) {
                Log.e(ex);
                throw new RuntimeException("Failed to load fragment for TWTSideBar", ex);
            }
            
        }
        return fragmentTemplate;
    }
    
    
   
    /**
     * Creates a new sidebar.
     * @param entity The view model.  See class documentation for specific view model requirements.
     * @param node The UI descriptor.  See class documentation for UI descriptor requirements, including supported action categories.
     */
    public TWTSideBarView(Entity entity, ViewNode node) {
        super(entity);
        this.node = node;
        loadProperties();
        initUI();
    }
    
    private void loadProperties() {
        Entity e = getEntity();
        nameProp = new PropertySelector(e, TWTApplicationSchema.user).createChildSelector(TWTUserProfileSchema.name);
        idProp = new PropertySelector(e, TWTApplicationSchema.user).createChildSelector(TWTUserProfileSchema.identifier);
        avatarProp = new PropertySelector(e, TWTApplicationSchema.user).createChildSelector(TWTUserProfileSchema.thumbnailUrl);
    }
    
    
    
    private void initUI() {
        setUIID("TWTSideBar");
        setLayout(new BorderLayout());
        Entity e = getEntity();
        EntityType et = e.getEntityType();
        setSafeAreaRoot(true);
        Entity userProfile = e.getEntity(TWTApplicationSchema.user);
        if (userProfile == null) {
            userProfile = new TWTUserProfileImpl();
        }

        avatar = new ProfileAvatarView(userProfile, getViewNode(), 8f);
        fragment = UIFragment.parseXML(loadFragmentTemplate());
        fragment.set("overflowmenu", overflowMenu);
        fragment.set("namelabel", nameLabel);
        fragment.set("idlabel", idLabel);
        fragment.set("avatar", avatar);
        avatarUrl = userProfile.getText(TWTUserProfileSchema.thumbnailUrl);
                
        add(BorderLayout.CENTER, fragment.getView());
        
        Container center = (Container)fragment.findById("center");
        center.setScrollVisible(false);
        
        center.setScrollableY(true);
        Border lineBorder = Border.createCompoundBorder(Border.createLineBorder(1, 0xeaeaea), Border.createEmpty(), Border.createEmpty(), Border.createEmpty());
        Border emptyBorder = Border.createEmpty();
        center.addScrollListener((scrollX, scrollY, oldScrollX, oldScrollY)->{
            if (scrollY > 0) {
                center.getStyle().setBorder(lineBorder);
            } else {
                center.getStyle().setBorder(emptyBorder);
            }
        });
        
        FontImage.setMaterialIcon(overflowMenu, FontImage.MATERIAL_MORE_HORIZ);
        
        
        Actions actions = getViewNode().getInheritedActions(SIDEBAR_TOP_ACTIONS).getEnabled(e);
        if (!actions.isEmpty()) {
            actions = actions.proxy().setAttributesIfNotSet(new UIID("TWTSideBarTopAction"));
            Container cnt = new Container(new GridLayout(actions.size()));
            cnt.getStyle().stripMarginAndPadding();
            actions.addToContainer(cnt, e);
            Container wcnt = (Container)fragment.findById("top-actions-wrapper");
            wcnt.add(BorderLayout.CENTER, cnt);
        }
        actions = getViewNode().getInheritedActions(SIDEBAR_ACTIONS).getEnabled(e);
        if (!actions.isEmpty()) {
            actions = actions.proxy().setAttributesIfNotSet(
                    new UIID("TWTSideBarAction"),
                    new IconUIID("TWTSideBarActionIcon")
                    );
            Container cnt = new Container(BoxLayout.y());
            cnt.getStyle().stripMarginAndPadding();
            actions.addToContainer(cnt, e);
            Container wcnt = (Container)fragment.findById("sidebar-actions-wrapper");
            wcnt.add(BorderLayout.CENTER, cnt);
        }
        actions = getViewNode().getInheritedActions(SIDEBAR_SETTINGS_ACTIONS).getEnabled(e);
        if (!actions.isEmpty()) {
            actions = actions.proxy().setAttributesIfNotSet(new UIID("TWTSideBarSettingsAction"));
            Container cnt = new Container(BoxLayout.y());
            cnt.getStyle().stripMarginAndPadding();
            actions.addToContainer(cnt, e);
            Container wcnt = (Container)fragment.findById("settings-actions-wrapper");
            wcnt.add(BorderLayout.CENTER, cnt);
        }
        actions = getViewNode().getInheritedActions(SIDEBAR_STATS).getEnabled(e);
        if (!actions.isEmpty()) {
            actions = actions.proxy().setAttributesIfNotSet(
                    new UIID("TWTSideBarStatsAction"),
                    new IconUIID("TWTSideBarStatsActionIcon"),
                    UI.actionStyle(ActionStyle.IconRight),
                    new ComponentDecoratorNode(cmp->{
                        if (cmp instanceof Label) {
                            Label l = (Label)cmp;
                            l.setVerticalAlignment(BASELINE);
                            l.setGap(CN.convertToPixels(1f));
                        }
                    })
            );
            Container cnt = new Container(new GridLayout(actions.size()));
            cnt.getStyle().stripMarginAndPadding();
            actions.addToContainer(cnt, e);
            Container wcnt = (Container)fragment.findById("stats-actions-wrapper");
            wcnt.add(BorderLayout.CENTER, cnt);
        }
        
        actions = getViewNode().getInheritedActions(SIDEBAR_BOTTOM_LEFT_ACTIONS).getEnabled(e);
        if (!actions.isEmpty()) {
            actions = actions.proxy().setAttributesIfNotSet(new UIID("TWTSideBarBottomLeftAction"));
            Container cnt = new Container(new GridLayout(actions.size()));
            cnt.setUIID("TWTSideBarBottomLeftActionsWrapper");
            //cnt.getStyle().stripMarginAndPadding();
            actions.addToContainer(cnt, e);
            Container wcnt = (Container)fragment.findById("bottom-left-actions-wrapper");
            wcnt.add(BorderLayout.CENTER, cnt);
        }
        
        actions = getViewNode().getInheritedActions(SIDEBAR_BOTTOM_RIGHT_ACTIONS).getEnabled(e);
        if (!actions.isEmpty()) {
            actions = actions.proxy().setAttributesIfNotSet(
                    new UIID("TWTSideBarBottomRightAction")
            );
            Container cnt = new Container(new GridLayout(actions.size()));
            cnt.getStyle().stripMarginAndPadding();
            actions.addToContainer(cnt, e);
            Container wcnt = (Container)fragment.findById("bottom-right-actions-wrapper");
            wcnt.add(BorderLayout.CENTER, cnt);
        }
        
        actions = getViewNode().getInheritedActions(SIDEBAR_TOP_OVERFLOW_MENU).getEnabled(e);
        if (!actions.isEmpty()) {
            
            overflowMenu.addActionListener(evt->{
                Actions actions2 = getViewNode().getInheritedActions(SIDEBAR_TOP_OVERFLOW_MENU).getEnabled(e);
                
                ActionSheet sheet = new ActionSheet(Sheet.findContainingSheet(this), e, actions2);
                sheet.show();
            });
            overflowMenu.setVisible(true);
            overflowMenu.setHidden(false);
        } else {
            overflowMenu.setVisible(false);
            overflowMenu.setHidden(true);
        }
        
        
        update();
        
    }
    
    @Override
    public void update() {
        Entity e = getEntity();
        boolean changed = false;
        String name = "";
        if (nameProp.exists()) {
            name = nameProp.getText("");
            if (!Objects.equals(name, nameLabel.getText())) {
                nameLabel.setText(name);
                changed = true;
            }
        }
        
        if (idProp.exists()) {
            String id = idProp.getText("");
            if (!Objects.equals(id, idLabel.getText())) {
                idLabel.setText(id);
                changed = true;
            }
        }

        if (!Objects.equals(avatarUrl, avatarProp.getText(null))) {
            Container parent = avatar.getParent();
            Component oldAvatar = avatar;
            if (parent != null) {

                avatar = new ProfileAvatarView(avatarProp.getLeafEntity(), getViewNode(), 8f);
                parent.replace(oldAvatar, avatar, null);
                changed = true;
                avatarUrl = avatarProp.getText(null);
            }
        }
        
        if (changed) {
            Form f = getComponentForm();
            if (f != null) {
                f.revalidateWithAnimationSafety();
            }
        }
    }

    @Override
    public void commit() {
        
    }

    @Override
    public Node getViewNode() {
        return node;
    }

    @Override
    public void layoutContainer() {
        super.layoutContainer();
        
        // Line up the actions with the name label
        
        int x = nameLabel.getAbsoluteX() + nameLabel.getStyle().getPaddingLeft(false);
        $("TWTSideBarAction, TWTSideBarSettingsAction, ProfileAvatarView", this).each(c->{
            int absX = c.getAbsoluteX() + c.getStyle().getPaddingLeft(false);
            if (absX < x) {
                c.setX(c.getX() + x - absX);
            }
            if (c instanceof Label) {
                Label l = (Label)c;
                l.setGap(CN.convertToPixels(3f));
            }
        });
        $("TWTSideBarStatsAction, TWTSideBarBottomLeftAction", this).getParent().firstChild().each(c->{
            int absX = c.getAbsoluteX() + c.getStyle().getPaddingLeft(false);
            if (absX < x) {
                c.getParent().setX(c.getParent().getX() + x - absX);
            }
        });
    }
    
    
    
}
