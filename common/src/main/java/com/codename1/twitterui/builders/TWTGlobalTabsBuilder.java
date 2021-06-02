package com.codename1.twitterui.builders;

import com.codename1.rad.annotations.Inject;
import com.codename1.rad.annotations.RAD;
import com.codename1.rad.models.Tag;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.ui.AbstractComponentBuilder;
import com.codename1.rad.ui.Actions;
import com.codename1.rad.ui.ViewContext;
import com.codename1.twitterui.views.TWTGlobalTabs;
import com.codename1.ui.Container;
import com.codename1.ui.InterFormContainer;

import java.util.Map;

@RAD(tag={"globalTabs"})
public class TWTGlobalTabsBuilder extends AbstractComponentBuilder<TWTGlobalTabsPlaceholder> {

    private ActionNode selectedTab;

    public TWTGlobalTabsBuilder(@Inject ViewContext context, String tagName, Map<String, String> attributes) {
        super(context, tagName, attributes);
    }

    public TWTGlobalTabsBuilder selectedTab(ActionNode selectedTab) {
        this.selectedTab = selectedTab;
        return this;
    }

    @Override
    public TWTGlobalTabsPlaceholder build() {

        TWTGlobalTabs globalTabs = getContext().getController().lookup(TWTGlobalTabs.class);
        if (globalTabs == null) {
            // No global tabs defined yet.  See if there are actions for it.
            Actions actions = getContext().getNode().getInheritedActions(TWTGlobalTabs.GLOBAL_TABS);
            if (!actions.isEmpty()) {
                globalTabs = new TWTGlobalTabs(getContext());
                getContext().getController().getApplicationController().addLookup(TWTGlobalTabs.class, globalTabs);
            }
        }


        globalTabs.setSelectedTab(selectedTab);
        if (globalTabs != null) {
            return new TWTGlobalTabsPlaceholder(globalTabs);
        }
        return new TWTGlobalTabsPlaceholder();
    }

    @Override
    public Object parseConstraint(String constraint) {
        return null;
    }
}
