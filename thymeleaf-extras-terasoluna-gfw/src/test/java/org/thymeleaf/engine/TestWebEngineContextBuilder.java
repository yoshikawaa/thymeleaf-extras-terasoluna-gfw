package org.thymeleaf.engine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.TestEngineConfigurationBuilder;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.util.MapUtils;

public class TestWebEngineContextBuilder {

    private static final Set<IDialect> DIALECTS = new HashSet<>(Arrays.asList(new StandardDialect()));

    private final String template;
    private final Map<String, Object> requestAttributes = new HashMap<>();
    private final Map<String, Object> sessionAttributes = new HashMap<>();
    private final Map<String, Object> servletContextAttributes = new HashMap<>();
    private final Map<String, Object> variables = new HashMap<>();

    private TestWebEngineContextBuilder(String template) {
        this.template = template;
    }

    public static void addDialect(IDialect dialect) {
        DIALECTS.add(dialect);
    }

    public static TestWebEngineContextBuilder from(String template) {
        return new TestWebEngineContextBuilder(template);
    }

    public TestWebEngineContextBuilder requestAttribute(String name, Object value) {
        requestAttributes.put(name, value);
        return this;
    }

    public TestWebEngineContextBuilder requestAttributes(Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            requestAttributes.putAll(map);
        }
        return this;
    }

    public TestWebEngineContextBuilder sessionAttribute(String name, Object value) {
        sessionAttributes.put(name, value);
        return this;
    }

    public TestWebEngineContextBuilder sessionAttributes(Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            sessionAttributes.putAll(map);
        }
        return this;
    }

    public TestWebEngineContextBuilder servletContextAttribute(String name, Object value) {
        servletContextAttributes.put(name, value);
        return this;
    }

    public TestWebEngineContextBuilder servletContextAttributes(Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            servletContextAttributes.putAll(map);
        }
        return this;
    }

    public TestWebEngineContextBuilder variable(String name, Object value) {
        variables.put(name, value);
        return this;
    }

    public TestWebEngineContextBuilder variables(Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            variables.putAll(map);
        }
        return this;
    }

    public WebEngineContext build() {
        final IEngineConfiguration configuration = TestEngineConfigurationBuilder.build(DIALECTS);
        final TemplateData templateData = TestTemplateDataBuilder.build(template);
        final MockServletContext servletContext = new MockServletContext();
        final MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Locale locale = request.getLocale();

        requestAttributes.entrySet().forEach(a -> request.setAttribute(a.getKey(), a.getValue()));
        if (!MapUtils.isEmpty(sessionAttributes)) {
            final MockHttpSession session = new MockHttpSession(servletContext);
            sessionAttributes.entrySet().forEach(a -> session.setAttribute(a.getKey(), a.getValue()));
            request.setSession(session);
        }
        servletContextAttributes.entrySet().forEach(a -> servletContext.setAttribute(a.getKey(), a.getValue()));

        return new WebEngineContext(configuration, templateData, null, request, response, servletContext, locale,
                variables);
    }

}
