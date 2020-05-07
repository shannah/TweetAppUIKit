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
import com.codename1.compat.java.util.Objects;
import com.codename1.rad.attributes.UIID;
import com.codename1.rad.models.Attribute;
import com.codename1.rad.models.DateFormatterAttribute;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.models.Property;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ActionNode.Category;
import com.codename1.rad.nodes.ListNode;
import com.codename1.rad.nodes.Node;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.ui.AbstractEntityView;
import com.codename1.rad.ui.Actions;
import com.codename1.rad.ui.DefaultEntityListCellRenderer;
import com.codename1.rad.ui.EntityView;
import com.codename1.rad.ui.UI;
import com.codename1.rad.ui.ViewProperty;
import com.codename1.rad.ui.entityviews.EntityListView;
import com.codename1.rad.ui.entityviews.ProfileAvatarView;
import com.codename1.rad.ui.menus.ActionSheet;
import com.codename1.twitterui.schemas.TWTKeyword;
import com.codename1.twitterui.schemas.TWTProfile;
import com.codename1.twitterui.text.TweetDateFormatter;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.Sheet;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;

/**
 * A component that renders a list of user profiles as either rows or badges.  This is used by the {@link TWTSearchView}
 * in {@link ListStyle#Badges} mode to render the list of recent profiles visited.
 * 
 * image::https://shannah.github.io/TweetAppUIKit/manual/images/TWTProfileList.png[]
 * 
 * === View Model
 * 
 * As an {@link EntityListView} subclass, the view model is expected to ben an {@link EntityList}.  When in {@link ListStyle#Badges} mode,
 * it uses {@link TWTProfileListRowX} to render each item.  When in {@link ListStyle#Rows} mode, it uses {@link TWTProfileListRow} to render
 * each item.  In both cases, it supports the {@link TWTProfile#name}, {@link TWTProfile#identifier}, and {@link TWTProfile#thumbnailUrl} tags.
 * 
 * NOTE: {@link TWTProfile#name} is the same as {@link Thing#name},  {@link TWTProfile#identifier} is the same as {@link Thing#identifier}, 
 * and {@link TWTProfile#thumbnailUrl} is the same as {@link Thing#thumbnailUrl}, so if your view model provides properties with these tags, they 
 * will be compatible.
 * 
 * === Actions
 * 
 * The supported actions will be different depending on whether the list is in {@link ListStyle#Badges} mode or {@link ListStyle#Rows} mode.  If in badges mode,
 * then see {@link TWTProfileListRowX} documentation for a list of supported actions.  If in "rows" mode, then see {@link TWTProfileListRow} for a list
 * of supported actions.
 * 
 * 
 * @author shannah
 */
public class TWTProfileList extends EntityListView {
    
    /**
     * Enum to specify the style of the profile list.
     */
    public static enum ListStyle {
        /**
         * The list should be displayed in rows.  When in this mode, the {@link TWTProfileList} uses {@link TWTProfileListRow} to render each
         * item of the list model.
         */
        Rows,
        
        /**
         * The list should be displayed as badges.  When in this mode, the {@link TWTProfileList} uses {@link TWTProfileListRowX} to render each 
         * item of the list model.
         */
        Badges
    }
    
    /**
     * An attribute that can be added to the UI descriptor for the TWTProfileList to specify the {@link ListStyle}.
     */
    public static class ListStyleAttribute extends Attribute<ListStyle> {
        public ListStyleAttribute(ListStyle value) {
            super(value);
        }
    }
    
    /**
     * Convenience method to create a {@link TWTProfileList} to render the given profiles in the given style.
     * @param profiles The view model for the {@link TWTProfileList}
     * @param style The style of the list.
     * @return The profile list.
     */
    public static TWTProfileList creatProfileList(EntityList profiles, ListStyle style) {
        ListNode node = new ListNode();
        switch (style) {
            case Rows: {
                node.setAttributes(new ListStyleAttribute(ListStyle.Rows));
                break;
            }
            case Badges: {
                node.setAttributes(new ListStyleAttribute(ListStyle.Badges));
            }
                
        }
        TWTProfileList out = new TWTProfileList(profiles, node);
        switch (style) {
            case Badges:
                out.setListLayout(BoxLayout.x());
                out.getScrollWrapper().setScrollableX(true);
        }
        return out;
    }
    
