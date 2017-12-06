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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.terasoluna.gfw.web.pagination.PaginationInfo;
import org.terasoluna.gfw.web.pagination.PaginationInfo.BeginAndEnd;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Text;
import org.thymeleaf.util.StringUtils;

import io.github.yoshikawaa.gfw.web.thymeleaf.processor.AbstractAttributeRemovalAttrProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.ElementUtils;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.ExpressionUtils;

/**
 * Attribute tag processor for generating tags from {@link Page}.
 * 
 * @author Atsushi Yoshikawa
 * @see Page
 */
public class PaginationAttrProcessor extends AbstractAttributeRemovalAttrProcessor {

    private static final Logger logger = LoggerFactory.getLogger(PaginationAttrProcessor.class);

    private static final String ATTRIBUTE_NAME = "pagination";
    private static final int PRECEDENCE = 1200;

    private static final String DEFAULT_PAGE_EXPRESSION = "${page}";

    /**
     * @param dialectPrefix prefix of attribute
     */
    public PaginationAttrProcessor(String dialectPrefix) {
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
        PaginationAttrAccessor attrs = new PaginationAttrAccessor(element, getDialectPrefix());
        Arrays.stream(attrs.getAttributeNames())
                .forEach(a -> ElementUtils.removeAttribute(element, getDialectPrefix(), a));

        // find page.
        final String attributeValue = element.getAttributeValue(attributeName);
        Page<?> page = getPage(arguments, attributeValue);
        if (page == null) {
            logger.debug("cannot found page.");
            return;
        }

        // build element.
        buildBody(arguments, element, page, attrs);
    }

    private Page<?> getPage(Arguments arguments, String attributeValue) {
        return ExpressionUtils.execute(arguments,
                (StringUtils.isEmptyOrWhitespace(attributeValue)) ? DEFAULT_PAGE_EXPRESSION : attributeValue,
                Page.class);
    }

    private void buildBody(Arguments arguments, Element element, Page<?> page, PaginationAttrAccessor attrs) {

        final String innerElement = attrs.getInnerElement();
        final String disabledClass = attrs.getDisabledClass();
        final String activeClass = attrs.getActiveClass();
        final String firstLinkText = attrs.getFirstLinkText();
        final String previousLinkText = attrs.getPreviousLinkText();
        final String nextLinkText = attrs.getNextLinkText();
        final String lastLinkText = attrs.getLastLinkText();
        final int maxDisplayCount = attrs.getMaxDisplayCount();
        final String disabledHref = attrs.getDisabledHref();
        final String hrefTmpl = attrs.getHrefTmpl();
        final String criteriaQuery = attrs.getCriteriaQuery();
        final boolean disableHtmlEscapeOfCriteriaQuery = attrs.isDisableHtmlEscapeOfCriteriaQuery();
        final boolean enableLinkOfCurrentPage = attrs.isEnableLinkOfCurrentPage();

        final ThymeleafPaginationInfo info = new ThymeleafPaginationInfo(arguments, page, hrefTmpl, criteriaQuery,
                disableHtmlEscapeOfCriteriaQuery, maxDisplayCount);

        List<Element> elements = new ArrayList<Element>();

        if (info.isFirstPage()) {
            elements.add(buildInnerElement(innerElement, disabledClass, disabledHref, firstLinkText));
            elements.add(buildInnerElement(innerElement, disabledClass, disabledHref, previousLinkText));
        } else {
            elements.add(buildInnerElement(innerElement, activeClass, info.getFirstUrl(), firstLinkText));
            elements.add(buildInnerElement(innerElement, activeClass, info.getPreviousUrl(), previousLinkText));
        }

        elements.addAll(buildPageNumberElements(info, innerElement, activeClass, disabledClass, disabledHref,
                enableLinkOfCurrentPage));

        if (info.isLastPage()) {
            elements.add(buildInnerElement(innerElement, disabledClass, disabledHref, nextLinkText));
            elements.add(buildInnerElement(innerElement, disabledClass, disabledHref, lastLinkText));
        } else {
            elements.add(buildInnerElement(innerElement, activeClass, info.getNextUrl(), nextLinkText));
            elements.add(buildInnerElement(innerElement, activeClass, info.getLastUrl(), lastLinkText));
        }

        element.clearChildren();
        elements.forEach(e -> element.addChild(e));
    }

    private List<Element> buildPageNumberElements(ThymeleafPaginationInfo info, String innerElement, String activeClass,
            String disabledClass, String disabledHref, boolean enableLinkOfCurrentPage) {

        List<Element> numbers = new ArrayList<Element>();

        BeginAndEnd be = info.getBeginAndEnd();
        IntStream.rangeClosed(be.getBegin(), be.getEnd()).forEachOrdered(i -> {
            if (info.isCurrent(i) && !enableLinkOfCurrentPage) {
                numbers.add(buildInnerElement(innerElement, disabledClass, disabledHref, String.valueOf(i + 1)));
            } else {
                numbers.add(buildInnerElement(innerElement, activeClass, info.getPageUrl(i), String.valueOf(i + 1)));
            }
        });

        return numbers;
    }

    private Element buildInnerElement(String innerElement, String activeOrDisabled, String href, String text) {

        Element inner = new Element(innerElement);
        inner.setAttribute(PaginationInfo.CLASS_ATTR, activeOrDisabled);

        if (StringUtils.isEmptyOrWhitespace(href) || StringUtils.isEmptyOrWhitespace(text)) {
            inner.addChild(new Text(text));
        } else {
            Element anchor = new Element(PaginationInfo.A_ELM);
            anchor.setAttribute(PaginationInfo.HREF_ATTR, href);
            anchor.addChild(new Text(text));
            inner.addChild(anchor);
        }

        return inner;
    }

}
