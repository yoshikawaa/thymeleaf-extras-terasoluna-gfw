package io.github.yoshikawaa.gfw.web.thymeleaf.processor.token.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.junit.Test;
import org.terasoluna.gfw.web.token.transaction.TransactionToken;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenInterceptor;

import io.github.yoshikawaa.gfw.test.engine.TerasolunaGfwTestEngine;
import io.github.yoshikawaa.gfw.test.support.LogbackMockSupport;

public class TransactionTokenTagProcessorTest extends LogbackMockSupport {

    public TransactionTokenTagProcessorTest() {
        super(TransactionTokenTagProcessor.class);
    }

    @Test
    public void testToken() {
        // setup.
        final String template = "<input t:transaction />";
        final TransactionToken token = new TransactionToken("test");
        final Map<String, Object> attributes = Collections
                .singletonMap(TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME, token);

        // execute.
        Element element = new TerasolunaGfwTestEngine().requestAttributes(attributes).parse(template);

        // assert.
        assertThat(element.attr("type")).isEqualTo("hidden");
        assertThat(element.attr("name")).isEqualTo(TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER);
        assertThat(element.attr("value")).isEqualTo(token.getTokenString());
    }

    @Test
    public void testNonToken() {
        // setup.
        final String template = "<input t:transaction />";

        // execute.
        Element element = new TerasolunaGfwTestEngine().parse(template);

        // assert.
        assertThat(element.attr("type")).isNullOrEmpty();
        assertThat(element.attr("name")).isNullOrEmpty();
        assertThat(element.attr("value")).isNullOrEmpty();
        assertLogMessage("cannot found TransactionToken.");
    }

}
