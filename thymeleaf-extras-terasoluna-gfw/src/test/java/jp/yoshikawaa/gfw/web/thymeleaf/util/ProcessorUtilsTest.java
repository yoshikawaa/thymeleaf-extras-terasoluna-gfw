package jp.yoshikawaa.gfw.web.thymeleaf.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.element.TestStandaloneElementBuilder;

public class ProcessorUtilsTest {

    public ProcessorUtilsTest() {
        new ProcessorUtils();
    }
    
    @Test
    public void testStringAttribute() {
        // setup.
        final String template = "<input t:test='success' />";
        final Element element = TestStandaloneElementBuilder.from(template);

        // execute.
        String result = ProcessorUtils.getAttributeValue(element, "t", "test", "default");
        
        // assert.
        assertThat(result).isEqualTo("success");
    }

    @Test
    public void testStringAttributeDefault() {
        // setup.
        final String template = "<input />";
        final Element element = TestStandaloneElementBuilder.from(template);

        // execute.
        String result = ProcessorUtils.getAttributeValue(element, "t", "test", "default");
        
        // assert.
        assertThat(result).isEqualTo("default");
    }

    @Test
    public void testIntegerAttribute() {
        // setup.
        final String template = "<input t:test='100' />";
        final Element element = TestStandaloneElementBuilder.from(template);

        // execute.
        Integer result = ProcessorUtils.getAttributeValue(element, "t", "test", -1);
        
        // assert.
        assertThat(result).isEqualTo(100);
    }

    @Test
    public void testIntegerAttributeDefault() {
        // setup.
        final String template = "<input />";
        final Element element = TestStandaloneElementBuilder.from(template);

        // execute.
        Integer result = ProcessorUtils.getAttributeValue(element, "t", "test", -1);
        
        // assert.
        assertThat(result).isEqualTo(-1);
    }

    @Test
    public void testBooleanAttribute() {
        // setup.
        final String template = "<input t:test='true' />";
        final Element element = TestStandaloneElementBuilder.from(template);

        // execute.
        Boolean result = ProcessorUtils.getAttributeValue(element, "t", "test", false);
        
        // assert.
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void testBooleanAttributeDefault() {
        // setup.
        final String template = "<input />";
        final Element element = TestStandaloneElementBuilder.from(template);

        // execute.
        Boolean result = ProcessorUtils.getAttributeValue(element, "t", "test", false);
        
        // assert.
        assertThat(result).isEqualTo(false);
    }

}
