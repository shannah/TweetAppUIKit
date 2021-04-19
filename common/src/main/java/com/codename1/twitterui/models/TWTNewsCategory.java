package com.codename1.twitterui.models;

import com.codename1.rad.models.AbstractEntityWrapper;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.models.EntityWrapper;
import com.codename1.twitterui.schemas.TWTNewsCategorySchema;

public interface TWTNewsCategory extends TWTNewsCategorySchema, EntityWrapper {

    public String getIdentifier();
    public void setIdentifier(String id);
    public String getName();
    public void setName(String name);
    public EntityList getFeed();
    public void setFeed(EntityList feed);

    public static class Wrapper extends AbstractEntityWrapper implements TWTNewsCategory {

        protected Wrapper(Entity entity) {
            super(entity);
        }

        @Override
        public String getIdentifier() {
            return entity.getText(identifier);
        }

        @Override
        public void setIdentifier(String id) {
            entity.setText(identifier, id);
        }

        public String getName() {
            return entity.getText(name);
        }

        public void setName(String nameVal) {
            entity.setText(name, nameVal);
        }

        public EntityList getFeed() {
            return entity.getEntityList(feed);
        }



        public void setFeed(EntityList feedVal) {
            entity.set(feed, feedVal);
        }


    }
}
