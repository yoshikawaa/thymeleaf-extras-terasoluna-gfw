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
