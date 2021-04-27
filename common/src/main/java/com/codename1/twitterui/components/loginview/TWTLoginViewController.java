package com.codename1.twitterui.components.loginview;

import com.codename1.rad.components.loginform.LoginView;
import com.codename1.rad.components.loginform.LoginViewController;
import com.codename1.rad.controllers.Controller;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.ui.UI;
import com.codename1.twitterui.controllers.TWTApplicationController;

import static com.codename1.rad.util.NonNull.with;

public class TWTLoginViewController extends LoginViewController {

    public TWTLoginViewController(Controller parent) {
        super(parent);
    }

    @Override
    protected ViewNode createViewNode() {
        initActions();
        ViewNode n = super.createViewNode();
        LoginView.builder(n)
                .titleUiid("TWTPageTitle")
                .textfieldUiid("TWTLoginField");


        return n;
    }

    private void initActions() {
        with(getAction(LoginView.loginAction), action -> {
            action.setAttributes(false, UI.uiid("TWTLoginButton"));
        });
    }

    @Override
    protected void onStartController() {
        super.onStartController();

    }

    @Override
    protected void onStopController() {
        with(getView(), LoginView.class, loginView -> {
            loginView.purgeTemplateCache();
        });
        super.onStopController();
    }


    private TWTApplicationController getTWTApplicationController() {
        return lookup(TWTApplicationController.class);
    }
}
