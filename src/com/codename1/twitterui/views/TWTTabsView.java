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

import ca.weblite.shared.components.CollapsibleHeaderContainer;
import com.codename1.rad.attributes.UIID;
import com.codename1.rad.models.Entity;
import com.codename1.rad.nodes.Node;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.ui.UI;
import com.codename1.rad.ui.entityviews.TabsEntityView;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.plaf.Border;

/**
 *
 * @author shannah
 */
public class TWTTabsView extends TabsEntityView {
    
    private static ViewNode decorate(ViewNode node) {
        node.setAttributesIfNotExists(new UIID("TWTTabs"));
        for (Node child : node.getChildNodes()) {
            child.setAttributesIfNotExists(
                    new UIID("TWTTab")
            );
            
        }
        return node;
    }
    
    public TWTTabsView(Entity entity, ViewNode node) {
        super(entity, decorate(node));
    }

    @Override
    protected void initComponent() {
        super.initComponent();
        
        // Normally the title component has a bottom border.  But when the tabs view is showing,
        // we need the title component to appear contiguous to the tabs.
        /*
        $(this).parents(".CollapsibleHeaderContainer")
                .first()
                .find(".TWTTitleComponent")
                .first().each(c->{
            $(c).selectAllStyles()
                    .setBorder(Border.createEmpty())
                    .setBgColor(0xffffff)
                    .setBgTransparency(0xff);
            
        });
        */
        $(this).parents(".CollapsibleHeaderContainer").first().each(c->{
            ((CollapsibleHeaderContainer)c).setPartialCollapseUIID("TWTTitleComponentOverTabs");
            ((CollapsibleHeaderContainer)c).setCollapseMode(CollapsibleHeaderContainer.CollapseMode.PartialCollapse);
        });
    }
    
    
}
