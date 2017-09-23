package jp.yoshikawaa.gfw.web.thymeleaf.processor.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessageUtils;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.thymeleaf.Arguments;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Macro;
import org.thymeleaf.dom.Text;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.processor.attr.AbstractMarkupRemovalAttrProcessor;

import jp.yoshikawaa.gfw.web.thymeleaf.util.ExpressionUtils;

public class MessagesPanelAttrProcessor extends AbstractMarkupRemovalAttrProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MessagesPanelAttrProcessor.class);

    private static final String ATTRIBUTE_NAME = "messages-panel";

    private final String dialectPrefix;
    private final MessageSource messageSource;

    public MessagesPanelAttrProcessor(String dialectPrefix, MessageSource messageSource) {
        super(ATTRIBUTE_NAME);
        if (messageSource == null) {
            throw new TemplateInputException("messageSource must not be null.");
        }
        this.dialectPrefix = dialectPrefix;
        this.messageSource = messageSource;
    }

    @Override
    public int getPrecedence() {
        return 1300;
    }

    @Override
    protected RemovalType getRemovalType(final Arguments arguments, final Element element, final String attributeName) {

        // find messages.
        final String attributeValue = element.getAttributeValue(attributeName);
        final Object messages = getResultMessages(arguments, attributeValue);

        // find relative attributes.
        MessagesPanelAttrAccessor attrs = new MessagesPanelAttrAccessor(element, dialectPrefix);
        attrs.removeAttributes(element);

        // exist messages?
        if (messages == null) {
            logger.debug("cannot found ResultMessages.");
            return RemovalType.ELEMENT;
        }

        // build element.
        buildElement(element, messages, attrs);
        buildBody(arguments, element, messages, attrs);

        return RemovalType.NONE;
    }

    private Object getResultMessages(Arguments arguments, String attributeValue) {

        if (StringUtils.hasText(attributeValue)) {
            return ExpressionUtils.execute(arguments, attributeValue);
        } else {
            HttpServletRequest request = ((WebContext) arguments.getContext()).getHttpServletRequest();
            return (ResultMessages) request.getAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME);
        }
    }

    private void buildElement(Element element, Object messages, MessagesPanelAttrAccessor attrs) {

        final String panelClassName = attrs.getPanelClassName();
        final String panelTypeClass = attrs.getPanelTypeClass(messages);

        element.setAttribute("class",
                StringUtils.hasText(panelClassName) ? panelClassName + " " + panelTypeClass : panelTypeClass);
    }

    private void buildBody(Arguments arguments, Element element, Object messages, MessagesPanelAttrAccessor attrs) {

        final String outerElement = attrs.getOuterElement();
        final String innerElement = attrs.getInnerElement();
        final boolean disableHtmlEscape = attrs.isDisableHtmlEscape();

        List<Element> inner = buildInnerElements(arguments, messages, innerElement, disableHtmlEscape);

        if (StringUtils.hasText(outerElement)) {
            Element outer = new Element(outerElement);
            inner.forEach(n -> outer.addChild(n));
            element.addChild(outer);
        } else {
            inner.forEach(n -> element.addChild(n));
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

        final Locale locale = arguments.getContext().getLocale();

        Element element = new Element(innerElement);
        element.addChild(disableHtmlEscape ? new Macro(resolveMessage(message, locale))
                : new Text(resolveMessage(message, locale)));
        return element;
    }

    private String resolveMessage(Object message, Locale locale) {

        if (message instanceof ResultMessage) {
            return ResultMessageUtils.resolveMessage((ResultMessage) message, messageSource, locale);
        } else if (message instanceof String) {
            return (String) message;
        } else if (message instanceof Throwable) {
            return ((Throwable) message).getMessage();
        }
        return message.toString();
    }

}
