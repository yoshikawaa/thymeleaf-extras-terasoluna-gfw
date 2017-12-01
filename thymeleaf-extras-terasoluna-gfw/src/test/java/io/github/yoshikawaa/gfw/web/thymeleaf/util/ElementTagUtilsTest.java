package io.github.yoshikawaa.gfw.web.thymeleaf.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.thymeleaf.model.IProcessableElementTag;

import io.github.yoshikawaa.gfw.test.engine.TerasolunaGfwTestEngine;
import io.github.yoshikawaa.gfw.test.util.ReflectionUtils;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.ElementTagUtils;

public class ElementTagUtilsTest {

    public ElementTagUtilsTest() {
        ReflectionUtils.newInstance(ElementTagUtils.class, true);
    }

    @Test
    public void testStringAttribute() {
        // setup.
        final String template = "<input t:test=\"success\" />";
        final IProcessableElementTag tag = new TerasolunaGfwTestEngine().tag(template);

        // execute.
        String result = ElementTagUtils.getAttributeValue(tag, "t", "test", "default");

        // assert.
        assertThat(result).isEqualTo("success");
    }

    @Test
    public void testStringAttributeDefault() {
        // setup.
        final String template = "<input />";
        final IProcessableElementTag tag = new TerasolunaGfwTestEngine().tag(template);

        // execute.
        String result = ElementTagUtils.getAttributeValue(tag, "t", "test", "default");

        // assert.
        assertThat(result).isEqualTo("default");
    }

    @Test
    public void testIntegerAttribute() {
        // setup.
        final String template = "<input t:test=\"100\" />";
        final IProcessableElementTag tag = new TerasolunaGfwTestEngine().tag(template);

        // execute.
        Integer result = ElementTagUtils.getAttributeValue(tag, "t", "test", -1);

        // assert.
        assertThat(result).isEqualTo(100);
    }

    @Test
    public void testIntegerAttributeDefault() {
        // setup.
        final String template = "<input />";
        final IProcessableElementTag tag = new TerasolunaGfwTestEngine().tag(template);

        // execute.
        Integer result = ElementTagUtils.getAttributeValue(tag, "t", "test", -1);

        // assert.
        assertThat(result).isEqualTo(-1);
    }

    @Test
    public void testBooleanAttribute() {
        // setup.
        final String template = "<input t:test=\"true\" />";
        final IProcessableElementTag tag = new TerasolunaGfwTestEngine().tag(template);

        // execute.
        Boolean result = ElementTagUtils.getAttributeValue(tag, "t", "test", false);

        // assert.
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void testBooleanAttributeDefault() {
        // setup.
        final String template = "<input />";
        final IProcessableElementTag tag = new TerasolunaGfwTestEngine().tag(template);

        // execute.
        Boolean result = ElementTagUtils.getAttributeValue(tag, "t", "test", false);

        // assert.
        assertThat(result).isEqualTo(false);
    }

}
