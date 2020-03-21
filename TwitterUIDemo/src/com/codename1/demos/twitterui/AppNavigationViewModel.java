/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.twitterui;

import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityType;
import com.codename1.rad.models.Property;

/**
 *
 * @author shannah
 */
public class AppNavigationViewModel extends Entity {
    
    
    
    
    public static enum NavSection {
        Home,
        Search,
        Alerts,
        Inbox
    }
    
    public static Property<NavSection> currentSection;
    
    public static final EntityType TYPE = new EntityType() {{
        currentSection = object(NavSection.class);
    }};
    {
        setEntityType(TYPE);
        set(currentSection, NavSection.Home);
    }
}
