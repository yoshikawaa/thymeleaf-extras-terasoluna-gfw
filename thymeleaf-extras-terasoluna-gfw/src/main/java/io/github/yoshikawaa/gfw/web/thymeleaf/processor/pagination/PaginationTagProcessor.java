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

import java.util.Arrays;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.terasoluna.gfw.web.pagination.PaginationInfo;
import org.terasoluna.gfw.web.pagination.PaginationInfo.BeginAndEnd;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.StringUtils;
import org.unbescape.html.HtmlEscape;

import io.github.yoshikawaa.gfw.web.thymeleaf.processor.AbstractAttributeRemovalAttributeTagProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.ExpressionUtils;

/**
 * Attribute tag processor for generating tags from {@link Page}.
 * 
 * @author Atsushi Yoshikawa
 * @see Page
 */
public class PaginationTagProcessor extends AbstractAttributeRemovalAttributeTagProcessor {
    private static final Logger logger = LoggerFactory.getLogger(PaginationTagProcessor.class);

    private static final TemplateMode TEMPLATE_MODE = TemplateMode.HTML;
    private static final String ATTRIBUTE_NAME = "pagination";
    private static final int PRECEDENCE = 1200;

    private static final String DEFAULT_PAGE_EXPRESSION = "${page}";

    /**
     * @param dialectPrefix prefix of attribute
     */
    public PaginationTagProcessor(String dialectPrefix) {
        super(TEMPLATE_MODE, dialectPrefix, ATTRIBUTE_NAME, PRECEDENCE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {

        // find relative attributes.
        final PaginationTagAccessor attrs = new PaginationTagAccessor(tag, getDialectPrefix());
        Arrays.stream(attrs.getAttributeNames()).forEach(a -> structureHandler.removeAttribute(getDialectPrefix(), a));

        // find page.
        final Page<?> page = getPage(context, attributeValue);
        if (page == null) {
            logger.debug("cannot found page.");
            return;
        }

        // build element.
        structureHandler.setBody(buildBody(context, page, attrs), false);
    }

    private Page<?> getPage(ITemplateContext context, String attributeValue) {
        return ExpressionUtils.execute(context,
                (StringUtils.isEmptyOrWhitespace(attributeValue)) ? DEFAULT_PAGE_EXPRESSION : attributeValue,
                Page.class);
    }

    private IModel buildBody(ITemplateContext context, Page<?> page, PaginationTagAccessor attrs) {

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
        final boolean enableLinkOfCurrentPage = attrs.isEnableLinkOfCurrentPage();

        final ThymeleafPaginationInfo info = new ThymeleafPaginationInfo(context, page, hrefTmpl, maxDisplayCount);

        final IModelFactory modelFactory = context.getModelFactory();
        final IModel model = modelFactory.createModel();

        if (info.isFirstPage()) {
            model.addModel(buildInnerElement(context, innerElement, disabledClass, disabledHref, firstLinkText));
            model.addModel(buildInnerElement(context, innerElement, disabledClass, disabledHref, previousLinkText));
        } else {
            model.addModel(buildInnerElement(context, innerElement, activeClass, info.getFirstUrl(), firstLinkText));
            model.addModel(
                    buildInnerElement(context, innerElement, activeClass, info.getPreviousUrl(), previousLinkText));
        }

        model.addModel(buildPageNumberElements(context, info, innerElement, activeClass, disabledClass, disabledHref,
                enableLinkOfCurrentPage));

        if (info.isLastPage()) {
            model.addModel(buildInnerElement(context, innerElement, disabledClass, disabledHref, nextLinkText));
            model.addModel(buildInnerElement(context, innerElement, disabledClass, disabledHref, lastLinkText));
        } else {
            model.addModel(buildInnerElement(context, innerElement, activeClass, info.getNextUrl(), nextLinkText));
            model.addModel(buildInnerElement(context, innerElement, activeClass, info.getLastUrl(), lastLinkText));
        }

        return model;
    }

    private IModel buildPageNumberElements(ITemplateContext context, ThymeleafPaginationInfo info, String innerElement,
            String activeClass, String disabledClass, String disabledHref, boolean enableLinkOfCurrentPage) {

        final IModelFactory modelFactory = context.getModelFactory();
        final IModel model = modelFactory.createModel();

        BeginAndEnd be = info.getBeginAndEnd();
        IntStream.rangeClosed(be.getBegin(), be.getEnd()).forEachOrdered(i -> {
            if (info.isCurrent(i) && !enableLinkOfCurrentPage) {
                model.addModel(
                        buildInnerElement(context, innerElement, disabledClass, disabledHref, String.valueOf(i + 1)));
            } else {
                model.addModel(buildInnerElement(context, innerElement, activeClass, info.getPageUrl(i),
                        String.valueOf(i + 1)));
            }
        });

        return model;
    }

    private IModel buildInnerElement(ITemplateContext context, String innerElement, String activeOrDisabled,
            String href, String text) {

        final IModelFactory modelFactory = context.getModelFactory();
        final IModel model = modelFactory.createModel();

        model.add(modelFactory.createOpenElementTag(innerElement, PaginationInfo.CLASS_ATTR, activeOrDisabled));

        if (StringUtils.isEmptyOrWhitespace(href) || StringUtils.isEmptyOrWhitespace(text)) {
            model.add(modelFactory.createText(HtmlEscape.escapeHtml5(text)));
        } else {
            model.add(modelFactory.createOpenElementTag(PaginationInfo.A_ELM, PaginationInfo.HREF_ATTR,
                    HtmlEscape.escapeHtml5(href)));
            model.add(modelFactory.createText(HtmlEscape.escapeHtml5(text)));
            model.add(modelFactory.createCloseElementTag(PaginationInfo.A_ELM));
        }

        model.add(modelFactory.createCloseElementTag(innerElement));

        return model;
    }

}
