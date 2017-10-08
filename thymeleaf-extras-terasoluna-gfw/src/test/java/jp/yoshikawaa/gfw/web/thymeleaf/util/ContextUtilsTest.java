package jp.yoshikawaa.gfw.web.thymeleaf.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.EngineContext;
import org.thymeleaf.context.TestEngineConfigurationBuilder;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.TemplateData;
import org.thymeleaf.engine.TestTemplateDataBuilder;
import org.thymeleaf.engine.TestWebEngineContextBuilder;
import org.thymeleaf.exceptions.TemplateInputException;

public class ContextUtilsTest {

    public ContextUtilsTest() {
        new ContextUtils();
    }

    @Test
    public void testVariable() {
        // setup.
        final String template = "<input />";
        final WebEngineContext context = TestWebEngineContextBuilder.from(template).variable("test", "success").build();

        // execute.
        Object result = ContextUtils.getAttribute(context, "test");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testRequestAttribute() {
        // setup.
        final String template = "<input />";
        final WebEngineContext context = TestWebEngineContextBuilder.from(template).requestAttribute("test", "success")
                .build();

        // execute.
        Object result = ContextUtils.getAttribute(context, "test");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testSessionAttribute() {
        // setup.
        final String template = "<input />";
        final WebEngineContext context = TestWebEngineContextBuilder.from(template).sessionAttribute("test", "success")
                .build();

        // execute.
        Object result = ContextUtils.getAttribute(context, "test");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testApplicationAttribute() {
        // setup.
        final String template = "<input />";
        final WebEngineContext context = TestWebEngineContextBuilder.from(template)
                .servletContextAttribute("test", "success").build();

        // execute.
        Object result = ContextUtils.getAttribute(context, "test");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testAttributeNotFound() {
        // setup.
        final String template = "<input />";
        final WebEngineContext context = TestWebEngineContextBuilder.from(template).build();

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

        final IEngineConfiguration configuration = TestEngineConfigurationBuilder.build();
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
        final WebEngineContext context = TestWebEngineContextBuilder.from(template).variable("test", "success").build();

        // execute.
        String result = ContextUtils.getAttribute(context, "test", String.class);

        // assert.
        assertThat(result).isEqualTo("success");
    }

    @Test
    public void testVariableTypedNotFound() {
        // setup.
        final String template = "<input />";
        final WebEngineContext context = TestWebEngineContextBuilder.from(template).build();

        // execute.
        String result = ContextUtils.getAttribute(context, "test", String.class);

        // assert.
        assertThat(result).isNull();
    }

    @Test
    public void testVariableTypedTypedUnmatch() {
        // setup.
        final String template = "<input />";
        final WebEngineContext context = TestWebEngineContextBuilder.from(template).variable("test", "success").build();

        // execute and assert.
        assertThatThrownBy(() -> {
            ContextUtils.getAttribute(context, "test", Integer.class);
        }).isInstanceOf(TemplateInputException.class).hasMessage(
                "attribute type is not expected. expected:java.lang.Integer actual:java.lang.String");
    }

}
