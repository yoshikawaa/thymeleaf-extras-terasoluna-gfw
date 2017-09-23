package jp.yoshikawaa.gfw.web.thymeleaf.processor.token.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.terasoluna.gfw.web.token.transaction.TransactionToken;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenInterceptor;
import org.thymeleaf.dom.element.TestElementWrapper;

import jp.yoshikawaa.gfw.web.thymeleaf.processor.TerasolunaGfwAttrProcessorTestSupport;

public class TransactionTokenAttrProcessorTest extends TerasolunaGfwAttrProcessorTestSupport {

    public TransactionTokenAttrProcessorTest() {
        super(TransactionTokenAttrProcessor.class);
    }

    @Test
    public void testToken() {
        // setup.
        final String template = "<input t:transaction-token='' />";
        final TransactionToken token = new TransactionToken("test");
        final Map<String, Object> attributes = Collections
                .singletonMap(TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME, token);

        // execute.
        TestElementWrapper elementWrapper = process(template, attributes, null);

        // assert.
        assertThat(elementWrapper.getAttributes()).containsEntry("type", "hidden")
                .containsEntry("name", TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER)
                .containsEntry("value", token.getTokenString());
    }

    @Test
    public void testNonToken() {
        // setup.
        final String template = "<input t:transaction-token='' />";

        // execute.
        TestElementWrapper elementWrapper = process(template);

        // assert.
        assertThat(elementWrapper.getAttributes()).doesNotContainKeys("type", "name", "value");
        assertLogMessage("cannot found TransactionToken.");
    }

    @Test
    public void testPrecedence() {
        TransactionTokenAttrProcessor processor = getProcessor(TransactionTokenAttrProcessor.class);
        assertThat(processor.getPrecedence()).isEqualTo(1300);
    }

}
