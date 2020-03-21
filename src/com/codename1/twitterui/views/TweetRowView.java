/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.twitterui.views;

import ca.weblite.shared.components.ComponentImage;
import com.codename1.compat.java.util.Objects;
import com.codename1.components.SpanLabel;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityType;
import com.codename1.rad.models.Property;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ActionNode.ActionNodeEvent;
import com.codename1.rad.nodes.ActionNode.Category;
import com.codename1.rad.nodes.ListNode;
import com.codename1.rad.nodes.Node;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.schemas.Thing;
import com.codename1.rad.text.DateFormatter;
import com.codename1.rad.ui.AbstractEntityView;
import com.codename1.rad.ui.Actions;
import com.codename1.rad.ui.EntityListCellRenderer;
import com.codename1.rad.ui.EntityView;
import com.codename1.rad.ui.UI;
import com.codename1.rad.ui.entityviews.EntityListView;
import com.codename1.rad.ui.entityviews.ProfileAvatarView;
import com.codename1.rad.ui.image.ImageContainer;
import com.codename1.rad.ui.menus.ActionSheet;
import com.codename1.twitterui.schemas.Tweet;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import com.codename1.ui.Component;
import com.codename1.ui.ComponentSelector;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.Sheet;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.RoundRectBorder;

/**
 *
 * @author shannah
 */
public class TweetRowView extends AbstractEntityView {
    
    public static final Category TWEET_MENU_ACTIONS = new Category();
    public static final Category TWEET_ACTIONS = new Category();
    public static final Category TWEET_CLICKED = new Category();
    public static final Category TWEET_CLICKED_MENU = new Category();
    public static final Category TWEET_LONG_PRESS = new Category();
    public static final Category TWEET_LONG_PRESS_MENU = new Category();
    public static final Category TWEET_AUTHOR_CLICKED = new Category();
    public static final Category TWEET_AUTHOR_LONG_PRESS = new Category();
    public static final Category TWEET_AUTHOR_CLICKED_MENU = new Category();
    public static final Category TWEET_AUTHOR_LONG_PRESS_MENU = new Category();
    
    private int imageWidth = (int)Math.round(CN.getDisplayWidth() * 0.75), 
            imageHeight = (int)Math.round(imageWidth * 9.0/16.0);
    private ViewNode node;
    private float profileAvatarSize = 8f;
    private Property 
            authorProp,
            authorIdProp,
            authorIconProp,
            postTimeProp,
            replyingToProp,
            subscriptionSourceProp,
            imageProp,
            linkProp,
            linkTitleProp,
            messageBodyProp,
            numViewsProp,
            numLikesProp,
            numRepliesProp,
            numRetweetsProp;
    
    private Label authorNameLabel = new Button("", "TweetAuthorName"), 
            authorIdLabel = new Button("", "TweetAuthorID"),
            postTimeLabel = new Label("", "TweetPostTime");
    
    private Button replyingToButton = new Button("", "TweetReplyingToButton"),
            tweetActionsMenuButton = new Button("", "TweetOverflowMenu"),
            
            // If the post is by someone I don't subscribe to, it will sometimes show an 
            // explanation of why it is in my feed.  E.g. "Joe Blow Retweeted", just above
            // the rest of the row view
            subscriptionSource = new Button(),
            subscriptionSourceBadge = new Button(),
            showThreadButton = new Button()
            ;
   
    // If this tweet contains a link, then it may have a preview
    //private LinkPreview linkPreview;      
    private Label replyingToLabel = new Label("", "TweetReplyToLabel");
    private ImageContainer imageContainer;
    private SpanLabel tweetBody = new SpanLabel("", "TweetBody");
    private ProfileAvatarView profileAvatar;
    
    private final Container avatarWrapper = new Container(new BorderLayout());
    private final Container leftColumn = new Container(BoxLayout.y());
    private final Container mainColumn = new Container(BoxLayout.y());
    private final Container imageContainerWrapper = new Container(new BorderLayout());
    private final Container actionsContainer = new Container();
    private final Button leadButton = new Button();
            
