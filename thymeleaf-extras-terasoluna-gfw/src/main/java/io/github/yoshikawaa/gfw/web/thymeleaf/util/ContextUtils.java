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

import org.thymeleaf.Arguments;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.exceptions.TemplateInputException;

public class ContextUtils {

    public static Object getAttribute(final Arguments arguments, final String name) {

        IContext context = arguments.getContext();
        if (context instanceof WebContext) {
            final WebContext wc = (WebContext) context;
            final HttpServletRequest request = wc.getHttpServletRequest();
            final HttpSession session = wc.getHttpSession();
            final ServletContext servletContext = wc.getServletContext();

            Object value = request.getAttribute(name);
            if (value == null)
                value = (session != null) ? session.getAttribute(name) : null;
            if (value == null)
                value = servletContext.getAttribute(name);
            return value;
        } else {
            return context.getVariables().get(name);
        }
    }

    public static <T> T getAttribute(final Arguments arguments, final String name, Class<T> clazz) {

        Object result = getAttribute(arguments, name);
        if (result != null && !clazz.isAssignableFrom(result.getClass())) {
            throw new TemplateInputException("attribute type is not expected. expected:" + clazz.getName() + " actual:"
                    + result.getClass().getName());
        }
        return clazz.cast(result);
    }
    
    private ContextUtils() {
    }

}
