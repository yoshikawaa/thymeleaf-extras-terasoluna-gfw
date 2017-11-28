package io.github.yoshikawaa.gfw.web.thymeleaf.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.thymeleaf.Arguments;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateInputException;

import io.github.yoshikawaa.gfw.test.engine.TerasolunaGfwTestEngine;
import io.github.yoshikawaa.gfw.test.engine.TestEngine;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.ContextUtils;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.ReflectionUtils;

public class ContextUtilsTest {

    public ContextUtilsTest() {
        ReflectionUtils.newInstance(ContextUtils.class, true);
    }

    @Test
    public void testVariable() {
        // setup.
        final TestEngine engine = new TerasolunaGfwTestEngine().variable("test", "success");
        final Arguments arguments = engine.arguments(engine.context(""));

        // execute.
        Object result = ContextUtils.getAttribute(arguments, "test");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testRequestAttribute() {
        // setup.
        final TestEngine engine = new TerasolunaGfwTestEngine().requestAttribute("test", "success");
        final Arguments arguments = engine.arguments(engine.context(""));

        // execute.
        Object result = ContextUtils.getAttribute(arguments, "test");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testSessionAttribute() {
        // setup.
        final TestEngine engine = new TerasolunaGfwTestEngine().sessionAttribute("test", "success");
        final Arguments arguments = engine.arguments(engine.context(""));

        // execute.
        Object result = ContextUtils.getAttribute(arguments, "test");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testApplicationAttribute() {
        // setup.
        final TestEngine engine = new TerasolunaGfwTestEngine().servletContextAttribute("test", "success");
        final Arguments arguments = engine.arguments(engine.context(""));

        // execute.
        Object result = ContextUtils.getAttribute(arguments, "test");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testAttributeNotFound() {
        // setup.
        final TestEngine engine = new TerasolunaGfwTestEngine();
        final Arguments arguments = engine.arguments(engine.context(""));

        // execute.
        Object result = ContextUtils.getAttribute(arguments, "test");

        // assert.
        assertThat(result).isNull();
    }

    @Test
    public void testVariableWithEngineContext() {
        // setup.
        final Map<String, Object> variables = Collections.singletonMap("test", "success");
        final Locale locale = Locale.getDefault();

        final Context context = new Context(locale, variables);
        final Arguments arguments = new TerasolunaGfwTestEngine().arguments(context);

        // execute.
        Object result = ContextUtils.getAttribute(arguments, "test");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testVariableTyped() {
        // setup.
        final TestEngine engine = new TerasolunaGfwTestEngine().variable("test", "success");
        final Arguments arguments = engine.arguments(engine.context(""));

        // execute.
        String result = ContextUtils.getAttribute(arguments, "test", String.class);

        // assert.
        assertThat(result).isEqualTo("success");
    }

    @Test
    public void testVariableTypedNotFound() {
        // setup.
        final TestEngine engine = new TerasolunaGfwTestEngine();
        final Arguments arguments = engine.arguments(engine.context(""));

        // execute.
        String result = ContextUtils.getAttribute(arguments, "test", String.class);

        // assert.
        assertThat(result).isNull();
    }

    @Test
    public void testVariableTypedTypedUnmatch() {
        // setup.
        final TestEngine engine = new TerasolunaGfwTestEngine().variable("test", "success");
        final Arguments arguments = engine.arguments(engine.context(""));

        // execute and assert.
        assertThatThrownBy(() -> {
            ContextUtils.getAttribute(arguments, "test", Integer.class);
        }).isInstanceOf(TemplateInputException.class)
                .hasMessage("attribute type is not expected. expected:java.lang.Integer actual:java.lang.String");
    }

}
