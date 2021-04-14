/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.twitterui.models;

import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityType;

/**
 *
 * @author shannah
 */
public class TWTNewsItem extends Entity implements INewsItem {
    public static final EntityType TYPE = new EntityType() {{
        entity(Entity.class, INewsItem.creator);
        date(INewsItem.date);
        string(INewsItem.headline);
        string(INewsItem.thumbnailUrl);
        string(INewsItem.image);
    }};
    {
        setEntityType(TYPE);
        
    }
}
