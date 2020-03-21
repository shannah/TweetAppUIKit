/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.twitterui.models;

import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityType;
import com.codename1.twitterui.schemas.TWTNewsItem;

/**
 *
 * @author shannah
 */
public class NewsItem extends Entity {
    public static final EntityType TYPE = new EntityType() {{
        entity(Entity.class, TWTNewsItem.creator);
        date(TWTNewsItem.date);
        string(TWTNewsItem.headline);
        string(TWTNewsItem.thumbnailUrl);
        string(TWTNewsItem.image);
    }};
    {
        setEntityType(TYPE);
        
    }
}
