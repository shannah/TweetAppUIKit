/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.twitterui.controllers;

import com.codename1.twitterui.controllers.TWTApplicationController.AppNavigationEvent;
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

/**
 *
 * @author shannah
 */
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
        dispatchEvent(new AppNavigationEvent(this, SearchTabsController.getSearchNavSection()));
    }
    
    
    
}
