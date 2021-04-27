/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.twitterui.models;

import com.codename1.rad.annotations.RAD;
import com.codename1.rad.models.BaseEntity;
import com.codename1.rad.models.BooleanProperty;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityType;
import com.codename1.rad.schemas.PostalAddress;
import com.codename1.rad.schemas.Thing;
import com.codename1.twitterui.schemas.TWTUserProfileSchema;

/**
 *
 * @author shannah
 */
@RAD
public interface TWTUserProfile extends Entity, TWTUserProfileSchema {


    @RAD(tag="name")
    String getName();
    void setName(String name);

    @RAD(tag="identifier")
    String getIdentifier();
    void setIdentifier(String id);

    @RAD(tag="thumbnailUrl")
    String getThumbnailUrl();
    void setThumbnailUrl(String url);

    @RAD(tag="addressCountry")
    String getCountry();
    void setCountry(String country);

    @RAD(tag="qualityFilter")
    boolean getQualityFilter();
    void setQualityFilter(boolean qualityFilter);

}
