package io.github.yoshikawaa.gfw.test.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.util.StringUtils;
import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.TemplateRepository;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.resourceresolver.ClassLoaderResourceResolver;
import org.thymeleaf.templateresolver.AlwaysValidTemplateResolutionValidity;
import org.thymeleaf.templateresolver.TemplateResolution;
import org.thymeleaf.util.MapUtils;

import io.github.yoshikawaa.gfw.test.templateresolver.StringTemplateResolver;

public class TestEngine {

    private static final String ENCODING = "UTF-8";
    private static final String TEMPLATE_MODE = "HTML5";

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final TemplateEngine engine = new TemplateEngine();

    private final MockServletContext servletContext = new MockServletContext();
    private final MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
    private final MockHttpServletResponse response = new MockHttpServletResponse();
    private final Map<String, Object> variables = new HashMap<>();

    public TestEngine() {
        engine.setTemplateResolver(new StringTemplateResolver());
    }

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

    public Arguments arguments(IContext context) {
        final Configuration configuration = engine.getConfiguration();
        configuration.initialize();
        final TemplateProcessingParameters parameters = new TemplateProcessingParameters(configuration, "", context);
        final TemplateResolution resolution = new TemplateResolution("", "", new ClassLoaderResourceResolver(),
                ENCODING, TEMPLATE_MODE, new AlwaysValidTemplateResolutionValidity());
        final TemplateRepository repository = new TemplateRepository(configuration);
        return new Arguments(engine, parameters, resolution, repository, null);
    }

    public WebContext context(String template) {
        return new WebContext(request, response, servletContext, request.getLocale(), variables);
    }

    public Element tag(String template) {
        StringTokenizer token = new StringTokenizer(template.replace("<", "").replace("/>", ""), " ");
        Element element = new Element(token.nextToken());
        while (token.hasMoreTokens()) {
            String attribute = token.nextToken();
            if (StringUtils.hasText(attribute) && attribute.contains("=")) {
                int i = attribute.indexOf("=");
                element.setAttribute(attribute.substring(0, i),
                        attribute.substring(i + 2).replaceAll("'", "").replaceAll("\"", ""));
            }
        }
        element.setParent(new Element("body"));
        return element;
    }

    public org.jsoup.nodes.Element parse(String template) {

        final IContext context = context(template);

        logger.debug("[parsing template]\n{}", template);

        Document document = Jsoup.parse(engine.process(template, context));
        org.jsoup.nodes.Element element = document.child(0).child(1).child(0);

        logger.debug("[parsing complate]\n{}", element.toString());
        return element;
    }

}
