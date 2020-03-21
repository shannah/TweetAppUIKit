/*
 * Copyright 2020 shannah.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codename1.twitterui.text;

import com.codename1.l10n.ParseException;
import com.codename1.rad.text.DateFormatter;
import java.util.Date;

/**
 *
 * @author shannah
 */
public class TweetDateFormatter implements DateFormatter {
    
    private static final long SECOND = 1000l;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;
    private static final long WEEK = 7 * DAY;
    private static final long YEAR = 365 * DAY;

    @Override
    public String format(Date date) {
        long time = date.getTime();
        long now = new Date().getTime();
        long diff = now - time;
        
        if (diff > YEAR) {
            return (diff / YEAR)+"y";
        }
        if (diff > WEEK) {
            return (diff / WEEK)+"wk";
        }
        if (diff > DAY) {
            return (diff / DAY)+"d";
        }
        if (diff > HOUR) {
            return (diff / HOUR)+"h";
        }
        if (diff > MINUTE) {
            return (diff / MINUTE)+"m";
        }
        if (diff > SECOND) {
            return (diff / SECOND)+"s";
        }
        return "just now";
        
    }

    @Override
    public Date parse(String date) throws ParseException {
        return new Date();
    }

    @Override
    public boolean supportsParse() {
        return false;
    }
    
}
