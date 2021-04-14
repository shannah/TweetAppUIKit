package com.codename1.twitterui.models;

import com.codename1.rad.models.EntityList;
import com.codename1.rad.ui.Actions;

public class TWTNavSections extends EntityList<TWTNavSection> {

    public Actions getActions() {
        Actions out = new Actions();
        for (TWTNavSection sec : this) {
            out.add(sec.getAction());
        }
        return out;
    }
}
