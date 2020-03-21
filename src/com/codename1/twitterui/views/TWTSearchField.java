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
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.Property;
import com.codename1.rad.nodes.FieldNode;
import com.codename1.rad.nodes.Node;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.propertyviews.TextFieldPropertyView;
import com.codename1.rad.schemas.SearchAction;
import com.codename1.rad.ui.AbstractEntityView;
import com.codename1.rad.ui.UI;
import com.codename1.ui.Button;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;

/**
 *
 * @author shannah
 */
public class TWTSearchField extends AbstractEntityView {
    private ViewNode node;
    private TextField searchField = new TextField();
    private Button clearButton = new Button("", "TWTSearchFieldCancelButton");
    private Label icon = new Label("", "TWTSearchFieldIcon");
    private Property queryProp;
    private TextFieldPropertyView searchFieldBinding;
    
    public TWTSearchField(Entity entity, ViewNode node) {
        super(entity);
        this.node = node;
        setUIID("TWTSearchField");
        setName("TWTSearchField");
        queryProp = entity.findProperty(SearchAction.query);
        if (queryProp == null) {
            throw new IllegalArgumentException("ViewModel for TWTSearchField must contain a query property.");
        }
        FontImage.setIcon(icon, FontImage.MATERIAL_SEARCH, -1);
        FontImage.setIcon(clearButton, FontImage.MATERIAL_CANCEL, -1);
        searchField.setUIID("TWTSearchFieldText");
        setLayout(new BorderLayout());
        
        searchFieldBinding = new TextFieldPropertyView(searchField, entity, new FieldNode(UI.property(queryProp)));
        add(BorderLayout.WEST, icon).add(BorderLayout.CENTER, searchFieldBinding).add(BorderLayout.EAST, clearButton);

        
        
        clearButton.addActionListener(evt->{
            getEntity().setText(queryProp, "");
        });
        update();
    }
    
    @Override
    public void update() {
        String q = "";
        if (!getEntity().isEmpty(queryProp)) {
            q = getEntity().getText(queryProp);
        }
        
        if (q.length() == 0 && clearButton.isVisible()) {
            clearButton.setVisible(false);
        } else if (q.length() > 0 && !clearButton.isVisible()) {
            clearButton.setVisible(true);
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
