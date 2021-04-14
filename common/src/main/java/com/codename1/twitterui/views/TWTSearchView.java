/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.twitterui.views;

import com.codename1.rad.attributes.UIID;
import com.codename1.rad.controllers.ActionSupport;
import com.codename1.rad.controllers.FormController;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.Property;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ActionNode.Category;
import com.codename1.rad.nodes.ListNode;
import com.codename1.rad.nodes.Node;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.ui.AbstractEntityView;
import com.codename1.rad.ui.Actions;
import com.codename1.rad.ui.UI;
import com.codename1.twitterui.schemas.TWTSearch;
import com.codename1.ui.Button;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;

/**
 * View for the twitter search form including the search field and autocomplete suggestions
 * and recent searches.
 * 
 * image::https://shannah.github.io/TweetAppUIKit/manual/images/Image-050520-095029.395.png[]
 * 
 * === View Model
 * 
 * The view model should conform to the {@link TWTSearch} schema.  This schema includes properties which are lists of results.
 * 
 * .Example View Model
 * [source,java]
 * ----
 * private static class ViewModel extends Entity {
        private static final EntityType TYPE = new EntityType() {{
            string(tags(query));
            list(EntityList.class, tags(autocompleteKeywords));
            list(EntityList.class, tags(autocompleteProfiles));
            list(EntityList.class, tags(recentSearchKeywords));
            list(EntityList.class, tags(recentSearchProfiles));
        }};
        {
            setEntityType(TYPE);
            set(query, "");
            set(autocompleteKeywords, new EntityList());
            set(autocompleteProfiles, new EntityList());
            set(recentSearchKeywords, new EntityList());
            set(recentSearchProfiles, new EntityList());
        }
    }
    
    ....
    
    private static ViewModel createViewModel() {
        ViewModel model = new ViewModel();
        EntityList recentKeywords = model.getEntityList(recentSearchKeywords);
        for (String kw : new String[]{"iOS", "Mobiledev", "Android", "Codename One", "UI design"}) {
            recentKeywords.add(createKeyword(kw));
        }
        
        EntityList recentProfiles = model.getEntityList(recentSearchProfiles);
        recentProfiles.add(createProfile("George", "@kostanza", "https://weblite.ca/cn1tests/radchat/george.jpg"));
        recentProfiles.add(createProfile("Kramer", "@cosmo", "https://weblite.ca/cn1tests/radchat/kramer.jpg"));
        recentProfiles.add(createProfile("Jerry", "@jerrys", null));
        recentProfiles.add(createProfile("Elaine", "@benes1", null));
        recentProfiles.add(createProfile("ReallyLong Name That should get clipped", "@longidshouldbeclipped", null));
        
        
        return model;
        
    }
   
 * ----
 * 
 * === Actions
 * 
 * . `SEARCH_ACTIONS` - Actions rendered to the right of the search field.
 * . `CLEAR_RECENT_SEARCHES` - Action triggered when the user presses the "Clear recent searches" button.
 * . `CANCEL_ACTION` - Action that can be registered to override the default cancel behaviour.  Default behaviour is to go back to the previous form.
 * 
 * === Styles
 * 
 * . `TWTSearchView` - UIID for the component.
 * . `TWTSearchViewRecentSearchesHeader` - UIID for the recent searches header.
 * . `TWTSearchViewRecentSearchesClearButton` - UIID for clear recent searches button.
 * . `TWTSearchViewRecentSearchesClearButtonCharged` - UIID for clear recent searches button when it is "charged".  I.e. after user has clicked on it once.  This gives the user an option to cancel.
 * . `TWTSearchViewTitleBar` - UIID for the top title bar.
 * . `TWTSearchAction` - UIID for the {@link #SEARCH_ACTIONS} buttons.
 * . `TWTSearchViewCancelAction` - UIID for the cancel button.
 * 
 * === Example
 * 
 * 
 * [source,java]
 * ----

package com.codename1.demos.twitterui;

import com.codename1.twitterui.TwitterUIDemo.AppNavigationEvent;
import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.FormController;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.models.EntityType;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.schemas.Thing;
import com.codename1.twitterui.views.TWTSearchView;
import com.codename1.twitterui.schemas.TWTKeyword;
import com.codename1.twitterui.schemas.TWTSearch;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;


public class SearchFormController extends FormController implements TWTSearch, TWTKeyword {
    
    public SearchFormController(Controller parent) {
        super(parent);
        Form form = new Form(new BorderLayout());
        TWTSearchView view = new TWTSearchView(createViewModel(), new ViewNode());
        form.getToolbar().hideToolbar();
        form.getContentPane().add(BorderLayout.CENTER, view);
        setView(form);
    }
    
    
    private static class ViewModel extends Entity {
        private static final EntityType TYPE = new EntityType() {{
            string(tags(query));
            list(EntityList.class, tags(autocompleteKeywords));
            list(EntityList.class, tags(autocompleteProfiles));
            list(EntityList.class, tags(recentSearchKeywords));
            list(EntityList.class, tags(recentSearchProfiles));
        }};
        {
            setEntityType(TYPE);
            set(query, "");
            set(autocompleteKeywords, new EntityList());
            set(autocompleteProfiles, new EntityList());
            set(recentSearchKeywords, new EntityList());
            set(recentSearchProfiles, new EntityList());
        }
    }
    
    private static class KeywordModel extends Entity {
        private static final EntityType TYPE = new EntityType() {{
            string(tags(keyword));
        }};
        {
            setEntityType(TYPE);
        }
    }
    
    
    private static class ProfileModel extends Entity {
        private static final EntityType TYPE = new EntityType() {{
            string(tags(Thing.name));
            string(tags(Thing.identifier));
            string(tags(Thing.thumbnailUrl));
            
        }};
        {
            setEntityType(TYPE);
        }
    }
    private static ViewModel createViewModel() {
        ViewModel model = new ViewModel();
        EntityList recentKeywords = model.getEntityList(recentSearchKeywords);
        for (String kw : new String[]{"iOS", "Mobiledev", "Android", "Codename One", "UI design"}) {
            recentKeywords.add(createKeyword(kw));
        }
        
        EntityList recentProfiles = model.getEntityList(recentSearchProfiles);
        recentProfiles.add(createProfile("George", "@kostanza", "https://weblite.ca/cn1tests/radchat/george.jpg"));
        recentProfiles.add(createProfile("Kramer", "@cosmo", "https://weblite.ca/cn1tests/radchat/kramer.jpg"));
        recentProfiles.add(createProfile("Jerry", "@jerrys", null));
        recentProfiles.add(createProfile("Elaine", "@benes1", null));
        recentProfiles.add(createProfile("ReallyLong Name That should get clipped", "@longidshouldbeclipped", null));
        
        
        return model;
        
    }
    
    private static KeywordModel createKeyword(String kw) {
        KeywordModel out = new KeywordModel();
        out.setText(keyword, kw);
        return out;
    }
    
    private static ProfileModel createProfile(String name, String id, String icon) {
        ProfileModel out = new ProfileModel();
        out.setText(Thing.name, name);
        out.setText(Thing.identifier, id);
        out.setText(Thing.thumbnailUrl, icon);
        return out;
    }

    @Override
    public void initController() {
        super.initController();
        dispatchEvent(new AppNavigationEvent(this, AppNavigationViewModel.NavSection.Search));
    }
  
}

 * 
 * ----
 * 
 * 
 * 
 * @author shannah
 */
