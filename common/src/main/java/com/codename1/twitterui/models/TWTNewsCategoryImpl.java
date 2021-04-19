package com.codename1.twitterui.models;

import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.models.EntityType;
import com.codename1.rad.schemas.Thing;
import com.codename1.twitterui.schemas.TWTNewsCategorySchema;
import com.codename1.twitterui.schemas.TWTNewsItemSchema;

public class TWTNewsCategoryImpl extends Entity implements TWTNewsCategory {
    private Wrapper wrapper = new Wrapper(this);

    public static final EntityType TYPE = new EntityType() {{
        string(name);
        string(identifier);
        list(EntityList.class, feed);

    }};

    {
        setEntityType(TYPE);
    }

    public TWTNewsCategoryImpl(String id, String nameVal) {
        set(name, nameVal);
        set(identifier, id);
        set(feed, new EntityList());
    }


    @Override
    public Entity getEntity() {
        return this;
    }

    @Override
    public String getIdentifier() {
        return wrapper.getIdentifier();
    }

    @Override
    public void setIdentifier(String id) {
        wrapper.setIdentifier(id);
    }

    @Override
    public String getName() {
        return wrapper.getName();
    }

    @Override
    public void setName(String name) {
        wrapper.setName(name);
    }

    @Override
    public EntityList getFeed() {
        return wrapper.getFeed();
    }

    @Override
    public void setFeed(EntityList feed) {
        wrapper.setFeed(feed);
    }
}
