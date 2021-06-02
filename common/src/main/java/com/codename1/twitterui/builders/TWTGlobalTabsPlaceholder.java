package com.codename1.twitterui.builders;

import com.codename1.twitterui.views.TWTGlobalTabs;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.InterFormContainer;
import com.codename1.ui.layouts.BorderLayout;

public class TWTGlobalTabsPlaceholder extends Container {

    public TWTGlobalTabsPlaceholder(TWTGlobalTabs content) {
        super(new BorderLayout());
        stripMarginAndPadding();
        add(BorderLayout.CENTER, new InterFormContainer(content));

    }

    public TWTGlobalTabsPlaceholder() {

    }
}
