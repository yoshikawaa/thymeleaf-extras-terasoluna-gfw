package io.github.yoshikawaa.gfw.web.thymeleaf.dialect;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.thymeleaf.processor.IProcessor;

import io.github.yoshikawaa.gfw.web.thymeleaf.expression.Query;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.message.MessagesPanelAttrProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.pagination.PaginationAttrProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.standard.MultiLineTextAttrProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.token.transaction.TransactionTokenAttrProcessor;

public class TerasolunaGfwDialectTest {

    @Test
    public void testDialect() {
        // execute.
        TerasolunaGfwDialect dialect = new TerasolunaGfwDialect();

        // assert.
        assertThat(dialect.getPrefix()).isEqualTo("t");

        Set<IProcessor> processors = dialect.getProcessors();
        assertThat(processors).hasSize(4);
        List<Class<?>> types = processors.stream().map(p -> p.getClass()).collect(Collectors.toList());
        assertThat(types).containsOnly(MessagesPanelAttrProcessor.class, PaginationAttrProcessor.class,
                TransactionTokenAttrProcessor.class, MultiLineTextAttrProcessor.class);

        dialect.getAdditionalExpressionObjects(null).entrySet().stream().forEach(e -> {
            assertThat(e.getKey()).isEqualTo("query");
            assertThat(e.getValue()).isInstanceOf(Query.class);
        });
    }

    @Test
    public void testDialectPrefix() {
        // setup.
        String dialectPrefix = "terasoluna";

        // execute.
        TerasolunaGfwDialect dialect = new TerasolunaGfwDialect(dialectPrefix);

        // assert.
        assertThat(dialect.getPrefix()).isEqualTo(dialectPrefix);
    }

}
