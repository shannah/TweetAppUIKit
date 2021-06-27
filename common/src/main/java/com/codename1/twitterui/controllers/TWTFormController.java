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
package com.codename1.twitterui.controllers;

import ca.weblite.shared.components.CollapsibleHeaderContainer;
import ca.weblite.shared.components.CollapsibleSideBarContainer;
import com.codename1.rad.controllers.ActionSupport;
import com.codename1.rad.controllers.AppSectionController;
import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.FormController;
import com.codename1.rad.models.Entity;
import com.codename1.twitterui.models.TWTApplicationModel;

import com.codename1.twitterui.schemas.TWTApplicationSchema;
import com.codename1.twitterui.views.TWTGlobalTabs;
import com.codename1.twitterui.views.TWTSideBarView;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.ComponentSelector;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.InterFormContainer;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.Style;

/**
 *
 * @author shannah
 */
public abstract class TWTFormController extends FormController {


    public TWTFormController(Controller parent) {
        super(parent);
    }
    

    public TWTApplicationController getTWTApplicationController() {
        TWTApplicationController out = lookup(TWTApplicationController.class);
        if (out == null) {
            throw new IllegalStateException("TWTFormController must either be used in the context of a TWTApplicationController, or a TWTApplicationController instance must be added as a lookup to the controller hierarchy above the TWTController");

        }
        return out;
    }

    /**
     * Creates the sidebar component.  The sidebar is only created for top-level
     * forms, with no back command.
     * as the view model to create an instance of {@link TWTSideBarView}.
     * @return  Creates sidebarview.
     */
    protected Component createSideBarView() {
        return new TWTSideBarView(lookup(TWTApplicationModel.class).getEntity(), getViewNode());
    }
    
    private boolean alignLeftOnLayout = false;
    
    @Override
    public void setView(Component cmp) {
        Form form = new Form() {
             @Override
            public void layoutContainer() {
                super.layoutContainer();
                if (!alignLeftOnLayout) {
                    return;
                }
                int maxLeftX = 0;
                ComponentSelector cmps =  $(".left-inset", this);
                for (Component c : cmps) {
                    Component wrap = $(c).parents(".left-edge").first().asComponent();
                    if (wrap == null) {
                        continue;
                    }
                    int thisLeftX = c.getAbsoluteX() + c.getStyle().getPaddingLeftNoRTL() - wrap.getAbsoluteX();
                    maxLeftX = Math.max(maxLeftX, thisLeftX);

                }
                maxLeftX -= getAbsoluteX();

                for (Component c : cmps) {
                    Component wrap = $(c).parents(".left-edge").first().asComponent();
                    if (wrap == null) {
                        continue;
                    }
                    int absX = c.getAbsoluteX() + c.getStyle().getPaddingLeftNoRTL() - wrap.getAbsoluteX();
                    if (absX < maxLeftX) {
                        int marginLeft = c.getStyle().getMarginLeftNoRTL();
                        c.getAllStyles().setMarginUnitLeft(Style.UNIT_TYPE_PIXELS);
                        c.getAllStyles().setMarginLeft(marginLeft + maxLeftX - absX);
                    }
                }

            }
        };
        
        Container mainWrap = new Container(new BorderLayout());
        mainWrap.getStyle().stripMarginAndPadding();
        mainWrap.add(BorderLayout.CENTER, cmp);
        
        boolean hasTitle = false;
        for(Component c : $("*", cmp).add(cmp, true)) {
            if (c instanceof CollapsibleHeaderContainer) {
                hasTitle = true;
                break;
            }
            if ("Title".equals(c)) {
                hasTitle = true;
                break;
            }
        }
        
        
        
        
        TWTGlobalTabs tabs = lookup(TWTGlobalTabs.class);
        if (tabs != null) {
            mainWrap.add(BorderLayout.SOUTH, new InterFormContainer(tabs));
        }
        cmp = mainWrap;
        if (!hasBackCommand()) {
            
            CollapsibleSideBarContainer contentPane = new CollapsibleSideBarContainer(
                    CollapsibleSideBarContainer.wrapSideMenu(createSideBarView()), 
                    cmp
            );
            contentPane.install(form);
        } else {
            
            form.getToolbar().hideToolbar();
            form.setLayout(new BorderLayout());
            form.setScrollable(false);
            form.getContentPane().getStyle().stripMarginAndPadding();
            form.getContentPane().add(BorderLayout.CENTER, cmp);
            
            if (!hasTitle) {
                alignLeftOnLayout = true;
                Container titleBar = new Container(new BorderLayout(BorderLayout.CENTER_BEHAVIOR_CENTER_ABSOLUTE));
                titleBar.setSafeArea(true);
                $(titleBar).addTags("left-edge");

                titleBar.setUIID("TitleArea");

                if (hasBackCommand()) {
                    
                    Button back = new Button();
                    $(back).addTags("left-inset");
                    FontImage.setIcon(back, FontImage.MATERIAL_ARROW_BACK_IOS, -1);
                    titleBar.add(BorderLayout.WEST, back);
                    back.addActionListener(evt->{
                        evt.consume();
                        ActionSupport.dispatchEvent(new FormController.FormBackEvent(back));
                    });

                }

                AppSectionController sectionCtl = getSectionController();
                if (sectionCtl != null) {
                    Button done = new Button("Done");
                    done.addActionListener(evt->{
                        evt.consume();
                        ActionSupport.dispatchEvent(new AppSectionController.ExitSectionEvent(done));
                    });
                    titleBar.add(BorderLayout.EAST, done);
                }
                
                if (getTitle() != null) {
                    titleBar.add(BorderLayout.CENTER, new Label(getTitle(), "Title"));
                }

                form.getContentPane().add(BorderLayout.NORTH, titleBar);
            }
            
        }
        super.setView(form);
    }

    public TWTApplicationModel getApplicationModel() {
        return lookup(TWTApplicationModel.class);
    }
}
