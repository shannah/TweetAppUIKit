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
package com.codename1.twitterui.views;

import com.codename1.rad.attributes.UIIDPrefix;
import com.codename1.rad.models.Entity;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.ui.SettingsForm;

/**
 * A settings form similar to the settings form in the Twitter mobile app.  This extends {@link SettingsForm} and adds the 
 * "TWT" UIID prefix so that it can override styles in the twitter stylesheet.
 * 
 * image::https://shannah.github.io/TweetAppUIKit/manual/images/Image-050520-095115.490.png[]
 * 
 * === Example Usage
 * 
 * The following snippet shows a FormController that uses the TWTSettingsForm as its view.
 * 
 * [source,java]
 * ----

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
----
 * 
 * @author shannah
 */
public class TWTSettingsForm extends SettingsForm {
    
    private static ViewNode decorate(Entity entity, ViewNode node) {
        node.setAttributesIfNotExists(new UIIDPrefix("TWT"));
        return node;
    }
    
    public TWTSettingsForm(Entity entity, ViewNode node) {
        super(entity, decorate(entity, node));
    }
}