    public TweetRowView(Entity tweet, ViewNode node) {
        super(tweet);
        setLeadComponent(leadButton);
        setUIID("TweetRowView");
        this.node = node;
        loadProperties();
        initUI();
    }
    
    private void loadProperties() {
        Entity e = getEntity();
        EntityType et = e.getEntityType();
        authorProp = e.findProperty(Tweet.author);
        if (authorProp != null && !authorProp.getContentType().isEntity()) {
            authorIdProp = e.findProperty(Tweet.authorId);
            authorIconProp = e.findProperty(Tweet.authorIcon);
        }
        
        postTimeProp = e.findProperty(Tweet.datePosted);
        replyingToProp = e.findProperty(Tweet.inReplyTo);
        subscriptionSourceProp = e.findProperty(Tweet.subscriptionSource);
        imageProp = e.findProperty(Tweet.image);
        linkProp = e.findProperty(Tweet.link);
        if (linkProp == null || !linkProp.getContentType().isEntity()) {
            linkTitleProp = e.findProperty(Tweet.linkSubject);
            if (linkProp == null) {
                linkProp = e.findProperty(Tweet.linkUrl);
            }
        }
        messageBodyProp = e.findProperty(Tweet.text);
        numViewsProp = e.findProperty(Tweet.numViews);
        numRepliesProp = e.findProperty(Tweet.numReplies);
        numRetweetsProp = e.findProperty(Tweet.numRetweets);
        numLikesProp = e.findProperty(Tweet.numLikes);
        
    }
    
    private void initUI() {
        
        // So we can align things left in UI.
        // See docs in CollapsiableHeaderContainer
        $(this).addTags("left-edge");
        mainColumn.setName("MainColumn");
        mainColumn.setUIID("TweetRowMainColumn");
        leftColumn.setName("LeftColumn");
        leftColumn.setUIID("TweetRowLeftColumn");
        tweetBody.setUIID("TweetBody");
        tweetBody.setTextUIID("TweetBodyText");
        setLayout(new BorderLayout());
        leftColumn.add(avatarWrapper);
        add(BorderLayout.WEST,leftColumn);
        add(BorderLayout.CENTER, mainColumn);
        Container topRow = new Container(new BorderLayout());
        topRow.getStyle().stripMarginAndPadding();
        Container authorRow = new Container(BoxLayout.x());
        
        authorRow.addAll(authorNameLabel, authorIdLabel, new Label("-", "TweetRowDot"), postTimeLabel);
        topRow.add(BorderLayout.CENTER, authorRow);
        tweetActionsMenuButton.setBlockLead(true);
        tweetActionsMenuButton.setMaterialIcon(FontImage.MATERIAL_KEYBOARD_ARROW_DOWN);
        tweetActionsMenuButton.addActionListener(evt->{
            Actions actions = getViewNode().getInheritedActions(TWEET_MENU_ACTIONS).getEnabled(getEntity());
            if (actions.isEmpty()) {
                return;
            }
            ActionSheet sheet = new ActionSheet(null, getEntity(), actions);
            sheet.show();
        });
        
        topRow.add(BorderLayout.EAST, tweetActionsMenuButton);
        mainColumn.add(topRow);
        mainColumn.add(tweetBody);
        mainColumn.add(imageContainerWrapper);
        
        
        Actions tweetActions = getViewNode().getInheritedActions(TWEET_ACTIONS).getEnabled(getEntity());
        if (!tweetActions.isEmpty()) {
            actionsContainer.setLayout(new GridLayout(1, tweetActions.size()));
            tweetActions.addToContainer(actionsContainer, getEntity());
        }
        
        mainColumn.add(actionsContainer);
        mainColumn.add(leadButton);
        
        addListenersToButtons($(leadButton), TWEET_CLICKED, TWEET_LONG_PRESS, TWEET_CLICKED_MENU, TWEET_LONG_PRESS_MENU);
        addListenersToButtons($(authorIdLabel, authorNameLabel), TWEET_AUTHOR_CLICKED, TWEET_AUTHOR_LONG_PRESS, TWEET_AUTHOR_CLICKED_MENU, TWEET_AUTHOR_LONG_PRESS_MENU);
        
        
        update();
        
        
        
    }
    
