/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.twitterui;

import com.codename1.demos.twitterui.models.UserProfile;
import com.codename1.rad.controllers.Controller;
import com.codename1.rad.models.Entity;
import com.codename1.twitterui.controllers.TWTFormController;

/**
 *
 * @author shannah
 */
public class BaseFormController extends TWTFormController {
    public BaseFormController(Controller parent) {
        super(parent);
    }

    @Override
    protected Entity getSideBarViewModel() {
        return lookup(UserProfile.class);
    }

    
    
    
}
