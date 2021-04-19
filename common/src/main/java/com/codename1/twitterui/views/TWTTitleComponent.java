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
import com.codename1.rad.models.Tag;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ActionNode.Category;
import com.codename1.rad.nodes.Node;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.ui.AbstractEntityView;
import com.codename1.rad.ui.Actions;
import com.codename1.rad.ui.Slot;
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
import com.codename1.rad.schemas.Thing;
import com.codename1.ui.events.ActionEvent;

/**
 * This component is the equivalent of the "ToolBar" for TweetAppUIKit apps.  It will display
 * either a "Back" button, or the current user's avatar in the upper left, depending on context,
 * The remainder of the component is filled with the "main content", which is passed to the component
 * constructor.
 * 
 * === View Model
 * 
 * There are no "hard" requirements with respect to schemas that the view model must support.  However
 * the view model will be used to add an avatar to its `WEST` slot which is a {@link ProfileAvatarView}
 * of the view model.  Hence if the view model includes a property with the {@link Thing#thumbnailUrl} or {@link ListRowItem#icon} tag,
 * it will be used to generate an avatar.  If neither of those is available, it will create an avatar out of the first letter
 * of the name, as supplied in the {@link Thing#name} property.
 * 
 * === Actions
 * 
 * Actions in the {@link #TITLE_ACTIONS} category will be rendered in the EAST slot of the border layout of this 
 * component.
 * 
 * You can explicitly handle when the user clicks the "Back" button via the {@link #BACK_BUTTON_CLICKED} action.  If you don't supply this
 * action, or if the resulting event is not consumed, it will proceed to perform default behaviour for the back button, which is to navigate back to the previous form.
 * 
 * You can explicitly handle when the user clicks on the avatar (in the WEST slot) via the {@link #AVATAR_CLICKED} action.  If you
 * don't supply this action, or if the resulting event is not consumed, it will proceed to perform default behaviour.
 * 
 * The default behaviour when the user clicks on the avatar is to open the sidebar.  This component doesn't perform this action on its own
 * though, it just fires a {@link CollapsibleSideBarContainer.ShowSideBarEvent} event up the Controller hierarchy, which will be consumed
 * by any CollapsibleSideBar container that is currently displayed, to show itself.  If you setup your UI to use a {@link CollapsibleSideBarContainer},
 * then this will "just work".
 * 
 * 
 * === Styles
 * 
 * . `TWTTitleComponent` - UIID for the component.
 * . `TWTTitleComponentLeftButton` - UIID for the back button and avatar button in WEST slot.
 * . `TWTTitleComponentOverflowMenuButton` - UIID used for the overflow actions button.  (If there is more than one action registered in the {@link #TITLE_ACTIONS} category, then they will be rendered in an overflow menu.
 * 
 * === Back Button vs Avatar
 * 
 * If the current form controller has a "back" option (e.g. there is a parent form controller
 * that it can navigate back to, then a "Back Arrow" button will be rendered in the WEST slot.  Otherwise
 * it will render the avatar generated from the view model.
 * 
 * === Examples
 * 
 * .TWTTitleComponent with a `TWTSearchButton` as its main content, and a single "Settings" action rendered in its `EAST` slot via the `TITLE_ACTIONS` category.
image::https://shannah.github.io/TweetAppUIKit/manual/images/Image-070520-073823.013.png[]
* 
* [source,java]
* ----
UserProfile p = new UserProfile();
p.set(Thing.name, "Steve Hannah");
p.set(Thing.identifier, "@shannah78");
p.set(Thing.thumbnailUrl, "https://www.codenameone.com/img/steve.jpg");
ViewNode viewNode = new ViewNode(actions(TWTTitleComponent.TITLE_ACTIONS, settings));
new TWTTitleComponent(p, viewNode, new TWTSearchButton(p, viewNode));
* ----
* 
* .TWTTitleComponent with a Label as its main content, and a single "Settings" action rendered in its `EAST` slot via the `TITLE_ACTIONS` category.  The "Back" button is rendered here instead of the avatar because there is a form to go back to.
image::https://shannah.github.io/TweetAppUIKit/manual/images/Image-070520-074236.178.png[]
* 
* 
 * 
 * @author shannah
 */
public class TWTTitleComponent extends AbstractEntityView {

    /**
     * Slot ID that allows controllers to override the main content of this view.
     */
    public static final Tag mainContentSlot = new Tag("TWTTitleComponent.mainContentSlot");
    
    /**
     * Actions displayed in the EAST slot of the title component.  If only one action is registered in this
     * category, then it will be displayed directly as a button.  If more than one is registered, then there will
     * be "menu" button which displays an action menu with all of the actions.
     */
    public static final Category TITLE_ACTIONS = new Category();
    
    /**
     * Action triggered with the avatar (in the WEST slot) is clicked.  If you do not supply this action, or the resulting event
     * is not consumed, then it will proceed to perform default behaviour which is to expand the {@link CollapsibleSideBarContainer}.
     */
    public static final Category AVATAR_CLICKED = new Category();
    
    /**
     * Action triggered when the back button is clicked.  If you do not supply this actiokn, or the resulting event is
     * not consumed, then it will proceed to perform defualt behaviour, which is to navigate to the previous form.
     */
    public static final Category BACK_BUTTON_CLICKED = new Category();
    

    private Button backButton, sidebarButton;
    private ProfileAvatarView avatar;
    private Component mainContent;
    
    /**
     * Creates a new title component.
     * @param entity The view model.
     * @param node The UI descriptor.
     * @param mainContent 
     */
    public TWTTitleComponent(Entity entity, ViewNode node, Component mainContent) {
        super(entity, node);

        this.mainContent = mainContent;
        initUI();
    }
    
    
    
    private void initUI() {
        this.setSafeArea(true);
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
            ActionNode backButtonClicked = getViewNode().getInheritedAction(BACK_BUTTON_CLICKED);
            if (backButtonClicked != null) {
                ActionEvent e2 = backButtonClicked.fireEvent(getEntity(), TWTTitleComponent.this);
                if (e2.isConsumed()) {
                    return;
                }
            }
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
            ActionNode sidebarClickedAction = getViewNode().getInheritedAction(AVATAR_CLICKED);
            if (sidebarClickedAction != null) {
                ActionEvent e2 = sidebarClickedAction.fireEvent(getEntity(), TWTTitleComponent.this);
                if (e2.isConsumed()) {
                    return;
                }
            }
            
            
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

        Slot mainSlot = new Slot(mainContentSlot, this);
        mainSlot.setContent(mainContent);
        add(BorderLayout.CENTER, mainSlot);
        
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
    
}
