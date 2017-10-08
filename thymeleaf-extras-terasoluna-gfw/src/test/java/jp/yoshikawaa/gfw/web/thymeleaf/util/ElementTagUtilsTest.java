package jp.yoshikawaa.gfw.web.thymeleaf.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.thymeleaf.engine.TestElementTagBuilder;
import org.thymeleaf.model.IProcessableElementTag;

public class ElementTagUtilsTest {

    public ElementTagUtilsTest() {
        new ElementTagUtils();
    }
    
    @Test
    public void testStringAttribute() {
        // setup.
        final String template = "<input t:test=\"success\" />";
        final IProcessableElementTag tag = TestElementTagBuilder.standalone(template);

        // execute.
        String result = ElementTagUtils.getAttributeValue(tag, "t", "test", "default");
        
        // assert.
        assertThat(result).isEqualTo("success");
    }

    @Test
    public void testStringAttributeDefault() {
        // setup.
        final String template = "<input />";
        final IProcessableElementTag tag = TestElementTagBuilder.standalone(template);

        // execute.
        String result = ElementTagUtils.getAttributeValue(tag, "t", "test", "default");
        
        // assert.
        assertThat(result).isEqualTo("default");
    }

    @Test
    public void testIntegerAttribute() {
        // setup.
        final String template = "<input t:test=\"100\" />";
        final IProcessableElementTag tag = TestElementTagBuilder.standalone(template);

        // execute.
        Integer result = ElementTagUtils.getAttributeValue(tag, "t", "test", -1);
        
        // assert.
        assertThat(result).isEqualTo(100);
    }

    @Test
    public void testIntegerAttributeDefault() {
        // setup.
        final String template = "<input />";
        final IProcessableElementTag tag = TestElementTagBuilder.standalone(template);

        // execute.
        Integer result = ElementTagUtils.getAttributeValue(tag, "t", "test", -1);
        
        // assert.
        assertThat(result).isEqualTo(-1);
    }

    @Test
    public void testBooleanAttribute() {
        // setup.
        final String template = "<input t:test=\"true\" />";
        final IProcessableElementTag tag = TestElementTagBuilder.standalone(template);

        // execute.
        Boolean result = ElementTagUtils.getAttributeValue(tag, "t", "test", false);
        
        // assert.
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void testBooleanAttributeDefault() {
        // setup.
        final String template = "<input />";
        final IProcessableElementTag tag = TestElementTagBuilder.standalone(template);

        // execute.
        Boolean result = ElementTagUtils.getAttributeValue(tag, "t", "test", false);
        
        // assert.
        assertThat(result).isEqualTo(false);
    }

}
