package jp.yoshikawaa.gfw.web.thymeleaf.processor.token.transaction;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.terasoluna.gfw.web.token.transaction.TransactionToken;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenInterceptor;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractMarkupRemovalAttrProcessor;

public class TransactionTokenAttrProcessor extends AbstractMarkupRemovalAttrProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TransactionTokenAttrProcessor.class);

    private static final String ATTRIBUTE_NAME = "transaction-token";

    public TransactionTokenAttrProcessor() {
        super(ATTRIBUTE_NAME);
    }

    @Override
    public int getPrecedence() {
        return 1300;
    }

    @Override
    protected RemovalType getRemovalType(final Arguments arguments, final Element element, final String attributeName) {

        // find token.
        TransactionToken nextToken = getTransactionToken();

        // exist token?
        if (nextToken == null) {
            logger.debug("cannot found TransactionToken.");
            return RemovalType.ELEMENT;
        }

        // build element.
        buildElement(element, nextToken);

        return RemovalType.NONE;
    }

    private TransactionToken getTransactionToken() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        return (TransactionToken) request.getAttribute(TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME);
    }

    private void buildElement(Element element, TransactionToken nextToken) {

        element.setAttribute("type", "hidden");
        element.setAttribute("name", TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER);
        element.setAttribute("value", nextToken.getTokenString());
    }

}