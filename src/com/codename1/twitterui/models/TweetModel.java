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
import com.codename1.twitterui.schemas.Tweet;

/**
 *
 * @author shannah
 */
public class TweetModel extends Entity {
    private static final EntityType TYPE = new EntityType() {{
        entity(TWTAuthor.class, tags(Tweet.author));
        entity(TWTAuthor.class, tags(Tweet.inReplyTo));
        entity(TWTAuthor.class, tags(Tweet.subscriptionSource));
        Integer(tags(Tweet.numLikes));
        Integer(tags(Tweet.numReplies));
        Integer(tags(Tweet.numRetweets));
        Integer(tags(Tweet.numViews));
        string(tags(Tweet.image));
        string(tags(Tweet.text));
        date(tags(Tweet.datePosted));
        
    }};
    {
        setEntityType(TYPE);
        
    }
}
