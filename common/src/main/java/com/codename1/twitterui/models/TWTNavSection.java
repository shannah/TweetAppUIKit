package com.codename1.twitterui.models;

import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.FormController;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityType;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.ui.UI;

public abstract class TWTNavSection  extends Entity {
    private ActionNode action;
    public static final EntityType TYPE = new EntityType() {{

    }};

    {
        setEntityType(TYPE);
    }



    public TWTNavSection(ActionNode action) {
        this.action = action;
        this.action.setAttributes(UI.selectedCondition(entity->{
            return entity.get(IApplicationModel.currentSection) == TWTNavSection.this;
        }));
    }

    public ActionNode getAction() {
        return action;
    }



    public abstract FormController createFormController(Controller parent);



}
