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

import com.codename1.compat.java.util.Objects;
import com.codename1.rad.attributes.UIID;
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
import com.codename1.rad.ui.entityviews.EntityListView;
import com.codename1.twitterui.schemas.TWTKeyword;
import com.codename1.twitterui.text.TweetDateFormatter;
import com.codename1.ui.Button;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.GridLayout;

/**
 *
 * @author shannah
 */
public class TWTKeywordList extends EntityListView {
    
    private static ListNode decorate(ListNode node) {
        if (node == null) {
            node = new ListNode();
        }

        ActionNode dummyAction = new ActionNode();
        dummyAction.setParent(node);
        
        node.setAttributes(UI.cellRenderer(new TWTKeywordListCellRenderer()));
        return node;
    }
    
    public TWTKeywordList(EntityList list) {
        this(list, null);
    }
    
    public TWTKeywordList(EntityList list, ListNode node) {
        super(list, decorate(node));
        setUIID("TWTKeywordList");
        setName("TWTKeywordList");
    }
    
    public static class TWTKeywordListRow extends AbstractEntityView {
        public static final Category KEYWORD_ACTIONS = new Category();
        
        private ViewNode node;
        private Label label = new Label("", "TWTKeywordListRowLabel");
        private Property keywordProp;
        
        
        public TWTKeywordListRow(Entity entity, ViewNode node) {
            super(entity);
            this.node = node;
            this.keywordProp = entity.findProperty(TWTKeyword.keyword);
            initUI();
            
        }
        
        private void initUI() {
            setUIID("TWTKeywordListRow");
            setName("TWTKeywordListRow");
            setLayout(new BorderLayout());
            add(BorderLayout.CENTER, label);
            Actions actions = getViewNode().getInheritedActions(KEYWORD_ACTIONS).getEnabled(getEntity())
                    .setAttributesIfNotSet(new UIID("TWTKeywordAction"));
            
            if (!actions.isEmpty()) {
                Container cnt = new Container(new GridLayout(actions.size()));
                cnt.setUIID("TWTKeywordActionsWrapper");
                actions.addToContainer(cnt, getEntity());
                $("*", cnt)
                    .filter(c->{return c instanceof Button;})
                    .first().each(c->{
                        TWTKeywordListRow.this.setLeadComponent(c);
                    });
                add(BorderLayout.EAST, cnt);
            }
            
            update();  
            
        }
        
        @Override
        public void update() {
            boolean changed = false;
            String keyword = "";
            if (!getEntity().isEmpty(keywordProp)) {
                keyword = getEntity().getText(keywordProp);
            }
            if (!Objects.equals(keyword, label.getText())) {
                label.setText(keyword);
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
    
    public static class TWTKeywordListCellRenderer extends DefaultEntityListCellRenderer {

        @Override
        public EntityView getListCellRendererComponent(EntityListView list, Entity value, int index, boolean isSelected, boolean isFocused) {
            ListNode node = (ListNode)list.getViewNode();
                    
            return new TWTKeywordListRow(value, node.getRowTemplate());
        }
        
    }
}
