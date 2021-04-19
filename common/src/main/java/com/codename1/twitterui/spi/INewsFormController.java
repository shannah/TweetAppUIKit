package com.codename1.twitterui.spi;

import com.codename1.twitterui.models.TWTNewsCategories;
import com.codename1.twitterui.providers.INewsProvider;

public interface INewsFormController extends IController {
    public TWTNewsCategories getNewsCategories();
    public INewsProvider getNewsProvider();

}
