package jp.yoshikawaa.gfw.web.thymeleaf.processor.token.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.terasoluna.gfw.web.token.transaction.TransactionToken;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenInterceptor;
import org.thymeleaf.processor.element.TestElementTagStructureHandler;

import jp.yoshikawaa.gfw.web.thymeleaf.processor.TerasolunaGfwAttributeProcessorTestSupport;

public class TransactionTokenAttributeProcessorTest extends TerasolunaGfwAttributeProcessorTestSupport {

    public TransactionTokenAttributeProcessorTest() {
        super(TransactionTokenAttributeProcessor.class);
    }

    @Test
    public void testToken() {
        // setup.
        final String template = "<input t:transaction-token />";
        final TransactionToken token = new TransactionToken("test");
        final Map<String, Object> attributes = Collections
                .singletonMap(TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME, token);

        // execute.
        TestElementTagStructureHandler structureHandler = process(template, attributes, null);

        // assert.
        assertThat(structureHandler.getAttributes()).containsEntry("type", "hidden")
                .containsEntry("name", TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER)
                .containsEntry("value", token.getTokenString());
    }

    @Test
    public void testNonToken() {
        // setup.
        final String template = "<input t:transaction-token />";

        // execute.
        TestElementTagStructureHandler structureHandler = process(template);

        // assert.
        assertThat(structureHandler.getAttributes()).doesNotContainKeys("type", "name", "value");
        assertLogMessage("cannot found TransactionToken.");
    }

}
