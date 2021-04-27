package com.codename1.twitterui.models;

import com.codename1.rad.annotations.RAD;
import com.codename1.rad.models.SimpleEntityWrapper;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.twitterui.schemas.TWTApplicationSchema;
import com.codename1.rad.models.Entity;

@RAD
public interface TWTApplicationModel extends TWTApplicationSchema, Entity {


    @RAD(tag="user")
    public TWTUserProfile getUser();
    public void setUser(TWTUserProfile user);

    @RAD(tag="feed")
    public EntityList getFeed();
    public void setFeed(EntityList feed);

    @RAD(tag="currentSection")
    public ActionNode getCurrentSection();
    public void setCurrentSection(ActionNode section);

    @RAD(tag="newsCategories")
    public EntityList<TWTNewsCategory> getNewsCategories();
    public void setNewsCategories(EntityList<TWTNewsCategory> el);




}
