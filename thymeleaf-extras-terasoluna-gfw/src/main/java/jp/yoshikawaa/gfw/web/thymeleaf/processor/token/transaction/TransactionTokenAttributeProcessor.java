package jp.yoshikawaa.gfw.web.thymeleaf.processor.token.transaction;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.terasoluna.gfw.web.token.transaction.TransactionToken;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenInterceptor;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;

import jp.yoshikawaa.gfw.web.thymeleaf.processor.AbstractHtmlAttributeProcessor;

public class TransactionTokenAttributeProcessor extends AbstractHtmlAttributeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TransactionTokenAttributeProcessor.class);

    private static final String ATTRIBUTE_NAME = "transaction-token";
    private static final int PRECEDENCE = 12000;

    public TransactionTokenAttributeProcessor(String dialectPrefix) {
        super(dialectPrefix, ATTRIBUTE_NAME, PRECEDENCE);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {

        // find token.
        TransactionToken nextToken = getTransactionToken();
        
        // exist token?
        if (nextToken == null) {
            logger.debug("cannot found TransactionToken.");
            return;
        }

        // build element.
        buildElement(structureHandler, nextToken);
    }

    private TransactionToken getTransactionToken() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        return (TransactionToken) request.getAttribute(TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME);
    }

    private void buildElement(IElementTagStructureHandler structureHandler, TransactionToken nextToken) {

        structureHandler.setAttribute("type", "hidden");
        structureHandler.setAttribute("name", TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER);
        structureHandler.setAttribute("value", nextToken.getTokenString());
    }

}