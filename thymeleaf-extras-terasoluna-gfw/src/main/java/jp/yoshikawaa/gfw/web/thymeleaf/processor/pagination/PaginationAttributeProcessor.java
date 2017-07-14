package jp.yoshikawaa.gfw.web.thymeleaf.processor.pagination;

import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.terasoluna.gfw.web.pagination.PaginationInfo;
import org.terasoluna.gfw.web.pagination.PaginationInfo.BeginAndEnd;
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

public class PaginationAttributeProcessor extends AbstractHtmlAttributeProcessor {
    private static final Logger logger = LoggerFactory.getLogger(PaginationAttributeProcessor.class);

    private static final String ATTRIBUTE_NAME = "pagination";
    private static final int PRECEDENCE = 12000;

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

    private static final String DEFAULT_PAGE_EXPRESSION = "${page}";
    private static final String DEFAULT_FIRST_LINK_TEXT = "<<";
    private static final String DEFAULT_PREVIOUS_LINK_TEXT = "<";
    private static final String DEFAULT_NEXT_LINK_TEXT = ">";
    private static final String DEFAULT_LAST_LINK_TEXT = ">>";
    private static final String DEFAULT_HREF_TMPL = null;
    private static final String DEFAULT_CRITERIA_QUERY = null;
    private static final boolean DEFAULT_DISABLE_HTML_ESCAPE_OF_CRITERIA_QUERY = false;
    private static final boolean DEFAULT_ENABLE_LINK_OF_CURRENT_PAGE = false;

    public PaginationAttributeProcessor(String dialectPrefix) {
        super(dialectPrefix, ATTRIBUTE_NAME, PRECEDENCE);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {

        // find page.
        Page<?> page = getPage(context, attributeValue);

        // find relative attributes.
        final String innerElement = ProcessorUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_INNER_ELEMENT,
                PaginationInfo.DEFAULT_INNER_ELM);
        final String disabledClass = ProcessorUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_DISABLED_CLASS,
                PaginationInfo.DEFAULT_DISABLED_CLASS);
        final String activeClass = ProcessorUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_ACTIVE_CLASS,
                PaginationInfo.DEFAULT_ACTIVE_CLASS);
        final String firstLinkText = ProcessorUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_FIRST_LINK_TEXT,
                DEFAULT_FIRST_LINK_TEXT);
        final String previousLinkText = ProcessorUtils.getAttributeValue(tag, dialectPrefix,
                ATTRIBUTE_PREVIOUS_LINK_TEXT, DEFAULT_PREVIOUS_LINK_TEXT);
        final String nextLinkText = ProcessorUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_NEXT_LINK_TEXT,
                DEFAULT_NEXT_LINK_TEXT);
        final String lastLinkText = ProcessorUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_LAST_LINK_TEXT,
                DEFAULT_LAST_LINK_TEXT);
        final int maxDisplayCount = ProcessorUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_MAX_DISPLAY_COUNT,
                PaginationInfo.DEFAULT_MAX_DISPLAY_COUNT);
        final String disabledHref = ProcessorUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_DISABLED_HREF,
                PaginationInfo.DEFAULT_DISABLED_HREF);
        final String hrefTmpl = ProcessorUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_HREF_TMPL,
                DEFAULT_HREF_TMPL);
        final String criteriaQuery = ProcessorUtils.getAttributeValue(tag, dialectPrefix, ATTRIBUTE_CRITERIA_QUERY,
                DEFAULT_CRITERIA_QUERY);
        final boolean disableHtmlEscapeOfCriteriaQuery = ProcessorUtils.getAttributeValue(tag, dialectPrefix,
                ATTRIBUTE_DISABLE_HTML_ESCAPE_OF_CRITERIA_QUERY, DEFAULT_DISABLE_HTML_ESCAPE_OF_CRITERIA_QUERY);
        final boolean enableLinkOfCurrentPage = ProcessorUtils.getAttributeValue(tag, dialectPrefix,
                ATTRIBUTE_ENABLE_LINK_OF_CURRENT_PAGE, DEFAULT_ENABLE_LINK_OF_CURRENT_PAGE);
        removeRelativeAttributes(structureHandler);

        // exist page?
        if (page == null) {
            logger.debug("cannot found page.");
            return;
        }

        // build element.
        final ThymeleafPaginationInfo info = new ThymeleafPaginationInfo(context, page, hrefTmpl, criteriaQuery,
                disableHtmlEscapeOfCriteriaQuery, maxDisplayCount);
        structureHandler.setBody(buildBody(context, info, innerElement, activeClass, disabledClass, firstLinkText,
                previousLinkText, nextLinkText, lastLinkText, maxDisplayCount, disabledHref, enableLinkOfCurrentPage),
                false);
    }

    private Page<?> getPage(ITemplateContext context, String attributeValue) {

        return ExpressionUtils.execute(context,
                (StringUtils.hasText(attributeValue)) ? attributeValue : DEFAULT_PAGE_EXPRESSION, Page.class);
    }

    private void removeRelativeAttributes(IElementTagStructureHandler structureHandler) {

        structureHandler.removeAttribute(dialectPrefix, ATTRIBUTE_INNER_ELEMENT);
        structureHandler.removeAttribute(dialectPrefix, ATTRIBUTE_DISABLED_CLASS);
        structureHandler.removeAttribute(dialectPrefix, ATTRIBUTE_ACTIVE_CLASS);
        structureHandler.removeAttribute(dialectPrefix, ATTRIBUTE_FIRST_LINK_TEXT);
        structureHandler.removeAttribute(dialectPrefix, ATTRIBUTE_PREVIOUS_LINK_TEXT);
        structureHandler.removeAttribute(dialectPrefix, ATTRIBUTE_NEXT_LINK_TEXT);
        structureHandler.removeAttribute(dialectPrefix, ATTRIBUTE_LAST_LINK_TEXT);
        structureHandler.removeAttribute(dialectPrefix, ATTRIBUTE_MAX_DISPLAY_COUNT);
        structureHandler.removeAttribute(dialectPrefix, ATTRIBUTE_DISABLED_HREF);
        structureHandler.removeAttribute(dialectPrefix, ATTRIBUTE_HREF_TMPL);
        structureHandler.removeAttribute(dialectPrefix, ATTRIBUTE_CRITERIA_QUERY);
        structureHandler.removeAttribute(dialectPrefix, ATTRIBUTE_DISABLE_HTML_ESCAPE_OF_CRITERIA_QUERY);
        structureHandler.removeAttribute(dialectPrefix, ATTRIBUTE_ENABLE_LINK_OF_CURRENT_PAGE);
    }

    private IModel buildBody(ITemplateContext context, ThymeleafPaginationInfo info, String innerElement,
            String activeClass, String disabledClass, String firstLinkText, String previousLinkText,
            String nextLinkText, String lastLinkText, int maxDisplayCount, String disabledHref,
            boolean enableLinkOfCurrentPage) {

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

        if (StringUtils.hasText(href) && StringUtils.hasText(text)) {
            model.add(modelFactory.createOpenElementTag(PaginationInfo.A_ELM, PaginationInfo.HREF_ATTR, href));
            model.add(modelFactory.createText(HtmlEscape.escapeHtml5(text)));
            model.add(modelFactory.createCloseElementTag(PaginationInfo.A_ELM));
        } else {
            model.add(modelFactory.createText(HtmlEscape.escapeHtml5(text)));
        }

        model.add(modelFactory.createCloseElementTag(innerElement));

        return model;
    }

}