    private void addListenersToButtons(ComponentSelector sel, Category clickedCat, Category longPress, Category clickedMenu, Category longPressMenu) {
        sel.addActionListener(evt->{
            ActionNode clicked = getViewNode().getInheritedAction(clickedCat);
            if (clicked != null) {
                evt.consume();
                ActionEvent ane = clicked.fireEvent(getEntity(), TweetRowView.this);
                if (ane.isConsumed()) {
                    return;
                }
            }
            Actions actions = getViewNode().getInheritedActions(clickedMenu).getEnabled(getEntity());
            if (!actions.isEmpty()) {
                evt.consume();
                ActionSheet actionSheet = new ActionSheet(Sheet.findContainingSheet(this), getEntity(), actions);
                actionSheet.show();
                return;
            }
            
        });
        sel.addLongPressListener(evt->{
            ActionNode clicked = getViewNode().getInheritedAction(longPress);
            if (clicked != null) {
                evt.consume();
                ActionEvent ane = clicked.fireEvent(getEntity(), TweetRowView.this);
                if (ane.isConsumed()) {
                    return;
                }
            }
            Actions actions = getViewNode().getInheritedActions(longPressMenu).getEnabled(getEntity());
            if (!actions.isEmpty()) {
                evt.consume();
                ActionSheet actionSheet = new ActionSheet(Sheet.findContainingSheet(this), getEntity(), actions);
                actionSheet.show();
                return;
            }
            
        });
        
    }
    
