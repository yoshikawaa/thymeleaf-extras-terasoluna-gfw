package jp.yoshikawaa.gfw.web.thymeleaf.processor.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.thymeleaf.dom.element.TestElementWrapper;

import jp.yoshikawaa.gfw.test.support.TerasolunaGfwAttrProcessorTestSupport;

public class MessagesPanelAttrProcessorTest extends TerasolunaGfwAttrProcessorTestSupport {

    public MessagesPanelAttrProcessorTest() {
        super(MessagesPanelAttrProcessor.class);
    }

    @Test
    public void testMessage() {
        // setup.
        final String template = "<div t:messages-panel='' />";
        final ResultMessages resultMessages = ResultMessages.success().add(ResultMessage.fromCode("test1"))
                .add(ResultMessage.fromCode("test2")).add(ResultMessage.fromCode("test3"));
        final Map<String, Object> attributes = Collections.singletonMap(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                resultMessages);

        // execute.
        TestElementWrapper elementWrapper = process(template, attributes, null);

        // assert.
        assertThat(elementWrapper.getAttributes()).containsEntry("class", "alert alert-success");
        assertThat(elementWrapper.getBody()).containsSequence("<ul>", "<li>test message 1.</li>",
                "<li>test message 2.</li>", "<li>test message 3.</li>", "</ul>");
    }

    @Test
    public void testMessage2() {
        // setup.
        final String template = "<div t:messages-panel='' t:panel-class-name='' t:outer-element='' t:disable-html-escape='true' />";
        final ResultMessages resultMessages = ResultMessages.success().add(ResultMessage.fromText("<span>test1</span>"))
                .add(ResultMessage.fromText("<span>test2</span>")).add(ResultMessage.fromText("<span>test3</span>"));
        final Map<String, Object> attributes = Collections.singletonMap(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME,
                resultMessages);

        // execute.
        TestElementWrapper elementWrapper = process(template, attributes, null);

        // assert.
        assertThat(elementWrapper.getAttributes()).containsEntry("class", "alert-success");
        assertThat(elementWrapper.getBody()).containsSequence("<li><span>test1</span></li>",
                "<li><span>test2</span></li>", "<li><span>test3</span></li>");
    }

    @Test
    public void testMessageNotFound() {
        // setup.
        final String template = "<div t:messages-panel='' />";

        // execute.
        TestElementWrapper elementWrapper = process(template);

        // assert.
        assertThat(elementWrapper.getAttributes()).doesNotContainKey("class");
        assertThat(elementWrapper.getBody()).isEmpty();
        assertLogMessage("cannot found ResultMessages.");
    }

    @Test
    public void testExpressionString() {
        // setup.
        final String template = "<div t:messages-panel='${messages}' t:messages-type='warn' />";
        final String resultMessages = "test message.";
        final Map<String, Object> variables = Collections.singletonMap("messages", resultMessages);

        // execute.
        TestElementWrapper elementWrapper = process(template, null, variables);

        // assert.
        assertThat(elementWrapper.getAttributes()).containsEntry("class", "alert alert-warn");
        assertThat(elementWrapper.getBody()).containsSequence("<ul>", "<li>test message.</li>", "</ul>");
    }

    @Test
    public void testExpressionThrowable() {
        // setup.
        final String template = "<div t:messages-panel='${messages}' t:messages-type='warn' />";
        final Throwable resultMessages = new Exception("test message.");
        final Map<String, Object> variables = Collections.singletonMap("messages", resultMessages);

        // execute.
        TestElementWrapper elementWrapper = process(template, null, variables);

        // assert.
        assertThat(elementWrapper.getAttributes()).containsEntry("class", "alert alert-warn");
        assertThat(elementWrapper.getBody()).containsSequence("<ul>", "<li>test message.</li>", "</ul>");
    }

    @Test
    public void testExpressionOtherType() {
        // setup.
        final String template = "<div t:messages-panel='${messages}' t:messages-type='warn' />";
        final Integer resultMessages = 100;
        final Map<String, Object> variables = Collections.singletonMap("messages", resultMessages);

        // execute.
        TestElementWrapper elementWrapper = process(template, null, variables);

        // assert.
        assertThat(elementWrapper.getAttributes()).containsEntry("class", "alert alert-warn");
        assertThat(elementWrapper.getBody()).containsSequence("<ul>", "<li>100</li>", "</ul>");
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
        MessagesPanelAttrProcessor processor = getProcessor(MessagesPanelAttrProcessor.class);
        assertThat(processor.getPrecedence()).isEqualTo(1200);
    }

}
