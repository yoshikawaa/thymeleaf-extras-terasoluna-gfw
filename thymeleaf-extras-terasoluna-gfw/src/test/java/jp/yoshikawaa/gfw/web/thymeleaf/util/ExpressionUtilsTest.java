package jp.yoshikawaa.gfw.web.thymeleaf.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;
import org.thymeleaf.Arguments;
import org.thymeleaf.exceptions.TemplateInputException;

import jp.yoshikawaa.gfw.test.engine.TerasolunaGfwTestEngine;
import jp.yoshikawaa.gfw.test.engine.TestEngine;

public class ExpressionUtilsTest {

    public ExpressionUtilsTest() {
        ReflectionUtils.newInstance(ExpressionUtils.class, true);
    }

    @Test
    public void testExpression() {
        // setup.
        final TestEngine engine = new TerasolunaGfwTestEngine().variable("test", "success");
        final Arguments arguments = engine.arguments(engine.context(""));

        // execute.
        Object result = ExpressionUtils.execute(arguments, "${test}", Object.class);

        // assert.
        assertThat(result).isInstanceOf(String.class).isEqualTo("success");
    }

    @Test
    public void testExpressionTyped() {
        // setup.
        final TestEngine engine = new TerasolunaGfwTestEngine().variable("test", "success");
        final Arguments arguments = engine.arguments(engine.context(""));

        // execute.
        String result = ExpressionUtils.execute(arguments, "${test}", String.class);

        // assert.
        assertThat(result).isEqualTo("success");
    }

    @Test
    public void testExpressionTypedNotFound() {
        // setup.
        final TestEngine engine = new TerasolunaGfwTestEngine();
        final Arguments arguments = engine.arguments(engine.context(""));

        // execute.
        String result = ExpressionUtils.execute(arguments, "${test}", String.class);

        // assert.
        assertThat(result).isNull();
    }

    @Test
    public void testExpressionTypedUnmatch() {
        // setup.
        final TestEngine engine = new TerasolunaGfwTestEngine().variable("test", "success");
        final Arguments arguments = engine.arguments(engine.context(""));

        // execute and assert.
        assertThatThrownBy(() -> {
            ExpressionUtils.execute(arguments, "${test}", Integer.class);
        }).isInstanceOf(TemplateInputException.class)
                .hasMessage("expression result is not expected. expected:java.lang.Integer actual:java.lang.String");
    }

}
