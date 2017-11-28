package io.github.yoshikawaa.gfw.web.thymeleaf.processor.pagination;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.thymeleaf.dom.Element;

import io.github.yoshikawaa.gfw.test.engine.TerasolunaGfwTestEngine;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.pagination.PaginationAttrAccessor;

public class PaginationAttrAccessorTest {

    @Test
    public void testAccessor() {
        // setup.
        final StringBuilder template = new StringBuilder("<ul t:pagination='' ");
        template.append("t:inner-element='span' ");
        template.append("t:disabled-class='d' ");
        template.append("t:active-class='a' ");
        template.append("t:first-link-text='first' ");
        template.append("t:previous-link-text='prev' ");
        template.append("t:next-link-text='next' ");
        template.append("t:last-link-text='last' ");
        template.append("t:max-display-count='5' ");
        template.append("t:disabled-href='disabled' ");
        template.append("t:href-tmpl='@{/sample/pagination/{page}(page=${page},size=${size})}' ");
        template.append("t:criteria-query='item=sample' ");
        template.append("t:disable-html-escape-of-criteria-query='true' ");
        template.append("t:enable-link-of-current-page='true' ");
        template.append("/>");

        final Element element = new TerasolunaGfwTestEngine().tag(template.toString());

        // execute.
        PaginationAttrAccessor accessor = new PaginationAttrAccessor(element, "t");

        // assert.
        assertThat(accessor.getInnerElement()).isEqualTo("span");
        assertThat(accessor.getDisabledClass()).isEqualTo("d");
        assertThat(accessor.getActiveClass()).isEqualTo("a");
        assertThat(accessor.getFirstLinkText()).isEqualTo("first");
        assertThat(accessor.getPreviousLinkText()).isEqualTo("prev");
        assertThat(accessor.getNextLinkText()).isEqualTo("next");
        assertThat(accessor.getLastLinkText()).isEqualTo("last");
        assertThat(accessor.getMaxDisplayCount()).isEqualTo(5);
        assertThat(accessor.getDisabledHref()).isEqualTo("disabled");
        assertThat(accessor.getHrefTmpl()).isEqualTo("@{/sample/pagination/{page}(page=${page},size=${size})}");
        assertThat(accessor.getCriteriaQuery()).isEqualTo("item=sample");
        assertThat(accessor.isDisableHtmlEscapeOfCriteriaQuery()).isTrue();
        assertThat(accessor.isEnableLinkOfCurrentPage()).isTrue();
        assertThat(accessor.getAttributeNames()).containsSequence("inner-element", "disabled-class", "active-class",
                "first-link-text", "previous-link-text", "next-link-text", "last-link-text", "max-display-count",
                "disabled-href", "href-tmpl", "criteria-query", "disable-html-escape-of-criteria-query",
                "enable-link-of-current-page");
    }

}
