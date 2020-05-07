/*
 * Copyright 2020 shannah.
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

import ca.weblite.shared.components.ComponentImage;
import ca.weblite.shared.components.CollapsibleSideBarContainer;
import com.codename1.rad.controllers.ActionSupport;
import com.codename1.rad.controllers.FormController;
import com.codename1.rad.controllers.ViewController;
import com.codename1.rad.models.Entity;
import com.codename1.rad.nodes.ActionNode.Category;
import com.codename1.rad.nodes.Node;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.ui.AbstractEntityView;
import com.codename1.rad.ui.Actions;
import com.codename1.rad.ui.entityviews.ProfileAvatarView;
import com.codename1.rad.ui.menus.ActionSheet;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Sheet;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Border;

/**
 * This component is the equivalent of the "ToolBar" for TweetAppUIKit apps.  It will display
 * either a "Back" button, or the current user's avatar in the upper left, depending on context,
 * The remainder of the component is filled with the "main content", which is passed to the component
 * constructor.
 * 
 * @author shannah
 */
public class TWTTitleComponent extends AbstractEntityView {
    
    public static final Category TITLE_ACTIONS = new Category();
    
    private ViewNode node;
    private Button backButton, sidebarButton;
    private ProfileAvatarView avatar;
    private Component mainContent;
    
    /**
     * Creates a new title 
     * @param entity
     * @param node
     * @param mainContent 
     */
    public TWTTitleComponent(Entity entity, ViewNode node, Component mainContent) {
        super(entity);
        this.node = node;
        this.mainContent = mainContent;
        initUI();
    }
    
    
    
    private void initUI() {
        $(this).addTags("TWTTitleComponent");
        setUIID("TWTTitleComponent");
        setName("TWTTitleComponent");
        //getStyle().setBorder(Border.createCompoundBorder(null, Border.createLineBorder(1, 0xdddddd), null, null));
        
        if ($(mainContent).filter(".center-fill").isEmpty()) {
            
            setLayout(new BorderLayout(BorderLayout.CENTER_BEHAVIOR_CENTER_ABSOLUTE) {
                @Override
                public void layoutContainer(Container target) {
                    super.layoutContainer(target);
                    int sideBarButtonY = sidebarButton.getAbsoluteY() - TWTTitleComponent.this.getAbsoluteY();
                    mainContent.setY(sideBarButtonY + sidebarButton.getStyle().getPaddingTop());
                    mainContent.setHeight(sidebarButton.getHeight() - sidebarButton.getStyle().getVerticalPadding());
                }
                
            });
        } else {
            
            setLayout(new BorderLayout(){
                @Override
                public void layoutContainer(Container target) {
                    super.layoutContainer(target);
                    int sideBarButtonY = sidebarButton.getAbsoluteY() - TWTTitleComponent.this.getAbsoluteY();
                    mainContent.setY(sideBarButtonY + sidebarButton.getStyle().getPaddingTop());
                    mainContent.setHeight(sidebarButton.getHeight() - sidebarButton.getStyle().getVerticalPadding());
                }
                
            });
        }
        backButton = new Button();
        FontImage.setIcon(backButton, FontImage.MATERIAL_ARROW_BACK_IOS, -1);
        backButton.addActionListener(evt->{
            evt.consume();
            ActionSupport.dispatchEvent(new FormController.FormBackEvent(this));
        });
        
        avatar = new ProfileAvatarView(getEntity(), 5f);
        avatar.setWidth(avatar.getPreferredW());
        avatar.setHeight(avatar.getPreferredH());
        avatar.layoutContainer();
        ComponentImage im = new ComponentImage(avatar, avatar.getWidth(), avatar.getHeight());
        
        sidebarButton = new Button(im);
        sidebarButton.addActionListener(evt->{
            evt.consume();
            
            ActionSupport.dispatchEvent(new CollapsibleSideBarContainer.ShowSideBarEvent(sidebarButton));
        });
        $(backButton, sidebarButton)
                .setUIID("TWTTitleComponentLeftButton");
        
        Container btnsWrap = BoxLayout.encloseX(sidebarButton, backButton);
        btnsWrap.getStyle().stripMarginAndPadding();
        // Aligning the top left button left with other components in the 
        // UI.  See docs in CollapsiableHeaderContainer
        $(this).addTags("left-edge");
        $(btnsWrap).addTags("left-inset");
        add(BorderLayout.WEST, btnsWrap);
        add(BorderLayout.CENTER, mainContent);
        
        Actions actions = getViewNode().getInheritedActions(TITLE_ACTIONS).getEnabled(getEntity());
        if (!actions.isEmpty()) {
            if (actions.size() == 1) {
                Container cnt = new Container(BoxLayout.x());
                cnt.getStyle().stripMarginAndPadding();
                actions.addToContainer(cnt, getEntity());
                add(BorderLayout.EAST, cnt);
            } else {
                Button moreButton = new Button();
                moreButton.setUIID("TWTTitleComponentOverflowMenuButton");
                moreButton.addActionListener(evt->{
                    evt.consume();
                    ActionSheet actionSheet = new ActionSheet(Sheet.findContainingSheet(this), getEntity(), actions);
                    actionSheet.show();
                });
                add(BorderLayout.EAST, moreButton);
            }
        }
        
        update();
        
        
    }

    @Override
    protected void initComponent() {
        super.initComponent();
        update();
    }
    
    
    
    @Override
    public void update() {
        boolean changed = false;
        Form f = getComponentForm();
        if (f != null) {
            ViewController vc = ViewController.getViewController(this);
            FormController fc = vc.getFormController();
            if (fc != null) {
                if (fc.hasBackCommand()) {
                    if (sidebarButton.isVisible()) {
                        sidebarButton.setVisible(false);
                        sidebarButton.setHidden(true);
                        changed = true;
                    }
                    if (!backButton.isVisible()) {
                        backButton.setVisible(true);
                        backButton.setHidden(false);
                        changed = true;
                    }
                } else {
                    if (!sidebarButton.isVisible()) {
                        sidebarButton.setVisible(true);
                        sidebarButton.setHidden(false);
                        changed = true;
                    }
                    if (backButton.isVisible()) {
                        backButton.setVisible(false);
                        backButton.setHidden(true);
                        changed = true;
                    }
                }
                if (changed) {
                    f.revalidateWithAnimationSafety();
                }
            } else {
                avatar.setVisible(false);
                avatar.setHidden(true);
                sidebarButton.setVisible(false);
                sidebarButton.setHidden(true);
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
    
}
