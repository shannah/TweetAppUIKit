package com.codename1.twitterui.models;

import com.codename1.rad.models.AbstractEntityWrapper;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityWrapper;
import com.codename1.rad.schemas.Thing;
import com.codename1.twitterui.schemas.TWTAuthorSchema;

public interface TWTAuthor extends TWTAuthorSchema, EntityWrapper {

    public String getName();
    public void setName(String name);

    public String getIdentifier();
    public void setIdentifier(String id);

    public String getThumbnailUrl();
    public void setThumbnailUrl(String url);

    public class Wrapper extends AbstractEntityWrapper implements TWTAuthor {

        public Wrapper(Entity entity) {
            super(entity);
        }

        @Override
        public String getName() {
            return entity.getText(name);
        }

        @Override
        public void setName(String name) {
            entity.setText(Thing.name, name);
        }

        @Override
        public String getIdentifier() {
            return entity.getText(identifier);
        }

        @Override
        public void setIdentifier(String id) {
            entity.setText(identifier, id);
        }

        @Override
        public String getThumbnailUrl() {
            return entity.getText(thumbnailUrl);
        }

        @Override
        public void setThumbnailUrl(String url) {
            entity.setText(thumbnailUrl, url);
        }


    }


}
