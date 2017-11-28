package io.github.yoshikawaa.gfw.web.thymeleaf.processor.message;

import java.util.Arrays;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessageUtils;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.StringUtils;
import org.unbescape.html.HtmlEscape;

import com.google.common.base.Joiner;

import io.github.yoshikawaa.gfw.web.thymeleaf.processor.AbstractAttributeRemovalAttributeTagProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.ContextUtils;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.ExpressionUtils;

public class MessagesPanelTagProcessor extends AbstractAttributeRemovalAttributeTagProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MessagesPanelTagProcessor.class);

    private static final TemplateMode TEMPLATE_MODE = TemplateMode.HTML;
    private static final String ATTRIBUTE_NAME = "messages-panel";
    private static final int PRECEDENCE = 1200;

    private static final String CLASS_ATTR_NAME = "class";

    private final MessageSource messageSource;

    public MessagesPanelTagProcessor(String dialectPrefix, MessageSource messageSource) {
        super(TEMPLATE_MODE, dialectPrefix, ATTRIBUTE_NAME, PRECEDENCE);
        if (messageSource == null) {
            throw new IllegalArgumentException("messageSource must not be null.");
        }
        this.messageSource = messageSource;
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {

        // find relative attributes.
        final MessagesPanelTagAccessor attrs = new MessagesPanelTagAccessor(tag, getDialectPrefix());
        Arrays.stream(attrs.getAttributeNames()).forEach(a -> structureHandler.removeAttribute(getDialectPrefix(), a));

        // find messages.
        final Object messages = getResultMessages(context, attributeValue);
        if (messages == null) {
            logger.debug("cannot found ResultMessages.");
            return;
        }

        // build element.
        buildElement(tag, structureHandler, messages, attrs);
        structureHandler.setBody(buildBody(context, messages, attrs), false);
    }

    private Object getResultMessages(ITemplateContext context, String attributeValue) {
        return StringUtils.isEmptyOrWhitespace(attributeValue)
                ? ContextUtils.getAttribute(context, ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME)
                : ExpressionUtils.execute(context, attributeValue);
    }

    private void buildElement(IProcessableElementTag tag, IElementTagStructureHandler structureHandler, Object messages,
            MessagesPanelTagAccessor attrs) {

        final String panelClasses = Joiner.on(" ")
                .skipNulls()
                .join(tag.getAttributeValue(CLASS_ATTR_NAME), attrs.getPanelClassName(),
                        attrs.getPanelTypeClass(messages))
                .trim();

        structureHandler.setAttribute(CLASS_ATTR_NAME, panelClasses);
    }

    private IModel buildBody(ITemplateContext context, Object messages, MessagesPanelTagAccessor attrs) {

        final String outerElement = attrs.getOuterElement();
        final String innerElement = attrs.getInnerElement();
        final boolean disableHtmlEscape = attrs.isDisableHtmlEscape();

        final IModelFactory modelFactory = context.getModelFactory();
        final IModel model = modelFactory.createModel();

        if (!StringUtils.isEmptyOrWhitespace(outerElement)) {
            model.add(modelFactory.createOpenElementTag(outerElement));
        }

        model.addModel(buildInnerElements(context, messages, innerElement, disableHtmlEscape));

        if (!StringUtils.isEmptyOrWhitespace(outerElement)) {
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
            return ResultMessageUtils.resolveMessage((ResultMessage) message, messageSource, locale);
        } else if (message instanceof String) {
            return (String) message;
        } else if (message instanceof Throwable) {
            return ((Throwable) message).getMessage();
        }
        return message.toString();
    }

}
