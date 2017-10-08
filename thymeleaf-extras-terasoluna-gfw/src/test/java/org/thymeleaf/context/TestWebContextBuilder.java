package org.thymeleaf.context;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.thymeleaf.util.MapUtils;

public class TestWebContextBuilder {

    private final Map<String, Object> requestAttributes = new HashMap<>();
    private final Map<String, Object> sessionAttributes = new HashMap<>();
    private final Map<String, Object> servletContextAttributes = new HashMap<>();
    private final Map<String, Object> variables = new HashMap<>();

    private TestWebContextBuilder() {
    }

    public static TestWebContextBuilder init() {
        return new TestWebContextBuilder();
    }

    public TestWebContextBuilder requestAttribute(String name, Object value) {
        requestAttributes.put(name, value);
        return this;
    }

    public TestWebContextBuilder requestAttributes(Map<String, Object> map) {
        if (!MapUtils.isEmpty(map)) {
            requestAttributes.putAll(map);
        }
        return this;
    }

    public TestWebContextBuilder sessionAttribute(String name, Object value) {
        sessionAttributes.put(name, value);
        return this;
    }

    public TestWebContextBuilder sessionAttributes(Map<String, Object> map) {
        if (!MapUtils.isEmpty(map)) {
            sessionAttributes.putAll(map);
        }
        return this;
    }

    public TestWebContextBuilder servletContextAttribute(String name, Object value) {
        servletContextAttributes.put(name, value);
        return this;
    }

    public TestWebContextBuilder servletContextAttributes(Map<String, Object> map) {
        if (!MapUtils.isEmpty(map)) {
            servletContextAttributes.putAll(map);
        }
        return this;
    }

    public TestWebContextBuilder variable(String name, Object value) {
        variables.put(name, value);
        return this;
    }

    public TestWebContextBuilder variables(Map<String, Object> map) {
        if (!MapUtils.isEmpty(map)) {
            variables.putAll(map);
        }
        return this;
    }

    public WebContext build() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockServletContext servletContext = new MockServletContext();
        final Locale locale = request.getLocale();

        requestAttributes.entrySet().forEach(a -> request.setAttribute(a.getKey(), a.getValue()));
        if (!MapUtils.isEmpty(sessionAttributes)) {
            final MockHttpSession session = new MockHttpSession(servletContext);
            sessionAttributes.entrySet().forEach(a -> session.setAttribute(a.getKey(), a.getValue()));
            request.setSession(session);
        }
        servletContextAttributes.entrySet().forEach(a -> servletContext.setAttribute(a.getKey(), a.getValue()));

        return new WebContext(request, response, servletContext, locale, variables);
    }

}
