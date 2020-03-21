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
package com.codename1.twitterui.images;

import ca.weblite.shared.components.ComponentImage;
import com.codename1.rad.ui.UI;
import com.codename1.ui.CN;
import com.codename1.ui.Component;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.plaf.RoundRectBorder;

/**
 *
 * @author shannah
 */
public class TWTImageUtil {
    private static final String CACHE_KEY = "TWTImageMask_";
    
    /**
     * Creates an image mask for images, to round the corners on them.
     * @param width
     * @param height
     * @return 
     */
    public static Object createImageMask(int width, int height) {
        
        String cacheKey = CACHE_KEY+width+"x"+height;
        Object mask = UI.getCache().get(cacheKey);
        if (mask != null) {
            return mask;
        }
        
        Component cmp = new Component() {
            
        };
        $(cmp).setBgColor(0xffffff).setBgTransparency(0xff);
        
        RoundRectBorder border = RoundRectBorder.create()
                .cornerRadius(2f)
                ;
        $(cmp).setBorder(border);
        cmp.setWidth(width);
        cmp.setHeight(height);
        ComponentImage cim = new ComponentImage(cmp, width, height);
        
        Image roundMask = Image.createImage(width, height, 0xff000000);
        Graphics gr = roundMask.getGraphics();
        gr.drawImage(cim, 0, 0, width, height);
        mask = roundMask.createMask();
        UI.getCache().set(cacheKey, mask);
        return mask;
    }
}
