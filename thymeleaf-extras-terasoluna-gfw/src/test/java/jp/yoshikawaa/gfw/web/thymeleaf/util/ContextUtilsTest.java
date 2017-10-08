package jp.yoshikawaa.gfw.web.thymeleaf.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.thymeleaf.Arguments;
import org.thymeleaf.TestArgumentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.TestWebContextBuilder;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.exceptions.TemplateInputException;

public class ContextUtilsTest {

    public ContextUtilsTest() {
        new ContextUtils();
    }

    @Test
    public void testVariable() {
        // setup.
        final WebContext context = TestWebContextBuilder.init().variable("test", "success").build();
        final Arguments arguments = TestArgumentsBuilder.build(context);

        // execute.
        Object result = ContextUtils.getAttribute(arguments, "test");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testRequestAttribute() {
        // setup.
        final WebContext context = TestWebContextBuilder.init().requestAttribute("test", "success").build();
        final Arguments arguments = TestArgumentsBuilder.build(context);

        // execute.
        Object result = ContextUtils.getAttribute(arguments, "test");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testSessionAttribute() {
        // setup.
        final WebContext context = TestWebContextBuilder.init().sessionAttribute("test", "success").build();
        final Arguments arguments = TestArgumentsBuilder.build(context);

        // execute.
        Object result = ContextUtils.getAttribute(arguments, "test");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testApplicationAttribute() {
        // setup.
        final WebContext context = TestWebContextBuilder.init().servletContextAttribute("test", "success").build();
        final Arguments arguments = TestArgumentsBuilder.build(context);

        // execute.
        Object result = ContextUtils.getAttribute(arguments, "test");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testAttributeNotFound() {
        // setup.
        final WebContext context = TestWebContextBuilder.init().build();
        final Arguments arguments = TestArgumentsBuilder.build(context);

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
        final Arguments arguments = TestArgumentsBuilder.build(context);

        // execute.
        Object result = ContextUtils.getAttribute(arguments, "test");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testVariableTyped() {
        // setup.
        final WebContext context = TestWebContextBuilder.init().variable("test", "success").build();
        final Arguments arguments = TestArgumentsBuilder.build(context);

        // execute.
        String result = ContextUtils.getAttribute(arguments, "test", String.class);

        // assert.
        assertThat(result).isEqualTo("success");
    }

    @Test
    public void testVariableTypedNotFound() {
        // setup.
        final WebContext context = TestWebContextBuilder.init().build();
        final Arguments arguments = TestArgumentsBuilder.build(context);

        // execute.
        String result = ContextUtils.getAttribute(arguments, "test", String.class);

        // assert.
        assertThat(result).isNull();
    }

    @Test
    public void testVariableTypedTypedUnmatch() {
        // setup.
        final WebContext context = TestWebContextBuilder.init().variable("test", "success").build();
        final Arguments arguments = TestArgumentsBuilder.build(context);

        // execute and assert.
        assertThatThrownBy(() -> {
            ContextUtils.getAttribute(arguments, "test", Integer.class);
        }).isInstanceOf(TemplateInputException.class).hasMessage(
                "attribute type is not expected. expected:java.lang.Integer actual:java.lang.String");
    }

}
