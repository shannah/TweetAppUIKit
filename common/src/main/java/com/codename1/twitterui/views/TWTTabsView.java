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
 * A subclass of {@link TabsEntityView} that injects Twitter-ish tab styles.
 * 
 * image::https://shannah.github.io/CodeRAD/manual/images/TabsEntityView.png[]
 * 
 * === Styles
 * 
 * Tab buttons are styled using the `TWTTabs` UIID which can be overridden in CSS.
 * 
 * === Interaction with {@link CollapsibleHeaderContainer}
 * 
 * The TweetAppUIKit supports collapsible headers via the {@link CollapsibleHeaderContainer} class.  This component
 * will detect there is a collapsible header container, and it will configure it so that the tabs remain
 * visible when the CollapsibleHeaderContainer is collapsed.  It does this by setting the {@link CollapsibleHeaderContainer#setCollapseMode(ca.weblite.shared.components.CollapsibleHeaderContainer.CollapseMode) }
 * with {@link CollapsibleHeaderContainer.CollapseMode#PartialCollapse}.
 * 
 * === Source Example
 * 
 * The following is a full example view controller that sets up a TWTTabsView, resulting in the above screenshot.
 * 
 * [source,java
 * ----

package com.codename1.demos.twitterui;

import ca.weblite.shared.components.CollapsibleHeaderContainer;
import com.codename1.demos.twitterui.models.NewsItem;
import com.codename1.demos.twitterui.models.UserProfile;
import com.codename1.rad.controllers.Controller;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.models.EntityType;
import com.codename1.rad.models.Property;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.schemas.Thing;
import com.codename1.twitterui.models.TWTAuthor;
import com.codename1.twitterui.models.TWTNewsItem;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static com.codename1.rad.ui.UI.*;
import com.codename1.rad.ui.entityviews.TabsEntityView;
import com.codename1.twitterui.views.TWTNewsList;
import com.codename1.twitterui.views.TWTSearchButton;
import com.codename1.twitterui.views.TWTTabsView;
import com.codename1.twitterui.views.TWTTitleComponent;
import com.codename1.ui.Form;


public class SearchTabsController extends BaseFormController {
    public static final ActionNode search = action(
            
    );
    private Map<String,Entity> authors = new HashMap<>();
    
    public SearchTabsController(Controller parent) {
        super(parent);
        SearchTabsViewModel model = createViewModel();
        addLookup(model);
        TabsEntityView view = new TWTTabsView(model, getViewNode());
       
        CollapsibleHeaderContainer wrapper = new CollapsibleHeaderContainer(
                new TWTTitleComponent(lookup(UserProfile.class), getViewNode(), new TWTSearchButton(lookup(UserProfile.class), getViewNode())),
                view, 
                view
        );
        setView(wrapper);
        
        addActionListener(search, evt->{
            evt.consume();
            Form form = new SearchFormController(this).getView();
            form.show();
        });
        
    }

    @Override
    protected ViewNode createViewNode() {
        ViewNode vn = new ViewNode(
                list(
                    property(SearchTabsViewModel.forYou),
                    label("For You"),
                    cellRenderer(new TWTNewsList.TWTNewsListCellRenderer())
                ),
                list(
                    property(SearchTabsViewModel.trending),
                    label("Trending"),
                    cellRenderer(new TWTNewsList.TWTNewsListCellRenderer())
                ),
                actions(TWTSearchButton.SEARCH_ACTION, search)
        );
        return vn;
    }
    
    
    
    public static class SearchTabsViewModel extends Entity {
        public static Property forYou, trending, news, sports, fun;
        public static final EntityType TYPE = new EntityType() {{
            forYou = list(EntityList.class);
            trending = list(EntityList.class);
            news = list(EntityList.class);
            sports = list(EntityList.class);
            fun = list(EntityList.class);
        }};
        {
            setEntityType(TYPE);
        }
    }
    
    private NewsItem createItem(Entity creator, Date date, String headline, String thumbnailUrl) {
        NewsItem item = new NewsItem();
        item.setEntity(TWTNewsItem.creator, creator);
        item.setDate(TWTNewsItem.date, date);
        item.setText(TWTNewsItem.headline, headline);
        item.setText(TWTNewsItem.thumbnailUrl, thumbnailUrl);
        item.setText(TWTNewsItem.image, thumbnailUrl);
        return item;
    }
    
    private EntityList createSection() {
        EntityList out = new EntityList();
        String georgeThumb = "https://weblite.ca/cn1tests/radchat/george.jpg";
        String kramerThumb = "https://weblite.ca/cn1tests/radchat/kramer.jpg";
        String jerryThumb = "https://weblite.ca/cn1tests/radchat/jerry.jpg";
        Entity george = getOrCreateAuthor("George", "@kostanza", georgeThumb);
        Entity kramer = getOrCreateAuthor("Kramer", "@kramer", kramerThumb);
        Entity jerry = getOrCreateAuthor("Jerry", "@jerry", jerryThumb);
        long SECOND = 1000l;
        long MINUTE = SECOND * 60;
        long HOUR = MINUTE * 60;
        long DAY = HOUR * 24;
        long t = System.currentTimeMillis() - 2 * DAY;
        out.add(createItem(jerry, new Date(t), "Stunning Public Domain Waterfall Photos", "https://weblite.ca/cn1tests/radchat/waterfalls.jpg"));
        out.add(createItem(george, new Date(t), "Use the RADChatRoom library to quickly and easily add a nice-looking, ...", "https://www.codenameone.com/img/blog/chat-ui-kit-feature.jpg"));
        out.add(createItem(kramer, new Date(t), "EasyThread  makes it much easier to write multi-threaded code in Codename One.", "https://www.codenameone.com/img/blog/new-features.jpg"));
        out.add(createItem(jerry, new Date(t), "XCODE 11 IS NOW THE DEFAULT", "https://www.codenameone.com/img/blog/xcode-7.png"));
        out.add(createItem(kramer, new Date(t), "CSS styles can now be distributed inside a cn1lib.", "https://www.codenameone.com/img/blog/css-header.jpg"));
        out.add(createItem(george, new Date(t), "Iran's vast coronavirus burial pits are visible by satellite", "https://pbs.twimg.com/media/ES6gV-xXkAQwZ0Y?format=jpg&name=small"));
        out.add(createItem(george, new Date(t), "We've added the ability to position your Sheets in different locations on the screen.", "https://www.codenameone.com/img/blog/new-features.jpg"));
        return out;
    }
    
    private EntityList createTrendingSection() {
        EntityList out = new EntityList();
        String georgeThumb = "https://weblite.ca/cn1tests/radchat/george.jpg";
        String kramerThumb = "https://weblite.ca/cn1tests/radchat/kramer.jpg";
        Entity george = getOrCreateAuthor("George", "@kostanza", georgeThumb);
        Entity kramer = getOrCreateAuthor("Kramer", "@kramer", kramerThumb);
        long SECOND = 1000l;
        long MINUTE = SECOND * 60;
        long HOUR = MINUTE * 60;
        long DAY = HOUR * 24;
        long t = System.currentTimeMillis() - 2 * DAY;
        
        out.add(createItem(george, new Date(t), "Now you can create CN1Libs with embedded CSS Styles", "https://www.codenameone.com/img/blog/css-header.jpg"));
        out.add(createItem(george, new Date(t), "We now have a reliable way to avoid clipping the Notch and Task bar on the iPhone X.", "https://www.codenameone.com/img/blog/new-features.jpg"));
        return out;
    }
    
    private SearchTabsViewModel createViewModel() {
        SearchTabsViewModel out = new SearchTabsViewModel();
        out.set(SearchTabsViewModel.forYou, createSection());
        out.set(SearchTabsViewModel.fun, createSection());
        out.set(SearchTabsViewModel.news, createSection());
        out.set(SearchTabsViewModel.sports, createSection());
        out.set(SearchTabsViewModel.trending, createTrendingSection());
        return out;
    }
    
     private Entity getOrCreateAuthor(String name, String id, String iconUrl) {
        if (authors.containsKey(id)) {
            return authors.get(id);
        }
        Entity author = new TWTAuthor();
        author.set(Thing.name, name);
        author.set(Thing.identifier, id);
        author.set(Thing.thumbnailUrl, iconUrl);
        authors.put(id, author);
        return author;
        
    }
}

 * ----
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
