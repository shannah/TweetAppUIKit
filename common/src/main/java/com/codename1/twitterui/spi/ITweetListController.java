package com.codename1.twitterui.spi;

import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.ui.Actions;
import com.codename1.twitterui.events.TweetRequestEvent;
import com.codename1.twitterui.providers.ITweetProvider;

public interface ITweetListController extends IController {

    Actions createTweetMenuActions();
    Actions createTweetActions();
    Actions createTweetProfileAvatarClickedMenuActions();
    ActionNode createTweetClickedAction();
    ITweetProvider createTweetProvider();





}
