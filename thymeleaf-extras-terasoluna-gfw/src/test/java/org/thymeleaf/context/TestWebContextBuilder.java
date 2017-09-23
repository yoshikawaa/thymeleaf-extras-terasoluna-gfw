package org.thymeleaf.context;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.standard.StandardDialect;

public class TestWebContextBuilder {

    private static final Set<IDialect> dialects = new HashSet<>(Arrays.asList(new StandardDialect()));

    private final Map<String, Object> attributes = new HashMap<>();
    private final Map<String, Object> variables = new HashMap<>();

    private TestWebContextBuilder() {
    }

    public static void addDialect(IDialect dialect) {
        dialects.add(dialect);
    }

    public static TestWebContextBuilder from() {
        return new TestWebContextBuilder();
    }

    public TestWebContextBuilder attribute(String name, Object value) {
        attributes.put(name, value);
        return this;
    }

    public TestWebContextBuilder attributes(Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            attributes.putAll(map);
        }
        return this;
    }

    public TestWebContextBuilder variable(String name, Object value) {
        variables.put(name, value);
        return this;
    }

    public TestWebContextBuilder variables(Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            variables.putAll(map);
        }
        return this;
    }

    public WebContext build() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockServletContext servletContext = new MockServletContext();
        final Locale locale = request.getLocale();

        attributes.entrySet().forEach(a -> request.setAttribute(a.getKey(), a.getValue()));

        return new WebContext(request, response, servletContext, locale, variables);
    }

}
