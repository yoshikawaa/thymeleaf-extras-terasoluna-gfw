package jp.yoshikawaa.gfw.web.thymeleaf.processor.pagination;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.terasoluna.gfw.web.pagination.PaginationInfo;
import org.terasoluna.gfw.web.pagination.PaginationInfo.BeginAndEnd;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Text;
import org.thymeleaf.processor.attr.AbstractMarkupRemovalAttrProcessor;

import jp.yoshikawaa.gfw.web.thymeleaf.util.ExpressionUtils;
import jp.yoshikawaa.gfw.web.thymeleaf.util.ProcessorUtils;

public class PaginationAttrProcessor extends AbstractMarkupRemovalAttrProcessor {
    private static final Logger logger = LoggerFactory.getLogger(PaginationAttrProcessor.class);

    private static final String ATTRIBUTE_NAME = "pagination";

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

    private final String dialectPrefix;

    public PaginationAttrProcessor(String dialectPrefix) {
        super(ATTRIBUTE_NAME);
        this.dialectPrefix = dialectPrefix;
    }

    @Override
    public int getPrecedence() {
        return 1300;
    }

    @Override
    protected RemovalType getRemovalType(final Arguments arguments, final Element element, final String attributeName) {

        // find page.
        Page<?> page = getPage(arguments, element, attributeName);

        // find relative attributes.
        final String innerElement = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_INNER_ELEMENT,
                PaginationInfo.DEFAULT_INNER_ELM);
        final String disabledClass = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_DISABLED_CLASS,
                PaginationInfo.DEFAULT_DISABLED_CLASS);
        final String activeClass = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_ACTIVE_CLASS,
                PaginationInfo.DEFAULT_ACTIVE_CLASS);
        final String firstLinkText = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_FIRST_LINK_TEXT,
                DEFAULT_FIRST_LINK_TEXT);
        final String previousLinkText = ProcessorUtils.getAttributeValue(element, dialectPrefix,
                ATTRIBUTE_PREVIOUS_LINK_TEXT, DEFAULT_PREVIOUS_LINK_TEXT);
        final String nextLinkText = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_NEXT_LINK_TEXT,
                DEFAULT_NEXT_LINK_TEXT);
        final String lastLinkText = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_LAST_LINK_TEXT,
                DEFAULT_LAST_LINK_TEXT);
        final int maxDisplayCount = ProcessorUtils.getAttributeValue(element, dialectPrefix,
                ATTRIBUTE_MAX_DISPLAY_COUNT, PaginationInfo.DEFAULT_MAX_DISPLAY_COUNT);
        final String disabledHref = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_DISABLED_HREF,
                PaginationInfo.DEFAULT_DISABLED_HREF);
        final String hrefTmpl = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_HREF_TMPL,
                DEFAULT_HREF_TMPL);
        final String criteriaQuery = ProcessorUtils.getAttributeValue(element, dialectPrefix, ATTRIBUTE_CRITERIA_QUERY,
                DEFAULT_CRITERIA_QUERY);
        final boolean disableHtmlEscapeOfCriteriaQuery = ProcessorUtils.getAttributeValue(element, dialectPrefix,
                ATTRIBUTE_DISABLE_HTML_ESCAPE_OF_CRITERIA_QUERY, DEFAULT_DISABLE_HTML_ESCAPE_OF_CRITERIA_QUERY);
        final boolean enableLinkOfCurrentPage = ProcessorUtils.getAttributeValue(element, dialectPrefix,
                ATTRIBUTE_ENABLE_LINK_OF_CURRENT_PAGE, DEFAULT_ENABLE_LINK_OF_CURRENT_PAGE);
        removeRelativeAttributes(element);

        // exist page?
        if (page == null) {
            logger.debug("cannot found page.");
            return RemovalType.ELEMENT;
        }

        // build element.
        final ThymeleafPaginationInfo info = new ThymeleafPaginationInfo(arguments, page, hrefTmpl, criteriaQuery,
                disableHtmlEscapeOfCriteriaQuery, maxDisplayCount);
        buildBody(element, info, innerElement, activeClass, disabledClass, firstLinkText, previousLinkText,
                nextLinkText, lastLinkText, maxDisplayCount, disabledHref, enableLinkOfCurrentPage);

        return RemovalType.NONE;
    }

    private Page<?> getPage(Arguments arguments, Element element, String attributeName) {

        final String attributeValue = element.getAttributeValue(attributeName);
        return ExpressionUtils.execute(arguments,
                (StringUtils.hasText(attributeValue)) ? attributeValue : DEFAULT_PAGE_EXPRESSION, Page.class);
    }

    private void removeRelativeAttributes(Element element) {

        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_INNER_ELEMENT);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_DISABLED_CLASS);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_ACTIVE_CLASS);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_FIRST_LINK_TEXT);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_PREVIOUS_LINK_TEXT);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_NEXT_LINK_TEXT);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_LAST_LINK_TEXT);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_MAX_DISPLAY_COUNT);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_DISABLED_HREF);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_HREF_TMPL);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_CRITERIA_QUERY);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_DISABLE_HTML_ESCAPE_OF_CRITERIA_QUERY);
        ProcessorUtils.removeAttribute(element, dialectPrefix, ATTRIBUTE_ENABLE_LINK_OF_CURRENT_PAGE);
    }

    private void buildBody(Element element, ThymeleafPaginationInfo info, String innerElement, String activeClass,
            String disabledClass, String firstLinkText, String previousLinkText, String nextLinkText,
            String lastLinkText, int maxDisplayCount, String disabledHref, boolean enableLinkOfCurrentPage) {

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

        if (StringUtils.hasText(href) && StringUtils.hasText(text)) {
            Element anchor = new Element(PaginationInfo.A_ELM);
            anchor.setAttribute(PaginationInfo.HREF_ATTR, href);
            anchor.addChild(new Text(text));
            inner.addChild(anchor);
        } else {
            inner.addChild(new Text(text));
        }

        return inner;
    }

}
