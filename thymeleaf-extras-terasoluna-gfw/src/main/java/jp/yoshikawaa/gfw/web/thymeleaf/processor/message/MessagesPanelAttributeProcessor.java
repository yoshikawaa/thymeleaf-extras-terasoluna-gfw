package jp.yoshikawaa.gfw.web.thymeleaf.processor.message;

import java.util.Locale;

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
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.unbescape.html.HtmlEscape;

import jp.yoshikawaa.gfw.web.thymeleaf.processor.AbstractHtmlAttributeProcessor;
import jp.yoshikawaa.gfw.web.thymeleaf.util.ExpressionUtils;
import jp.yoshikawaa.gfw.web.thymeleaf.util.ProcessorUtils;

public class MessagesPanelAttributeProcessor extends AbstractHtmlAttributeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MessagesPanelAttributeProcessor.class);

    private static final String ATTRIBUTE_NAME = "messages-panel";
    private static final int PRECEDENCE = 12000;

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

    private final MessageSource messageSource;

    public MessagesPanelAttributeProcessor(String dialectPrefix, MessageSource messageSource) {
        super(dialectPrefix, ATTRIBUTE_NAME, PRECEDENCE);
        this.messageSource = messageSource;
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {

        // find messages.
        final Object messages = getResultMessages(context, attributeValue);

        // find relative attributes.
        final String messagesType = ProcessorUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_MESSAGES_TYPE,
                DEFAULT_MESSAGES_TYPE);
        final String panelClassName = ProcessorUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_PANEL_CLASS_NAME,
                DEFAULT_PANEL_CLASS_NAME);
        final String panelTypeClassPrefix = ProcessorUtils.getAttributeValue(tag, dialectPrefix,
                ATTRIBUTE_PANEL_TYPE_CLASS_PREFIX, DEFAULT_PANEL_TYPE_CLASS_PREFIX);
        final String outerElement = ProcessorUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_OUTER_ELEMENT,
                DEFAULT_OUTER_ELEMENT);
        final String innerElement = ProcessorUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_INNER_ELEMENT,
                DEFAULT_INNER_ELEMENT);
        final boolean disableHtmlEscape = ProcessorUtils.getAttributeValue(tag, dialectPrefix,
                ATTRIBUTE_DISABLE_HTML_ESCAPE, DEFAULT_DISABLE_HTML_ESCAPE);
        removeRelativeAttributes(structureHandler);

        // exist messages?
        if (messages == null) {
            logger.debug("cannot found ResultMessages.");
            return;
        }

        // build element.
        buildElement(structureHandler, tag, messages, messagesType, panelClassName, panelTypeClassPrefix);
        structureHandler.setBody(buildBody(context, tag, messages, outerElement, innerElement, disableHtmlEscape),
                false);
    }

    private Object getResultMessages(ITemplateContext context, String attributeValue) {

        if (StringUtils.hasText(attributeValue)) {
            return ExpressionUtils.execute(context, attributeValue);
        } else {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            return request.getAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME);
        }
    }

    private void removeRelativeAttributes(IElementTagStructureHandler structureHandler) {

        structureHandler.removeAttribute(dialectPrefix, ATTRIBUTE_MESSAGES_TYPE);
        structureHandler.removeAttribute(dialectPrefix, ATTRIBUTE_PANEL_CLASS_NAME);
        structureHandler.removeAttribute(dialectPrefix, ATTRIBUTE_PANEL_TYPE_CLASS_PREFIX);
        structureHandler.removeAttribute(dialectPrefix, ATTRIBUTE_OUTER_ELEMENT);
        structureHandler.removeAttribute(dialectPrefix, ATTRIBUTE_INNER_ELEMENT);
        structureHandler.removeAttribute(dialectPrefix, ATTRIBUTE_DISABLE_HTML_ESCAPE);
    }

    private void buildElement(IElementTagStructureHandler structureHandler, IProcessableElementTag tag,
            Object messages, String messagesType, String panelClassName, String panelTypeClassPrefix) {

        final String panelTypeClassSuffix = StringUtils.hasText(messagesType) ? messagesType
                : messages instanceof ResultMessages ? ((ResultMessages) messages).getType().getType() : null;
        final String panelTypeClass = panelTypeClassPrefix + panelTypeClassSuffix;
        structureHandler.setAttribute("class",
                StringUtils.hasText(panelClassName) ? panelClassName + " " + panelTypeClass : panelTypeClass);
    }

    private IModel buildBody(ITemplateContext context, IProcessableElementTag tag, Object messages, String outerElement,
            String innerElement, boolean disableHtmlEscape) {

        final IModelFactory modelFactory = context.getModelFactory();
        final IModel model = modelFactory.createModel();

        if (StringUtils.hasText(outerElement)) {
            model.add(modelFactory.createOpenElementTag(outerElement));
        }

        model.addModel(buildInnerElements(context, messages, innerElement, disableHtmlEscape));

        if (StringUtils.hasText(outerElement)) {
            model.add(modelFactory.createCloseElementTag(outerElement));
        }

        return model;
    }

    private IModel buildInnerElements(ITemplateContext context, Object messages, String innerElement,
            boolean disableHtmlEscape) {

        final IModelFactory modelFactory = context.getModelFactory();
        final IModel model = modelFactory.createModel();

        if (messages instanceof Iterable<?>) {
            ((Iterable<?>) messages).forEach(m -> {
                model.addModel(buildInnerElement(context, m, innerElement, disableHtmlEscape));
            });
        } else {
            model.addModel(buildInnerElement(context, messages, innerElement, disableHtmlEscape));
        }

        return model;
    }

    private IModel buildInnerElement(ITemplateContext context, Object message, String innerElement,
            boolean disableHtmlEscape) {

        final Locale locale = context.getLocale();

        final IModelFactory modelFactory = context.getModelFactory();
        final IModel model = modelFactory.createModel();

        model.add(modelFactory.createOpenElementTag(innerElement));
        model.add(modelFactory.createText((disableHtmlEscape) ? resolveMessage(message, locale)
                : HtmlEscape.escapeHtml5(resolveMessage(message, locale))));
        model.add(modelFactory.createCloseElementTag(innerElement));

        return model;
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
