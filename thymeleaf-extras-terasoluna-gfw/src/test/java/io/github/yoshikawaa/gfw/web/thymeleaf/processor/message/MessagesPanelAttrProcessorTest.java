package io.github.yoshikawaa.gfw.web.thymeleaf.processor.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.junit.Test;
import org.springframework.context.support.StaticMessageSource;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;

import io.github.yoshikawaa.gfw.test.engine.TerasolunaGfwTestEngine;
import io.github.yoshikawaa.gfw.test.support.LogbackMockSupport;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.message.MessagesPanelAttrProcessor;

public class MessagesPanelAttrProcessorTest extends LogbackMockSupport {

    public MessagesPanelAttrProcessorTest() {
        super(MessagesPanelAttrProcessor.class);
    }

    @Test
    public void testMessage() {
        // setup.
        final String template = "<div t:messages-panel='' />";
        final ResultMessages resultMessages = ResultMessages.success()
                .add(ResultMessage.fromCode("test1"))
                .add(ResultMessage.fromCode("test2"))
                .add(ResultMessage.fromCode("test3"));
        final Map<String, Object> attributes = Collections.singletonMap(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                resultMessages);

        // execute.
        Element element = new TerasolunaGfwTestEngine().requestAttributes(attributes).parse(template);

        // assert.
        assertThat(element.attr("class")).isEqualTo("alert alert-success");
        assertThat(element.children().outerHtml()).containsSequence("<ul>", "<li>test message 1.</li>",
                "<li>test message 2.</li>", "<li>test message 3.</li>", "</ul>");
    }

    @Test
    public void testMessage2() {
        // setup.
        final String template = "<div t:messages-panel='' t:panel-class-name='' t:outer-element='' t:disable-html-escape='true' />";
        final ResultMessages resultMessages = ResultMessages.success()
                .add(ResultMessage.fromText("<span>test1</span>"))
                .add(ResultMessage.fromText("<span>test2</span>"))
                .add(ResultMessage.fromText("<span>test3</span>"));
        final Map<String, Object> attributes = Collections.singletonMap(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                resultMessages);

        // execute.
        Element element = new TerasolunaGfwTestEngine().requestAttributes(attributes).parse(template);

        // assert.
        assertThat(element.attr("class")).isEqualTo("alert-success");
        assertThat(element.children().outerHtml()).containsSequence("<li><span>test1</span></li>",
                "<li><span>test2</span></li>", "<li><span>test3</span></li>");
    }

    @Test
    public void testMessageNotFound() {
        // setup.
        final String template = "<div t:messages-panel='' />";

        // execute.
        Element element = new TerasolunaGfwTestEngine().parse(template);

        // assert.
        assertThat(element.attr("class")).isNullOrEmpty();
        assertThat(element.children()).isNullOrEmpty();
        assertLogMessage("cannot found ResultMessages.");
    }

    @Test
    public void testExpressionString() {
        // setup.
        final String template = "<div t:messages-panel='${messages}' t:messages-type='warn' />";
        final String resultMessages = "test message.";
        final Map<String, Object> variables = Collections.singletonMap("messages", resultMessages);

        // execute.
        Element element = new TerasolunaGfwTestEngine().variables(variables).parse(template);

        // assert.
        assertThat(element.attr("class")).isEqualTo("alert alert-warn");
        assertThat(element.children().outerHtml()).containsSequence("<ul>", "<li>test message.</li>", "</ul>");
    }

    @Test
    public void testExpressionThrowable() {
        // setup.
        final String template = "<div t:messages-panel='${messages}' t:messages-type='warn' />";
        final Throwable resultMessages = new Exception("test message.");
        final Map<String, Object> variables = Collections.singletonMap("messages", resultMessages);

        // execute.
        Element element = new TerasolunaGfwTestEngine().variables(variables).parse(template);

        // assert.
        assertThat(element.attr("class")).isEqualTo("alert alert-warn");
        assertThat(element.children().outerHtml()).containsSequence("<ul>", "<li>test message.</li>", "</ul>");
    }

    @Test
    public void testExpressionOtherType() {
        // setup.
        final String template = "<div t:messages-panel='${messages}' t:messages-type='warn' />";
        final Integer resultMessages = 100;
        final Map<String, Object> variables = Collections.singletonMap("messages", resultMessages);

        // execute.
        Element element = new TerasolunaGfwTestEngine().variables(variables).parse(template);

        // assert.
        assertThat(element.attr("class")).isEqualTo("alert alert-warn");
        assertThat(element.children().outerHtml()).containsSequence("<ul>", "<li>100</li>", "</ul>");
    }

    @Test
    public void testMessageWithoutMessageSource() {
        // execute and assert.
        assertThatThrownBy(() -> {
            new MessagesPanelAttrProcessor("t", null);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage("messageSource must not be null.");
    }

    @Test
    public void testPrecedence() {
        MessagesPanelAttrProcessor processor = new MessagesPanelAttrProcessor("t", new StaticMessageSource());
        assertThat(processor.getPrecedence()).isEqualTo(1200);
    }

}
