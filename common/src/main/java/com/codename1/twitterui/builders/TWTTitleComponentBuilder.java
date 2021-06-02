package com.codename1.twitterui.builders;

import com.codename1.rad.annotations.Inject;
import com.codename1.rad.annotations.RAD;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.ui.AbstractComponentBuilder;
import com.codename1.rad.ui.ComponentBuilder;
import com.codename1.rad.ui.EntityView;
import com.codename1.rad.ui.ViewContext;
import com.codename1.rad.ui.builders.AbstractEntityViewBuilder;
import com.codename1.twitterui.views.TWTTitleComponent;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;

import java.util.Map;
import java.util.Objects;

@RAD(tag={"twtTitleComponent", "twtTitle"})
public class TWTTitleComponentBuilder extends AbstractEntityViewBuilder<TWTTitleComponent> {

    private Component mainContent;
    private String text;

    public TWTTitleComponentBuilder(@Inject ViewContext context, String tagName, Map<String, String> attributes) {
        super(context, tagName, attributes);
    }

    public TWTTitleComponentBuilder mainContent(@Inject Component mainContent) {
        System.out.println("Trying to set mainContent to "+mainContent);
        if (Objects.equals(mainContent, getContext().getEntityView())) return this;
        System.out.println("It passes muster");
        this.mainContent = mainContent;
        return this;
    }

    public TWTTitleComponentBuilder text(String text) {
        this.text = text;
        return this;
    }



    @Override
    public TWTTitleComponent build() {
        if (mainContent == null) {
            if (text == null) {
                text = getContext().getController().getFormController().getTitle();
            }
            mainContent = text != null ? new Label(text, "Title") : new Label();

        }
        System.out.println("Creating title component with "+mainContent);
        TWTTitleComponent out = new TWTTitleComponent(getContext(), mainContent);
        return out;
    }

}
