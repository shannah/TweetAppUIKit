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
import com.codename1.rad.annotations.Inject;
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
import com.codename1.rad.ui.*;
import com.codename1.rad.ui.entityviews.ProfileAvatarView;
import com.codename1.rad.ui.menus.ActionSheet;
import com.codename1.twitterui.controllers.TWTApplicationController;
import com.codename1.twitterui.models.TWTApplicationModel;
import com.codename1.twitterui.models.TWTUserProfileImpl;
import com.codename1.twitterui.schemas.TWTApplicationSchema;
import com.codename1.twitterui.schemas.TWTUserProfileSchema;
import com.codename1.twitterui.models.TWTUserProfile;
import com.codename1.ui.*;

import static com.codename1.ui.ComponentSelector.$;

import com.codename1.ui.geom.Dimension;
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
 * === Example Usage
 *
 * [source,xml]
 * ----
 * <twtSideBarView />
 * ----
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
    
    

    public TWTSideBarView(@Inject ViewContext context) {
        super(context);

        loadProperties();
        initUI();
    }

    /**
     * Creates a new sidebar.
     * @param entity The view model.  See class documentation for specific view model requirements.
     * @param node The UI descriptor.  See class documentation for UI descriptor requirements, including supported action categories.
     * @deprecated Use {@link #TWTSideBarView(ViewContext)}
     */
    public TWTSideBarView(Entity entity, ViewNode node) {
        super(entity, node);

        loadProperties();
        initUI();
    }
    
    private void loadProperties() {
        Entity e = getEntity();
        TWTUserProfile userProfile = getContext().getController().lookup(TWTUserProfile.class);
        if (userProfile != null) {
            nameProp = new PropertySelector(userProfile, TWTUserProfileSchema.name);
            idProp = new PropertySelector(userProfile, TWTUserProfileSchema.identifier);
            avatarProp = new PropertySelector(userProfile, TWTUserProfileSchema.thumbnailUrl);
        } else {
            TWTApplicationModel applicationModel = getContext().getController().lookup(TWTApplicationModel.class);
            if (applicationModel == null) {
                TWTApplicationController appController = getContext().getController().lookup(TWTApplicationController.class);
                if (appController != null) {
                    applicationModel = appController.getApplicationModel();
                }
            }
            if (applicationModel != null) {
                nameProp = new PropertySelector(applicationModel, TWTApplicationSchema.user).createChildSelector(TWTUserProfileSchema.name);
                idProp = new PropertySelector(applicationModel, TWTApplicationSchema.user).createChildSelector(TWTUserProfileSchema.identifier);
                avatarProp = new PropertySelector(applicationModel, TWTApplicationSchema.user).createChildSelector(TWTUserProfileSchema.thumbnailUrl);
            } else {
                nameProp = new PropertySelector(e, TWTUserProfileSchema.name);
                idProp = new PropertySelector(e, TWTUserProfileSchema.identifier);
                avatarProp = new PropertySelector(e, TWTUserProfileSchema.thumbnailUrl);

            }
        }

    }

    private TWTUserProfile userProfile;

    private TWTUserProfile getUserProfile() {
        if (userProfile == null) {
            TWTUserProfile out = getContext().getController().lookup(TWTUserProfile.class);
            if (out != null) {
                userProfile = out;
                return userProfile;
            }
            TWTApplicationModel appModel = getContext().getController().lookup(TWTApplicationModel.class);
            if (appModel == null) {
                TWTApplicationController cnt = getContext().getController().lookup(TWTApplicationController.class);
                if (cnt != null) {
                    appModel = cnt.getApplicationModel();
                }
            }
            if (appModel != null && appModel.getUser() != null) {
                 userProfile = appModel.getUser();
                 return userProfile;
            }
            userProfile = new TWTUserProfileImpl();
        }
        return userProfile;
    }
    
    private void initUI() {
        setUIID("TWTSideBar");
        setLayout(new BorderLayout());
        Entity e = getEntity();
        EntityType et = e.getEntityType();
        setSafeAreaRoot(true);
        Entity userProfile = getUserProfile();
        getViewNode().setAttributesIfNotExists(UI.param(ProfileAvatarView.FALLBACK_SETTINGS, ProfileAvatarView.FallbackSettings.DefaultIcon));
        avatar = new ProfileAvatarView(userProfile, getViewNode(), 8f);
        fragment = UIFragment.parseXML(loadFragmentTemplate());
        fragment.set("overflowMenu", overflowMenu);
        fragment.set("nameLabel", nameLabel);
        fragment.set("idLabel", idLabel);
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


    @Override
    protected Dimension calcPreferredSize() {
        if (CN.isPortrait() && !CN.isTablet()) {
            return new Dimension((int)Math.round(CN.getDisplayWidth() * 0.85), CN.getDisplayHeight());
        } else {
            return new Dimension((int)Math.min(CN.getDisplayWidth(), CN.convertToPixels(150)), CN.getDisplayHeight());
        }

    }
}
