package io.github.yoshikawaa.gfw.test.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.IEngineContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.TestElementTagBuilder;
import org.thymeleaf.engine.TestTemplateDataBuilder;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.util.MapUtils;

public class TestEngine {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final TemplateEngine engine = new TemplateEngine();

    private final MockServletContext servletContext = new MockServletContext();
    private final MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
    private final MockHttpServletResponse response = new MockHttpServletResponse();
    private final Map<String, Object> variables = new HashMap<>();

    public TestEngine requestAttribute(String name, Object value) {
        request.setAttribute(name, value);
        return this;
    }

    public TestEngine requestAttributes(Map<String, Object> map) {
        if (!MapUtils.isEmpty(map)) {
            for (Entry<String, Object> entry : map.entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    public TestEngine sessionAttribute(String name, Object value) {
        request.getSession().setAttribute(name, value);
        return this;
    }

    public TestEngine sessionAttributes(Map<String, Object> map) {
        if (!MapUtils.isEmpty(map)) {
            HttpSession session = request.getSession();
            for (Entry<String, Object> entry : map.entrySet()) {
                session.setAttribute(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    public TestEngine servletContextAttribute(String name, Object value) {
        servletContext.setAttribute(name, value);
        return this;
    }

    public TestEngine servletContextAttributes(Map<String, Object> map) {
        if (!MapUtils.isEmpty(map)) {
            for (Entry<String, Object> entry : map.entrySet()) {
                servletContext.setAttribute(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    public TestEngine variable(String name, Object value) {
        variables.put(name, value);
        return this;
    }

    public TestEngine variables(Map<String, Object> map) {
        if (!MapUtils.isEmpty(map)) {
            variables.putAll(map);
        }
        return this;
    }

    public IEngineConfiguration configuration() {
        return engine.getConfiguration();
    }

    public IEngineContext context(String template) {
        return new WebEngineContext(configuration(), TestTemplateDataBuilder.build(template), null, request, response,
                servletContext, request.getLocale(), variables);
    }

    public IProcessableElementTag tag(String template) {
        return TestElementTagBuilder.standalone(configuration(), template);
    }

    public Element parse(String template) {

        final IContext context = context(template);

        logger.debug("[parsing template]\n{}", template);

        Document document = Jsoup.parse(engine.process(template, context));
        Element element = document.child(0).child(1).child(0);

        logger.debug("[parsing complate]\n{}", element.toString());
        return element;
    }

}
