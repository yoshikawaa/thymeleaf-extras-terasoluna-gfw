package jp.yoshikawaa.gfw.web.thymeleaf.processor.message;

import org.terasoluna.gfw.common.message.ResultMessages;
import org.thymeleaf.dom.Element;
import org.thymeleaf.util.StringUtils;

import jp.yoshikawaa.gfw.web.thymeleaf.processor.IAttrAccessor;
import jp.yoshikawaa.gfw.web.thymeleaf.util.ElementUtils;

public class MessagesPanelAttrAccessor implements IAttrAccessor {

    private static final String ATTRIBUTE_PANEL_CLASS_NAME = "panel-class-name";
    private static final String ATTRIBUTE_PANEL_TYPE_CLASS_PREFIX = "panel-type-class-prefix";
    private static final String ATTRIBUTE_MESSAGES_TYPE = "messages-type";
    private static final String ATTRIBUTE_OUTER_ELEMENT = "outer-element";
    private static final String ATTRIBUTE_INNER_ELEMENT = "inner-element";
    private static final String ATTRIBUTE_DISABLE_HTML_ESCAPE = "disable-html-escape";

    private static final String[] ATTRIBUTE_NAMES = { ATTRIBUTE_PANEL_CLASS_NAME, ATTRIBUTE_PANEL_TYPE_CLASS_PREFIX,
            ATTRIBUTE_MESSAGES_TYPE, ATTRIBUTE_OUTER_ELEMENT, ATTRIBUTE_INNER_ELEMENT, ATTRIBUTE_DISABLE_HTML_ESCAPE };

    private static final String DEFAULT_PANEL_CLASS_NAME = "alert";
    private static final String DEFAULT_PANEL_TYPE_CLASS_PREFIX = "alert-";
    private static final String DEFAULT_MESSAGES_TYPE = null;
    private static final String DEFAULT_OUTER_ELEMENT = "ul";
    private static final String DEFAULT_INNER_ELEMENT = "li";
    private static final boolean DEFAULT_DISABLE_HTML_ESCAPE = false;

    private final String messagesType;
    private final String panelClassName;
    private final String panelTypeClassPrefix;
    private final String outerElement;
    private final String innerElement;
    private final boolean disableHtmlEscape;

    public MessagesPanelAttrAccessor(Element element, String dialectPrefix) {

        this.panelClassName = ElementUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_PANEL_CLASS_NAME,
                DEFAULT_PANEL_CLASS_NAME);
        this.panelTypeClassPrefix = ElementUtils.getAttributeValue(element, dialectPrefix,
                ATTRIBUTE_PANEL_TYPE_CLASS_PREFIX, DEFAULT_PANEL_TYPE_CLASS_PREFIX);
        this.messagesType = ElementUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_MESSAGES_TYPE,
                DEFAULT_MESSAGES_TYPE);
        this.outerElement = ElementUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_OUTER_ELEMENT,
                DEFAULT_OUTER_ELEMENT);
        this.innerElement = ElementUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_INNER_ELEMENT,
                DEFAULT_INNER_ELEMENT);
        this.disableHtmlEscape = ElementUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_DISABLE_HTML_ESCAPE,
                DEFAULT_DISABLE_HTML_ESCAPE);
    }

    @Override
    public String[] getAttributeNames() {
        return ATTRIBUTE_NAMES;
    }

    public String getPanelTypeClass(Object messages) {
        final String panelTypeClassSuffix = !StringUtils.isEmptyOrWhitespace(messagesType) ? messagesType
                : messages instanceof ResultMessages ? ((ResultMessages) messages).getType().getType() : "";
        return panelTypeClassPrefix + panelTypeClassSuffix;
    }

    public String getMessagesType() {
        return messagesType;
    }

    public String getPanelClassName() {
        return panelClassName;
    }

    public String getPanelTypeClassPrefix() {
        return panelTypeClassPrefix;
    }

    public String getOuterElement() {
        return outerElement;
    }

    public String getInnerElement() {
        return innerElement;
    }

    public boolean isDisableHtmlEscape() {
        return disableHtmlEscape;
    }

}
