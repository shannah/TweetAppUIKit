package com.codename1.twitterui.schemas;

import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.models.Tag;
import com.codename1.rad.schemas.Thing;

public interface TWTNewsCategorySchema {
    public static final Tag identifier = Thing.identifier;
    public static final Tag name = Thing.name;
    public static final Tag feed = new Tag("news feed");


}
