package com.codename1.twitterui.providers;

import com.codename1.rad.models.EntityListProvider;
import com.codename1.twitterui.models.TWTNewsCategory;

public interface INewsProvider extends EntityListProvider {



    public static class NewsRequest extends EntityListProvider.Request {
        private TWTNewsCategory newsCategory;

        public NewsRequest(RequestType type) {
            super(type);
        }

        public TWTNewsCategory getNewsCategory() {
            return newsCategory;
        }

        public void setNewsCategory(TWTNewsCategory newsCategory) {
            this.newsCategory = newsCategory;
        }
    }

}
