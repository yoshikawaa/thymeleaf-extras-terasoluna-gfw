package jp.yoshikawaa.gfw.web.thymeleaf.processor.message;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessageUtils;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.unbescape.html.HtmlEscape;

import jp.yoshikawaa.gfw.web.thymeleaf.processor.AbstractHtmlAttributeProcessor;
import jp.yoshikawaa.gfw.web.thymeleaf.util.ExpressionUtils;

public class MessagesPanelAttributeProcessor extends AbstractHtmlAttributeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MessagesPanelAttributeProcessor.class);

    private static final String ATTRIBUTE_NAME = "messages-panel";
    private static final int PRECEDENCE = 12000;

    private final MessageSource messageSource;

    public MessagesPanelAttributeProcessor(String dialectPrefix, MessageSource messageSource) {
        super(dialectPrefix, ATTRIBUTE_NAME, PRECEDENCE);
        if (messageSource == null) {
            throw new TemplateInputException("messageSource must not be null.");
        }
        this.messageSource = messageSource;
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {

        // find messages.
        final Object messages = getResultMessages(context, attributeValue);

        // find relative attributes.
        MessagesPanelAttributeAccessor attrs = new MessagesPanelAttributeAccessor(tag, dialectPrefix);
        attrs.removeAttributes(structureHandler);

        // exist messages?
        if (messages == null) {
            logger.debug("cannot found ResultMessages.");
            return;
        }

        // build element.
        buildElement(structureHandler, messages, attrs);
        structureHandler.setBody(buildBody(context, messages, attrs), false);
    }

    private Object getResultMessages(ITemplateContext context, String attributeValue) {

        if (StringUtils.hasText(attributeValue)) {
            return ExpressionUtils.execute(context, attributeValue);
        } else {
            HttpServletRequest request = ((WebEngineContext)context).getRequest();
            return request.getAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME);
        }
    }

    private void buildElement(IElementTagStructureHandler structureHandler, Object messages,
            MessagesPanelAttributeAccessor attrs) {

        final String panelClassName = attrs.getPanelClassName();
        final String panelTypeClass = attrs.getPanelTypeClass(messages);

        structureHandler.setAttribute("class",
                StringUtils.hasText(panelClassName) ? panelClassName + " " + panelTypeClass : panelTypeClass);
    }

    private IModel buildBody(ITemplateContext context, Object messages, MessagesPanelAttributeAccessor attrs) {

        final String outerElement = attrs.getOuterElement();
        final String innerElement = attrs.getInnerElement();
        final boolean disableHtmlEscape = attrs.isDisableHtmlEscape();

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
            return ResultMessageUtils.resolveMessage((ResultMessage) message, messageSource, locale);
        } else if (message instanceof String) {
            return (String) message;
        } else if (message instanceof Throwable) {
            return ((Throwable) message).getMessage();
        }
        return message.toString();
    }

}
