package com.codename1.twitterui.models;

import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;

public class Tweets<T extends Entity & ITweet> extends EntityList<T> {

    public void add(ITweet link) {
        super.add((T)link);
    }
}
