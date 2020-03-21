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

import com.codename1.charts.util.ColorUtil;
import com.codename1.components.SpanLabel;
import com.codename1.rad.models.Attribute;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.models.PropertySelector;
import com.codename1.rad.nodes.FieldNode;
import com.codename1.rad.nodes.ListNode;
import com.codename1.rad.nodes.Node;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.propertyviews.LabelPropertyView;
import com.codename1.rad.propertyviews.SpanLabelPropertyView;
import com.codename1.rad.schemas.Thing;
import com.codename1.rad.text.TimeAgoDateFormatter;
import com.codename1.rad.ui.AbstractEntityView;
import com.codename1.rad.ui.EntityListCellRenderer;
import com.codename1.rad.ui.EntityView;
import com.codename1.rad.ui.UI;
import com.codename1.rad.ui.entityviews.EntityListView;
import com.codename1.rad.ui.image.ImageContainer;
import com.codename1.rad.ui.image.RoundImageRenderer;
import com.codename1.twitterui.schemas.TWTNewsItem;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;

/**
 *
 * @author shannah
 */
public class TWTNewsList extends EntityListView {
    
    
    private ViewNode node;
    
    private static ListNode decorate(ListNode node) {
        node.setAttributesIfNotExists(UI.cellRenderer(new TWTNewsListCellRenderer()));
        return node;
    }
    
    public TWTNewsList(EntityList newsList, ListNode node) {
        super(newsList, decorate(node));
        
    }
    
    public static class TWTNewsListRowView extends AbstractEntityView {
        private ViewNode node;
        private final Button creatorButton;
        private final Label dateLabel;
        private final SpanLabel headlineLabel;
        private final Label thumbnailLabel;
        
        private final LabelPropertyView creatorView, thumbnailView, dateView;
        private final SpanLabelPropertyView headlineView;
        
        private final int creatorIconSize = CN.convertToPixels(2);
        private final int thumbnailSize = CN.convertToPixels(10);
        
        private PropertySelector creatorProp, dateProp, headlineProp, imageProp, thumbnailProp;
        
        public TWTNewsListRowView(Entity entity, ViewNode node, boolean featureItem) {
            super(entity);
            
            this.node = node;
            creatorProp = new PropertySelector(entity, TWTNewsItem.creator);
            dateProp = new PropertySelector(entity, TWTNewsItem.date);
            headlineProp = new PropertySelector(entity, TWTNewsItem.headline);
            imageProp = new PropertySelector(entity, TWTNewsItem.image);
            thumbnailProp = new PropertySelector(entity, TWTNewsItem.thumbnailUrl);
            featureItem = featureItem && !imageProp.isEmpty();
            String featureSuffix = featureItem ? "Feature" : "";
            setName("TWTNewsListRowView");
            setUIID("TWTNewsListRowView"+featureSuffix);
            
            
            setLayout(new LayeredLayout());
            Container contentPane = new Container(new BorderLayout());
            contentPane.getStyle().stripMarginAndPadding();
            
            
            
            
            // A view to display the creator
            
            creatorButton = new Button();
            creatorButton.setUIID("TWTNewsListRowCreator"+featureSuffix);
            creatorButton.setIconUIID("TWTNewsListRowCreatorIcon"+featureSuffix);
            
            // Adding tags to align left.  See docs in CollapsibleHeaderContainer
            $(this).addTags("left-edge");
            $(creatorButton).addTags("left-inset");
            
            PropertySelector creatorSelector = new PropertySelector(getEntity(), TWTNewsItem.creator);
            
            PropertySelector creatorNameSelector = creatorSelector.child(Thing.name);
            FieldNode creatorNameField =  new FieldNode(
                    UI.property(creatorNameSelector)
            );
            FieldNode creatorIconField = new FieldNode(
                    UI.property(new PropertySelector(getEntity(), TWTNewsItem.creator).child(Thing.thumbnailUrl)),
                    UI.iconRenderer(new RoundImageRenderer(creatorIconSize))
            );
            creatorNameField.setParent(node);
            creatorIconField.setParent(node);
            creatorButton.setGap(CN.convertToPixels(1f));
            creatorView = new LabelPropertyView(
                    creatorButton,
                    getEntity(),
                    creatorNameField,
                    creatorIconField
            );
            
            
            
            
            thumbnailLabel = new Label("", "TWTNewsListRowThumbnail"+featureSuffix);
            thumbnailView = LabelPropertyView.createRoundRectIconLabel(
                    thumbnailLabel, 
                    entity, 
                    thumbnailProp, 
                    thumbnailSize, 
                    thumbnailSize, 
                    1.5f
            );
            
            headlineLabel = new SpanLabel();
            headlineLabel.setUIID("TWTNewsListRowHeadline"+featureSuffix);
            headlineLabel.setTextUIID("TWTNewsListRowHeadlineText"+featureSuffix);
            headlineView = new SpanLabelPropertyView(headlineLabel, entity, headlineProp);
            // Aligning left.  See docs in CollapsibleHeaderContainer
            $(headlineLabel).addTags("left-inset");
            
            dateLabel = new Label();
            dateLabel.setUIID("TWTNewsListRowDate"+featureSuffix);
            FieldNode dateField = new FieldNode(
                    UI.property(dateProp),
                    UI.dateFormat(new TimeAgoDateFormatter())
            );
            dateField.setParent(node);
            dateView = new LabelPropertyView(dateLabel, entity, dateField);
            if (!featureItem) {
                contentPane.add(BorderLayout.EAST, thumbnailView);
            }
            
            Container center = new Container(BoxLayout.y());
            center.add(BoxLayout.encloseX(creatorView, dateView));
            center.add(headlineView);
            
            
            contentPane.add(BorderLayout.CENTER, center);
            
            if (featureItem) {
                ImageContainer bgImg = ImageContainer.createToFileSystem(imageProp);
                add(bgImg);
                
                        
            }
            add(contentPane);
            if (featureItem) {
                contentPane.getStyle().setBackgroundType(Style.BACKGROUND_GRADIENT_LINEAR_VERTICAL);
                contentPane.getStyle().setBackgroundGradientStartColor(ColorUtil.argb(1,0,0,0));
                contentPane.getStyle().setBackgroundGradientEndColor(ColorUtil.argb(255, 0, 0, 0));
                
                LayeredLayout ll = (LayeredLayout)getLayout();
                ll.setInsets(contentPane, "auto 0 0 0");
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
        
    }
    
    public static class TWTNewsListCellRenderer implements EntityListCellRenderer {

        @Override
        public EntityView getListCellRendererComponent(EntityListView list, Entity value, int index, boolean isSelected, boolean isFocused) {
             ListNode node = (ListNode)list.getViewNode();
            return new TWTNewsListRowView(value, node.getRowTemplate(), index==0);
        }
        
    }
    
}
