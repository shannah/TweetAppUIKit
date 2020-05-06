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

import com.codename1.components.SpanLabel;
import com.codename1.rad.attributes.PropertyImageRendererAttribute;
import com.codename1.rad.attributes.UIID;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.PropertySelector;
import com.codename1.rad.nodes.ActionNode.Category;
import com.codename1.rad.nodes.FieldNode;
import com.codename1.rad.nodes.Node;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.propertyviews.ImageContainerPropertyView;
import com.codename1.rad.propertyviews.LabelPropertyView;
import com.codename1.rad.propertyviews.SpanLabelPropertyView;
import com.codename1.rad.schemas.Comment;
import com.codename1.rad.schemas.Thing;
import com.codename1.rad.text.LocalDateTimeFormatter;
import com.codename1.rad.ui.AbstractEntityView;
import com.codename1.rad.ui.Actions;
import com.codename1.rad.ui.UI;
import com.codename1.rad.ui.image.ImageContainer;
import com.codename1.rad.ui.image.RoundImageRenderer;
import com.codename1.rad.ui.menus.ActionSheet;
import com.codename1.ui.Button;
import com.codename1.twitterui.schemas.Tweet;
import com.codename1.ui.CN;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Label;
import com.codename1.ui.Sheet;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;

/**
 * A view for showing a "tweet" details.  This is the logical view that would be shown
 * when a user clicks on a "tweet" in the {@link TweetListView} to see an expanded version of
 * the tweet.
 * 
 * .Screenshot of sample tweet rendered using the TweetDetailView.
 * image::https://shannah.github.io/TweetAppUIKit/manual/images/Image-050520-092454.768.png[]
 * 
 * === View Model
 * 
 * Will accept any entity conforming to the {@link Tweet} schema.
 * 
 * === Actions
 * 
 * The there are 3 primary action categories for injecting content into this view: {@link #TWEET_ACTIONS}, {@link #STATS_ACTIONS}, and {@link #OVERFLOW_ACTIONS}.
 * 
 * The {@link #TWEET_ACTIONS} are the actions rendered just below the tweet.  These are usually things like "Like", "Retweet", or "Reply", but you can place any
 * actions you like here.
 * 
 * The {@link #STATS_ACTIONS} are actions rendered (also) below the tweet that are meant to contain information about the tweet such as the number of
 * likes or rewteets.  Example action definitions to take advantage of this:
 * 
 * [source,java]
 * ----
 * public static final ActionNode retweets = action(
            icon("Retweets"),
            label("245"),
            actionStyle(ActionStyle.IconRight)
    ), likes = action(
            icon("Likes"),
            label("1444"),
            actionStyle(ActionStyle.IconRight)
    );
    ...
    
    ViewNode vn = new ViewNode(
            actions(TweetDetailView.TWEET_ACTIONS, TwitterUIDemo.reply, TwitterUIDemo.retweet, TwitterUIDemo.favorite, TwitterUIDemo.share),
            actions(TweetDetailView.STATS_ACTIONS, retweets, likes),
            actions(TweetDetailView.OVERFLOW_ACTIONS, TwitterUIDemo.mute)
    ); 
 * ---- 
 * 
 * 
 * === Styles
 * 
 * The following UIIDs are used for styling this component, and can be overridden in your own stylesheets.
 * 
 * . `TweetDetailView` - The UIID for the detail view container.
 * . `TweetDetailViewAuthorName` - UIID used for author's full name.
 * . `TweetDetailViewAuthorID` - UIID for author's ID (e.g. user ID).
 * . `TweetDetailViewAuthorIcon` - UIID for the author's Icon/thumbnail.
 * . `TweetDetailViewContent` - UIID for the SpanLabel containing the tweet content.
 * . `TweetDetailViewContentText` - UIID for the text in the SpanLabel containing the tweet content.
 * . `TweetDetailViewImage` - UIID for the image in the tweet, if the tweet contains an image.
 * . `TweetDetailsViewDatePosted` - UIID for the date posted label.
 * . `TweetDetailViewOverflowMenuButton` - UIID for the overflow menu button.
 * . `TweetDetailViewStatsAction` - UIID for the label of a {@link #STATS_ACTIONS} action button.
 * . `TweetDetailViewStatsActionIcon` - UIID for the "icon" of a {@link #STATS_ACTIONS} action button.  The "label" is actually used to render text containing the amount of the stats.
 * . `TweetDetailViewStatsActions` - UIID for the container containing all of the {@link #STATS_ACTIONS} action buttons.
 * . `TweetDetailViewTweetAction` - UIID for the {@link #TWEET_ACTIONS} action buttons.
 * . `TweetDetailViewTweetActions` - UIID for the container containing the {@link #TWEET_ACTIONS} buttons.
 * 
 * === Full Example
 * 
 * [source,java]
 * ----

package com.codename1.demos.twitterui;

import ca.weblite.shared.components.CollapsibleHeaderContainer;
import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.ControllerEvent;
import com.codename1.rad.models.Entity;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.ui.ActionStyle;
import com.codename1.rad.ui.UI;
import com.codename1.twitterui.controllers.TWTFormController;
import com.codename1.twitterui.views.TWTTitleComponent;
import com.codename1.twitterui.views.TweetDetailView;
import com.codename1.ui.Label;
import static com.codename1.rad.ui.UI.*;


public class TweetDetailsController extends BaseFormController {
    public static final ActionNode retweets = action(
            icon("Retweets"),
            label("245"),
            actionStyle(ActionStyle.IconRight)
    ), likes = action(
            icon("Likes"),
            label("1444"),
            actionStyle(ActionStyle.IconRight)
    );
    
    public TweetDetailsController(Controller parent, Entity tweet) {
        super(parent);
        
        TweetDetailView view = new TweetDetailView(tweet, getViewNode());
        CollapsibleHeaderContainer wrapper = new CollapsibleHeaderContainer(
                new TWTTitleComponent(tweet, getViewNode(), new Label("Tweet")), 
                view, 
        view);
        setView(wrapper);
        
    }

    @Override
    protected ViewNode createViewNode() {
        ViewNode vn = new ViewNode(
                actions(TweetDetailView.TWEET_ACTIONS, TwitterUIDemo.reply, TwitterUIDemo.retweet, TwitterUIDemo.favorite, TwitterUIDemo.share),
                actions(TweetDetailView.STATS_ACTIONS, retweets, likes),
                actions(TweetDetailView.OVERFLOW_ACTIONS, TwitterUIDemo.mute)
        );
        
        return vn;
    }
    
    

    @Override
    public void actionPerformed(ControllerEvent evt) {
        
        super.actionPerformed(evt); 
    }
    
    
   
    
}

 * ----
 * 
 * @author shannah
 */
