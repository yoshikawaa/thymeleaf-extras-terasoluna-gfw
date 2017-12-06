package io.github.yoshikawaa.gfw.web.thymeleaf.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.thymeleaf.Arguments;

import io.github.yoshikawaa.gfw.test.engine.TerasolunaGfwTestEngine;
import io.github.yoshikawaa.gfw.test.engine.TestEngine;
import io.github.yoshikawaa.gfw.test.util.ReflectionUtils;

public class MessageUtilsTest {

    public MessageUtilsTest() {
        ReflectionUtils.newInstance(MessageUtils.class, true);
    }

    @Test
    public void testResolved() {
        // setup.
        final String template = "<input />";
        final TestEngine engine = new TerasolunaGfwTestEngine();
        final Arguments arguments = engine.arguments(engine.context(template));

        // execute and assert.
        assertThat(MessageUtils.resolveMessage(arguments, "test1", null, "unresolved-message"))
                .isEqualTo("test message 1.");
    }

    @Test
    public void testResolvedWithArgs() {
        // setup.
        final String template = "<input />";
        final TestEngine engine = new TerasolunaGfwTestEngine();
        final Arguments arguments = engine.arguments(engine.context(template));

        // execute and assert.
        assertThat(MessageUtils.resolveMessage(arguments, "args1", new String[] {
                "test1", "test2", "test3" }, "unresolved-message")).isEqualTo("args [test1,test2,test3].");
    }

    @Test
    public void testNotResolved() {
        // setup.
        final String template = "<input />";
        final TestEngine engine = new TerasolunaGfwTestEngine();
        final Arguments arguments = engine.arguments(engine.context(template));

        // execute and assert.
        assertThat(MessageUtils.resolveMessage(arguments, "test0", null, "unresolved-message"))
                .isEqualTo("unresolved-message");
    }

    @Test
    public void testCodeEmpty() {
        // setup.
        final String template = "<input />";
        final TestEngine engine = new TerasolunaGfwTestEngine();
        final Arguments arguments = engine.arguments(engine.context(template));

        // execute and assert.
        assertThat(MessageUtils.resolveMessage(arguments, "", null, "unresolved-message"))
                .isEqualTo("unresolved-message");
    }

}
