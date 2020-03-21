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
import com.codename1.rad.models.Property;
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
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.Sheet;
import com.codename1.ui.UIFragment;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Border;
import java.io.IOException;

/**
 *
 * @author shannah
 */
public class TWTSideBarView extends AbstractEntityView {
    public static final Category SIDEBAR_TOP_ACTIONS = new Category(),
            SIDEBAR_TOP_OVERFLOW_MENU = new Category(),
            SIDEBAR_STATS = new Category(),
            SIDEBAR_ACTIONS = new Category(),
            SIDEBAR_SETTINGS_ACTIONS = new Category(),
            SIDEBAR_BOTTOM_LEFT_ACTIONS = new Category(),
            SIDEBAR_BOTTOM_RIGHT_ACTIONS = new Category();
            
            
    
    
    private ViewNode node;
    private Property nameProp, idProp;
    private Button nameLabel = new Button(), idLabel = new Button(), overflowMenu = new Button();
    private ProfileAvatarView avatar;
    private static String fragmentTemplate;
    //private Container wrapper = new Container(new BorderLayout());
    private UIFragment fragment;
    private Container avatarWrapper,
            topActionsWrapper,
            statsActionsWrapper,
            sidebarActionsWrapper,
            settingsActionsWrapper,
            bottomLeftActionsWrapper,
            bottomRightActionsWrapper;
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
    
    
    
    public TWTSideBarView(Entity entity, ViewNode node) {
        super(entity);
        this.node = node;
        loadProperties();
        initUI();
    }
    
    private void loadProperties() {
        Entity e = getEntity();
        EntityType et = e.getEntityType();
        nameProp = et.findProperty(Thing.name);
        idProp = et.findProperty(Thing.identifier);
    }
    
    
    
    private void initUI() {
        setUIID("TWTSideBar");
        setLayout(new BorderLayout());
        Entity e = getEntity();
        EntityType et = e.getEntityType();
        setSafeAreaRoot(true);
        
        fragment = UIFragment.parseXML(loadFragmentTemplate());
        fragment.set("overflowmenu", overflowMenu);
        fragment.set("namelabel", nameLabel);
        fragment.set("idlabel", idLabel);
        fragment.set("avatar", new ProfileAvatarView(e, getViewNode(), 8f));
                
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
        if (nameProp != null) {
            name = e.getText(nameProp);
            if (!Objects.equals(name, nameLabel.getText())) {
                nameLabel.setText(name);
                changed = true;
            }
        }
        
        if (idProp != null) {
            String id = e.getText(idProp);
            if (!Objects.equals(id, idLabel.getText())) {
                idLabel.setText(id);
                changed = true;
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
