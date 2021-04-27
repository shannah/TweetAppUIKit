package com.codename1.twitterui.models;

import com.codename1.rad.models.BaseEntity;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.twitterui.schemas.INewsCategories;
import com.codename1.twitterui.schemas.TWTNewsCategorySchema;

import java.util.Arrays;

public class TWTNewsCategories extends EntityList<TWTNewsCategory> {

    public TWTNewsCategories(){

    }

    public TWTNewsCategories(EntityList<TWTNewsCategory> l) {
        for ( TWTNewsCategory e : l) {
            add(e);
        }
    }

    public TWTNewsCategories(TWTNewsCategory... cats) {
        for (TWTNewsCategory cat : cats) {
            add(cat);
        }

    }

    public static TWTNewsCategories from(EntityList<TWTNewsCategory> l) {
        if (l instanceof  TWTNewsCategories) return (TWTNewsCategories)l;
        return new TWTNewsCategories(l);

    }
}
