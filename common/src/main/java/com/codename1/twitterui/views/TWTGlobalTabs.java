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

import com.codename1.rad.attributes.UIID;
import com.codename1.rad.models.Entity;
import com.codename1.rad.nodes.ActionNode.Category;
import com.codename1.rad.nodes.Node;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.ui.AbstractEntityView;
import com.codename1.rad.ui.Actions;
import com.codename1.rad.ui.UI;
import com.codename1.ui.layouts.GridLayout;

/**
 *
 * @author shannah
 */
public class TWTGlobalTabs extends AbstractEntityView {
    public static final Category GLOBAL_TABS = new Category();
    private ViewNode node;
    
    
    public TWTGlobalTabs(Entity entity, ViewNode node) {
        super(entity);
        this.node = node;
        initUI();
    }
    
    private void initUI() {
        setName("TWTGlobalTabs");
        setUIID("TWTGlobalTabs");
        Actions actions = getViewNode().getInheritedActions(GLOBAL_TABS);
        actions.setAttributesIfNotSet(
                new UIID("TWTGlobalTabsAction"),
                UI.badgeUiid("TWTGlobalTabsActionBadge")
        );
        if (!actions.isEmpty()) {
            setLayout(new GridLayout(actions.size()));
        }
        actions.addToContainer(this, getEntity());
    }
    
    @Override
    public void update() {
        
    }

    @Override
    public void commit() {
        
    }

    @Override
    public Node getViewNode() {
        return node;
    }
    
}
