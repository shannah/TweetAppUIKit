package com.codename1.twitterui.components.loginview;

import com.codename1.rad.components.loginform.LoginViewController;
import com.codename1.rad.components.loginform.LoginViewDelegate;
import com.codename1.rad.controllers.Controller;
import com.codename1.rad.controllers.FormController;
import com.codename1.twitterui.controllers.TWTApplicationController;

import static com.codename1.ui.ComponentSelector.$;

public class TWTLoginFormController extends FormController  {

    LoginViewController viewController;

    public TWTLoginFormController(Controller parent) {
        super(parent);


    }

    @Override
    protected void onStartController() {
        super.onStartController();

        viewController = createLoginViewController();

        setView(viewController.getView());
        getView().getToolbar().hideToolbar();
        getView().getToolbar().getTitleComponent().setHidden(true);
        getView().getToolbar().getTitleComponent().setVisible(false);
        $("TitleArea", getView()).remove();

    }

    protected LoginViewController createLoginViewController() {
        return new TWTLoginViewController(this);
    }



    public TWTApplicationController getTWTApplicationController() {
        return lookup(TWTApplicationController.class);
    }


    public void setLoginViewDelegate(LoginViewDelegate delegate) {
        addLookup(LoginViewDelegate.class, delegate);
    }

    public LoginViewDelegate getLoginViewDelegate() {
        return lookup(LoginViewDelegate.class);
    }


}
