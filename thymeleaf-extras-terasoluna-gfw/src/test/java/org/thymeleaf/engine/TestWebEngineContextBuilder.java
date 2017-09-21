package org.thymeleaf.engine;

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
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.TestEngineConfigurationBuilder;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

public class TestWebEngineContextBuilder {

    private static final Set<IDialect> dialects = new HashSet<>(Arrays.asList(new StandardDialect()));

    private final String template;
    private final Map<String, Object> attributes = new HashMap<>();
    private final Map<String, Object> variables = new HashMap<>();

    private TestWebEngineContextBuilder(String template) {
        this.template = template;
    }

    public static void addDialect(IDialect dialect) {
        dialects.add(dialect);
    }

    public static TestWebEngineContextBuilder from(String template) {
        return new TestWebEngineContextBuilder(template);
    }

    public TestWebEngineContextBuilder attribute(String name, Object value) {
        attributes.put(name, value);
        return this;
    }

    public TestWebEngineContextBuilder attributes(Map<String, Object> map) {
        if (!CollectionUtils.isEmpty(map)) {
            attributes.putAll(map);
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
        final IEngineConfiguration configuration = TestEngineConfigurationBuilder.build(dialects);
        final TemplateData templateData = TestTemplateDataBuilder.build(template, TemplateMode.HTML);
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockServletContext servletContext = new MockServletContext();
        final Locale locale = request.getLocale();

        attributes.entrySet().forEach(a -> request.setAttribute(a.getKey(), a.getValue()));

        return new WebEngineContext(configuration, templateData, null, request, response, servletContext, locale,
                variables);
    }

}
