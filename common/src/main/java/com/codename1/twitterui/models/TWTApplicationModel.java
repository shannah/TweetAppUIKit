package com.codename1.twitterui.models;

import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.models.EntityType;
import com.codename1.rad.models.Tag;
import com.codename1.twitterui.controllers.HomeFormController;

public class TWTApplicationModel extends Entity implements IApplicationModel {



    public static final EntityType TYPE = new EntityType() {{
        entity(Entity.class, user);
        list(EntityList.class, feed);
        object(TWTNavSection.class, currentSection);
    }};


    {
        setEntityType(TYPE);
        set(currentSection, HomeFormController.getHomeNavSection());
    }



}
