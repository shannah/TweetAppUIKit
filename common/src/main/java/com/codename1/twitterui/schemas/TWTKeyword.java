package com.codename1.twitterui.schemas;

import com.codename1.rad.annotations.RAD;
import com.codename1.rad.models.Entity;

@RAD
public interface TWTKeyword extends Entity, TWTKeywordSchema{

    @RAD(tag="keyword")
    public String getKeyword();
    public void setKeyword(String keyword);
}
