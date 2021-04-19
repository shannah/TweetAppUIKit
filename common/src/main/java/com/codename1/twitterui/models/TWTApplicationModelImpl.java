package com.codename1.twitterui.models;

import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.models.EntityType;
import com.codename1.twitterui.controllers.HomeFormController;
import com.codename1.twitterui.schemas.TWTApplicationSchema;
import com.codename1.twitterui.schemas.TWTNewsCategorySchema;
import com.codename1.twitterui.schemas.TweetSchema;
import com.codename1.twitterui.schemas.TWTUserProfileSchema;

public class TWTApplicationModelImpl extends Entity implements TWTApplicationModel {

    private final Wrapper wrapper = new Wrapper(this);

    public static final EntityType TYPE = new EntityType() {{
        entityList(user);
        entityList(feed);
        object(Section.class, currentSection);
        entityList(newsCategories);
        object(Sections.class, sections);
    }};


    {
        setEntityType(TYPE);

    }

    @Override
    public TWTUserProfile getUser() {
        return wrapper.getUser();
    }

    @Override
    public void setUser(TWTUserProfile user) {
        wrapper.setUser(user);
    }

    @Override
    public EntityList getFeed() {
        return wrapper.getFeed();
    }

    @Override
    public void setFeed(EntityList feed) {
        wrapper.setFeed(feed);
    }

    @Override
    public Section getCurrentSection() {
        return wrapper.getCurrentSection();
    }

    @Override
    public void setCurrentSection(Section section) {
        wrapper.setCurrentSection(section);
    }

    @Override
    public EntityList getNewsCategories() {
        return wrapper.getNewsCategories();
    }

    @Override
    public void setNewsCategories(EntityList el) {
        wrapper.setNewsCategories(el);
    }

    @Override
    public Sections getSections() {
        return wrapper.getSections();
    }

    @Override
    public void setSections(Sections sections) {
        wrapper.setSections(sections);
    }

    @Override
    public Entity getEntity() {
        return this;
    }
}
