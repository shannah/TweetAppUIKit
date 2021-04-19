package com.codename1.twitterui.providers;

import com.codename1.rad.models.AbstractEntityListProvider;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.schemas.Thing;
import com.codename1.twitterui.models.*;
import com.codename1.twitterui.schemas.TWTNewsItemSchema;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.codename1.rad.util.NonNull.with;

public class DemoNewsProvider extends AbstractEntityListProvider implements INewsProvider {
    private Map<String,TWTAuthor> authors = new HashMap<>();

    public static final String ID_FOR_YOU = "forYou";
    public static final String ID_TRENDING = "trending";

    @Override
    public Request getEntities(Request request) {
        with(request, NewsRequest.class, newsReq -> {
            TWTNewsCategory cat = newsReq.getNewsCategory();
            if (cat != null) {
                String id = cat.getIdentifier();
                if (ID_FOR_YOU.equals(id)) {
                    request.complete(createTrendingSection());
                } else {
                    request.complete(createSection());
                }
            }
        });
        return request;
    }

    @Override
    public  Request createRequest(RequestType type) {
        return new NewsRequest(type);
    }

    private TWTNewsItem createItem(TWTAuthor creator, Date date, String headline, String thumbnailUrl) {
        TWTNewsItem item = new TWTNewsItemImpl();
        item.setCreator(creator);
        item.setDate(date);
        item.setHeadline(headline);
        item.setThumbnailUrl(thumbnailUrl);
        item.setImage(thumbnailUrl);
        return item;
    }

    private EntityList createSection() {
        EntityList out = new EntityList();
        String georgeThumb = "https://weblite.ca/cn1tests/radchat/george.jpg";
        String kramerThumb = "https://weblite.ca/cn1tests/radchat/kramer.jpg";
        String jerryThumb = "https://weblite.ca/cn1tests/radchat/jerry.jpg";
        TWTAuthor george = getOrCreateAuthor("George", "@kostanza", georgeThumb);
        TWTAuthor kramer = getOrCreateAuthor("Kramer", "@kramer", kramerThumb);
        TWTAuthor jerry = getOrCreateAuthor("Jerry", "@jerry", jerryThumb);
        long SECOND = 1000l;
        long MINUTE = SECOND * 60;
        long HOUR = MINUTE * 60;
        long DAY = HOUR * 24;
        long t = System.currentTimeMillis() - 2 * DAY;
        out.add(createItem(jerry, new Date(t), "Stunning Public Domain Waterfall Photos", "https://weblite.ca/cn1tests/radchat/waterfalls.jpg"));
        out.add(createItem(george, new Date(t), "Use the RADChatRoom library to quickly and easily add a nice-looking, ...", "https://www.codenameone.com/img/blog/chat-ui-kit-feature.jpg"));
        out.add(createItem(kramer, new Date(t), "EasyThread  makes it much easier to write multi-threaded code in Codename One.", "https://www.codenameone.com/img/blog/new-features.jpg"));
        out.add(createItem(jerry, new Date(t), "XCODE 11 IS NOW THE DEFAULT", "https://www.codenameone.com/img/blog/xcode-7.png"));
        out.add(createItem(kramer, new Date(t), "CSS styles can now be distributed inside a cn1lib.", "https://www.codenameone.com/img/blog/css-header.jpg"));
        out.add(createItem(george, new Date(t), "Iran's vast coronavirus burial pits are visible by satellite", "https://pbs.twimg.com/media/ES6gV-xXkAQwZ0Y?format=jpg&name=small"));
        out.add(createItem(george, new Date(t), "We've added the ability to position your Sheets in different locations on the screen.", "https://www.codenameone.com/img/blog/new-features.jpg"));
        return out;
    }

    private EntityList createTrendingSection() {
        EntityList out = new EntityList();
        String georgeThumb = "https://weblite.ca/cn1tests/radchat/george.jpg";
        String kramerThumb = "https://weblite.ca/cn1tests/radchat/kramer.jpg";
        TWTAuthor george = getOrCreateAuthor("George", "@kostanza", georgeThumb);
        TWTAuthor kramer = getOrCreateAuthor("Kramer", "@kramer", kramerThumb);
        long SECOND = 1000l;
        long MINUTE = SECOND * 60;
        long HOUR = MINUTE * 60;
        long DAY = HOUR * 24;
        long t = System.currentTimeMillis() - 2 * DAY;

        out.add(createItem(george, new Date(t), "Now you can create CN1Libs with embedded CSS Styles", "https://www.codenameone.com/img/blog/css-header.jpg"));
        out.add(createItem(george, new Date(t), "We now have a reliable way to avoid clipping the Notch and Task bar on the iPhone X.", "https://www.codenameone.com/img/blog/new-features.jpg"));
        return out;
    }

    private TWTAuthor getOrCreateAuthor(String name, String id, String iconUrl) {
        if (authors.containsKey(id)) {
            return authors.get(id);
        }
        TWTAuthor author = new TWTAuthorImpl();
        author.setName(name);
        author.setIdentifier(id);
        author.setThumbnailUrl(iconUrl);
        authors.put(id, author);
        return author;

    }

}
