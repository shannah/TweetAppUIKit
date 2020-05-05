/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.twitterui.models;

import com.codename1.rad.models.BooleanProperty;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityType;
import com.codename1.rad.schemas.PostalAddress;
import com.codename1.rad.schemas.Thing;

/**
 *
 * @author shannah
 */
public class UserProfile extends Entity {
    public static BooleanProperty qualityFilter;
    private static final EntityType TYPE = new EntityType() {{
        string(tags(Thing.name));
        string(tags(Thing.identifier));
        string(tags(Thing.thumbnailUrl));
        string(tags(PostalAddress.addressCountry));
        qualityFilter = Boolean();
    }};
    {
        setEntityType(TYPE);
    }
}
