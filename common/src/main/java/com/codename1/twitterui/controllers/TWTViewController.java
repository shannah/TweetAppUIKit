package com.codename1.twitterui.controllers;

import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.ViewController;
import com.codename1.twitterui.spi.IController;

public class TWTViewController extends ViewController implements IController {
    public TWTViewController(Controller parent) {
        super(parent);
    }


    @Override
    public boolean isDemoMode() {
        IController parent = parentLookup(IController.class);
        if (parent != null) return parent.isDemoMode();
        return false;
    }


}
