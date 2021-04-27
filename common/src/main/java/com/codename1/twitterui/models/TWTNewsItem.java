/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.twitterui.models;

import com.codename1.rad.annotations.RAD;
import com.codename1.rad.models.SimpleEntityWrapper;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityType;
import com.codename1.twitterui.schemas.TWTNewsItemSchema;

import java.util.Date;
import com.codename1.rad.models.Entity;

/**
 *
 * @author shannah
 */
@RAD
public interface TWTNewsItem extends Entity, TWTNewsItemSchema {


    @RAD(tag="creator")
    public TWTAuthor getCreator();
    public void setCreator(TWTAuthor author);

    @RAD(tag="headline")
    public String getHeadline();
    public void setHeadline(String hl);

    @RAD(tag="thumbnailUrl")
    public String getThumbnailUrl();
    public void setThumbnailUrl(String url);

    @RAD(tag="image")
    public String getImage();
    public void setImage(String image);

    @RAD(tag="date")
    public Date getDate();
    public void setDate(Date date);


}
