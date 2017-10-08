package jp.yoshikawaa.gfw.web.thymeleaf.processor.pagination;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.thymeleaf.engine.TestElementTagBuilder;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.TestElementTagStructureHandler;

public class PaginationTagAccessorTest {

    @Test
    public void testAccessor() {
        // setup.
        final StringBuilder template = new StringBuilder("<ul t:pagination ");
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

        final IProcessableElementTag tag = TestElementTagBuilder.standalone(template.toString());

        // execute.
        PaginationTagAccessor accessor = new PaginationTagAccessor(tag, "t");
        TestElementTagStructureHandler structureHandler = new TestElementTagStructureHandler(tag);
        accessor.removeAttributes(structureHandler);

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
        assertThat(structureHandler.getAttributes()).doesNotContainKeys("t:inner-element", "t:disabled-class",
                "t:active-class", "t:first-link-text", "t:previous-link-text", "t:next-link-text", "t:last-link-text",
                "t:max-display-count", "t:disabled-href", "t:href-tmpl", "t:criteria-query",
                "t:disable-html-escape-of-criteria-query", "t:enable-link-of-current-page");
    }

}
