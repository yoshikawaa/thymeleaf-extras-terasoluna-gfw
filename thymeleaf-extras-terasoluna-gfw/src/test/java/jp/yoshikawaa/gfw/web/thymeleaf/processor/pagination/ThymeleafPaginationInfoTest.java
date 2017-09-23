package jp.yoshikawaa.gfw.web.thymeleaf.processor.pagination;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.Arguments;
import org.thymeleaf.TestArgumentsBuilder;
import org.thymeleaf.context.TestWebContextBuilder;
import org.thymeleaf.context.WebContext;

public class ThymeleafPaginationInfoTest {

    @Test
    public void testPaginationInfo() {
        // setup.
        final WebContext context = TestWebContextBuilder.from().build();
        final Arguments arguments = TestArgumentsBuilder.build(context);

        ThymeleafPaginationInfo info = new ThymeleafPaginationInfo(arguments, buildPage(5, 10),
                "@{/sample/pagination/{page}(page=${page},size=${size})}", 5);

        // execute.
        assertThat(info.getPageUrl(5)).isEqualTo("/sample/pagination/5?size=10");
    }

    @Test
    public void testCriteriaQuery() {
        // setup.
        final WebContext context = TestWebContextBuilder.from().build();
        final Arguments arguments = TestArgumentsBuilder.build(context);

        ThymeleafPaginationInfo info = new ThymeleafPaginationInfo(arguments, buildPage(5, 10),
                "@{/sample/pagination/{page}(page=${page},size=${size})}", "item=sample", true, 5);

        // execute.
        assertThat(info.getPageUrl(5)).isEqualTo("/sample/pagination/5?size=10&item=sample");
    }

    @Test
    public void testCriteriaQuery2() {
        // setup.
        final WebContext context = TestWebContextBuilder.from().build();
        final Arguments arguments = TestArgumentsBuilder.build(context);

        ThymeleafPaginationInfo info = new ThymeleafPaginationInfo(arguments, buildPage(5, 10),
                "@{/sample/pagination/{page}/{size}(page=${page},size=${size})}", "item=sample", true, 5);

        // execute.
        assertThat(info.getPageUrl(5)).isEqualTo("/sample/pagination/5/10?item=sample");
    }

    @Test
    public void testCriteriaQuery3() {
        // setup.
        final WebContext context = TestWebContextBuilder.from().build();
        final Arguments arguments = TestArgumentsBuilder.build(context);

        ThymeleafPaginationInfo info = new ThymeleafPaginationInfo(arguments, buildPage(5, 10),
                "@{/sample/pagination/{page}(page=${page})}", "item=sample", true, 5);

        // execute.
        assertThat(info.getPageUrl(5)).isEqualTo("/sample/pagination/5?item=sample");
    }

    private Page<Integer> buildPage(int page, int size) {

        final Pageable pageable = new PageRequest(page, size);
        List<Integer> content = IntStream
                .rangeClosed(pageable.getOffset(), pageable.getOffset() + pageable.getPageSize() + 1).boxed()
                .collect(Collectors.toList());
        return new PageImpl<Integer>(content, pageable, 1000);
    }

}
