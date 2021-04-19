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
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.models.PropertySelector;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ActionNode.Category;
import com.codename1.rad.nodes.FieldNode;
import com.codename1.rad.nodes.ListNode;
import com.codename1.rad.nodes.Node;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.propertyviews.LabelPropertyView;
import com.codename1.rad.propertyviews.SpanLabelPropertyView;
import com.codename1.rad.schemas.Thing;
import com.codename1.rad.text.TimeAgoDateFormatter;
import com.codename1.rad.ui.AbstractEntityView;
import com.codename1.rad.ui.Actions;
import com.codename1.rad.ui.EntityListCellRenderer;
import com.codename1.rad.ui.EntityView;
import com.codename1.rad.ui.UI;
import com.codename1.rad.ui.entityviews.EntityListView;
import com.codename1.rad.ui.image.ImageContainer;
import com.codename1.rad.ui.image.RoundImageRenderer;
import com.codename1.rad.ui.menus.ActionSheet;
import com.codename1.twitterui.schemas.TWTNewsItemSchema;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.Sheet;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;

/**
 * A view for displaying a list of news items.  This is similar to {@link TweetListView}, except this 
 * more closely resembles the "News" section of the Twitter mobile app rather than a "tweet" list.  E.g.
 * it displays a headline, a news source, and an image associated with the headline.  
 * 
 * image::https://shannah.github.io/TweetAppUIKit/manual/images/Image-070520-064442.119.png[]
 * 
 * This is really just a vanilla {@link EntityListView} that uses the {@link TWTNewsListRowView} to render
 * each row.  The first row is rendered with a large splash image, and the rest are rendered using
 * a more compact layout.
 * 
 * === View Model
 * 
 * Any {@link EntityList} will do for the view model.  Each row should be an entity that conforms to
 * the {@link TWTNewsItemSchema} schema.  For more details on row view model requirements see {@link TWTNewsListRowView}.
 * 
 * === Actions
 * 
 * . {@link #NEWS_ITEM_CLICKED} - Fired when news item is clicked.
 * . {@link #NEWS_ITEM_LONGPRESS} - Fired when news item is longpressed.
 * . {@link #NEWS_ITEM_CLICKED_MENU} - Actions that will appear in action sheet when news item is clicked.
 * . {@link #NEWS_ITEM_LONGPRESS_MENU} - Actions that will appear in action sheet when news item is longpressed.
 * . {@link #CREATOR_CLICKED} - Fired when the "creator" label is clicked.
 * . {@link #CREATOR_LONGPRESS} - Fired when the "creator" label is longpressed.
 * . {@link #CREATOR_CLICKED_MENU} - Actions that will appear in action sheet when "creator" label is clicked.
 * . {@link #CREATOR_LONGPRESS_MENU} - Actions that will appear in action sheet when "creator" label is longpressed.
 * 
 * === Styles
 * 
 * See {@link TWTNewsListRowView} for styles in each row.
 * 
 * @author shannah
 */
public class TWTNewsList extends EntityListView {
    
    public static final Category NEWS_ITEM_CLICKED = new Category(),
            NEWS_ITEM_LONGPRESS = new Category(),
            NEWS_ITEM_CLICKED_MENU = new Category(),
            NEWS_ITEM_LONGPRESS_MENU = new Category(),
            CREATOR_CLICKED = new Category(),
            CREATOR_LONGPRESS = new Category(),
            CREATOR_CLICKED_MENU = new Category(),
            CREATOR_LONGPRESS_MENU = new Category();
    
    private ViewNode node;
    
    private static ListNode decorate(ListNode node) {
        node.setAttributesIfNotExists(UI.cellRenderer(new TWTNewsListCellRenderer()));
        return node;
    }
    
    public TWTNewsList(EntityList newsList, ListNode node) {
        super(newsList, decorate(node));
        
    }
    
