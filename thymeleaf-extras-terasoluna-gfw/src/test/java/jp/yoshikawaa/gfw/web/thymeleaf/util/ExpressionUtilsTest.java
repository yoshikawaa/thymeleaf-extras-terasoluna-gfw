package jp.yoshikawaa.gfw.web.thymeleaf.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.TestWebEngineContextBuilder;
import org.thymeleaf.exceptions.TemplateInputException;

public class ExpressionUtilsTest {

    public ExpressionUtilsTest() {
        new ExpressionUtils();
    }
    
    @Test
    public void testExpression() {
        // setup.
        final String template = "<input />";
        final WebEngineContext context = TestWebEngineContextBuilder.from(template).variable("test", "success").build();

        // execute.
        Object result = ExpressionUtils.execute(context, "${test}");

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testExpressionTyped() {
        // setup.
        final String template = "<input />";
        final WebEngineContext context = TestWebEngineContextBuilder.from(template).variable("test", "success").build();

        // execute.
        String result = ExpressionUtils.execute(context, "${test}", String.class);

        // assert.
        assertThat(result).isEqualTo("success");
    }

    @Test
    public void testExpressionTypedNotFound() {
        // setup.
        final String template = "<input />";
        final WebEngineContext context = TestWebEngineContextBuilder.from(template).build();

        // execute.
        String result = ExpressionUtils.execute(context, "${test}", String.class);
        
        // assert.
        assertThat(result).isNull();
    }

    @Test
    public void testExpressionTypedUnmatch() {
        // setup.
        final String template = "<input />";
        final WebEngineContext context = TestWebEngineContextBuilder.from(template).variable("test", "success").build();

        // execute and assert.
        assertThatThrownBy(() -> {
            ExpressionUtils.execute(context, "${test}", Integer.class);
        }).isInstanceOf(TemplateInputException.class)
                .hasMessage("expression result type is not expected. expected:java.lang.Integer actual:java.lang.String");
    }

}
