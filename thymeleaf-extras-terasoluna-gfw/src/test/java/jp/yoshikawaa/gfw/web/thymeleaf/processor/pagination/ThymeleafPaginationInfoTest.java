package jp.yoshikawaa.gfw.web.thymeleaf.processor.pagination;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.context.IEngineContext;

import jp.yoshikawaa.gfw.test.engine.TerasolunaGfwTestEngine;

public class ThymeleafPaginationInfoTest {

    @Test
    public void testPaginationInfo() {
        // setup.
        final String template = "<input />";
        final IEngineContext context = new TerasolunaGfwTestEngine().context(template);

        ThymeleafPaginationInfo info = new ThymeleafPaginationInfo(context, buildPage(5, 10),
                "@{/sample/pagination/{page}(page=${page},size=${size})}", 5);

        // execute.
        assertThat(info.getPageUrl(5)).isEqualTo("/sample/pagination/5?size=10");
    }

    @Test
    public void testCriteriaQuery() {
        // setup.
        final String template = "<input />";
        final IEngineContext context = new TerasolunaGfwTestEngine().context(template);

        ThymeleafPaginationInfo info = new ThymeleafPaginationInfo(context, buildPage(5, 10),
                "@{/sample/pagination/{page}(page=${page},size=${size})}", "item=sample", true, 5);

        // execute.
        assertThat(info.getPageUrl(5)).isEqualTo("/sample/pagination/5?size=10&item=sample");
    }

    @Test
    public void testCriteriaQuery2() {
        // setup.
        final String template = "<input />";
        final IEngineContext context = new TerasolunaGfwTestEngine().context(template);

        ThymeleafPaginationInfo info = new ThymeleafPaginationInfo(context, buildPage(5, 10),
                "@{/sample/pagination/{page}/{size}(page=${page},size=${size})}", "item=sample", true, 5);

        // execute.
        assertThat(info.getPageUrl(5)).isEqualTo("/sample/pagination/5/10?item=sample");
    }

    @Test
    public void testCriteriaQuery3() {
        // setup.
        final String template = "<input />";
        final IEngineContext context = new TerasolunaGfwTestEngine().context(template);

        ThymeleafPaginationInfo info = new ThymeleafPaginationInfo(context, buildPage(5, 10),
                "@{/sample/pagination/{page}(page=${page})}", "item=sample", true, 5);

        // execute.
        assertThat(info.getPageUrl(5)).isEqualTo("/sample/pagination/5?item=sample");
    }

    @Test
    public void testCriteriaQueryExpression() {
        // setup.
        final String template = "<input />";
        final Map<String, String> query = Collections.singletonMap("item", "sample");
        final IEngineContext context = new TerasolunaGfwTestEngine().variable("query", query).context(template);

        ThymeleafPaginationInfo info = new ThymeleafPaginationInfo(context, buildPage(5, 10),
                "@{/sample/pagination/{page}(page=${page},size=${size})}", "${#f.query(query)}", true, 5);

        // execute.
        assertThat(info.getPageUrl(5)).isEqualTo("/sample/pagination/5?size=10&item=sample");
    }

    private Page<Integer> buildPage(int page, int size) {

        final Pageable pageable = new PageRequest(page, size);
        List<Integer> content = IntStream
                .rangeClosed(pageable.getOffset(), pageable.getOffset() + pageable.getPageSize() + 1)
                .boxed()
                .collect(Collectors.toList());
        return new PageImpl<Integer>(content, pageable, 1000);
    }

}
