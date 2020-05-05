/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.twitterui;


import ca.weblite.shared.components.CollapsibleHeaderContainer;

import com.codename1.demos.twitterui.models.UserProfile;
import static com.codename1.rad.ui.UI.*;
import com.codename1.rad.controllers.Controller;
import com.codename1.rad.models.PropertySelector;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.propertyviews.ButtonListPropertyView;
import com.codename1.rad.propertyviews.ButtonListPropertyView.ButtonListLayout;
import com.codename1.rad.schemas.Person;
import com.codename1.rad.schemas.PostalAddress;
import com.codename1.rad.schemas.Thing;
import com.codename1.rad.ui.UI;
import com.codename1.twitterui.views.TWTSettingsForm;
import com.codename1.ui.list.DefaultListModel;

/**
 *
 * @author shannah
 */
public class SettingsFormController extends BaseFormController {
    public static final ActionNode username = action(
                label("Username"), 
                property(entity->{
                    return new PropertySelector(entity, Thing.identifier);
                })
            ),
            qualityFilter = action(
                    label("Quality filter"),
                    toggleSwitch(
                        property(UserProfile.qualityFilter),
                        description("Filter lower-quality content from your notifications. This won't filter out notifications from people you follow or accounts you've interacted with recently.")
                    )
            ),
            phone = action(
                    label("Phone"),
                    tags(Person.telephone)
            ),
            email = action(
                    label("Email"),
                    tags(Person.email)
            ),
            password = action(
                    label("Password")
            ),
            country = action(
                    label("Country"),
                    radioListY(
                        label("Select Country"),
                        description("Please select a country from the list below"),
                        options(new DefaultListModel("Canada", "United States", "Mexico", "Spain", "England", "France")),
                        tags(PostalAddress.addressCountry)
                    )
            ),
            yourTwitterData = action(
                    label("Your twitter data")
            ),
            security = action(
                    label("Security")
            ),
            deactivate = action(
                    label("Deactivate your account")
            );
    public SettingsFormController(Controller parent) {
        super(parent);
        
        TWTSettingsForm view = new TWTSettingsForm(lookup(UserProfile.class), getViewNode());
        
        setTitle("Settings and privacy");
        
        setView(view);
        
        
    }

    @Override
    protected ViewNode createViewNode() {
        return new ViewNode(
                section(
                        label("Login and Security"),
                        username, phone, email, password, qualityFilter
                ),
                section(
                        label("Data and permissions"),
                        country, yourTwitterData, security
                ),
                section(
                        deactivate
                )
        );
    }
    
    
}
