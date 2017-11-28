package io.github.yoshikawaa.gfw.web.thymeleaf.dialect;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.context.MessageSource;
import org.terasoluna.gfw.web.el.Functions;
import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionEnhancingDialect;
import org.thymeleaf.processor.IProcessor;

import com.google.common.collect.ImmutableMap;

import io.github.yoshikawaa.gfw.web.thymeleaf.processor.message.MessagesPanelAttrProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.pagination.PaginationAttrProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.token.transaction.TransactionTokenAttrProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.ReflectionUtils;

public class TerasolunaGfwDialect extends AbstractDialect implements IExpressionEnhancingDialect {

    private static final String DIALECT_PREFIX = "t";
    private static final String EXPRESSION_NAME = "f";

    private final String dialectPrefix;
    private final MessageSource messageSource;

    public TerasolunaGfwDialect(MessageSource messageSource) {
        this(DIALECT_PREFIX, messageSource);
    }

    public TerasolunaGfwDialect(String dialectPrefix, MessageSource messageSource) {
        super();
        this.dialectPrefix = dialectPrefix;
        this.messageSource = messageSource;
    }

    @Override
    public String getPrefix() {
        return dialectPrefix;
    }

    @Override
    public Set<IProcessor> getProcessors() {
        Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new MessagesPanelAttrProcessor(dialectPrefix, messageSource));
        processors.add(new TransactionTokenAttrProcessor(dialectPrefix));
        processors.add(new PaginationAttrProcessor(dialectPrefix));
        return processors;
    }

    @Override
    public Map<String, Object> getAdditionalExpressionObjects(IProcessingContext processingContext) {
        return ImmutableMap.of(EXPRESSION_NAME, ReflectionUtils.newInstance(Functions.class, true));
    }
}
