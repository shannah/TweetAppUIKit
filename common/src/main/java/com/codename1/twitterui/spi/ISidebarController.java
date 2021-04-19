package com.codename1.twitterui.spi;

import com.codename1.rad.ui.Actions;

public interface ISidebarController extends IController {
    Actions createSidebarActions();

    Actions createSidebarTopOverflowMenuActions();

    Actions createSidebarBottomLeftActions();

    Actions createSidebarSettingsActions() ;

    Actions createSidebarBottomRightActions();

    Actions createSidebarTopActions();

    Actions createSidebarStatsActions() ;
}