public class TweetDetailView extends AbstractEntityView {
    
    /**
     * Actions that are meant to display a statistic about the tweet.  These are rendered
     * just below the tweet content.  E.g. "245 Retweets", or "1444 Likes".
     */
    public static final Category STATS_ACTIONS = new Category(),
            TWEET_ACTIONS = new Category(),
            OVERFLOW_ACTIONS = new Category();
    
    private ViewNode node;
    private LabelPropertyView authorIcon, authorName, authorId, datePosted;
    private SpanLabelPropertyView content;
    private ImageContainerPropertyView image;
    private static final int avatarSize = CN.convertToPixels(8.5f);
    
    
    /**
     * Creates a new detail view.
     * @param entity The view model.  Any entity conforming to the {@link Tweet} schema.
     * @param node The view node.  
     */
    public TweetDetailView(Entity entity, ViewNode node) {
        super(entity);
        setUIID("TweetDetailView");
        setName("TweetDetailView");
        $(this).addTags("TweetDetailView");
        this.node = node;
        Button authorNameBtn = new Button("", "TweetDetailViewAuthorName");
        authorName = new LabelPropertyView(authorNameBtn, getEntity(), new FieldNode(
                UI.property(new PropertySelector(entity, Comment.author).child(Thing.name))
        ));
        Button authorIdBtn = new Button("", "TweetDetailViewAuthorID");
        authorId = new LabelPropertyView(authorIdBtn, getEntity(), new FieldNode(
                UI.property(new PropertySelector(entity, Comment.author).child(Thing.identifier))
        ));
        //Button authorIconBtn = new Button("", "TweetDetailViewAuthorIcon");
        PropertySelector authorIconSelector = entity.findProperty(Tweet.authorIcon) != null ?
                new PropertySelector(entity, Tweet.authorIcon) :
                new PropertySelector(entity, Comment.author).child(Thing.thumbnailUrl);
        
        authorIcon = LabelPropertyView.createIconLabel(
                entity, 
                new FieldNode(
                        UI.property(authorIconSelector),
                        new PropertyImageRendererAttribute(new RoundImageRenderer(avatarSize))
        ));
        authorIcon.getComponent().setUIID("TweetDetailViewAuthorIcon");
        SpanLabel contentLbl = new SpanLabel();
        contentLbl.setUIID("TweetDetailViewContent");
        contentLbl.setTextUIID("TweetDetailViewContentText");
        content = new SpanLabelPropertyView(contentLbl, entity, new FieldNode(
                UI.property(new PropertySelector(entity, Tweet.text))
        ));
        PropertySelector imagePropertySelector = new PropertySelector(entity, Comment.image);
        ImageContainer imCnt = ImageContainer.createToFileSystem(imagePropertySelector);
        imCnt.setUIID("TweetDetailViewImage");
        image = new ImageContainerPropertyView(imCnt, entity, new FieldNode(
                UI.property(imagePropertySelector)
        ));
        
        Label datePostedLbl = new Label("", "TweetDetailsViewDatePosted");
        datePosted = new LabelPropertyView(datePostedLbl, entity, new FieldNode(
                UI.dateFormat(new LocalDateTimeFormatter()),
                UI.property(new PropertySelector(entity, Tweet.datePosted))
        ));
        
        setLayout(new BorderLayout());
        Container north = new Container(new BorderLayout());
        north.add(BorderLayout.WEST, authorIcon);
        Container northC = new Container(BoxLayout.y());
        northC.getStyle().stripMarginAndPadding();
        Container northCWrap = new Container(new FlowLayout(LEFT, CENTER));
        northC.getStyle().stripMarginAndPadding();
        northC.addAll(authorName, authorId);
        northCWrap.add(northC);
        north.add(BorderLayout.CENTER, northCWrap);
        
        Button overflowmenuBtn = new Button("", "TweetDetailViewOverflowMenuButton");
        overflowmenuBtn.setVerticalAlignment(TOP);
        FontImage.setIcon(overflowmenuBtn, FontImage.MATERIAL_KEYBOARD_ARROW_DOWN, -1);
        north.add(BorderLayout.EAST, BoxLayout.encloseY(overflowmenuBtn).stripMarginAndPadding());
        
        add(BorderLayout.NORTH, north);
        
        Container center = new Container(BoxLayout.y());
        center.add(content);
        center.add(image);
        center.add(datePosted);
        
        
        Actions actions = node.getInheritedActions(STATS_ACTIONS).getEnabled(entity).setAttributesIfNotSet(
                new UIID("TweetDetailViewStatsAction"),
                UI.iconUiid("TweetDetailViewStatsActionIcon")
        );
        if (!actions.isEmpty()) {
            Container actionsCnt = new Container(new FlowLayout());
            actionsCnt.setUIID("TweetDetailViewStatsActions");
            actions.addToContainer(actionsCnt, entity);
            center.add(actionsCnt);
        }
        
        actions = node.getInheritedActions(TWEET_ACTIONS).getEnabled(entity).setAttributesIfNotSet(new UIID("TweetDetailViewTweetAction"));
        if (!actions.isEmpty()) {
            Container actionsCnt = new Container(new GridLayout(actions.size()));
            actionsCnt.setUIID("TweetDetailViewTweetActions");
            actions.addToContainer(actionsCnt, entity);
            center.add(actionsCnt);
        }
        
        actions = node.getInheritedActions(OVERFLOW_ACTIONS).getEnabled(entity);
        if (!actions.isEmpty()) {
            overflowmenuBtn.addActionListener(evt->{
                evt.consume();
                Actions actions2 = node.getInheritedActions(OVERFLOW_ACTIONS).getEnabled(entity);
                ActionSheet sheet = new ActionSheet(Sheet.findContainingSheet(this), entity, actions2);
                sheet.show();
            });
            
        } else {
            overflowmenuBtn.setVisible(false);
            overflowmenuBtn.setHidden(true);
        }
        
        add(BorderLayout.CENTER, center);
        
        
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
