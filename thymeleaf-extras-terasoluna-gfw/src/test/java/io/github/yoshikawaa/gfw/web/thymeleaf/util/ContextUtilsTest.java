package io.github.yoshikawaa.gfw.web.thymeleaf.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.EngineContext;
import org.thymeleaf.context.IEngineContext;
import org.thymeleaf.engine.TemplateData;
import org.thymeleaf.engine.TestTemplateDataBuilder;
import org.thymeleaf.exceptions.TemplateInputException;

import io.github.yoshikawaa.gfw.test.engine.TerasolunaGfwTestEngine;
import io.github.yoshikawaa.gfw.test.util.ReflectionUtils;

public class ContextUtilsTest {

    public ContextUtilsTest() {
        ReflectionUtils.newInstance(ContextUtils.class, true);
    }

    @Test
    public void testVariable() {
        // setup.
        final String template = "<input />";
        final IEngineContext context = new TerasolunaGfwTestEngine().variable("test", "success").context(template);

        // execute.
        Object result = ContextUtils.getAttribute(context, "test");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testRequestAttribute() {
        // setup.
        final String template = "<input />";
        final IEngineContext context = new TerasolunaGfwTestEngine().requestAttribute("test", "success")
                .context(template);

        // execute.
        Object result = ContextUtils.getAttribute(context, "test");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testSessionAttribute() {
        // setup.
        final String template = "<input />";
        final IEngineContext context = new TerasolunaGfwTestEngine().sessionAttribute("test", "success")
                .context(template);

        // execute.
        Object result = ContextUtils.getAttribute(context, "test");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testApplicationAttribute() {
        // setup.
        final String template = "<input />";
        final IEngineContext context = new TerasolunaGfwTestEngine().servletContextAttribute("test", "success")
                .context(template);

        // execute.
        Object result = ContextUtils.getAttribute(context, "test");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testAttributeNotFound() {
        // setup.
        final String template = "<input />";
        final IEngineContext context = new TerasolunaGfwTestEngine().context(template);

        // execute.
        Object result = ContextUtils.getAttribute(context, "test");

        // assert.
        assertThat(result).isNull();
    }

    @Test
    public void testVariableWithEngineContext() {
        // setup.
        final String template = "<input />";
        final Map<String, Object> variables = Collections.singletonMap("test", "success");
        final Locale locale = Locale.getDefault();

        final IEngineConfiguration configuration = new TerasolunaGfwTestEngine().configuration();
        final TemplateData templateData = TestTemplateDataBuilder.build(template);
        final EngineContext context = new EngineContext(configuration, templateData, null, locale, variables);

        // execute.
        Object result = ContextUtils.getAttribute(context, "test");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testVariableTyped() {
        // setup.
        final String template = "<input />";
        final IEngineContext context = new TerasolunaGfwTestEngine().variable("test", "success").context(template);

        // execute.
        String result = ContextUtils.getAttribute(context, "test", String.class);

        // assert.
        assertThat(result).isEqualTo("success");
    }

    @Test
    public void testVariableTypedNotFound() {
        // setup.
        final String template = "<input />";
        final IEngineContext context = new TerasolunaGfwTestEngine().context(template);

        // execute.
        String result = ContextUtils.getAttribute(context, "test", String.class);

        // assert.
        assertThat(result).isNull();
    }

    @Test
    public void testVariableTypedTypedUnmatch() {
        // setup.
        final String template = "<input />";
        final IEngineContext context = new TerasolunaGfwTestEngine().variable("test", "success").context(template);

        // execute and assert.
        assertThatThrownBy(() -> {
            ContextUtils.getAttribute(context, "test", Integer.class);
        }).isInstanceOf(TemplateInputException.class)
                .hasMessage("attribute type is not expected. expected:java.lang.Integer actual:java.lang.String");
    }

}
