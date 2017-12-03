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
package io.github.yoshikawaa.gfw.web.thymeleaf.processor.pagination;

import org.terasoluna.gfw.web.pagination.PaginationInfo;
import org.thymeleaf.model.IProcessableElementTag;

import io.github.yoshikawaa.gfw.web.thymeleaf.processor.IAttributeTagAccessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.ElementTagUtils;

/**
 * Attribute accessor for {@link PaginationTagProcessor}
 * 
 * @author Atsushi Yoshikawa
 * @see PaginationTagProcessor
 */
public class PaginationTagAccessor implements IAttributeTagAccessor {

    private static final String ATTRIBUTE_INNER_ELEMENT = "inner-element";
    private static final String ATTRIBUTE_DISABLED_CLASS = "disabled-class";
    private static final String ATTRIBUTE_ACTIVE_CLASS = "active-class";
    private static final String ATTRIBUTE_FIRST_LINK_TEXT = "first-link-text";
    private static final String ATTRIBUTE_PREVIOUS_LINK_TEXT = "previous-link-text";
    private static final String ATTRIBUTE_NEXT_LINK_TEXT = "next-link-text";
    private static final String ATTRIBUTE_LAST_LINK_TEXT = "last-link-text";
    private static final String ATTRIBUTE_MAX_DISPLAY_COUNT = "max-display-count";
    private static final String ATTRIBUTE_DISABLED_HREF = "disabled-href";
    private static final String ATTRIBUTE_HREF_TMPL = "href-tmpl";
    private static final String ATTRIBUTE_CRITERIA_QUERY = "criteria-query";
    private static final String ATTRIBUTE_DISABLE_HTML_ESCAPE_OF_CRITERIA_QUERY = "disable-html-escape-of-criteria-query";
    private static final String ATTRIBUTE_ENABLE_LINK_OF_CURRENT_PAGE = "enable-link-of-current-page";

    private static final String[] ATTRIBUTE_NAMES = { ATTRIBUTE_INNER_ELEMENT, ATTRIBUTE_DISABLED_CLASS,
            ATTRIBUTE_ACTIVE_CLASS, ATTRIBUTE_FIRST_LINK_TEXT, ATTRIBUTE_PREVIOUS_LINK_TEXT, ATTRIBUTE_NEXT_LINK_TEXT,
            ATTRIBUTE_LAST_LINK_TEXT, ATTRIBUTE_MAX_DISPLAY_COUNT, ATTRIBUTE_DISABLED_HREF, ATTRIBUTE_HREF_TMPL,
            ATTRIBUTE_CRITERIA_QUERY, ATTRIBUTE_DISABLE_HTML_ESCAPE_OF_CRITERIA_QUERY,
            ATTRIBUTE_ENABLE_LINK_OF_CURRENT_PAGE };

    private static final String DEFAULT_FIRST_LINK_TEXT = "<<";
    private static final String DEFAULT_PREVIOUS_LINK_TEXT = "<";
    private static final String DEFAULT_NEXT_LINK_TEXT = ">";
    private static final String DEFAULT_LAST_LINK_TEXT = ">>";
    private static final String DEFAULT_HREF_TMPL = null;
    private static final String DEFAULT_CRITERIA_QUERY = null;
    private static final boolean DEFAULT_DISABLE_HTML_ESCAPE_OF_CRITERIA_QUERY = false;
    private static final boolean DEFAULT_ENABLE_LINK_OF_CURRENT_PAGE = false;

    private final String innerElement;
    private final String disabledClass;
    private final String activeClass;
    private final String firstLinkText;
    private final String previousLinkText;
    private final String nextLinkText;
    private final String lastLinkText;
    private final int maxDisplayCount;
    private final String disabledHref;
    private final String hrefTmpl;
    private final String criteriaQuery;
    private final boolean disableHtmlEscapeOfCriteriaQuery;
    private final boolean enableLinkOfCurrentPage;

