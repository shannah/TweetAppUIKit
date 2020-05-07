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
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ActionNode.Category;
import com.codename1.rad.nodes.Node;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.ui.AbstractEntityView;
import com.codename1.rad.ui.EntityView;
import static com.codename1.rad.ui.UI.icon;
import static com.codename1.rad.ui.UI.label;
import com.codename1.ui.Button;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.FontImage;
import com.codename1.ui.layouts.FlowLayout;

/**
 * This component is a button that is made to look like a search field.  The idea is that when the user presses the button,
 * it will transition to a new form which has an actual search field.  The look of this component is meant match the {@link TWTSearchField}
 * component.
 * 
 * image::https://shannah.github.com/TweetAppUIKit/manual/images/TWTSearchButton.png[]
 * 
 * === View Model
 * 
 * This component doesn't have any special view model requirements.  It doesn't use any dynamic properties.
 * 
 * === Actions
 * 
 * {@link SEARCH_ACTION} - Action that is fired when the user clicks on the button.
 * 
 * === Styles
 * 
 * `TWTSearchButton` - The UIID of the component.
 * `TWTSearchButtonAction` - The UIID of the label inside the button.
 * 
 * @author shannah
 */
public class TWTSearchButton extends  AbstractEntityView {
    
    /**
     * Action that is fired when the user clicks on the button.
     */
    public static final Category SEARCH_ACTION = new Category();
    
    private ViewNode node;
    private Button inner;
    
    public TWTSearchButton(Entity entity, ViewNode node) {
        super(entity);
        this.node = node;
        initUI();
    }
    
    private void initUI() {
        setName("TWTSearchButton");
        setUIID("TWTSearchButton");
        $(this).addTags("center-fill");
        
        FlowLayout fl = new FlowLayout(CENTER);
        fl.setValign(CENTER);
        setLayout(fl);
        ActionNode action = getViewNode().getAction(SEARCH_ACTION);
        if (action == null) {
            action = new ActionNode(
                    label("Search"),
                    icon(FontImage.MATERIAL_SEARCH)
            );
        }
        action.setAttributesIfNotExists(
                new UIID("TWTSearchButtonAction"),
                icon(FontImage.MATERIAL_SEARCH),
                label("Search")
        );
        inner = (Button)action.createView(getEntity());
        setLeadComponent(inner);
        add(inner);
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
