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

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.util.StringUtils;

/**
 * Utility for handling {@link IMessageResolver}
 * 
 * @author Atsushi Yoshikawa
 * @see IMessageResolver
 */
public class MessageUtils {

    /**
     * Resolve message.
     * 
     * @param origin class of processor
     * @param context template context used to resolve message
     * @param code message key
     * @param args message arguments
     * @param defaultMessage return when message is not found
     * @return resolved message value or default
     */
    public static String resolveMessage(Class<?> origin, ITemplateContext context, String code, Object[] args,
            String defaultMessage) {

        if (StringUtils.isEmpty(code)) {
            return defaultMessage;
        }
        String resolvedMessage = context.getMessage(origin, code, args, false);
        return StringUtils.isEmpty(resolvedMessage) ? defaultMessage : resolvedMessage;
    }

    private MessageUtils() {
    }

}
