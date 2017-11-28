package io.github.yoshikawaa.gfw.web.thymeleaf.dialect;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.MessageSource;
import org.terasoluna.gfw.web.el.Functions;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import io.github.yoshikawaa.gfw.web.thymeleaf.processor.message.MessagesPanelTagProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.pagination.PaginationTagProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.token.transaction.TransactionTokenProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.ReflectionUtils;

public class TerasolunaGfwDialect extends AbstractProcessorDialect implements IExpressionObjectDialect {

    private static final String DIALECT_NAME = "TERASOLUNA GFW Dialect";
    private static final String DIALECT_PREFIX = "t";
    private static final String EXPRESSION_NAME = "f";

    private final MessageSource messageSource;

    public TerasolunaGfwDialect(MessageSource messageSource) {
        this(DIALECT_PREFIX, messageSource);
    }

    public TerasolunaGfwDialect(String dialectPrefix, MessageSource messageSource) {
        super(DIALECT_NAME, dialectPrefix, StandardDialect.PROCESSOR_PRECEDENCE);
        this.messageSource = messageSource;
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new MessagesPanelTagProcessor(dialectPrefix, messageSource));
        processors.add(new TransactionTokenProcessor(dialectPrefix));
        processors.add(new PaginationTagProcessor(dialectPrefix));
        processors.add(new StandardXmlNsTagProcessor(TemplateMode.HTML, dialectPrefix));
        return processors;
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return new IExpressionObjectFactory() {
            @Override
            public Set<String> getAllExpressionObjectNames() {
                return Collections.singleton(EXPRESSION_NAME);
            }

            @Override
            public Object buildObject(IExpressionContext context, String expressionObjectName) {
                return ReflectionUtils.newInstance(Functions.class, true);
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return false;
            }
        };
    }
}
