package io.github.yoshikawaa.gfw.web.thymeleaf.processor.message;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.thymeleaf.model.IProcessableElementTag;

import io.github.yoshikawaa.gfw.test.engine.TerasolunaGfwTestEngine;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.message.MessagesPanelTagAccessor;

public class MessagesPanelTagAccessorTest {

    @Test
    public void testAccessor() {
        // setup.
        final StringBuilder template = new StringBuilder("<div t:messages-panel ");
        template.append("t:panel-class-name='message' ");
        template.append("t:panel-type-class-prefix='message-' ");
        template.append("t:messages-type='warn' ");
        template.append("t:outer-element='tr' ");
        template.append("t:inner-element='td' ");
        template.append("t:disable-html-escape='true' ");
        template.append("/>");

        final IProcessableElementTag tag = new TerasolunaGfwTestEngine().tag(template.toString());

        // execute.
        MessagesPanelTagAccessor accessor = new MessagesPanelTagAccessor(tag, "t");

        // assert.
        assertThat(accessor.getPanelClassName()).isEqualTo("message");
        assertThat(accessor.getPanelTypeClassPrefix()).isEqualTo("message-");
        assertThat(accessor.getMessagesType()).isEqualTo("warn");
        assertThat(accessor.getPanelTypeClass("message")).isEqualTo("message-warn");
        assertThat(accessor.getOuterElement()).isEqualTo("tr");
        assertThat(accessor.getInnerElement()).isEqualTo("td");
        assertThat(accessor.isDisableHtmlEscape()).isTrue();
        assertThat(accessor.getAttributeNames()).containsSequence("panel-class-name", "panel-type-class-prefix",
                "messages-type", "outer-element", "inner-element", "disable-html-escape");
    }

    @Test
    public void testAccessor2() {
        // setup.
        final StringBuilder template = new StringBuilder("<div t:messages-panel ");
        template.append("/>");

        final IProcessableElementTag tag = new TerasolunaGfwTestEngine().tag(template.toString());

        // execute.
        MessagesPanelTagAccessor accessor = new MessagesPanelTagAccessor(tag, "t");

        // assert.
        assertThat(accessor.getPanelTypeClass("message")).isEqualTo("alert-");
    }

}
