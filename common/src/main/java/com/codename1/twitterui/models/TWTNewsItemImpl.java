package com.codename1.twitterui.models;

import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityType;
import com.codename1.twitterui.schemas.TWTNewsItemSchema;

import java.util.Date;

public class TWTNewsItemImpl extends Entity implements TWTNewsItem {
    private final Wrapper wrapper = new Wrapper(this);
    public static final EntityType TYPE = new EntityType() {{
        entity(Entity.class, TWTNewsItemSchema.creator);
        date(TWTNewsItemSchema.date);
        string(TWTNewsItemSchema.headline);
        string(TWTNewsItemSchema.thumbnailUrl);
        string(TWTNewsItemSchema.image);
    }};
    {
        setEntityType(TYPE);

    }

    @Override
    public TWTAuthor getCreator() {
        return wrapper.getCreator();
    }

    @Override
    public void setCreator(TWTAuthor author) {
        wrapper.setCreator(author);
    }

    @Override
    public String getHeadline() {
        return wrapper.getHeadline();
    }

    @Override
    public void setHeadline(String hl) {
        wrapper.setHeadline(hl);
    }

    @Override
    public String getThumbnailUrl() {
        return wrapper.getThumbnailUrl();
    }

    @Override
    public void setThumbnailUrl(String url) {
        wrapper.setThumbnailUrl(url);
    }

    @Override
    public String getImage() {
        return wrapper.getImage();
    }

    @Override
    public void setImage(String image) {
        wrapper.setImage(image);
    }

    @Override
    public Date getDate() {
        return wrapper.getDate();
    }

    @Override
    public void setDate(Date date) {
        wrapper.setDate(date);
    }

    @Override
    public Entity getEntity() {
        return this;
    }
}
