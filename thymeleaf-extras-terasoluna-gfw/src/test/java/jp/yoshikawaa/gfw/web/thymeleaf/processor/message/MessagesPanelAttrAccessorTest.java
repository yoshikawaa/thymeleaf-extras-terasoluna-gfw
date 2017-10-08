package jp.yoshikawaa.gfw.web.thymeleaf.processor.message;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.element.TestElementBuilder;

public class MessagesPanelAttrAccessorTest {

    @Test
    public void testAccessor() {
        // setup.
        final StringBuilder template = new StringBuilder("<div t:messages-panel='' ");
        template.append("t:panel-class-name='message' ");
        template.append("t:panel-type-class-prefix='message-' ");
        template.append("t:messages-type='warn' ");
        template.append("t:outer-element='tr' ");
        template.append("t:inner-element='td' ");
        template.append("t:disable-html-escape='true' ");
        template.append("/>");

        final Element element = TestElementBuilder.standalone(template.toString());

        // execute.
        MessagesPanelAttrAccessor accessor = new MessagesPanelAttrAccessor(element, "t");
        accessor.removeAttributes(element);

        // assert.
        assertThat(accessor.getPanelClassName()).isEqualTo("message");
        assertThat(accessor.getPanelTypeClassPrefix()).isEqualTo("message-");
        assertThat(accessor.getMessagesType()).isEqualTo("warn");
        assertThat(accessor.getPanelTypeClass("message")).isEqualTo("message-warn");
        assertThat(accessor.getOuterElement()).isEqualTo("tr");
        assertThat(accessor.getInnerElement()).isEqualTo("td");
        assertThat(accessor.isDisableHtmlEscape()).isTrue();
        String[] expectedAttributes = { "t:panel-class-name", "t:panel-type-class-prefix", "t:messages-type",
                "t:outer-element", "t:inner-element", "t:disable-html-escape" };
        for (String expectedAttribute : expectedAttributes) {
            assertThat(element.hasAttribute(expectedAttribute)).isFalse();
        }
    }

    @Test
    public void testAccessor2() {
        // setup.
        final StringBuilder template = new StringBuilder("<div t:messages-panel='' ");
        template.append("/>");

        final Element element = TestElementBuilder.standalone(template.toString());

        // execute.
        MessagesPanelAttrAccessor accessor = new MessagesPanelAttrAccessor(element, "t");
        accessor.removeAttributes(element);

        // assert.
        assertThat(accessor.getPanelTypeClass("message")).isEqualTo("alert-");
    }

}
