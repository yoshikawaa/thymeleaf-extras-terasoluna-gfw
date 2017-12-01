package io.github.yoshikawaa.gfw.web.thymeleaf.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.thymeleaf.context.IEngineContext;

import io.github.yoshikawaa.gfw.test.engine.TerasolunaGfwTestEngine;
import io.github.yoshikawaa.gfw.test.util.ReflectionUtils;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.message.MessagesPanelTagProcessor;

public class MessageUtilsTest {

    public MessageUtilsTest() {
        ReflectionUtils.newInstance(MessageUtils.class, true);
    }

    @Test
    public void testResolved() {
        // setup.
        final String template = "<input />";
        final IEngineContext context = new TerasolunaGfwTestEngine().context(template);

        // execute and assert.
        assertThat(MessageUtils.resolveMessage(MessagesPanelTagProcessor.class, context, "test1", null,
                "unresolved-message")).isEqualTo("test message 1.");
    }

    @Test
    public void testResolvedWithArgs() {
        // setup.
        final String template = "<input />";
        final IEngineContext context = new TerasolunaGfwTestEngine().context(template);

        // execute and assert.
        assertThat(MessageUtils.resolveMessage(MessagesPanelTagProcessor.class, context, "args1",
                new String[] { "test1", "test2", "test3" }, "unresolved-message"))
                        .isEqualTo("args [test1,test2,test3].");
    }

    @Test
    public void testNotResolved() {
        // setup.
        final String template = "<input />";
        final IEngineContext context = new TerasolunaGfwTestEngine().context(template);

        // execute and assert.
        assertThat(MessageUtils.resolveMessage(MessagesPanelTagProcessor.class, context, "test0", null,
                "unresolved-message")).isEqualTo("unresolved-message");
    }

    @Test
    public void testCodeEmpty() {
        // setup.
        final String template = "<input />";
        final IEngineContext context = new TerasolunaGfwTestEngine().context(template);

        // execute and assert.
        assertThat(
                MessageUtils.resolveMessage(MessagesPanelTagProcessor.class, context, "", null, "unresolved-message"))
                        .isEqualTo("unresolved-message");
    }

}
