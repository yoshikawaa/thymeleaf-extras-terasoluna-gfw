package io.github.yoshikawaa.gfw.web.thymeleaf.dialect;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;

import io.github.yoshikawaa.gfw.web.thymeleaf.expression.Query;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.message.MessagesPanelTagProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.pagination.PaginationTagProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.standard.MultiLineTextTagProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.token.transaction.TransactionTokenTagProcessor;

public class TerasolunaGfwDialectTest {

    @Test
    public void testDialect() {
        // setup.
        String dialectPrefix = "t";

        // execute.
        TerasolunaGfwDialect dialect = new TerasolunaGfwDialect();

        // assert.
        assertThat(dialect.getPrefix()).isEqualTo(dialectPrefix);

        Set<IProcessor> processors = dialect.getProcessors(dialectPrefix);
        assertThat(processors).hasSize(5);
        List<Class<?>> types = processors.stream().map(p -> p.getClass()).collect(Collectors.toList());
        assertThat(types).containsOnly(MessagesPanelTagProcessor.class, PaginationTagProcessor.class,
                TransactionTokenTagProcessor.class, MultiLineTextTagProcessor.class, StandardXmlNsTagProcessor.class);

        IExpressionObjectFactory factory = dialect.getExpressionObjectFactory();
        factory.getAllExpressionObjectNames().stream().forEach(n -> {
            assertThat(n).isEqualTo("query");
            assertThat(factory.buildObject(null, n)).isInstanceOf(Query.class);
            assertThat(factory.isCacheable(n)).isTrue();
        });
    }

    @Test
    public void testDialectPrefix() {
        // setup.
        String dialectPrefix = "terasoluna";

        // execute.
        TerasolunaGfwDialect dialect = new TerasolunaGfwDialect("terasoluna");

        // assert.
        assertThat(dialect.getPrefix()).isEqualTo(dialectPrefix);
    }

}
