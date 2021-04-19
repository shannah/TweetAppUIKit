package com.codename1.twitterui.models;

import com.codename1.rad.models.AbstractEntityWrapper;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.models.EntityWrapper;
import com.codename1.twitterui.schemas.TWTApplicationSchema;

public interface TWTApplicationModel extends TWTApplicationSchema, EntityWrapper {


    public TWTUserProfile getUser();
    public void setUser(TWTUserProfile user);

    public EntityList getFeed();
    public void setFeed(EntityList feed);

    public Section getCurrentSection();
    public void setCurrentSection(Section section);

    public EntityList getNewsCategories();
    public void setNewsCategories(EntityList el);

    public Sections getSections();
    public void setSections(Sections sections);

    public static class Wrapper extends AbstractEntityWrapper implements TWTApplicationModel {

        public Wrapper(Entity entity) {
            super(entity);
        }

        @Override
        public TWTUserProfile getUser() {
            return entity.getWrappedEntity(user, TWTUserProfile.class);
        }

        @Override
        public void setUser(TWTUserProfile userVal) {
            entity.setEntity(user, userVal);
        }

        @Override
        public EntityList getFeed() {
            return entity.getEntityList(feed);
        }

        @Override
        public void setFeed(EntityList feedVal) {
            entity.set(feed, feedVal);
        }

        @Override
        public Section getCurrentSection() {
            return entity.getAs(currentSection, Section.class);
        }

        @Override
        public void setCurrentSection(Section section) {
            entity.set(currentSection, section);
        }

        @Override
        public EntityList getNewsCategories() {
            return entity.getEntityList(newsCategories);
        }

        @Override
        public void setNewsCategories(EntityList el) {
            entity.set(newsCategories, el);
        }

        @Override
        public Sections getSections() {
            return entity.getAs(sections, Sections.class);
        }

        @Override
        public void setSections(Sections sectionsVal) {
            entity.set(sections, sectionsVal);
        }
    }

}
