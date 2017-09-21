package jp.yoshikawaa.gfw.web.thymeleaf.dialect;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.context.support.StaticMessageSource;
import org.terasoluna.gfw.web.el.Functions;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;

import jp.yoshikawaa.gfw.web.thymeleaf.processor.message.MessagesPanelAttributeProcessor;
import jp.yoshikawaa.gfw.web.thymeleaf.processor.pagination.PaginationAttributeProcessor;
import jp.yoshikawaa.gfw.web.thymeleaf.processor.token.transaction.TransactionTokenAttributeProcessor;

public class TerasolunaGfwDialectTest {

    @Test
    public void testDialect() {
        // setup.
        String dialectPrefix = "terasoluna";

        // execute.
        TerasolunaGfwDialect dialect = new TerasolunaGfwDialect(dialectPrefix, new StaticMessageSource());

        // assert.
        assertThat(dialect.getPrefix()).isEqualTo(dialectPrefix);

        Set<IProcessor> processors = dialect.getProcessors(dialectPrefix);
        assertThat(processors).hasSize(4);
        List<Class<?>> types = processors.stream().map(p -> p.getClass()).collect(Collectors.toList());
        assertThat(types).containsOnly(MessagesPanelAttributeProcessor.class, TransactionTokenAttributeProcessor.class,
                PaginationAttributeProcessor.class, StandardXmlNsTagProcessor.class);

        IExpressionObjectFactory factory = dialect.getExpressionObjectFactory();
        factory.getAllExpressionObjectNames().stream().forEach(n -> {
            assertThat(factory.buildObject(null, n)).isInstanceOf(Functions.class);
            assertThat(factory.isCacheable(n)).isFalse();
        });
    }

    @Test
    public void testDialectPrefix() {
        // execute.
        TerasolunaGfwDialect dialect = new TerasolunaGfwDialect(new StaticMessageSource());

        // assert.
        assertThat(dialect.getPrefix()).isEqualTo("t");
    }

}