    /**
     * The row view for a news list.  This can be used directly as an independent component, but
     * it is more commonly used to render rows in the {@link TWTNewsList} component.
     * 
     * === Actions
     *
     * . {@link #NEWS_ITEM_CLICKED} - Fired when news item is clicked.
     * . {@link #NEWS_ITEM_LONGPRESS} - Fired when news item is longpressed.
     * . {@link #NEWS_ITEM_CLICKED_MENU} - Actions that will appear in action
     * sheet when news item is clicked. 
     * . {@link #NEWS_ITEM_LONGPRESS_MENU} -
     * Actions that will appear in action sheet when news item is longpressed. 
     * . {@link #CREATOR_CLICKED} - Fired when the "creator" label is clicked. .
     * . {@link #CREATOR_LONGPRESS} - Fired when the "creator" label is
     * longpressed. 
     * . {@link #CREATOR_CLICKED_MENU} - Actions that will appear
     * in action sheet when "creator" label is clicked. 
     * . {@link #CREATOR_LONGPRESS_MENU} - Actions that will appear in action
     * sheet when "creator" label is longpressed.
     * 
     * === Styles
     * 
     * . `TWTNewsListRowCreator` - UIID for the Creator label
     * . `TWTNewsListRowCreatorFeature` - UIID for Creator label in "featured" row.
     * . `TWTNewsListRowCreatorIcon` - UIID for creator icon.
     * . `TWTNewsListRowCreatorIconFeature` - UIID for creator icon in featured row.
     * . `TWTNewsListRowThumbnail` - UIID for the news item thumbnail.
     * . `TWTNewsListRowThumbnailFeature` - UIID for the news item thumbnail in a "featured" row.
     * . `TWTNewsListRowView` - UIID for the component.
     * . `TWTNewsListRowViewFeature` - UIID for featured component.
     * . `TWTNewsListRowThumbnail` - UIID for news item's thumbnail image.
     * . `TWTNewsListRowThumbnailFeature` - UIID for news items thumbnail image in featured row.
     * . `TWTNewsListRowHeadline` - UIID for news item's headline SpanLabel
     * . `TWTNewsListRowHeadlineText` - UIID for news item's headline text.
     * . `TWTNewsListRowHeadlineFeature` - UIID for news item's headline SpanLabel in featured row.
     * . `TWTNewsListRowHeadlineTextFeature` - UIID for news item's headline text in featured row.
     * . `TWTNewsListRowDate` - UIID for the date of the news item.
     * . `TWTNewsListRowDateFeature` - UIID for the date of a news item in featured row.
     */
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
            creatorProp = new PropertySelector(entity, TWTNewsItemSchema.creator);
            dateProp = new PropertySelector(entity, TWTNewsItemSchema.date);
            headlineProp = new PropertySelector(entity, TWTNewsItemSchema.headline);
            imageProp = new PropertySelector(entity, TWTNewsItemSchema.image);
            thumbnailProp = new PropertySelector(entity, TWTNewsItemSchema.thumbnailUrl);
            featureItem = featureItem && !imageProp.isEmpty();
            String featureSuffix = featureItem ? "Feature" : "";
            setName("TWTNewsListRowView");
            setUIID("TWTNewsListRowView"+featureSuffix);
            
            
            setLayout(new LayeredLayout());
            Container contentPane = new Container(new BorderLayout());
            contentPane.getStyle().stripMarginAndPadding();
            
            
            
            
            // A view to display the creator
            