    /**
     * Collect attributes related to {@link PaginationTagProcessor}.
     * 
     * @param tag source tag
     * @param dialectPrefix prefix of attribute
     */
    public PaginationTagAccessor(IProcessableElementTag tag, String dialectPrefix) {

        this.innerElement = ElementTagUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_INNER_ELEMENT,
                PaginationInfo.DEFAULT_INNER_ELM);
        this.disabledClass = ElementTagUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_DISABLED_CLASS,
                PaginationInfo.DEFAULT_DISABLED_CLASS);
        this.activeClass = ElementTagUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_ACTIVE_CLASS,
                PaginationInfo.DEFAULT_ACTIVE_CLASS);
        this.firstLinkText = ElementTagUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_FIRST_LINK_TEXT,
                DEFAULT_FIRST_LINK_TEXT);
        this.previousLinkText = ElementTagUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_PREVIOUS_LINK_TEXT,
                DEFAULT_PREVIOUS_LINK_TEXT);
        this.nextLinkText = ElementTagUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_NEXT_LINK_TEXT,
                DEFAULT_NEXT_LINK_TEXT);
        this.lastLinkText = ElementTagUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_LAST_LINK_TEXT,
                DEFAULT_LAST_LINK_TEXT);
        this.maxDisplayCount = ElementTagUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_MAX_DISPLAY_COUNT,
                PaginationInfo.DEFAULT_MAX_DISPLAY_COUNT);
        this.disabledHref = ElementTagUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_DISABLED_HREF,
                PaginationInfo.DEFAULT_DISABLED_HREF);
        this.hrefTmpl = ElementTagUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_HREF_TMPL, DEFAULT_HREF_TMPL);
        this.criteriaQuery = ElementTagUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_CRITERIA_QUERY,
                DEFAULT_CRITERIA_QUERY);
        this.disableHtmlEscapeOfCriteriaQuery = ElementTagUtils.getAttributeValue(tag, dialectPrefix,
                ATTRIBUTE_DISABLE_HTML_ESCAPE_OF_CRITERIA_QUERY, DEFAULT_DISABLE_HTML_ESCAPE_OF_CRITERIA_QUERY);
        this.enableLinkOfCurrentPage = ElementTagUtils.getAttributeValue(tag, dialectPrefix,
                ATTRIBUTE_ENABLE_LINK_OF_CURRENT_PAGE, DEFAULT_ENABLE_LINK_OF_CURRENT_PAGE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getAttributeNames() {
        return ATTRIBUTE_NAMES;
    }

    /**
     * @return value of {@code inner-element} attribute
     */
    public String getInnerElement() {
        return innerElement;
    }

    /**
     * @return value of {@code disabled-class} attribute
     */
    public String getDisabledClass() {
        return disabledClass;
    }

    /**
     * @return value of {@code active-class} attribute
     */
    public String getActiveClass() {
        return activeClass;
    }

    /**
     * @return value of {@code first-link-text} attribute
     */
    public String getFirstLinkText() {
        return firstLinkText;
    }

    /**
     * @return value of {@code previous-link-text} attribute
     */
    public String getPreviousLinkText() {
        return previousLinkText;
    }

    /**
     * @return value of {@code next-link-text} attribute
     */
    public String getNextLinkText() {
        return nextLinkText;
    }

    /**
     * @return value of {@code last-link-text} attribute
     */
    public String getLastLinkText() {
        return lastLinkText;
    }

    /**
     * @return value of {@code max-display-count} attribute
     */
    public int getMaxDisplayCount() {
        return maxDisplayCount;
    }

    /**
     * @return value of {@code disabled-href} attribute
     */
    public String getDisabledHref() {
        return disabledHref;
    }

    /**
     * @return value of {@code href-tmpl} attribute
     */
    public String getHrefTmpl() {
        return hrefTmpl;
    }

    /**
     * @return value of {@code criteria-query} attribute
     */
    public String getCriteriaQuery() {
        return criteriaQuery;
    }

    /**
     * @return value of {@code disable-html-escape-of-criteria-query} attribute
     */
    public boolean isDisableHtmlEscapeOfCriteriaQuery() {
        return disableHtmlEscapeOfCriteriaQuery;
    }

    /**
     * @return value of {@code enable-link-of-current-page} attribute
     */
    public boolean isEnableLinkOfCurrentPage() {
        return enableLinkOfCurrentPage;
    }

}
