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
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.IWebSession;

/**
 * Utility for handling {@link ITemplateContext}.
 * 
 * @author Atsushi Yoshikawa
 * @see ITemplateContext
 */
public class ContextUtils {

    /**
     * Get attribute value.
     * <ul>
     * <li>If context is {@link IWebContext}, get attribute value from request, session, servlet context.</li>
     * <li>If other context, get attribute value from local variable.</li>
     * </ul>
     * 
     * @param context template context used to resolve attribute
     * @param name attribute name
     * @return resolved attribute value
     * @see IWebContext
     */
    public static Object getAttribute(final ITemplateContext context, final String name) {

        if (context instanceof IWebContext) {
            final IWebExchange exchange = ((IWebContext) context).getExchange();
            final IWebSession session = exchange.getSession();
            final IWebApplication application = exchange.getApplication();

            Object value = exchange.getAttributeValue(name);
            if (value == null)
                value = (session != null) ? session.getAttributeValue(name) : null;
            if (value == null)
                value = (application != null) ? application.getAttributeValue(name) : null;
            return value;
        } else {
            return context.getVariable(name);
        }
    }

    /**
     * Get attribute value type safely.
     * 
     * @param <T> attribute type
     * @param context template context used to resolve attribute
     * @param name attribute name
     * @param clazz attribute type
     * @return resolved attribute value
     * @see #getAttribute(ITemplateContext, String)
     */
    public static <T> T getAttribute(final ITemplateContext context, final String name, Class<T> clazz) {

        Object result = getAttribute(context, name);
        if (result != null && !clazz.isAssignableFrom(result.getClass())) {
            throw new TemplateInputException("attribute type is not expected. expected:" + clazz.getName() + " actual:"
                    + result.getClass().getName());
        }
        return clazz.cast(result);
    }

    private ContextUtils() {
    }

}