            creatorButton = new Button();
            creatorButton.setBlockLead(true);
            creatorButton.addActionListener(evt->{
                ActionNode clickedAction = node.getInheritedAction(CREATOR_CLICKED);
                if (clickedAction != null && clickedAction.isEnabled(entity)) {
                    ActionEvent e2 = clickedAction.fireEvent(entity, this);
                    if (e2.isConsumed()) {
                        evt.consume();
                        return;
                    }
                }
                Actions clickedActions = node.getInheritedActions(CREATOR_CLICKED_MENU).getEnabled(entity);
                if (!clickedActions.isEmpty()) {
                    evt.consume();
                    ActionSheet actionSheet = new ActionSheet(Sheet.findContainingSheet(this), getEntity(), clickedActions);
                    actionSheet.show();
                    return;
                }
            });
            creatorButton.addLongPressListener(evt->{
                ActionNode longpressAction = node.getInheritedAction(CREATOR_LONGPRESS);
                if (longpressAction != null && longpressAction.isEnabled(entity)) {
                    ActionEvent e2 = longpressAction.fireEvent(entity, this);
                    if (e2.isConsumed()) {
                        evt.consume();
                        return;
                    }
                }
                Actions longpressActions = node.getInheritedActions(CREATOR_LONGPRESS_MENU).getEnabled(entity);
                if (!longpressActions.isEmpty()) {
                    evt.consume();
                    ActionSheet actionSheet = new ActionSheet(Sheet.findContainingSheet(this), getEntity(), longpressActions);
                    actionSheet.show();
                    return;
                }
            });
            creatorButton.setUIID("TWTNewsListRowCreator"+featureSuffix);
            creatorButton.setIconUIID("TWTNewsListRowCreatorIcon"+featureSuffix);
            
            // Adding tags to align left.  See docs in CollapsibleHeaderContainer
            $(this).addTags("left-edge");
            $(creatorButton).addTags("left-inset");
            
            PropertySelector creatorSelector = new PropertySelector(getEntity(), TWTNewsItemSchema.creator);
            
            PropertySelector creatorNameSelector = creatorSelector.child(Thing.name);
            FieldNode creatorNameField =  new FieldNode(
                    UI.property(creatorNameSelector)
            );
            FieldNode creatorIconField = new FieldNode(
                    UI.property(new PropertySelector(getEntity(), TWTNewsItemSchema.creator).child(Thing.thumbnailUrl)),
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
            
            
            
            
            thumbnailLabel = new Button("", "TWTNewsListRowThumbnail"+featureSuffix);
            thumbnailView = LabelPropertyView.createRoundRectIconLabel(
                    thumbnailLabel, 
                    entity, 
                    thumbnailProp, 
                    thumbnailSize, 
                    thumbnailSize, 
                    1.5f
            );
            setLeadComponent((Button)thumbnailLabel);
            ((Button)thumbnailLabel).addActionListener(evt->{
                ActionNode clickedAction = node.getInheritedAction(NEWS_ITEM_CLICKED);
                if (clickedAction != null && clickedAction.isEnabled(entity)) {
                    ActionEvent e2 = clickedAction.fireEvent(entity, this);
                    if (e2.isConsumed()) {
                        evt.consume();
                        return;
                    }
                }
                Actions clickedActions = node.getInheritedActions(NEWS_ITEM_CLICKED_MENU).getEnabled(entity);
                if (!clickedActions.isEmpty()) {
                    evt.consume();
                    ActionSheet actionSheet = new ActionSheet(Sheet.findContainingSheet(this), getEntity(), clickedActions);
                    actionSheet.show();
                    return;
                }
            });
            ((Button)thumbnailLabel).addLongPressListener(evt->{
                ActionNode longpressAction = node.getInheritedAction(NEWS_ITEM_LONGPRESS);
                if (longpressAction != null && longpressAction.isEnabled(entity)) {
                    ActionEvent e2 = longpressAction.fireEvent(entity, this);
                    if (e2.isConsumed()) {
                        evt.consume();
                        return;
                    }
                }
                Actions longpressActions = node.getInheritedActions(NEWS_ITEM_LONGPRESS_MENU).getEnabled(entity);
                if (!longpressActions.isEmpty()) {
                    evt.consume();
                    ActionSheet actionSheet = new ActionSheet(Sheet.findContainingSheet(this), getEntity(), longpressActions);
                    actionSheet.show();
                    return;
                }
            });
            
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
