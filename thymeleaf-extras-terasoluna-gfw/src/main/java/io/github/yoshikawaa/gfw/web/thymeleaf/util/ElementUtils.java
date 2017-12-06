/**
 * Copyright (c) 2017 Atsushi Yoshikawa (https://yoshikawaa.github.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.yoshikawaa.gfw.web.thymeleaf.util;

import java.util.Arrays;

import org.thymeleaf.dom.Attribute;
import org.thymeleaf.dom.Element;

public class ElementUtils {

    public static String getAttributeValue(Element element, String dialectPrefix, String attributeName,
            String defaultValue) {
        return element.hasNormalizedAttribute(dialectPrefix, attributeName)
                ? element.getAttributeValueFromNormalizedName(dialectPrefix, attributeName) : defaultValue;
    }

    public static int getAttributeValue(Element element, String dialectPrefix, String attributeName, int defaultValue) {
        return element.hasNormalizedAttribute(dialectPrefix, attributeName)
                ? Integer.valueOf(element.getAttributeValueFromNormalizedName(dialectPrefix, attributeName))
                : defaultValue;
    }

    public static boolean getAttributeValue(Element element, String dialectPrefix, String attributeName,
            boolean defaultValue) {
        return element.hasNormalizedAttribute(dialectPrefix, attributeName)
                ? Boolean.valueOf(element.getAttributeValueFromNormalizedName(dialectPrefix, attributeName))
                : defaultValue;
    }

    public static void removeAttribute(Element element, String dialectPrefix, String attributeName) {
        Arrays.stream(Attribute.applyPrefixToAttributeName(attributeName, dialectPrefix)).forEach(a -> {
            if (element.hasAttribute(a)) {
                element.removeAttribute(a);
            }
        });
    }
    
    private ElementUtils() {
    }

}
