/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.twitterui.controllers;

import ca.weblite.shared.components.CollapsibleHeaderContainer;
import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.ControllerEvent;
import com.codename1.rad.models.Entity;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.ui.ActionStyle;
import com.codename1.rad.ui.UI;
import com.codename1.twitterui.controllers.TWTFormController;
import com.codename1.twitterui.models.ITweet;
import com.codename1.twitterui.views.TWTTitleComponent;
import com.codename1.twitterui.views.TweetDetailView;
import com.codename1.ui.Label;
import static com.codename1.rad.ui.UI.*;

/**
 *
 * @author shannah
 */
public class TweetDetailsController<T extends Entity & ITweet> extends BaseFormController {
    public static final ActionNode retweets = action(
            icon("Retweets"),
            label("245"),
            actionStyle(ActionStyle.IconRight)
    ), likes = action(
            icon("Likes"),
            label("1444"),
            actionStyle(ActionStyle.IconRight)
    );
    
    public TweetDetailsController(Controller parent, T tweet) {
        super(parent);
        
        TweetDetailView view = new TweetDetailView(tweet, getViewNode());
        CollapsibleHeaderContainer wrapper = new CollapsibleHeaderContainer(
                new TWTTitleComponent(tweet, getViewNode(), new Label("Tweet")), 
                view, 
        view);
        setView(wrapper);
        
    }

    @Override
    protected ViewNode createViewNode() {
        ViewNode vn = new ViewNode(
                actions(TweetDetailView.TWEET_ACTIONS, TWTApplicationController.reply, TWTApplicationController.retweet, TWTApplicationController.favorite, TWTApplicationController.share),
                actions(TweetDetailView.STATS_ACTIONS, retweets, likes),
                actions(TweetDetailView.OVERFLOW_ACTIONS, TWTApplicationController.mute)
        );
        
        return vn;
    }
    
    

    @Override
    public void actionPerformed(ControllerEvent evt) {
        
        super.actionPerformed(evt); 
    }
    
    
   
    
}
