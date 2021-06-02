package com.codename1.twitterui;

import com.codename1.rad.annotations.TagLib;

/**
 * A tag library for the Twitter UI kit.  Import this into your applications using:
 *
 * [source,xml]
 * ----
 * <use-taglib class="com.codename1.twitterui.TagLibrary"/>
 * ----
 *
 */
@TagLib(imports={
        "import com.codename1.twitterui.builders.*",
        "import com.codename1.twitterui.views.*",
        "import com.codename1.twitterui.models.*",
        "import com.codename1.twitterui.controllers.*",
        "import com.codename1.twitterui.providers.*"

})
public class TagLibrary {
}
