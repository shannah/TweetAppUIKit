package com.codename1.twitterui.models;

import com.codename1.rad.annotations.RAD;
import com.codename1.rad.models.SimpleEntityWrapper;
import com.codename1.rad.models.Entity;
import com.codename1.rad.schemas.Thing;
import com.codename1.twitterui.schemas.TWTAuthorSchema;
import com.codename1.rad.models.Entity;

@RAD
public interface TWTAuthor extends TWTAuthorSchema, Entity {

    @RAD(tag="name")
    public String getName();
    public void setName(String name);

    @RAD(tag="identifier")
    public String getIdentifier();
    public void setIdentifier(String id);

    @RAD(tag="thumbnailUrl")
    public String getThumbnailUrl();
    public void setThumbnailUrl(String url);


}
