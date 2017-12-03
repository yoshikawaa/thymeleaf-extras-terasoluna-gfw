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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.exceptions.TemplateInputException;

public class ContextUtils {

    public static Object getAttribute(final ITemplateContext context, final String name) {

        if (context instanceof WebEngineContext) {
            final WebEngineContext wec = (WebEngineContext) context;
            final HttpServletRequest request = wec.getRequest();
            final HttpSession session = wec.getSession();
            final ServletContext servletContext = wec.getServletContext();

            Object value = request.getAttribute(name);
            if (value == null)
                value = (session != null) ? session.getAttribute(name) : null;
            if (value == null)
                value = servletContext.getAttribute(name);
            return value;
        } else {
            return context.getVariable(name);
        }
    }

    public static <T> T getAttribute(final ITemplateContext context, final String name, Class<T> clazz) {

        Object result = getAttribute(context, name);
        if (result != null && !clazz.isAssignableFrom(result.getClass())) {
            throw new TemplateInputException("attribute type is not expected. expected:" + clazz.getName() + " actual:"
                    + result.getClass().getName());
        }
        return clazz.cast(result);
    }

}
