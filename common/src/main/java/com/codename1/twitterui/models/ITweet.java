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

import com.codename1.rad.models.Tag;
import com.codename1.rad.schemas.Comment;
import com.codename1.rad.schemas.Person;
import com.codename1.rad.schemas.SocialMediaPosting;

/**
 *
 * @author shannah
 */
public interface ITweet {
    public static final Tag author = Comment.author,
            authorId = new Tag(),
            text = Comment.text,
            datePosted = Comment.datePublished,
            numLikes = Comment.upvoteCount,
            numReplies = Comment.commentCount,
            numRetweets = new Tag(),
            image = SocialMediaPosting.image,
            link = SocialMediaPosting.isBasedOn,
            linkUrl = SocialMediaPosting.url,
            authorIcon = Person.thumbnailUrl,
            linkSubject = SocialMediaPosting.headline,
            inReplyTo = Comment.parentItem,
            subscriptionSource = Comment.sourceOrganization,
            numViews = Comment.viewCount;
            
            
    
}
