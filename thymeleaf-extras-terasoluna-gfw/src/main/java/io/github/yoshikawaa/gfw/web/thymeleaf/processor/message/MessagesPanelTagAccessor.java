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

import org.terasoluna.gfw.common.message.ResultMessages;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.util.StringUtils;

import io.github.yoshikawaa.gfw.web.thymeleaf.processor.IAttributeTagAccessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.ElementTagUtils;

public class MessagesPanelTagAccessor implements IAttributeTagAccessor {

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

    public MessagesPanelTagAccessor(IProcessableElementTag tag, String dialectPrefix) {

        this.messagesType = ElementTagUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_MESSAGES_TYPE,
                DEFAULT_MESSAGES_TYPE);
        this.panelClassName = ElementTagUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_PANEL_CLASS_NAME,
                DEFAULT_PANEL_CLASS_NAME);
        this.panelTypeClassPrefix = ElementTagUtils.getAttributeValue(tag, dialectPrefix,
                ATTRIBUTE_PANEL_TYPE_CLASS_PREFIX, DEFAULT_PANEL_TYPE_CLASS_PREFIX);
        this.outerElement = ElementTagUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_OUTER_ELEMENT,
                DEFAULT_OUTER_ELEMENT);
        this.innerElement = ElementTagUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_INNER_ELEMENT,
                DEFAULT_INNER_ELEMENT);
        this.disableHtmlEscape = ElementTagUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_DISABLE_HTML_ESCAPE,
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
