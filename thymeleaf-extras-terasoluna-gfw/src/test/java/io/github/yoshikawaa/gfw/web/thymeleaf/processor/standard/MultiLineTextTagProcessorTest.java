package io.github.yoshikawaa.gfw.web.thymeleaf.processor.standard;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.junit.Test;

import io.github.yoshikawaa.gfw.test.engine.TerasolunaGfwTestEngine;

public class MultiLineTextTagProcessorTest {

    @Test
    public void testCarriageReturn() {
        // setup.
        final String template = "<span t:mtext='${test}' />";
        final Map<String, Object> variables = Collections.singletonMap("test", "test\rsuccess");

        // execute.
        Element element = new TerasolunaGfwTestEngine().variables(variables).parse(template);

        // assert.
        assertThat(element.html()).contains("test", "<br>", "success");
    }

    @Test
    public void testLineFeed() {
        // setup.
        final String template = "<span t:mtext='${test}' />";
        final Map<String, Object> variables = Collections.singletonMap("test", "test\nsuccess");

        // execute.
        Element element = new TerasolunaGfwTestEngine().variables(variables).parse(template);

        // assert.
        assertThat(element.html()).contains("test", "<br>", "success");
    }

    @Test
    public void testCarriageReturnLineFeed() {
        // setup.
        final String template = "<span t:mtext='${test}' />";
        final Map<String, Object> variables = Collections.singletonMap("test", "test\r\nsuccess");

        // execute.
        Element element = new TerasolunaGfwTestEngine().variables(variables).parse(template);

        // assert.
        assertThat(element.html()).contains("test", "<br>", "success");
    }

    @Test
    public void testNonLineBreak() {
        // setup.
        final String template = "<span t:mtext='${test}' />";
        final Map<String, Object> variables = Collections.singletonMap("test", "testsuccess");

        // execute.
        Element element = new TerasolunaGfwTestEngine().variables(variables).parse(template);

        // assert.
        assertThat(element.html()).contains("test", "success").doesNotContain("<br>");
    }

    @Test
    public void testNonText() {
        // setup.
        final String template = "<span t:mtext='${test}' />";
        final Map<String, Object> variables = Collections.singletonMap("test", "");

        // execute.
        Element element = new TerasolunaGfwTestEngine().variables(variables).parse(template);

        // assert.
        assertThat(element.html()).isEqualTo("");
    }

}
