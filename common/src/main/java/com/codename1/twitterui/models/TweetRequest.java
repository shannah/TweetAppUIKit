package com.codename1.twitterui.models;

import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityType;
import com.codename1.twitterui.schemas.ITweetRequest;

public class TweetRequest extends Entity implements ITweetRequest {
    public static final EntityType TYPE = new EntityType() {{

    }};

    {
        setEntityType(TYPE);
    }
}
