package com.codename1.twitterui.models;

import com.codename1.rad.models.Tag;

public interface IApplicationModel {
    public static final Tag user = new Tag("user");
    public static final Tag feed = new Tag("feed");
    public static final Tag currentSection = new Tag("currentSection");
}