    @Override
    public void update() {
        boolean changed = false;
        Entity e = getEntity();
        EntityType et = e.getEntityType();
        String authorName = "";
        String authorId = "";
        if (!e.isEmpty(authorProp)) {
            if (authorProp.getContentType().isEntity()) {
                Entity author = e.getEntity(authorProp);
                if (author != null) {
                    authorName = author.getText(Thing.name);
                    authorId = author.getText(Thing.identifier);
                    if (profileAvatar == null) {
                        ViewNode n = new ViewNode();
                        n.setParent(getViewNode());
                        profileAvatar = new ProfileAvatarView(author, n, profileAvatarSize);
                        profileAvatar.getAllStyles().stripMarginAndPadding();
                        profileAvatar.getAllStyles().setMarginTop(CN.convertToPixels(1.5f));
                        // Make sure avatar aligns left in the UI.
                        // See docs in CollapsibleHeaderContainer
                        $(profileAvatar).addTags("left-inset");
                        profileAvatar.setBlockLead(true);
                        avatarWrapper.add(BorderLayout.CENTER, profileAvatar);
                        changed = true;
                    }
                }
            } else {
                authorName = e.getText(authorProp);
                authorId = e.getText(authorIdProp);
                ViewNode n = new ViewNode(
                        UI.param(ProfileAvatarView.ICON_PROPERTY_TAGS, Tweet.authorIcon),
                        UI.param(ProfileAvatarView.NAME_PROPERTY, authorProp)
                );
                n.setParent(getViewNode());
                profileAvatar = new ProfileAvatarView(e, n, profileAvatarSize);
                profileAvatar.getAllStyles().stripMarginAndPadding();
                profileAvatar.getAllStyles().setMarginTop(CN.convertToPixels(1.5f));
                // Align avatar left with UI.  See docs in CollapsibleHeaderContainer
                $(profileAvatar).addTags("left-inset");
                profileAvatar.setBlockLead(true);
                avatarWrapper.add(BorderLayout.CENTER, profileAvatar);
                changed = true;
            }
        }
        
        if (!Objects.equals(authorId, authorIdLabel.getText())) {
            authorIdLabel.setText(authorId);
            changed = true;
        }
        
        if (!Objects.equals(authorName, authorNameLabel.getText())) {
            authorNameLabel.setText(authorName);
            changed = true;
        }
        
        String postTime = "";
        if (!e.isEmpty(postTimeProp)) {
            DateFormatter formatter = getViewNode().getDateFormatter().getValue();
            postTime = formatter.format(e.getDate(postTimeProp));
            
        }
        if (!Objects.equals(postTime, postTimeLabel.getText())) {
            postTimeLabel.setText(postTime);
            changed = true;
        }
        
        String replyingTo = "";
        if (!e.isEmpty(replyingToProp)) {
            if(replyingToProp.getContentType().isEntity()) {
                Entity replyingToEntity = e.getEntity(replyingToProp);
                if (!replyingToEntity.isEmpty(Thing.identifier)) {
                    replyingTo = replyingToEntity.getText(Thing.identifier);
                } else {
                    replyingTo = replyingToEntity.getText(Thing.name);
                }
            } else {
                replyingTo = e.getText(replyingToProp);
            }
        }
        if (!Objects.equals(replyingTo, replyingToLabel.getText())) {
            replyingToLabel.setText(replyingTo);
            changed = true;
        }
        
        String subscriptionSource = "";
        if (!e.isEmpty(subscriptionSourceProp)) {
            if (subscriptionSourceProp.getContentType().isEntity()) {
                Entity subscriptionSourceEntity = e.getEntity(subscriptionSourceProp);
                if (!subscriptionSourceEntity.isEmpty(Thing.identifier)) {
                    subscriptionSource = subscriptionSourceEntity.getText(Thing.identifier);
                } else {
                    subscriptionSource = subscriptionSourceEntity.getText(Thing.name);
                }
            }
        }
        
        if (!e.isEmpty(imageProp) && imageContainer == null) {
            
            Component cmp = new Component() {
                
            };
            cmp.setWidth(imageWidth);
            cmp.setHeight(imageHeight);
            ComponentImage placeholderImage = new ComponentImage(cmp, imageWidth, imageHeight);
            
            imageContainer = ImageContainer.createToFileSystem(e, imageProp);
            $(imageContainer).setBgColor(0xffffff).setBgTransparency(0xff).setBorder(RoundRectBorder.create().cornerRadius(2f));
            imageContainerWrapper.add(BorderLayout.CENTER, imageContainer);
            changed = true;
            
        }
        
        if (!e.isEmpty(messageBodyProp)) {
            String bodyText = e.getText(messageBodyProp);
            if (!Objects.equals(bodyText, tweetBody.getText())) {
                
                tweetBody.setText(e.getText(messageBodyProp));
                changed = true;
            }
        }
        
        Actions menuActions = getViewNode().getInheritedActions(TWEET_MENU_ACTIONS).getEnabled(e);
        if (menuActions.isEmpty()) {
            if (tweetActionsMenuButton.isVisible()) {
                tweetActionsMenuButton.setHidden(true);
                tweetActionsMenuButton.setVisible(false);
            }
        } else {
            if (!tweetActionsMenuButton.isVisible()) {
                tweetActionsMenuButton.setVisible(true);
                tweetActionsMenuButton.setHidden(false);
            }
        }
        
        if (changed && getComponentForm() != null) {
            Form f = getComponentForm();
            if (f != null) {
                f.revalidateWithAnimationSafety();
            }
        }
        
    }

    @Override
    public void commit() {
        
    }

    @Override
    public Node getViewNode() {
        return node;
    }
    
    public static class TweetRowCellRenderer implements EntityListCellRenderer {

        @Override
        public EntityView getListCellRendererComponent(EntityListView list, Entity value, int index, boolean isSelected, boolean isFocused) {
            ViewNode tplNode = ((ListNode)list.getViewNode()).getRowTemplate();
            return new TweetRowView(value, tplNode);
        }
        
    }
}