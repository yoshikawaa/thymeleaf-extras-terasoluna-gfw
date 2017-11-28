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