    private static ListNode decorate(ListNode node) {
        if (node == null) {
            node = new ListNode();
        }

        ActionNode dummyAction = new ActionNode();
        dummyAction.setParent(node);
        ListStyleAttribute listStyle = (ListStyleAttribute)node.findAttribute(ListStyleAttribute.class);
        if (listStyle != null) {
            switch (listStyle.getValue()) {
                case Badges:
                    node.setAttributes(UI.cellRenderer(new TWTProfileListXCellRenderer()));
                    break;
                default:
                    node.setAttributes(UI.cellRenderer(new TWTProfileListCellRenderer()));
            }
        } else {
            node.setAttributes(UI.cellRenderer(new TWTProfileListCellRenderer()));
        }
        return node;
    }
    
    /**
     * Creates a new profile list with default UI descriptor (which is in {@link ListStyle#Rows} mode.
     * @param list 
     */
    public TWTProfileList(EntityList list) {
        this(list, null);
    }
    
    /**
     * Creates a new profile list.
     * @param list The view model.
     * @param node The UI descriptor.
     */
    public TWTProfileList(EntityList list, ListNode node) {
        super(list, decorate(node));
        setUIID("TWTProfileList");
        setName("TWTProfileList");
        if (node.findAttribute(ListStyleAttribute.class) != null && node.findAttribute(ListStyleAttribute.class).getValue() == ListStyle.Badges) {
            setSafeArea(true);
        }
    }
    
    /**
     * Component to render a twitter profile as a badge.  This includes just the avatar ({@link TWTProfile#thumbnailUrl}), name ({@link TWTProfile#name}),
     * and account ID ({@link TWTProfile#identifier}).
     * 
     * === View Model
     * 
     * This view supports the {@link TWTProfile#name}, {@link TWTProfile#identifier}, and {@link TWTProfile#thumbnailUrl} tags.
     * 
     * image::https://shannah.github.io/TweetAppUIKit/manual/images/TWTProfileListRowX.png[]
     * 
     * NOTE: {@link TWTProfile#name} is the same as {@link Thing#name},  {@link TWTProfile#identifier} is the same as {@link Thing#identifier}, 
     * and {@link TWTProfile#thumbnailUrl} is the same as {@link Thing#thumbnailUrl}, so if your view model provides properties with these tags, they 
     * will be compatible.
     * 
     * === Actions
     * 
     * . {@link #PROFILE_CLICKED} - Action triggered when the profile is clicked.
     * . {@link #PROFILE_LONGPRESS} - Action triggered when profile is long pressed.
     * . {@link #PROFILE_CLICKED_MENU} - Actions to include in popup menu when user clicks on the profile.
     * . {@link #PROFILE_LONGPRESS_MENU} - ACtions to include in popup menu when user long presses on the profile.
     * 
     * === Styles
     * 
     * . `TWTProfileListRowXName` - UIID for the user's name
     * . `TWTProfileListRowXID` - UIID for the user's ID
     * . `TWTProfileListRowXIcon` - UIID for the user's avatar.
     * . `TWTProfileListRowX` - UIID for the component.
     */
    public static class TWTProfileListRowX extends AbstractEntityView {
        
        /**
         * Action triggered when the profile is clicked.
         */
        public static final Category PROFILE_CLICKED = new Category(),
                
                /**
                 * Action triggered when profile is long pressed.
                 */
                PROFILE_LONGPRESS = new Category(),
                
                /**
                 * Actions to include in popup menu when user clicks on the profile.
                 */
                PROFILE_CLICKED_MENU = new Category(),
                
                /**
                 * ACtions to include in popup menu when user long presses on the profile.
                 */
                PROFILE_LONGPRESS_MENU = new Category();
        
        
        
        private ViewNode node;
        private Label nameLabel = new Label("", "TWTProfileListRowXName");
        private Label idLabel = new Label("", "TWTProfileListRowXID");
        private Button icon = new Button("", "TWTProfileListRowXIcon");
        private Property nameProp, iconProp, idProp;
        
        /**
         * Creates a new profile view.
         * @param entity The view model.
         * @param node The ui descriptor.
         */
        public TWTProfileListRowX(Entity entity, ViewNode node) {
            super(entity);
            setPreferredW(CN.convertToPixels(14));
            setPreferredH(CN.convertToPixels(18));
            this.node = node;
            this.nameProp = entity.findProperty(TWTProfile.name);
            this.iconProp = entity.findProperty(TWTProfile.thumbnailUrl);
            this.idProp = entity.findProperty(TWTProfile.identifier);
            initUI();
        }
        
