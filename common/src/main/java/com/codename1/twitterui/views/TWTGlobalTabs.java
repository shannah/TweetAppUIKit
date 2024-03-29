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

import com.codename1.rad.annotations.Inject;
import com.codename1.rad.attributes.UIID;
import com.codename1.rad.controllers.FormController;
import com.codename1.rad.models.Entity;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ActionNode.Category;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.ui.AbstractEntityView;
import com.codename1.rad.ui.Actions;
import com.codename1.rad.ui.UI;
import com.codename1.rad.ui.ViewContext;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.GridLayout;

/**
 *
 * @author shannah
 */
public class TWTGlobalTabs extends AbstractEntityView {
    public static final Category GLOBAL_TABS = new Category();
    private ActionNode selectedTab;

    
    public TWTGlobalTabs(@Inject ViewContext context) {
        super(context);
        initUI();

    }

    public void setSelectedTab(ActionNode selectedTab) {
        if (selectedTab != this.selectedTab) {
            this.selectedTab = selectedTab;
            getContext().getEntity().setChanged(null, false);
            getContext().getEntity().getEntity().notifyObservers();
        }

    }

    /**
     * 
     * @param entity
     * @param node
     * @deprecated Use {@link #TWTGlobalTabs(ViewContext)}
     */
    public TWTGlobalTabs(Entity entity, ViewNode node) {
        super(entity, node);
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
        for (ActionNode action : actions) {
            action.setAttributes(UI.selectedCondition(e-> action.isSameNode(selectedTab)));
        }
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

    private int getActionIndex(ActionNode actionNode) {
        Actions actions = getViewNode().getInheritedActions(GLOBAL_TABS);
        int index = -1;
        for (ActionNode a : actions) {
            index++;
            if (a.isSameNode(actionNode)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Shows the tab that triggered the given action event.
     * @param evt
     * @param targetController The target controller to show.  The show() or showBack() method will be triggered depending
     *                         on the relative location of the buttons on the global tabs.
     */
    public static void showTab(ActionEvent evt, FormController targetController) {
        if (!(evt instanceof ActionNode.ActionNodeEvent)){
            targetController.show();
            return;
        }
        ActionNode.ActionNodeEvent nodeEvent = (ActionNode.ActionNodeEvent) evt;

        TWTGlobalTabs globalTabs = nodeEvent.getViewController().lookup(TWTGlobalTabs.class);
        if (globalTabs == null) {
            targetController.show();
            return;
        }

        int targetIndex = globalTabs.getActionIndex(nodeEvent.getAction());
        int sourceIndex = globalTabs.getActionIndex(globalTabs.selectedTab);
        if (targetIndex < 0 || sourceIndex < 0) {
            targetController.show();
            return;
        }

        if (targetIndex < sourceIndex) {
            targetController.showBack();
        } else if (targetIndex > sourceIndex) {
            targetController.show();
        } else {
            // If the indexes are the same, then we don't do anything.
        }



    }

    
}
