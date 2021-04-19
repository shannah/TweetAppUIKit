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
import com.codename1.rad.schemas.Thing;
import com.codename1.twitterui.schemas.TWTAuthorSchema;

/**
 *
 * @author shannah
 */
public class TWTAuthorImpl extends Entity implements TWTAuthor {
    private static final EntityType TYPE = new EntityType(){{
        string(tags(Thing.name));
        string(tags(Thing.identifier));
        string(tags(Thing.thumbnailUrl));
        
    }};
    {
        setEntityType(TYPE);
        
    }

    @Override
    public String getName() {
        return getText(name);
    }

    @Override
    public void setName(String name) {
        setText(Thing.name, name);
    }

    @Override
    public String getIdentifier() {
        return getText(identifier);
    }

    @Override
    public void setIdentifier(String id) {
        setText(identifier, id);
    }

    @Override
    public String getThumbnailUrl() {
        return getText(thumbnailUrl);
    }

    @Override
    public void setThumbnailUrl(String url) {
        setText(thumbnailUrl, url);
    }

    @Override
    public Entity getEntity() {
        return this;
    }
}
