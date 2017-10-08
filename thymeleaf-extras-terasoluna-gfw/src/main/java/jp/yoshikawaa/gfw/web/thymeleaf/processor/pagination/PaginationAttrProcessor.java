package jp.yoshikawaa.gfw.web.thymeleaf.processor.pagination;

import java.util.ArrayList;
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
import org.thymeleaf.processor.attr.AbstractMarkupRemovalAttrProcessor;
import org.thymeleaf.util.StringUtils;

import jp.yoshikawaa.gfw.web.thymeleaf.util.ExpressionUtils;

public class PaginationAttrProcessor extends AbstractMarkupRemovalAttrProcessor {

    private static final Logger logger = LoggerFactory.getLogger(PaginationAttrProcessor.class);

    private static final String ATTRIBUTE_NAME = "pagination";
    private static final int PRECEDENCE = 1200;

    private static final String DEFAULT_PAGE_EXPRESSION = "${page}";

    private final String dialectPrefix;

    public PaginationAttrProcessor(String dialectPrefix) {
        super(ATTRIBUTE_NAME);
        this.dialectPrefix = dialectPrefix;
    }

    @Override
    public int getPrecedence() {
        return PRECEDENCE;
    }

    @Override
    protected RemovalType getRemovalType(final Arguments arguments, final Element element, final String attributeName) {

        // find relative attributes.
        PaginationAttrAccessor attrs = new PaginationAttrAccessor(element, dialectPrefix);
        attrs.removeAttributes(element);

        // find page.
        final String attributeValue = element.getAttributeValue(attributeName);
        Page<?> page = getPage(arguments, attributeValue);
        if (page == null) {
            logger.debug("cannot found page.");
            return RemovalType.ELEMENT;
        }

        // build element.
        buildBody(arguments, element, page, attrs);

        return RemovalType.NONE;
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
