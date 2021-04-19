package com.codename1.twitterui.models;

import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.twitterui.schemas.TweetSchema;

public class Tweets<T extends Entity & TweetSchema> extends EntityList<T> {

    public void add(TweetSchema link) {
        super.add((T)link);
    }
}