public class TWTSearchView extends AbstractEntityView {
    
    /**
     * Actions to be rendered to the right of the search field.
     */
    public static final Category SEARCH_ACTIONS = new Category();
    
    /**
     * Action to handle "clear recent" searches button clicks.
     */
    public static final Category CLEAR_RECENT_SEARCHES = new Category();
    
    /**
     * A cancel action.  Register this action to override the behaviour of the cancel button.  IF this is not registered,
     * then the default behaviour will be used, which will be to navigate back to the previous form.
     */
    public static final Category CANCEL_ACTION = new Category();
    
    
    
    private ViewNode node;
    private TWTKeywordList recentKeywords, autocompleteKeywords;
    private TWTProfileList recentProfiles, autocompleteProfiles;
    private TWTSearchField searchField;
    private Property queryProp, recentKeywordsProp, recentProfilesProp, autocompleteKeywordsProp, autocompleteProfilesProp, statusProp;
    private Container autocompleteWrapper, recentSearchesWrapper;
    private Label recentSearchesHeader = new Label("Recent searches", "TWTSearchViewRecentSearchesHeaderLabel");
    private Button recentSearchesClearButton = new Button("", "TWTSearchViewRecentSearchesClearButton");
    
    public TWTSearchView(Entity entity, ViewNode node) {
        super(entity);
        this.node = node;
        this.queryProp = entity.findProperty(TWTSearch.query);
        this.recentKeywordsProp = entity.findProperty(TWTSearch.recentSearchKeywords);
        this.autocompleteProfilesProp = entity.findProperty(TWTSearch.autocompleteProfiles);
        this.recentProfilesProp = entity.findProperty(TWTSearch.recentSearchProfiles);
        this.statusProp = entity.findProperty(TWTSearch.searchStatus);
        initUI();
    }
    
