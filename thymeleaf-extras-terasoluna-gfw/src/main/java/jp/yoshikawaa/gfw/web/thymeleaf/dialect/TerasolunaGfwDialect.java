package jp.yoshikawaa.gfw.web.thymeleaf.dialect;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.terasoluna.gfw.web.el.Functions;
import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionEnhancingDialect;
import org.thymeleaf.processor.IProcessor;

import jp.yoshikawaa.gfw.web.thymeleaf.processor.message.MessagesPanelAttrProcessor;
import jp.yoshikawaa.gfw.web.thymeleaf.processor.pagination.PaginationAttrProcessor;
import jp.yoshikawaa.gfw.web.thymeleaf.processor.token.transaction.TransactionTokenAttrProcessor;

public class TerasolunaGfwDialect extends AbstractDialect implements IExpressionEnhancingDialect {

    private static final Logger logger = LoggerFactory.getLogger(TerasolunaGfwDialect.class);

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
    public boolean isLenient() {
        return false;
    }

    @Override
    public Set<IProcessor> getProcessors() {
        Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new MessagesPanelAttrProcessor(dialectPrefix, messageSource));
        processors.add(new TransactionTokenAttrProcessor());
        processors.add(new PaginationAttrProcessor(dialectPrefix));
        return processors;
    }

    @Override
    public Map<String, Object> getAdditionalExpressionObjects(IProcessingContext processingContext) {
        Constructor<Functions> constructor;
        try {
            constructor = Functions.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            return Collections.singletonMap(EXPRESSION_NAME, constructor.newInstance());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
