package com.codename1.twitterui.models;

import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.twitterui.schemas.TweetSchema;

public class Tweets extends EntityList<Tweet> {

    public void add(Tweet link) {
        super.add(link);
    }
}
