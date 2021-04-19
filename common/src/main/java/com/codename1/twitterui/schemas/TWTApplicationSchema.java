package com.codename1.twitterui.schemas;

import com.codename1.rad.controllers.Controller;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.models.EntityWrapper;
import com.codename1.rad.models.Tag;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.schemas.Thing;
import com.codename1.rad.ui.Actions;
import com.codename1.rad.ui.UI;
import com.codename1.twitterui.models.TWTApplicationModel;
import com.codename1.ui.events.ActionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public interface TWTApplicationSchema {
    public static final Tag user = new Tag("user");
    public static final Tag feed = new Tag("feed");
    public static final Tag currentSection = new Tag("currentSection");
    public static final Tag sections = new Tag("sections");
    public static final Tag newsCategories = new Tag("newsCategories");

    public static class Section {

        private ActionNode action;
        private ActionListener<ActionNode.ActionNodeEvent> actionListener;

        public Section(ActionNode action, ActionListener<ActionNode.ActionNodeEvent> actionListener) {
            this.action = action;
            this.actionListener = actionListener;
            action.setAttributes(UI.selectedCondition(e->{
                return e.wrap(TWTApplicationModel.class).getCurrentSection() == this;
            }));

        }

        public ActionNode getAction() {
            return action;
        }

        public void addActionListenerTo(Controller controller) {
            controller.addActionListener(action, actionListener);
        }

        public void removeActionListener(Controller controller) {
            controller.removeActionListener(action, actionListener);
        }

    }



    public static class Sections implements Iterable<Section> {
        private ArrayList<Section> sections = new ArrayList<Section>();

        public Sections(Section... sections) {
            this.sections.addAll(Arrays.asList(sections));
        }

        @Override
        public Iterator<Section> iterator() {
            return sections.iterator();
        }

        public void addActionListenersTo(Controller controller) {
            for (Section section : this) {
                section.addActionListenerTo(controller);
            }
        }

        public Actions getActions() {
            Actions out = new Actions();
            for (Section section : this) {
                out.add(section.getAction());
            }
            return out;
        }
    }



}
