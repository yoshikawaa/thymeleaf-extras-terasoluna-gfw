package jp.yoshikawaa.gfw.web.thymeleaf.dialect;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.context.support.StaticMessageSource;
import org.terasoluna.gfw.web.el.Functions;
import org.thymeleaf.processor.IProcessor;

import jp.yoshikawaa.gfw.web.thymeleaf.processor.message.MessagesPanelAttrProcessor;
import jp.yoshikawaa.gfw.web.thymeleaf.processor.pagination.PaginationAttrProcessor;
import jp.yoshikawaa.gfw.web.thymeleaf.processor.token.transaction.TransactionTokenAttrProcessor;

public class TerasolunaGfwDialectTest {

    @Test
    public void testDialect() {
        // setup.
        String dialectPrefix = "terasoluna";

        // execute.
        TerasolunaGfwDialect dialect = new TerasolunaGfwDialect(dialectPrefix, new StaticMessageSource());

        // assert.
        assertThat(dialect.getPrefix()).isEqualTo(dialectPrefix);

        Set<IProcessor> processors = dialect.getProcessors();
        assertThat(processors).hasSize(3);
        List<Class<?>> types = processors.stream().map(p -> p.getClass()).collect(Collectors.toList());
        assertThat(types).containsOnly(MessagesPanelAttrProcessor.class, TransactionTokenAttrProcessor.class,
                PaginationAttrProcessor.class);

        dialect.getAdditionalExpressionObjects(null).entrySet().stream().forEach(e -> {
            assertThat(e.getKey()).isEqualTo("f");
            assertThat(e.getValue()).isInstanceOf(Functions.class);
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
