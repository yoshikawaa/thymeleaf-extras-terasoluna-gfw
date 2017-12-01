package io.github.yoshikawaa.gfw.web.thymeleaf.dialect;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import io.github.yoshikawaa.gfw.web.thymeleaf.expression.Query;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.message.MessagesPanelTagProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.pagination.PaginationTagProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.standard.MultiLineTextTagProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.token.transaction.TransactionTokenTagProcessor;

public class TerasolunaGfwDialect extends AbstractProcessorDialect implements IExpressionObjectDialect {

    private static final String DIALECT_NAME = "TERASOLUNA GFW Dialect";
    private static final String DIALECT_PREFIX = "t";
    private static final Set<String> EXPRESSION_NAMES = Collections.singleton("query");

    public TerasolunaGfwDialect() {
        this(DIALECT_PREFIX);
    }

    public TerasolunaGfwDialect(String dialectPrefix) {
        super(DIALECT_NAME, dialectPrefix, StandardDialect.PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new MessagesPanelTagProcessor(dialectPrefix));
        processors.add(new PaginationTagProcessor(dialectPrefix));
        processors.add(new TransactionTokenTagProcessor(dialectPrefix));
        processors.add(new MultiLineTextTagProcessor(dialectPrefix));
        processors.add(new StandardXmlNsTagProcessor(TemplateMode.HTML, dialectPrefix));
        return processors;
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return new IExpressionObjectFactory() {
            @Override
            public Set<String> getAllExpressionObjectNames() {
                return EXPRESSION_NAMES;
            }

            @Override
            public Object buildObject(IExpressionContext context, String expressionObjectName) {
                return new Query();
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return true;
            }
        };
    }

}
