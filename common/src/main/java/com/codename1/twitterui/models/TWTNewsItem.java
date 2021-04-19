/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.twitterui.models;

import com.codename1.rad.models.AbstractEntityWrapper;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityType;
import com.codename1.rad.models.EntityWrapper;
import com.codename1.twitterui.schemas.TWTNewsItemSchema;

import java.util.Date;

/**
 *
 * @author shannah
 */
public interface TWTNewsItem extends EntityWrapper, TWTNewsItemSchema {

    /*
    public static final Tag creator = SocialMediaPosting.creator,
            headline = SocialMediaPosting.headline,
            thumbnailUrl = SocialMediaPosting.thumbnailUrl,
            image = SocialMediaPosting.image,
            date = SocialMediaPosting.datePublished;
     */

    public TWTAuthor getCreator();
    public void setCreator(TWTAuthor author);
    public String getHeadline();
    public void setHeadline(String hl);
    public String getThumbnailUrl();
    public void setThumbnailUrl(String url);
    public String getImage();
    public void setImage(String image);
    public Date getDate();
    public void setDate(Date date);

    public static class Wrapper extends AbstractEntityWrapper implements TWTNewsItem {

        protected Wrapper(Entity entity) {
            super(entity);
        }

        @Override
        public TWTAuthor getCreator() {
            return entity.getAs(creator, TWTAuthor.class);
        }

        @Override
        public void setCreator(TWTAuthor author) {
            entity.setEntity(creator, author);
        }

        @Override
        public String getHeadline() {
            return entity.getText(headline);
        }

        @Override
        public void setHeadline(String hl) {
            entity.setText(headline, hl);
        }

        @Override
        public String getThumbnailUrl() {
            return entity.getText(thumbnailUrl);
        }

        @Override
        public void setThumbnailUrl(String url) {
            entity.setText(thumbnailUrl, url);
        }

        @Override
        public String getImage() {
            return entity.getText(image);
        }

        @Override
        public void setImage(String imageVal) {
            entity.setText(image, imageVal);
        }

        @Override
        public Date getDate() {
            return entity.getDate(date);
        }

        @Override
        public void setDate(Date dateVal) {
            entity.setDate(date, dateVal);
        }
    }
}
