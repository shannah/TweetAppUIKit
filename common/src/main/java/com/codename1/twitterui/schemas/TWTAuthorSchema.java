package com.codename1.twitterui.schemas;

import com.codename1.rad.models.Tag;
import com.codename1.rad.schemas.PostalAddress;
import com.codename1.rad.schemas.Thing;

public interface TWTAuthorSchema {
    public static final Tag identifier = Thing.identifier;
    public static final Tag name = Thing.name;
    public static final Tag thumbnailUrl = Thing.thumbnailUrl;
    public static final Tag addressCountry = PostalAddress.addressCountry;
}
