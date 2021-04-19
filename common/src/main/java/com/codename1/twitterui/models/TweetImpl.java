/*
 * Copyright 2020 shannah.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codename1.twitterui.models;

import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityType;

import java.util.Date;

/**
 *
 * @author shannah
 */
public class TweetImpl extends Entity implements Tweet {
    private Wrapper wrapper = new Wrapper(this);
    private static final EntityType TYPE = new EntityType() {{
        entity(TWTAuthorImpl.class, tags(author));
        entity(TWTAuthorImpl.class, tags(inReplyTo));
        entity(TWTAuthorImpl.class, tags(subscriptionSource));
        Integer(tags(numLikes));
        Integer(tags(numReplies));
        Integer(tags(numRetweets));
        Integer(tags(numViews));
        string(tags(image));
        string(tags(text));
        date(tags(datePosted));
        
    }};
    {
        setEntityType(TYPE);
        
    }


    @Override
    public TWTAuthor getAuthor() {
        return wrapper.getAuthor();
    }

    @Override
    public void setAuthor(TWTAuthor author) {
        wrapper.setAuthor(author);
    }

    @Override
    public String getAuthorId() {
        return wrapper.getAuthorId();
    }

    @Override
    public void setAuthorId(String id) {
        wrapper.setAuthorId(id);
    }

    @Override
    public String getText() {
        return wrapper.getText();
    }

    @Override
    public void setText(String text) {
        wrapper.setText(text);
    }

    @Override
    public Date getDatePosted() {
        return wrapper.getDatePosted();
    }

    @Override
    public void setDatePosted(Date date) {
        wrapper.setDatePosted(date);
    }

    @Override
    public int getNumLikes() {
        return wrapper.getNumLikes();
    }

    @Override
    public void setNumLikes(int numLikes) {
        wrapper.setNumLikes(numLikes);
    }

    @Override
    public int getNumReplies() {
        return wrapper.getNumReplies();
    }

    @Override
    public void setNumReplies(int num) {
        wrapper.setNumReplies(num);
    }

    @Override
    public int getNumRetweets() {
        return wrapper.getNumRetweets();
    }

    @Override
    public void setNumRetweets(int num) {
        wrapper.setNumRetweets(num);
    }

    @Override
    public String getLink() {
        return wrapper.getLink();
    }

    @Override
    public void setLink(String url) {
        wrapper.setLink(url);
    }

    @Override
    public String getImage() {
        return wrapper.getImage();
    }

    @Override
    public void setImage(String imageUrl) {
        wrapper.setImage(imageUrl);
    }

    @Override
    public String getAuthorIcon() {
        return wrapper.getAuthorIcon();
    }

    @Override
    public void setAuthorIcon(String iconUrl) {
        wrapper.setAuthorIcon(iconUrl);
    }

    @Override
    public String getLinkSubject() {
        return wrapper.getLinkSubject();
    }

    @Override
    public void setLinkSubject(String subject) {
        wrapper.setLinkSubject(subject);
    }

    @Override
    public Tweet getInReplyTo() {
        return wrapper.getInReplyTo();
    }

    @Override
    public void setInReplyTo(Tweet tweet) {
        wrapper.setInReplyTo(tweet);
    }

    @Override
    public String getSubscriptionSource() {
        return wrapper.getSubscriptionSource();
    }

    @Override
    public void setSubscriptionSource(String src) {
        wrapper.setSubscriptionSource(src);
    }

    @Override
    public int getNumViews() {
        return wrapper.getNumViews();
    }

    @Override
    public void setNumViews(int num) {
        wrapper.setNumViews(num);

    }

    @Override
    public Entity getEntity() {
        return this;
    }
}
