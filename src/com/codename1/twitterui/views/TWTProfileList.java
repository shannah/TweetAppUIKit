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
 *
 * @author shannah
 */
public class TWTProfileList extends EntityListView {
    
    public static enum ListStyle {
        Rows,
        Badges
    }
    
    public static class ListStyleAttribute extends Attribute<ListStyle> {
        public ListStyleAttribute(ListStyle value) {
            super(value);
        }
    }
    
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
    
    public TWTProfileList(EntityList list) {
        this(list, null);
    }
    
    public TWTProfileList(EntityList list, ListNode node) {
        super(list, decorate(node));
        setUIID("TWTProfileList");
        setName("TWTProfileList");
        if (node.findAttribute(ListStyleAttribute.class) != null && node.findAttribute(ListStyleAttribute.class).getValue() == ListStyle.Badges) {
            setSafeArea(true);
        }
    }
    
    public static class TWTProfileListRowX extends AbstractEntityView {
         
        public static final Category PROFILE_CLICKED = new Category(),
                PROFILE_LONGPRESS = new Category(),
                PROFILE_CLICKED_MENU = new Category(),
                PROFILE_LONGPRESS_MENU = new Category();
        
        
        
        private ViewNode node;
        private Label nameLabel = new Label("", "TWTProfileListRowXName");
        private Label idLabel = new Label("", "TWTProfileListRowXID");
        private Button icon = new Button("", "TWTProfileListRowXIcon");
        private Property nameProp, iconProp, idProp;
        
        
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
