package jp.yoshikawaa.gfw.web.thymeleaf.processor.token.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasoluna.gfw.web.token.transaction.TransactionToken;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenInterceptor;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import jp.yoshikawaa.gfw.web.thymeleaf.processor.AbstractRemovalAttributeTagProcessor;
import jp.yoshikawaa.gfw.web.thymeleaf.util.ContextUtils;

public class TransactionTokenProcessor extends AbstractRemovalAttributeTagProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TransactionTokenProcessor.class);

    private static final TemplateMode TEMPLATE_MODE = TemplateMode.HTML;
    private static final String ATTRIBUTE_NAME = "transaction-token";
    private static final int PRECEDENCE = 1200;

    private static final String TYPE_ATTR_NAME = "type";
    private static final String NAME_ATTR_NAME = "name";
    private static final String VALUE_ATTR_NAME = "value";

    public TransactionTokenProcessor(String dialectPrefix) {
        super(TEMPLATE_MODE, dialectPrefix, ATTRIBUTE_NAME, PRECEDENCE);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {

        // find token.
        TransactionToken nextToken = getTransactionToken(context);
        if (nextToken == null) {
            logger.debug("cannot found TransactionToken.");
            return;
        }

        // build element.
        buildElement(structureHandler, nextToken);
    }

    private TransactionToken getTransactionToken(ITemplateContext context) {
        return ContextUtils.getAttribute(context, TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME,
                TransactionToken.class);
    }

    private void buildElement(IElementTagStructureHandler structureHandler, TransactionToken nextToken) {

        structureHandler.setAttribute(TYPE_ATTR_NAME, "hidden");
        structureHandler.setAttribute(NAME_ATTR_NAME, TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER);
        structureHandler.setAttribute(VALUE_ATTR_NAME, nextToken.getTokenString());
    }

}