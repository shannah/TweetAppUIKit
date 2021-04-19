/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.twitterui.controllers;

import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.FormController;
import com.codename1.rad.controllers.ViewController;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.models.EntityType;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.schemas.Thing;
import com.codename1.twitterui.events.AppNavigationEvent;
import com.codename1.twitterui.schemas.TWTApplicationSchema;
import com.codename1.twitterui.views.TWTSearchView;
import com.codename1.twitterui.schemas.TWTKeyword;
import com.codename1.twitterui.schemas.TWTSearch;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;

import static com.codename1.rad.ui.UI.action;
import static com.codename1.rad.ui.UI.icon;

/**
 *
 * @author shannah
 */
public class SearchFormController extends FormController implements TWTSearch, TWTKeyword {

    public static interface Factory {
        public SearchFormController newController(Controller parent);
    }

    /**
     * Creates a new instance of SearchFormController at the given context.  This will do a
     * lookup for {@link SearchFormController.Factory} and use that factory to create the instance if found.
     *
     * It will fall back to creating a new {@link SearchFormController}.  This implementation
     * allows you to override the {@link SearchFormController.Factory} by adding a lookup to the application controller.
     *
     * @param context The context from where the lookup is made to find a {@link SearchFormController.Factory}.
     * @param parent The parent controller passed to the new form controller to be its parent.
     * @return A SearchFormController.
     */
    public static SearchFormController newInstance(Controller context, Controller parent) {
        SearchFormController.Factory factory = context.lookup(SearchFormController.Factory.class);
        SearchFormController out = null;
        if (factory != null) {
            out = factory.newController(parent);
            if (out != null) return out;
        }
        return new SearchFormController(parent);
    }

    public static final TWTApplicationSchema.Section section = new TWTApplicationSchema.Section(action(icon(FontImage.MATERIAL_SEARCH)), evt->{
        ViewController vc = evt.getViewController();
        newInstance(vc, vc.getFormController()).show();
    });



    public SearchFormController(Controller parent) {
        super(parent);
        setPathName("search");
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
        dispatchEvent(new AppNavigationEvent(this, section));
    }
    
    
    
}