        private void initUI() {
            setUIID("TWTProfileListRowX");
            setName("TWTProfileListRowX");
            setLayout(new BorderLayout());
            $(nameLabel, idLabel)
                    .setAlignment(CENTER)
                    .setEndsWith3Points(true)
                    .getParent().getAllStyles().stripMarginAndPadding();
            add(BorderLayout.CENTER, BoxLayout.encloseY(nameLabel, idLabel));
            setLeadComponent(icon);
            icon.addActionListener(evt->{
                ActionNode clicked = getViewNode().getInheritedAction(PROFILE_CLICKED);
                if (clicked != null) {
                    ActionEvent ae = clicked.fireEvent(getEntity(), icon);
                    if (ae.isConsumed()) {
                        return;
                    }
                }
                
                Actions clickedMenu = getViewNode().getInheritedActions(PROFILE_CLICKED_MENU).getEnabled(getEntity());
                if (!clickedMenu.isEmpty()) {
                    ActionSheet sheet = new ActionSheet(Sheet.findContainingSheet(icon), getEntity(), clickedMenu);
                    sheet.show();
                }
            });
            
            icon.addLongPressListener(evt->{
                
                ActionNode clicked = getViewNode().getInheritedAction(PROFILE_LONGPRESS);
                if (clicked != null) {
                    ActionEvent ae = clicked.fireEvent(getEntity(), icon);
                    if (ae.isConsumed()) {
                        evt.consume();
                        return;
                    }
                }
                
                Actions clickedMenu = getViewNode().getInheritedActions(PROFILE_LONGPRESS_MENU).getEnabled(getEntity());
                if (!clickedMenu.isEmpty()) {
                    evt.consume();
                    ActionSheet sheet = new ActionSheet(Sheet.findContainingSheet(icon), getEntity(), clickedMenu);
                    sheet.show();
                }
            });
            
            
            
            add(BorderLayout.NORTH, FlowLayout.encloseCenter(icon));
            update();
                
            
        }
        
        
        @Override
        public void update() {
            boolean changed = false;
            String name = "";
            if (!getEntity().isEmpty(nameProp)) {
                name = getEntity().getText(nameProp);
            }
            if (!Objects.equals(name, nameLabel.getText())) {
                nameLabel.setText(name);
                changed = true;
            }
            
            String id = "";
            if (!getEntity().isEmpty(idProp)) {
                id = getEntity().getText(idProp);
            }
            if (!Objects.equals(id, idLabel.getText())) {
                idLabel.setText(id);
                changed = true;
            }
            
            
            
            if (icon.getIcon() == null) {
                ProfileAvatarView avatar = new ProfileAvatarView(getEntity(), 10);
                avatar.getStyle().stripMarginAndPadding();
                avatar.setWidth(avatar.getPreferredW());
                avatar.setHeight(avatar.getPreferredH());
                avatar.layoutContainer();
                ComponentImage img = new ComponentImage(avatar, avatar.getWidth(), avatar.getHeight());
                icon.setIcon(img);
                changed = true;
                
            }
            
            if (changed) {
                Form f = getComponentForm();
                if (f != null) {
                    revalidateWithAnimationSafety();
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
    
     /**
     * Component to render a twitter profile as a row in the {@link TWTProfileList}.  This includes just the avatar ({@link TWTProfile#thumbnailUrl}), name ({@link TWTProfile#name}),
     * and account ID ({@link TWTProfile#identifier}) - in addition to a number of actions defined in the {@link #PROFILE_ACTIONS} category.
     * 
     * === View Model
     * 
     * This view supports the {@link TWTProfile#name}, {@link TWTProfile#identifier}, and {@link TWTProfile#thumbnailUrl} tags.
     * 
     * NOTE: {@link TWTProfile#name} is the same as {@link Thing#name},  {@link TWTProfile#identifier} is the same as {@link Thing#identifier}, 
     * and {@link TWTProfile#thumbnailUrl} is the same as {@link Thing#thumbnailUrl}, so if your view model provides properties with these tags, they 
     * will be compatible.
     * 
     * === Actions
     * 
     * . {@link #PROFILE_ACTIONS} - Actions rendered in the row.
     * 
     * === Styles
     * 
     * . `TWTProfileListRowName` - UIID for the user's name
     * . `TWTProfileListRowID` - UIID for the user's ID
     * . `TWTProfileListRowIcon` - UIID for the user's avatar.
     * . `TWTProfileListRow` - UIID for the component.
     * . `TWTProfileAction` - UIID for buttons to render the {@link #PROFILE_ACTIONS} actions.
     * . `TWTProfileActionsWrapper` - UIID for container that wraps the {@link #PROFILE_ACTIONS} actions.
     * 
     */
    public static class TWTProfileListRow extends AbstractEntityView {
         
        public static final Category PROFILE_ACTIONS = new Category();
        
        
        private ViewNode node;
        private Label nameLabel = new Label("", "TWTProfileListRowName");
        private Label idLabel = new Label("", "TWTProfileListRowID");
        private Label icon = new Label("", "TWTProfileListRowIcon");
        private Property nameProp, iconProp, idProp;
        
        
        public TWTProfileListRow(Entity entity, ViewNode node) {
            super(entity);
            this.node = node;
            this.nameProp = entity.findProperty(TWTProfile.name);
            this.iconProp = entity.findProperty(TWTProfile.thumbnailUrl);
            this.idProp = entity.findProperty(TWTProfile.identifier);
            setSafeArea(true);
            initUI();
        }
        
        private void initUI() {
            setUIID("TWTProfileListRow");
            setName("TWTProfileListRow");
            setLayout(new BorderLayout());
            add(BorderLayout.CENTER, BoxLayout.encloseY(nameLabel, idLabel));
            Actions actions = getViewNode().getInheritedActions(PROFILE_ACTIONS).getEnabled(getEntity())
                    .setAttributesIfNotSet(new UIID("TWTProfileAction"));
            
            if (!actions.isEmpty()) {
                Container cnt = new Container(new GridLayout(actions.size()));
                cnt.setUIID("TWTProfileActionsWrapper");
                actions.addToContainer(cnt, getEntity());
                $("*", cnt)
                    .filter(c->{return c instanceof Button;})
                    .first().each(c->{
                        TWTProfileListRow.this.setLeadComponent(c);
                    });
                add(BorderLayout.EAST, cnt);
            }
            
            add(BorderLayout.WEST, icon);
            update();
                
            
        }
        
        
        @Override
        public void update() {
            boolean changed = false;
            String name = "";
            if (!getEntity().isEmpty(nameProp)) {
                name = getEntity().getText(nameProp);
            }
            if (!Objects.equals(name, nameLabel.getText())) {
                nameLabel.setText(name);
                changed = true;
            }
            
            String id = "";
            if (!getEntity().isEmpty(idProp)) {
                id = getEntity().getText(idProp);
            }
            if (!Objects.equals(id, idLabel.getText())) {
                idLabel.setText(id);
                changed = true;
            }
            
            
            
            if (icon.getIcon() == null) {
                ProfileAvatarView avatar = new ProfileAvatarView(getEntity(), 6);
                avatar.getStyle().stripMarginAndPadding();
                avatar.setWidth(avatar.getPreferredW());
                avatar.setHeight(avatar.getPreferredH());
                avatar.layoutContainer();
                ComponentImage img = new ComponentImage(avatar, avatar.getWidth(), avatar.getHeight());
                icon.setIcon(img);
                changed = true;
                
            }
            
            if (changed) {
                Form f = getComponentForm();
                if (f != null) {
                    revalidateWithAnimationSafety();
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
    
    public static class TWTProfileListCellRenderer extends DefaultEntityListCellRenderer {

        @Override
        public EntityView getListCellRendererComponent(EntityListView list, Entity value, int index, boolean isSelected, boolean isFocused) {
            ListNode node = (ListNode)list.getViewNode();
                    
            return new TWTProfileListRow(value, node.getRowTemplate());
        }
        
    }
    
    public static class TWTProfileListXCellRenderer extends DefaultEntityListCellRenderer {

        @Override
        public EntityView getListCellRendererComponent(EntityListView list, Entity value, int index, boolean isSelected, boolean isFocused) {
            ListNode node = (ListNode)list.getViewNode();
                    
            return new TWTProfileListRowX(value, node.getRowTemplate());
        }
        
    }
}
