package io.github.yoshikawaa.gfw.web.thymeleaf.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.thymeleaf.dom.Element;

import io.github.yoshikawaa.gfw.test.engine.TerasolunaGfwTestEngine;
import io.github.yoshikawaa.gfw.test.util.ReflectionUtils;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.ElementUtils;

public class ElementUtilsTest {

    public ElementUtilsTest() {
        ReflectionUtils.newInstance(ElementUtils.class, true);
    }

    @Test
    public void testStringAttribute() {
        // setup.
        final String template = "<input t:test='success' />";
        final Element element = new TerasolunaGfwTestEngine().tag(template);

        // execute.
        String result = ElementUtils.getAttributeValue(element, "t", "test", "default");

        // assert.
        assertThat(result).isEqualTo("success");
    }

    @Test
    public void testStringAttributeDefault() {
        // setup.
        final String template = "<input />";
        final Element element = new TerasolunaGfwTestEngine().tag(template);

        // execute.
        String result = ElementUtils.getAttributeValue(element, "t", "test", "default");

        // assert.
        assertThat(result).isEqualTo("default");
    }

    @Test
    public void testIntegerAttribute() {
        // setup.
        final String template = "<input t:test='100' />";
        final Element element = new TerasolunaGfwTestEngine().tag(template);

        // execute.
        Integer result = ElementUtils.getAttributeValue(element, "t", "test", -1);

        // assert.
        assertThat(result).isEqualTo(100);
    }

    @Test
    public void testIntegerAttributeDefault() {
        // setup.
        final String template = "<input />";
        final Element element = new TerasolunaGfwTestEngine().tag(template);

        // execute.
        Integer result = ElementUtils.getAttributeValue(element, "t", "test", -1);

        // assert.
        assertThat(result).isEqualTo(-1);
    }

    @Test
    public void testBooleanAttribute() {
        // setup.
        final String template = "<input t:test='true' />";
        final Element element = new TerasolunaGfwTestEngine().tag(template);

        // execute.
        Boolean result = ElementUtils.getAttributeValue(element, "t", "test", false);

        // assert.
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void testBooleanAttributeDefault() {
        // setup.
        final String template = "<input />";
        final Element element = new TerasolunaGfwTestEngine().tag(template);

        // execute.
        Boolean result = ElementUtils.getAttributeValue(element, "t", "test", false);

        // assert.
        assertThat(result).isEqualTo(false);
    }

}
