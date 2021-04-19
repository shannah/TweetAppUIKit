package com.codename1.twitterui.models;


import com.codename1.rad.models.AbstractEntityWrapper;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityWrapper;
import com.codename1.twitterui.schemas.TweetSchema;

import java.util.Date;

import static com.codename1.rad.util.NonNull.nonNull;

public interface Tweet extends TweetSchema, EntityWrapper {

    public TWTAuthor getAuthor();
    public void setAuthor(TWTAuthor author);

    public String getAuthorId() ;
    public void setAuthorId(String id);

    public String getText();
    public void setText(String text);

    public Date getDatePosted();
    public void setDatePosted(Date date);

    public int getNumLikes();
    public void setNumLikes(int numLikes);

    public int getNumReplies();
    public void setNumReplies(int num);

    public int getNumRetweets();
    public void setNumRetweets(int num);

    public String getLink();
    public void setLink(String url);

    public String getImage();
    public void setImage(String imageUrl);

    public String getAuthorIcon();
    public void setAuthorIcon(String iconUrl);

    public String getLinkSubject();
    public void setLinkSubject(String subject);

    public Tweet getInReplyTo();
    public void setInReplyTo(Tweet tweet);

    public String getSubscriptionSource();
    public void setSubscriptionSource(String src);

    public int getNumViews();
    public void setNumViews(int num);

    public static class Wrapper extends AbstractEntityWrapper implements Tweet {

        public Wrapper(Entity entity) {
            super(entity);
        }

        @Override
        public TWTAuthor getAuthor() {
            return entity.getWrappedEntity(author, TWTAuthor.class);
        }

        @Override
        public void setAuthor(TWTAuthor authorVal) {
            entity.setEntity(author, authorVal);
        }

        @Override
        public String getAuthorId() {
            return entity.getText(authorId);
        }

        @Override
        public void setAuthorId(String id) {
            entity.setText(authorId, id);
        }

        @Override
        public String getText() {
            return entity.getText(text);
        }

        @Override
        public void setText(String textVal) {
            entity.setText(text, textVal);
        }

        @Override
        public Date getDatePosted() {
            return entity.getDate(datePosted);
        }

        @Override
        public void setDatePosted(Date date) {
            entity.setDate(datePosted, date);
        }

        @Override
        public int getNumLikes() {
            return nonNull(entity.getInt(numLikes), 0);
        }

        @Override
        public void setNumLikes(int numLikesVal) {
            entity.setInt(numLikes, numLikesVal);
        }

        @Override
        public int getNumReplies() {
            return nonNull(entity.getInt(numReplies), 0);
        }

        @Override
        public void setNumReplies(int num) {
            entity.setInt(numReplies, num);

        }

        @Override
        public int getNumRetweets() {
            return nonNull(entity.getInt(numRetweets), 0);
        }

        @Override
        public void setNumRetweets(int num) {
            entity.setInt(numRetweets, num);
        }

        @Override
        public String getLink() {
            return entity.getText(link);
        }

        @Override
        public void setLink(String url) {
            entity.setText(link, url);
        }

        @Override
        public String getImage() {
            return entity.getText(image);
        }

        @Override
        public void setImage(String imageUrl) {
            entity.setText(image, imageUrl);
        }

        @Override
        public String getAuthorIcon() {
            return entity.getText(authorIcon);
        }

        @Override
        public void setAuthorIcon(String iconUrl) {
            entity.setText(authorIcon, iconUrl);
        }

        @Override
        public String getLinkSubject() {
            return entity.getText(linkSubject);
        }

        @Override
        public void setLinkSubject(String subject) {
            entity.setText(linkSubject, subject);
        }

        @Override
        public Tweet getInReplyTo() {
            return entity.getWrappedEntity(inReplyTo, Tweet.class);
        }

        @Override
        public void setInReplyTo(Tweet tweet) {
            entity.setEntity(inReplyTo, tweet);
        }

        @Override
        public String getSubscriptionSource() {
            return entity.getText(subscriptionSource);
        }

        @Override
        public void setSubscriptionSource(String src) {
            entity.setText(subscriptionSource, src);
        }

        @Override
        public int getNumViews() {
            return nonNull(entity.getInt(numViews), 0);
        }

        @Override
        public void setNumViews(int num) {
            entity.setInt(numViews, num);
        }
    }



}