    private void initUI() {
        setUIID("TWTSearchView");
        setName("TWTSearchView");
        setLayout(new BorderLayout());
        setSafeAreaRoot(true);
        autocompleteWrapper = new Container(BoxLayout.y());
        recentSearchesWrapper = new Container(BoxLayout.y());
        
        final Container header = new Container(new BorderLayout());
        header.setName("TWTSearchViewRecentSearchesHeader");
        header.setUIID("TWTSearchViewRecentSearchesHeader");
        header.setLayout(new BorderLayout());
        header.add(BorderLayout.CENTER, recentSearchesHeader);
        header.add(BorderLayout.EAST, FlowLayout.encloseCenterMiddle(recentSearchesClearButton));
        
        FontImage.setIcon(recentSearchesClearButton, FontImage.MATERIAL_CLEAR, -1);
        
        recentSearchesClearButton.addActionListener(evt->{
            if (recentSearchesClearButton.getText() == "") {
                // On first click, it will just change to a "clear" button
                recentSearchesClearButton.setText("Clear");
                recentSearchesClearButton.setIcon(null);
                
                recentSearchesClearButton.setUIID("TWTSearchViewRecentSearchesClearButtonCharged");
                recentSearchesClearButton.getParent().getParent().animateLayout(200);
            } else {
                ActionNode clearRecentAction = getViewNode().getInheritedAction(CLEAR_RECENT_SEARCHES);
                if (clearRecentAction != null) {
                    ActionEvent ae = clearRecentAction.fireEvent(getEntity(), recentSearchesClearButton);
                    if (ae.isConsumed()) {
                        recentSearchesClearButton.setText("");
                        recentSearchesClearButton.setUIID("TWTSearchViewRecentSearchesClearButton");
                        FontImage.setIcon(recentSearchesClearButton, FontImage.MATERIAL_CLEAR, -1);
                        recentSearchesClearButton.getParent().getParent().animateLayout(200);
                        return;
                    }
                }
                if (!getEntity().isEmpty(recentKeywordsProp)) {
                    getEntity().getEntityList(recentKeywordsProp).clear();
                }
                if (!getEntity().isEmpty(recentProfilesProp)) {
                    getEntity().getEntityList(recentProfilesProp).clear();
                }
                update();
                recentSearchesClearButton.setText("");
                recentSearchesClearButton.setUIID("TWTSearchViewRecentSearchesClearButton");
                FontImage.setIcon(recentSearchesClearButton, FontImage.MATERIAL_CLEAR, -1);
                recentSearchesClearButton.getParent().getParent().animateLayout(200);
                
            }
        });
        recentSearchesWrapper.add(header);
        
        $(autocompleteWrapper, recentSearchesWrapper).setScrollableY(true);
        
        Container north = new Container(new BorderLayout());
        north.getStyle().stripMarginAndPadding();
        searchField = new TWTSearchField(getEntity(), node);
        north.setSafeArea(true);
        north.setUIID("TWTSearchViewTitleBar");
        north.add(BorderLayout.CENTER, searchField);
        Actions actions = getViewNode().getInheritedActions(SEARCH_ACTIONS).getEnabled(getEntity())
                .setAttributesIfNotSet(new UIID("TWTSearchAction"));
        ActionNode cancelAction = getViewNode().getInheritedAction(CANCEL_ACTION);
        if (cancelAction != null) {
            if (cancelAction.isEnabled(getEntity())) {
                actions.add(cancelAction);
            }
        } else {
            cancelAction = new ActionNode(UI.label("Cancel"), new UIID("TWTSearchViewCancelAction"));
            cancelAction.addActionListener(evt->{
                evt.consume();
                System.out.println("About to send form back event");
                ActionSupport.dispatchEvent(new FormController.FormBackEvent(TWTSearchView.this));
            });
            actions.add(cancelAction);
            
        }
                
        if (!actions.isEmpty()) {
            Container actionsWrapper = new Container(new GridLayout(actions.size()));
            actionsWrapper.getStyle().stripMarginAndPadding();
            actions.addToContainer(actionsWrapper, getEntity());
            north.add(BorderLayout.EAST, actionsWrapper);
        }
        add(BorderLayout.NORTH, north);
        
        // Create a slot to add the global toolbar
        Container south = new Container(new BorderLayout());
        south.getStyle().stripMarginAndPadding();
        south.setName("TWTGlobalToolbarSlot");
        add(BorderLayout.SOUTH, south);
        
        update();
        
        
    }
    @Override
    public void update() {
        boolean changed = false;
        String query = "";
        if (!getEntity().isEmpty(queryProp)) {
            query = getEntity().getText(queryProp);
        }
       
        if ("".equals(query)) {
            if (!contains(recentSearchesWrapper)) {
                autocompleteWrapper.remove();
                add(BorderLayout.CENTER, recentSearchesWrapper);
                changed = true;
            }
            
        } else {
            if (!contains(autocompleteWrapper)) {
                recentSearchesWrapper.remove();
                add(BorderLayout.CENTER, autocompleteWrapper);
                changed = true;
            }
            
        }
        if (recentProfiles == null && !getEntity().isEmpty(recentProfilesProp)) {
            ListNode recentProfilesNode = new ListNode();
            recentProfilesNode.setParent(getViewNode());
            recentProfiles = TWTProfileList.creatProfileList(getEntity().getEntityList(recentProfilesProp), TWTProfileList.ListStyle.Badges);
            recentSearchesWrapper.add(recentProfiles);
            changed = true;
        }
        
        
        if (recentKeywords == null && !getEntity().isEmpty(recentKeywordsProp)) {
            ListNode recentKeywordsNode = new ListNode();
            recentKeywordsNode.setParent(getViewNode());
            recentKeywords = new TWTKeywordList(getEntity().getEntityList(recentKeywordsProp), recentKeywordsNode);
            recentSearchesWrapper.add(recentKeywords);
            changed = true;
        }
        
        
        
        if (autocompleteKeywords == null && !getEntity().isEmpty(autocompleteKeywordsProp)) {
            ListNode autocompleteKeywordsNode = new ListNode();
            autocompleteKeywordsNode.setParent(getViewNode());
            autocompleteKeywords = new TWTKeywordList(getEntity().getEntityList(autocompleteKeywordsProp), autocompleteKeywordsNode);
            autocompleteWrapper.add(autocompleteKeywords);
            changed = true;
        }
        
        if (autocompleteProfiles == null && !getEntity().isEmpty(autocompleteProfilesProp)) {
            ListNode autocompleteProfilesNode = new ListNode();
            autocompleteProfilesNode.setParent(getViewNode());
            autocompleteProfiles = new TWTProfileList(getEntity().getEntityList(autocompleteProfilesProp), autocompleteProfilesNode);
            recentSearchesWrapper.add(autocompleteProfiles);
            changed = true;
        }
        
        Container recentsHead = recentSearchesHeader.getParent();
        if (recentsHead.isVisible() && getEntity().isEmpty(recentKeywordsProp) && getEntity().isEmpty(recentProfilesProp)) {
            recentsHead.setVisible(false);
            recentsHead.setHidden(true);
            changed = true;
        } else if (!recentsHead.isVisible() && (!getEntity().isEmpty(recentKeywordsProp) || !getEntity().isEmpty(recentProfilesProp))) {
            recentsHead.setVisible(true);
            recentsHead.setHidden(false);
        }
        
        if (changed) {
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
    private Form form;
    
    private void addClearButtonPointerListener() {
        if (form != null) {
            return;
        }
        form = getComponentForm();
        if (form != null) {
            form.addPointerPressedListener(cancelButtonFormPointerListener);
        }
    }
    
    private void removeClearButtonPointerListener() {
        if (form == null) {
            return;
        }
        form.removePointerPressedListener(cancelButtonFormPointerListener);
        form = null;
    }

    @Override
    protected void initComponent() {
        super.initComponent();
        addClearButtonPointerListener();
    }

    @Override
    protected void deinitialize() {
        removeClearButtonPointerListener();
        super.deinitialize();
    }
    
    
    
    
    
    private ActionListener cancelButtonFormPointerListener = evt -> {
        if (recentSearchesClearButton.getText().length() > 0) {
            if (!recentSearchesClearButton.contains(evt.getX(), evt.getY())) {
                recentSearchesClearButton.setText("");
                recentSearchesClearButton.setUIID("TWTSearchViewRecentSearchesClearButton");
                FontImage.setIcon(recentSearchesClearButton, FontImage.MATERIAL_CLEAR, -1);
                recentSearchesClearButton.getParent().getParent().animateLayout(200);
            }
        }
    };
    
}
