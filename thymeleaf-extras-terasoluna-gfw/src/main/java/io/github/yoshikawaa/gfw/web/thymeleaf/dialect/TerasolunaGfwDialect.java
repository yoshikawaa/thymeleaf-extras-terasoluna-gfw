package io.github.yoshikawaa.gfw.web.thymeleaf.dialect;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionEnhancingDialect;
import org.thymeleaf.processor.IProcessor;

import io.github.yoshikawaa.gfw.web.thymeleaf.expression.Query;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.message.MessagesPanelAttrProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.pagination.PaginationAttrProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.standard.MultiLineTextAttrProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.token.transaction.TransactionTokenAttrProcessor;

public class TerasolunaGfwDialect extends AbstractDialect implements IExpressionEnhancingDialect {

    private static final String DIALECT_PREFIX = "t";
    private static final String EXPRESSION_NAME = "query";

    private final String dialectPrefix;

    public TerasolunaGfwDialect() {
        this(DIALECT_PREFIX);
    }

    public TerasolunaGfwDialect(String dialectPrefix) {
        super();
        this.dialectPrefix = dialectPrefix;
    }

    @Override
    public String getPrefix() {
        return dialectPrefix;
    }

    @Override
    public Set<IProcessor> getProcessors() {
        Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new MessagesPanelAttrProcessor(dialectPrefix));
        processors.add(new PaginationAttrProcessor(dialectPrefix));
        processors.add(new TransactionTokenAttrProcessor(dialectPrefix));
        processors.add(new MultiLineTextAttrProcessor(dialectPrefix));
        return processors;
    }

    @Override
    public Map<String, Object> getAdditionalExpressionObjects(IProcessingContext processingContext) {
        return Collections.singletonMap(EXPRESSION_NAME, new Query());
    }

}
