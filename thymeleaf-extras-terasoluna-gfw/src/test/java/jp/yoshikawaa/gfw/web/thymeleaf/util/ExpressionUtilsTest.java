package jp.yoshikawaa.gfw.web.thymeleaf.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;
import org.thymeleaf.Arguments;
import org.thymeleaf.TestArgumentsBuilder;
import org.thymeleaf.context.TestWebContextBuilder;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.exceptions.TemplateInputException;

public class ExpressionUtilsTest {

    public ExpressionUtilsTest() {
        new ExpressionUtils();
    }
    
    @Test
    public void testExpression() {
        // setup.
        final WebContext context = TestWebContextBuilder.init().variable("test", "success").build();
        final Arguments arguments = TestArgumentsBuilder.build(context);

        // execute.
        Object result = ExpressionUtils.execute(arguments, "${test}", Object.class);

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testExpressionTyped() {
        // setup.
        final WebContext context = TestWebContextBuilder.init().variable("test", "success").build();
        final Arguments arguments = TestArgumentsBuilder.build(context);

        // execute.
        String result = ExpressionUtils.execute(arguments, "${test}", String.class);

        // assert.
        assertThat(result).isEqualTo("success");
    }

    @Test
    public void testExpressionTypedNotFound() {
        // setup.
        final WebContext context = TestWebContextBuilder.init().build();
        final Arguments arguments = TestArgumentsBuilder.build(context);

        // execute.
        String result = ExpressionUtils.execute(arguments, "${test}", String.class);
        
        // assert.
        assertThat(result).isNull();
    }

    @Test
    public void testExpressionTypedUnmatch() {
        // setup.
        final WebContext context = TestWebContextBuilder.init().variable("test", "success").build();
        final Arguments arguments = TestArgumentsBuilder.build(context);

        // execute and assert.
        assertThatThrownBy(() -> {
            ExpressionUtils.execute(arguments, "${test}", Integer.class);
        }).isInstanceOf(TemplateInputException.class)
                .hasMessage("expression result is not expected. expected:java.lang.Integer actual:java.lang.String");
    }

}
