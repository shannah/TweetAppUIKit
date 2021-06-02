package com.codename1.rad.components.loginform;
import com.codename1.rad.annotations.RAD;
import com.codename1.rad.ui.EntityViewFragment;
import com.codename1.rad.ui.EntityView;
import com.codename1.io.CharArrayReader;
@RAD
public abstract class AbstractLoginForm extends EntityViewFragment {
    private static final FRAGMENT_XML="<?xml version=\"1.0\" encoding=\"UTF-8\"?><container elementId=\"0\">\n    <container elementId=\"1\" layout=\"border\">\n        <container elementId=\"2\" layout-constraint=\"center\"\/>\n    <\/container>\n<\/container>";
    public AbstractLoginForm(EntityView contextView) {
        super(contextView);
    }

}
