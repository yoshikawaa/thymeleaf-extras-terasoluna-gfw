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
package io.github.yoshikawaa.gfw.web.thymeleaf.processor.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Macro;
import org.thymeleaf.dom.Text;
import org.thymeleaf.util.StringUtils;

import com.google.common.base.Joiner;

import io.github.yoshikawaa.gfw.web.thymeleaf.processor.AbstractAttributeRemovalAttrProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.ContextUtils;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.ElementUtils;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.ExpressionUtils;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.MessageUtils;

/**
 * Attribute tag processor for generating tags from {@link ResultMessages}.
 * 
 * @author Atsushi Yoshikawa
 * @see ResultMessages
 */
public class MessagesPanelAttrProcessor extends AbstractAttributeRemovalAttrProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MessagesPanelAttrProcessor.class);

    private static final String ATTRIBUTE_NAME = "messages-panel";
    private static final int PRECEDENCE = 1200;

    private static final String CLASS_ATTR_NAME = "class";

    /**
     * @param dialectPrefix prefix of attribute
     */
    public MessagesPanelAttrProcessor(String dialectPrefix) {
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

        // find relative attributes.
        final MessagesPanelAttrAccessor attrs = new MessagesPanelAttrAccessor(element, getDialectPrefix());
        Arrays.stream(attrs.getAttributeNames())
                .forEach(a -> ElementUtils.removeAttribute(element, getDialectPrefix(), a));

        // find messages.
        final String attributeValue = element.getAttributeValue(attributeName);
        final Object messages = getResultMessages(arguments, attributeValue);
        if (messages == null) {
            logger.debug("cannot found ResultMessages.");
            return;
        }

        // build element.
        buildElement(element, messages, attrs);
        buildBody(arguments, element, messages, attrs);
    }

    private Object getResultMessages(Arguments arguments, String attributeValue) {
        return StringUtils.isEmptyOrWhitespace(attributeValue)
                ? ContextUtils.getAttribute(arguments, ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME)
                : ExpressionUtils.execute(arguments, attributeValue);
    }

    private void buildElement(Element element, Object messages, MessagesPanelAttrAccessor attrs) {

        final String panelClasses = Joiner.on(" ")
                .skipNulls()
                .join(element.getAttributeValue(CLASS_ATTR_NAME), attrs.getPanelClassName(),
                        attrs.getPanelTypeClass(messages))
                .trim();

        element.setAttribute(CLASS_ATTR_NAME, panelClasses);
    }

    private void buildBody(Arguments arguments, Element element, Object messages, MessagesPanelAttrAccessor attrs) {

        final String outerElement = attrs.getOuterElement();
        final String innerElement = attrs.getInnerElement();
        final boolean disableHtmlEscape = attrs.isDisableHtmlEscape();

        final List<Element> inner = buildInnerElements(arguments, messages, innerElement, disableHtmlEscape);

        element.clearChildren();
        if (StringUtils.isEmptyOrWhitespace(outerElement)) {
            inner.forEach(n -> element.addChild(n));
        } else {
            Element outer = new Element(outerElement);
            inner.forEach(n -> outer.addChild(n));
            element.addChild(outer);
        }
    }

    private List<Element> buildInnerElements(Arguments arguments, Object messages, String innerElement,
            boolean disableHtmlEscape) {

        if (messages instanceof Iterable<?>) {
            return StreamSupport.stream(((Iterable<?>) messages).spliterator(), false)
                    .map(m -> buildInnerElement(arguments, m, innerElement, disableHtmlEscape))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<Element>(
                    Arrays.asList(buildInnerElement(arguments, messages, innerElement, disableHtmlEscape)));
        }
    }

    private Element buildInnerElement(Arguments arguments, Object message, String innerElement,
            boolean disableHtmlEscape) {

        final Element element = new Element(innerElement);
        element.addChild(disableHtmlEscape ? new Macro(resolveMessage(arguments, message))
                : new Text(resolveMessage(arguments, message)));
        return element;
    }

    private String resolveMessage(Arguments arguments, Object message) {

        if (message instanceof ResultMessage) {
            ResultMessage resultMessage = (ResultMessage) message;
            return MessageUtils.resolveMessage(arguments, resultMessage.getCode(), resultMessage.getArgs(),
                    resultMessage.getText());
        } else if (message instanceof String) {
            return (String) message;
        } else if (message instanceof Throwable) {
            return ((Throwable) message).getMessage();
        }
        return message.toString();
    }

}
