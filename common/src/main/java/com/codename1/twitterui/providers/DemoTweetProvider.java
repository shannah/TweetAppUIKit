package com.codename1.twitterui.providers;

import com.codename1.rad.models.AbstractEntityListProvider;
import com.codename1.rad.models.Entity;
import com.codename1.rad.schemas.Thing;
import com.codename1.twitterui.models.*;
import com.codename1.twitterui.schemas.TweetSchema;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DemoTweetProvider extends AbstractEntityListProvider implements ITweetProvider {

    private Map<String,Entity> authors = new HashMap<>();



    private Tweets createDemoTweetList() {
        String georgeThumb = "https://weblite.ca/cn1tests/radchat/george.jpg";
        String kramerThumb = "https://weblite.ca/cn1tests/radchat/kramer.jpg";
        String jerryThumb = "https://weblite.ca/cn1tests/radchat/jerry.jpg";
        String elaineThumb = "https://weblite.ca/cn1tests/radchat/elaine.jpg";
        String newmanThumb = "https://weblite.ca/cn1tests/radchat/newman.jpg";
        Entity george = getOrCreateAuthor("George", "@kostanza", georgeThumb);
        Entity kramer = getOrCreateAuthor("Kramer", "@kramer", kramerThumb);
        Entity jerry = getOrCreateAuthor("Jerry", "@jerrys", jerryThumb);
        Entity elaine = getOrCreateAuthor("Elaine", "@elaine", elaineThumb);
        Entity newman = getOrCreateAuthor("Newman", "@newman", newmanThumb);
        long SECOND = 1000l;
        long MINUTE = SECOND * 60;
        long HOUR = MINUTE * 60;
        long DAY = HOUR * 24;
        long t = System.currentTimeMillis() - 2 * DAY;

        Tweets list = new Tweets();
        list.add(createTweet(george, "Just made my first sale", new Date(t), "https://optinmonster.com/wp-content/uploads/2018/05/sales-promotion-examples.jpg"));
        list.add(createTweet(kramer, "Check out these cool new features we added today.  I think you're going to like them.", new Date(t), "https://www.codenameone.com/img/blog/new-features.jpg"));
        list.add(createTweet(newman, "This is how I feel every time Jerry gets what he wants", new Date(t), "https://weblite.ca/cn1tests/radchat/damn-you-seinfeld.jpg"));
        list.add(createTweet(elaine, "These urban sombreros are going to be a huge hit.  Don't miss out.  Get them from the Peterman collection.", new Date(t), "https://weblite.ca/cn1tests/radchat/urban-sombrero.jpg"));
        return list;
    }

    private Entity getOrCreateAuthor(String name, String id, String iconUrl) {
        if (authors.containsKey(id)) {
            return authors.get(id);
        }
        Entity author = new TWTAuthorImpl();
        author.set(Thing.name, name);
        author.set(Thing.identifier, id);
        author.set(Thing.thumbnailUrl, iconUrl);
        authors.put(id, author);
        return author;

    }

    private TweetSchema createTweet(Entity author, String content, Date date, String imageUrl) {
        TweetImpl tweet = new TweetImpl();
        tweet.set(TweetSchema.author, author);
        tweet.setText(TweetSchema.text, content);
        tweet.setDate(TweetSchema.datePosted, date);
        tweet.setText(TweetSchema.image, imageUrl);
        return tweet;
    }


    @Override
    public Request getEntities(Request request) {
        request.complete(createDemoTweetList());
        return request;
    }


    @Override
    public Request createRequest(RequestType type) {
        return new Request(type);
    }
}
