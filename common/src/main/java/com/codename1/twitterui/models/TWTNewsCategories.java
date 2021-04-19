package com.codename1.twitterui.models;

import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.twitterui.schemas.INewsCategories;
import com.codename1.twitterui.schemas.TWTNewsCategorySchema;

public class TWTNewsCategories<T extends Entity & TWTNewsCategory> extends EntityList<T> {

    public TWTNewsCategories(){

    }

    public TWTNewsCategories(EntityList l) {
        for (Object e : l) {
            T converted = (T)Entity.wrap(TWTNewsCategory.class, (Entity)e);
            if (converted != null) add(converted);
        }
    }

    public static TWTNewsCategories from(EntityList l) {
        if (l instanceof  TWTNewsCategories) return (TWTNewsCategories)l;
        return new TWTNewsCategories(l);

    }
}
