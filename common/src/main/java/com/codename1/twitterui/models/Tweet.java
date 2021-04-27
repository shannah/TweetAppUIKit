package com.codename1.twitterui.models;


import com.codename1.rad.annotations.RAD;
import com.codename1.rad.models.SimpleEntityWrapper;
import com.codename1.rad.models.Entity;
import com.codename1.twitterui.schemas.TweetSchema;

import java.util.Date;

import static com.codename1.rad.util.NonNull.nonNull;
import com.codename1.rad.models.Entity;
@RAD
public interface Tweet extends TweetSchema, Entity {
    /*
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
     */
    @RAD(tag="author")
    public TWTAuthor getAuthor();
    public void setAuthor(TWTAuthor author);

    @RAD(tag="authorId")
    public String getAuthorId() ;
    public void setAuthorId(String id);

    @RAD(tag="text")
    public String getText();
    public void setText(String text);

    @RAD(tag="datePosted")
    public Date getDatePosted();
    public void setDatePosted(Date date);

    @RAD(tag="numLikes")
    public int getNumLikes();
    public void setNumLikes(int numLikes);

    @RAD(tag="numReplies")
    public int getNumReplies();
    public void setNumReplies(int num);

    @RAD(tag="numRetweets")
    public int getNumRetweets();
    public void setNumRetweets(int num);

    @RAD(tag="link")
    public String getLink();
    public void setLink(String url);

    @RAD(tag="image")
    public String getImage();
    public void setImage(String imageUrl);

    @RAD(tag="authorIcon")
    public String getAuthorIcon();
    public void setAuthorIcon(String iconUrl);

    @RAD(tag="linkSubject")
    public String getLinkSubject();
    public void setLinkSubject(String subject);

    @RAD(tag="inReplyTo")
    public Tweet getInReplyTo();
    public void setInReplyTo(Tweet tweet);

    @RAD(tag="subscriptionSource")
    public String getSubscriptionSource();
    public void setSubscriptionSource(String src);

    @RAD(tag="numViews")
    public int getNumViews();
    public void setNumViews(int num);





}
