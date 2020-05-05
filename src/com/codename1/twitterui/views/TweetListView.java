/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.twitterui.views;

import com.codename1.rad.models.DateFormatterAttribute;
import com.codename1.rad.models.Entity;
import com.codename1.rad.models.EntityList;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ActionNode.Category;
import com.codename1.rad.nodes.ListNode;
import com.codename1.rad.ui.ActionViewFactory;
import com.codename1.rad.ui.UI;
import com.codename1.rad.ui.entityviews.EntityListView;
import com.codename1.twitterui.text.TweetDateFormatter;
import com.codename1.ui.Button;
import com.codename1.ui.CN;
import com.codename1.ui.Component;

/**
 *
 * A view for displaying a list of tweets.
 * 
 * 
 * @author shannah
 */
public class TweetListView extends EntityListView {
    
    
    private static class TweetListActionViewFactory implements ActionViewFactory {

        private final ActionViewFactory parent;
        
        TweetListActionViewFactory(ActionViewFactory parent) {
            this.parent = parent;
        }
        
        @Override
        public Component createActionView(Entity entity, ActionNode action) {
            
            Category cat = action.getCategory();
            if (cat == TweetRowView.TWEET_MENU_ACTIONS) {
                action.setAttributes(UI.uiid("TweetMenuAction"));
            } else if (cat == TweetRowView.TWEET_ACTIONS) {
                action.setAttributes(UI.uiid("TweetAction"));
            }
            Component out = parent.createActionView(entity, action);
            if (action.getCategory() == TweetRowView.TWEET_MENU_ACTIONS) {
                if (out instanceof Button) {
                    Button btn = (Button)out;
                    btn.setGap(CN.convertToPixels(3f));
                }
            }
            return out;
        }
        
    }
    
    private static ListNode decorate(ListNode node) {
        if (node == null) {
            node = new ListNode();
        }
        if (node.findAttribute(DateFormatterAttribute.class) == null) {
            node.setAttributes(new DateFormatterAttribute(new TweetDateFormatter()));
        }
        ActionNode dummyAction = new ActionNode();
        dummyAction.setParent(node);
        node.setAttributes(UI.viewFactory(new TweetListActionViewFactory(dummyAction.getViewFactory())));
        node.setAttributes(UI.cellRenderer(new TweetRowView.TweetRowCellRenderer()));
        return node;
    }
    
   
    
    public TweetListView(EntityList tweets) {
        this(tweets, null);
    }
    
    public TweetListView(EntityList tweets, ListNode node) {
        super(tweets, decorate(node));
    }

    @Override
    public void update() {
        
        super.update();
    }
    
    

   
    
}
