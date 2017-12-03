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

import org.thymeleaf.model.IProcessableElementTag;

/**
 * Utility for handling {@link IProcessableElementTag}.
 * 
 * @author Atsushi Yoshikawa
 * @see IProcessableElementTag
 */
public class ElementTagUtils {

    /**
     * Get attribute value or default.
     * 
     * @param tag source tag
     * @param dialectPrefix prefix of attribute
     * @param attributeName attribute name
     * @param defaultValue return when attribute is not found
     * @return resolved attribute value or default
     */
    public static String getAttributeValue(IProcessableElementTag tag, String dialectPrefix, String attributeName,
            String defaultValue) {
        return tag.hasAttribute(dialectPrefix, attributeName) ? tag.getAttributeValue(dialectPrefix, attributeName)
                : defaultValue;
    }

    /**
     * Get attribute value or default.
     * 
     * @param tag source tag
     * @param dialectPrefix prefix of attribute
     * @param attributeName attribute name
     * @param defaultValue return when attribute is not found
     * @return resolved attribute value or default
     */
    public static int getAttributeValue(IProcessableElementTag tag, String dialectPrefix, String attributeName,
            int defaultValue) {
        return tag.hasAttribute(dialectPrefix, attributeName)
                ? Integer.valueOf(tag.getAttributeValue(dialectPrefix, attributeName)) : defaultValue;
    }

    /**
     * Get attribute value or default.
     * 
     * @param tag source tag
     * @param dialectPrefix prefix of attribute
     * @param attributeName attribute name
     * @param defaultValue return when attribute is not found
     * @return resolved attribute value or default
     */
    public static boolean getAttributeValue(IProcessableElementTag tag, String dialectPrefix, String attributeName,
            boolean defaultValue) {
        return tag.hasAttribute(dialectPrefix, attributeName)
                ? Boolean.valueOf(tag.getAttributeValue(dialectPrefix, attributeName)) : defaultValue;
    }

    private ElementTagUtils() {
    }

}
