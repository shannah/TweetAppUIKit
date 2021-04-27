package com.codename1.twitterui.models;

import com.codename1.rad.annotations.RAD;
import com.codename1.rad.models.SimpleEntityWrapper;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.twitterui.schemas.TWTNewsCategorySchema;
import com.codename1.rad.models.Entity;

@RAD
public interface TWTNewsCategory extends TWTNewsCategorySchema, Entity {

    @RAD(tag="identifier")
    public String getIdentifier();
    public void setIdentifier(String id);
    @RAD(tag="name")
    public String getName();
    public void setName(String name);
    @RAD(tag="feed")
    public EntityList getFeed();
    public void setFeed(EntityList feed);


}
