/**
 * Copyright (c) 2017 Atsushi Yoshikawa (https://yoshikawaa.github.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.yoshikawaa.gfw.web.thymeleaf.processor.token.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasoluna.gfw.web.token.transaction.TransactionToken;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenInterceptor;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;

import io.github.yoshikawaa.gfw.web.thymeleaf.processor.AbstractAttributeRemovalAttrProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.ContextUtils;

/**
 * Attribute tag processor for generating tags from {@link TransactionToken}.
 * 
 * @author Atsushi Yoshikawa
 * @see TransactionToken
 */
public class TransactionTokenAttrProcessor extends AbstractAttributeRemovalAttrProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TransactionTokenAttrProcessor.class);

    private static final String ATTRIBUTE_NAME = "transaction";
    private static final int PRECEDENCE = 1200;

    private static final String TYPE_ATTR_NAME = "type";
    private static final String NAME_ATTR_NAME = "name";
    private static final String VALUE_ATTR_NAME = "value";

    /**
     * @param dialectPrefix prefix of attribute
     */
    public TransactionTokenAttrProcessor(String dialectPrefix) {
        super(dialectPrefix, ATTRIBUTE_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPrecedence() {
        return PRECEDENCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void process(final Arguments arguments, final Element element, final String attributeName) {

        // find token.
        TransactionToken nextToken = getTransactionToken(arguments);
        if (nextToken == null) {
            logger.debug("cannot found TransactionToken.");
            return;
        }

        // build element.
        buildElement(element, nextToken);
    }

    private TransactionToken getTransactionToken(Arguments arguments) {
        return ContextUtils.getAttribute(arguments, TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME,
                TransactionToken.class);
    }

    private void buildElement(Element element, TransactionToken nextToken) {

        element.setAttribute(TYPE_ATTR_NAME, "hidden");
        element.setAttribute(NAME_ATTR_NAME, TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER);
        element.setAttribute(VALUE_ATTR_NAME, nextToken.getTokenString());
    }

}