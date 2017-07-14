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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessageUtils;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Macro;
import org.thymeleaf.dom.Text;
import org.thymeleaf.processor.attr.AbstractMarkupRemovalAttrProcessor;

import jp.yoshikawaa.gfw.web.thymeleaf.util.ExpressionUtils;
import jp.yoshikawaa.gfw.web.thymeleaf.util.ProcessorUtils;

public class MessagesPanelAttrProcessor extends AbstractMarkupRemovalAttrProcessor {
    private static final Logger logger = LoggerFactory.getLogger(MessagesPanelAttrProcessor.class);

    private static final String ATTRIBUTE_NAME = "message-panel";

    private static final String ATTRIBUTE_PANEL_CLASS_NAME = "panel-class-name";
    private static final String ATTRIBUTE_PANEL_TYPE_CLASS_PREFIX = "panel-type-class-prefix";
    private static final String ATTRIBUTE_MESSAGES_TYPE = "message-type";
    private static final String ATTRIBUTE_OUTER_ELEMENT = "outer-element";
    private static final String ATTRIBUTE_INNER_ELEMENT = "inner-element";
    private static final String ATTRIBUTE_DISABLE_HTML_ESCAPE = "disable-html-escape";

    private static final String DEFAULT_PANEL_CLASS_NAME = "alert";
    private static final String DEFAULT_PANEL_TYPE_CLASS_PREFIX = "alert-";
    private static final String DEFAULT_MESSAGES_TYPE = null;
    private static final String DEFAULT_OUTER_ELEMENT = "ul";
    private static final String DEFAULT_INNER_ELEMENT = "li";
    private static final boolean DEFAULT_DISABLE_HTML_ESCAPE = false;

    private final String dialectPrefix;
    private final MessageSource messageSource;

    public MessagesPanelAttrProcessor(String dialectPrefix, MessageSource messageSource) {
        super(ATTRIBUTE_NAME);
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
        final String panelClassName = ProcessorUtils.getAttributeValue(element, dialectPrefix,
                ATTRIBUTE_PANEL_CLASS_NAME, DEFAULT_PANEL_CLASS_NAME);
        final String panelTypeClassPrefix = ProcessorUtils.getAttributeValue(element, dialectPrefix,
                ATTRIBUTE_PANEL_TYPE_CLASS_PREFIX, DEFAULT_PANEL_TYPE_CLASS_PREFIX);
        final String messagesType = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_MESSAGES_TYPE,
                DEFAULT_MESSAGES_TYPE);
        final String outerElement = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_OUTER_ELEMENT,
                DEFAULT_OUTER_ELEMENT);
        final String innerElement = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_INNER_ELEMENT,
                DEFAULT_INNER_ELEMENT);
        final boolean disableHtmlEscape = ProcessorUtils.getAttributeValue(element, dialectPrefix,
                ATTRIBUTE_DISABLE_HTML_ESCAPE, DEFAULT_DISABLE_HTML_ESCAPE);
        removeRelativeAttributes(element);

        // exist messages?
        if (messages == null) {
            logger.debug("cannot found ResultMessages.");
            return RemovalType.ELEMENT;
        }

        // build element.
        buildElement(element, messages, messagesType, panelClassName, panelTypeClassPrefix);
        buildBody(arguments, element, messages, outerElement, innerElement, disableHtmlEscape);

        return RemovalType.NONE;
    }

    private ResultMessages getResultMessages(Arguments arguments, String attributeValue) {

        if (StringUtils.hasText(attributeValue)) {
            return ExpressionUtils.execute(arguments, attributeValue, ResultMessages.class);
        } else {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            return (ResultMessages) request.getAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME);
        }
    }

    private void removeRelativeAttributes(Element element) {

        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_MESSAGES_TYPE);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_PANEL_CLASS_NAME);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_PANEL_TYPE_CLASS_PREFIX);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_OUTER_ELEMENT);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_INNER_ELEMENT);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_DISABLE_HTML_ESCAPE);
    }

    private void buildElement(Element element, Object messages, String messagesType, String panelClassName,
            String panelTypeClassPrefix) {

        final String panelTypeClassSuffix = StringUtils.hasText(messagesType) ? messagesType
                : messages instanceof ResultMessages ? ((ResultMessages) messages).getType().getType() : null;
        final String panelTypeClass = panelTypeClassPrefix + panelTypeClassSuffix;
        element.setAttribute("class",
                StringUtils.hasText(panelClassName) ? panelClassName + " " + panelTypeClass : panelTypeClass);
    }

    private void buildBody(Arguments arguments, Element element, Object messages, String outerElement,
            String innerElement, boolean disableHtmlEscape) {

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
            ResultMessage resultMessage = (ResultMessage) message;
            return (messageSource == null) ? resultMessage.getText()
                    : ResultMessageUtils.resolveMessage(resultMessage, messageSource, locale);
        } else if (message instanceof String) {
            return (String) message;
        } else if (message instanceof Throwable) {
            return ((Throwable) message).getMessage();
        }
        return null;
    }
}
