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

import com.codename1.rad.attributes.PropertySelectorAttribute;
import com.codename1.rad.attributes.WidgetType;
import com.codename1.rad.controllers.FieldEditorFormController;
import com.codename1.rad.controllers.ViewController;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.PropertySelector;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.FieldNode;
import com.codename1.rad.nodes.Node;
import com.codename1.rad.nodes.SectionNode;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.propertyviews.LabelPropertyView;
import com.codename1.rad.ui.AbstractEntityView;
import com.codename1.rad.ui.ActionViewFactory;
import com.codename1.rad.ui.NodeList;
import com.codename1.rad.ui.UI;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;

/**
 *
 * @author shannah
 */
public class TWTSettingsForm extends AbstractEntityView {
    private ViewNode node;
    public TWTSettingsForm(Entity entity, ViewNode node) {
        super(entity);
        this.node = node;
        node.setAttributesIfNotExists(UI.viewFactory(new SettingsFormActionViewFactory()));
        setUIID("TWTSettingsForm");
        $(this).addTags("TWTSettingsForm");
        setLayout(BoxLayout.y());
        NodeList l = node.getChildNodes(SectionNode.class);
        for (Node n : l) {
            SectionNode sn = (SectionNode)n;
            add(new SettingsFormSection(entity, sn));
        }
        
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
    
    public static class SettingsFormSection extends Container {
        public SettingsFormSection(Entity entity, SectionNode section) {
            super(BoxLayout.y());
            setUIID("TWTSettingsFormSection");
            $(this).addTags("TWTSettingsFormSection");
            com.codename1.rad.models.Property.Label l = section.getLabel();
            if (l != null) {
                add(new Label(l.getValue(entity), "TWTSettingsFormSectionLabel"));
            }
            for (Node n : section.getChildNodes(ActionNode.class)) {
                ActionNode an = (ActionNode)n;
                if (!an.isEnabled(entity)) {
                    continue;
                }
                add(an.createView(entity));
            }
        }
    }
    
    public static class SettingsFormActionView extends Container {
        
        public SettingsFormActionView(Entity entity, ActionNode action) {
            super(new BorderLayout());
            Container cnt = this;
            cnt.setUIID("TWTSettingsFormAction");
            $(cnt).addTags("TWTSettingsFormAction");
            String labelText = action.getLabelText(entity);
            
            FieldNode fieldNode = (FieldNode)action.findAttribute(FieldNode.class);
            if (fieldNode == null) {
                fieldNode = new FieldNode();
                fieldNode.setParent(action);
                
            }
            PropertySelector psel = fieldNode.getPropertySelector(entity);
            String value = "";
            Component valueView = null;
            if (psel != null ) {
                value = psel.getText("");
                Label valueLabel = new Label(value, "TWTSettingsFormActionValue");
                valueView = new LabelPropertyView(valueLabel, entity, fieldNode);
            } else {
                PropertySelectorAttribute pselAtt = (PropertySelectorAttribute)fieldNode.findInheritedAttribute(PropertySelectorAttribute.class);
                if (pselAtt != null) {
                    valueView = new Label(pselAtt.getValue(entity).getText(""), "TWTSettingsFormActionValue");
                }
            }

            Button button = new Button("", "TWTSettingsFormActionArrowButton");
            FontImage.setIcon(button, FontImage.MATERIAL_ARROW_FORWARD_IOS, -1);

            cnt.add(BorderLayout.CENTER, new Label(labelText, "TWTSettingsFormActionLabel"));
            
            WidgetType wtype = fieldNode.getWidgetType(entity.getEntityType());
            
            
            
            cnt.add(BorderLayout.EAST, BorderLayout.centerEastWest(valueView, button, null).stripMarginAndPadding());
            cnt.setLeadComponent(button);
            FieldNode fFieldNode = fieldNode;
            button.addActionListener(evt->{
                evt.consume();
                ActionEvent ae = action.fireEvent(entity, button);
                if (ae.isConsumed()) {
                    return;
                }
                
                if (wtype != null) {
                    FieldEditorFormController ctl = new FieldEditorFormController(
                            ViewController.getViewController(button), 
                            entity, 
                            fFieldNode
                    );
                    ctl.getView().show();
                }
            });
        }
    }
    
    
    
    public static class SettingsFormActionViewFactory implements ActionViewFactory {

        @Override
        public Component createActionView(Entity entity, ActionNode action) {
            return new SettingsFormActionView(entity, action);
        }
        
    }
    
}
