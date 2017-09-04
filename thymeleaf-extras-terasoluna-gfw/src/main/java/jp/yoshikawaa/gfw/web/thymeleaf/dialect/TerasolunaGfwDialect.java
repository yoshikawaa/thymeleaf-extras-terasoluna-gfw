package jp.yoshikawaa.gfw.web.thymeleaf.dialect;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import jp.yoshikawaa.gfw.web.thymeleaf.processor.message.MessagesPanelAttributeProcessor;
import jp.yoshikawaa.gfw.web.thymeleaf.processor.pagination.PaginationAttributeProcessor;
import jp.yoshikawaa.gfw.web.thymeleaf.processor.token.transaction.TransactionTokenAttributeProcessor;

public class TerasolunaGfwDialect extends AbstractProcessorDialect implements IExpressionObjectDialect {

    private static final Logger logger = LoggerFactory.getLogger(TerasolunaGfwDialect.class);

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
        processors.add(new MessagesPanelAttributeProcessor(dialectPrefix, messageSource));
        processors.add(new TransactionTokenAttributeProcessor(dialectPrefix));
        processors.add(new PaginationAttributeProcessor(dialectPrefix));
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
                if (expressionObjectName.equals(EXPRESSION_NAME)) {
                    try {
                        Constructor<Functions> constructor = Functions.class.getDeclaredConstructor();
                        constructor.setAccessible(true);
                        return constructor.newInstance();
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                return null;
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return false;
            }
        };
    }
}
