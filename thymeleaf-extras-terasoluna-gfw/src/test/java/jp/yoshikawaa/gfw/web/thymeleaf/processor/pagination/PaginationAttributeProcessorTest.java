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
import org.thymeleaf.processor.element.TestElementTagStructureHandler;

import jp.yoshikawaa.gfw.web.thymeleaf.processor.TerasolunaGfwAttributeProcessorTestSupport;

public class PaginationAttributeProcessorTest extends TerasolunaGfwAttributeProcessorTestSupport {

    public PaginationAttributeProcessorTest() {
        super(PaginationAttributeProcessor.class);
    }

    @Test
    public void testPagination() {
        // setup.
        final String template = "<ul t:pagination />";
        final Page<Integer> page = buildPage(5, 10);
        final Map<String, Object> variables = Collections.singletonMap("page", page);

        // execute.
        TestElementTagStructureHandler structureHandler = process(template, null, variables);

        // assert.
        assertThat(structureHandler.getBody()).containsSequence(
                "<li class='active'><a href='?page=0&amp;size=10'>&lt;&lt;</a></li>",
                "<li class='active'><a href='?page=4&amp;size=10'>&lt;</a></li>",
                "<li class='active'><a href='?page=0&amp;size=10'>1</a></li>",
                "<li class='active'><a href='?page=1&amp;size=10'>2</a></li>",
                "<li class='active'><a href='?page=2&amp;size=10'>3</a></li>",
                "<li class='active'><a href='?page=3&amp;size=10'>4</a></li>",
                "<li class='active'><a href='?page=4&amp;size=10'>5</a></li>",
                "<li class='disabled'><a href='javascript:void(0)'>6</a></li>",
                "<li class='active'><a href='?page=6&amp;size=10'>7</a></li>",
                "<li class='active'><a href='?page=7&amp;size=10'>8</a></li>",
                "<li class='active'><a href='?page=8&amp;size=10'>9</a></li>",
                "<li class='active'><a href='?page=9&amp;size=10'>10</a></li>",
                "<li class='active'><a href='?page=6&amp;size=10'>&gt;</a></li>",
                "<li class='active'><a href='?page=99&amp;size=10'>&gt;&gt;</a></li>");
    }

    @Test
    public void testPagination2() {
        // setup.
        final String template = "<ul t:pagination t:enable-link-of-current-page='true' />";
        final Page<Integer> page = buildPage(5, 10);
        final Map<String, Object> variables = Collections.singletonMap("page", page);

        // execute.
        TestElementTagStructureHandler structureHandler = process(template, null, variables);

        // assert.
        assertThat(structureHandler.getBody()).containsSequence(
                "<li class='active'><a href='?page=0&amp;size=10'>&lt;&lt;</a></li>",
                "<li class='active'><a href='?page=4&amp;size=10'>&lt;</a></li>",
                "<li class='active'><a href='?page=0&amp;size=10'>1</a></li>",
                "<li class='active'><a href='?page=1&amp;size=10'>2</a></li>",
                "<li class='active'><a href='?page=2&amp;size=10'>3</a></li>",
                "<li class='active'><a href='?page=3&amp;size=10'>4</a></li>",
                "<li class='active'><a href='?page=4&amp;size=10'>5</a></li>",
                "<li class='active'><a href='?page=5&amp;size=10'>6</a></li>",
                "<li class='active'><a href='?page=6&amp;size=10'>7</a></li>",
                "<li class='active'><a href='?page=7&amp;size=10'>8</a></li>",
                "<li class='active'><a href='?page=8&amp;size=10'>9</a></li>",
                "<li class='active'><a href='?page=9&amp;size=10'>10</a></li>",
                "<li class='active'><a href='?page=6&amp;size=10'>&gt;</a></li>",
                "<li class='active'><a href='?page=99&amp;size=10'>&gt;&gt;</a></li>");
    }

    @Test
    public void testPagination3() {
        // setup.
        final String template = "<ul t:pagination t:disabled-href='' />";
        final Page<Integer> page = buildPage(0, 10);
        final Map<String, Object> variables = Collections.singletonMap("page", page);

        // execute.
        TestElementTagStructureHandler structureHandler = process(template, null, variables);

        // assert.
        assertThat(structureHandler.getBody()).containsSequence("<li class='disabled'>&lt;&lt;</li>",
                "<li class='disabled'>&lt;</li>", "<li class='disabled'>1</li>",
                "<li class='active'><a href='?page=1&amp;size=10'>2</a></li>",
                "<li class='active'><a href='?page=2&amp;size=10'>3</a></li>",
                "<li class='active'><a href='?page=3&amp;size=10'>4</a></li>",
                "<li class='active'><a href='?page=4&amp;size=10'>5</a></li>",
                "<li class='active'><a href='?page=5&amp;size=10'>6</a></li>",
                "<li class='active'><a href='?page=6&amp;size=10'>7</a></li>",
                "<li class='active'><a href='?page=7&amp;size=10'>8</a></li>",
                "<li class='active'><a href='?page=8&amp;size=10'>9</a></li>",
                "<li class='active'><a href='?page=9&amp;size=10'>10</a></li>",
                "<li class='active'><a href='?page=1&amp;size=10'>&gt;</a></li>",
                "<li class='active'><a href='?page=99&amp;size=10'>&gt;&gt;</a></li>");
    }

    @Test
    public void testPaginationFirst() {
        // setup.
        final String template = "<ul t:pagination />";
        final Page<Integer> page = buildPage(0, 10);
        final Map<String, Object> variables = Collections.singletonMap("page", page);

        // execute.
        TestElementTagStructureHandler structureHandler = process(template, null, variables);

        // assert.
        assertThat(structureHandler.getBody()).containsSequence(
                "<li class='disabled'><a href='javascript:void(0)'>&lt;&lt;</a></li>",
                "<li class='disabled'><a href='javascript:void(0)'>&lt;</a></li>",
                "<li class='disabled'><a href='javascript:void(0)'>1</a></li>",
                "<li class='active'><a href='?page=1&amp;size=10'>2</a></li>",
                "<li class='active'><a href='?page=2&amp;size=10'>3</a></li>",
                "<li class='active'><a href='?page=3&amp;size=10'>4</a></li>",
                "<li class='active'><a href='?page=4&amp;size=10'>5</a></li>",
                "<li class='active'><a href='?page=5&amp;size=10'>6</a></li>",
                "<li class='active'><a href='?page=6&amp;size=10'>7</a></li>",
                "<li class='active'><a href='?page=7&amp;size=10'>8</a></li>",
                "<li class='active'><a href='?page=8&amp;size=10'>9</a></li>",
                "<li class='active'><a href='?page=9&amp;size=10'>10</a></li>",
                "<li class='active'><a href='?page=1&amp;size=10'>&gt;</a></li>",
                "<li class='active'><a href='?page=99&amp;size=10'>&gt;&gt;</a></li>");
    }

    @Test
    public void testPaginationLast() {
        // setup.
        final String template = "<ul t:pagination />";
        final Page<Integer> page = buildPage(99, 10);
        final Map<String, Object> variables = Collections.singletonMap("page", page);

        // execute.
        TestElementTagStructureHandler structureHandler = process(template, null, variables);

        // assert.
        assertThat(structureHandler.getBody()).containsSequence(
                "<li class='active'><a href='?page=0&amp;size=10'>&lt;&lt;</a></li>",
                "<li class='active'><a href='?page=98&amp;size=10'>&lt;</a></li>",
                "<li class='active'><a href='?page=90&amp;size=10'>91</a></li>",
                "<li class='active'><a href='?page=91&amp;size=10'>92</a></li>",
                "<li class='active'><a href='?page=92&amp;size=10'>93</a></li>",
                "<li class='active'><a href='?page=93&amp;size=10'>94</a></li>",
                "<li class='active'><a href='?page=94&amp;size=10'>95</a></li>",
                "<li class='active'><a href='?page=95&amp;size=10'>96</a></li>",
                "<li class='active'><a href='?page=96&amp;size=10'>97</a></li>",
                "<li class='active'><a href='?page=97&amp;size=10'>98</a></li>",
                "<li class='active'><a href='?page=98&amp;size=10'>99</a></li>",
                "<li class='disabled'><a href='javascript:void(0)'>100</a></li>",
                "<li class='disabled'><a href='javascript:void(0)'>&gt;</a></li>",
                "<li class='disabled'><a href='javascript:void(0)'>&gt;&gt;</a></li>");
    }

    @Test
    public void testExpression() {
        // setup.
        final String template = "<ul t:pagination='${contents}' />";
        final Page<Integer> page = buildPage(5, 10);
        final Map<String, Object> variables = Collections.singletonMap("contents", page);

        // execute.
        TestElementTagStructureHandler structureHandler = process(template, null, variables);

        // assert.
        assertThat(structureHandler.getBody()).containsSequence(
                "<li class='active'><a href='?page=0&amp;size=10'>&lt;&lt;</a></li>",
                "<li class='active'><a href='?page=4&amp;size=10'>&lt;</a></li>",
                "<li class='active'><a href='?page=0&amp;size=10'>1</a></li>",
                "<li class='active'><a href='?page=1&amp;size=10'>2</a></li>",
                "<li class='active'><a href='?page=2&amp;size=10'>3</a></li>",
                "<li class='active'><a href='?page=3&amp;size=10'>4</a></li>",
                "<li class='active'><a href='?page=4&amp;size=10'>5</a></li>",
                "<li class='disabled'><a href='javascript:void(0)'>6</a></li>",
                "<li class='active'><a href='?page=6&amp;size=10'>7</a></li>",
                "<li class='active'><a href='?page=7&amp;size=10'>8</a></li>",
                "<li class='active'><a href='?page=8&amp;size=10'>9</a></li>",
                "<li class='active'><a href='?page=9&amp;size=10'>10</a></li>",
                "<li class='active'><a href='?page=6&amp;size=10'>&gt;</a></li>",
                "<li class='active'><a href='?page=99&amp;size=10'>&gt;&gt;</a></li>");
    }

    @Test
    public void testPageNotFound() {
        // setup.
        final String template = "<ul t:pagination />";

        // execute.
        TestElementTagStructureHandler structureHandler = process(template);

        // assert.
        assertThat(structureHandler.getBody()).isEmpty();
        assertLogMessage("cannot found page.");
    }

    private Page<Integer> buildPage(int page, int size) {

        final Pageable pageable = new PageRequest(page, size);
        List<Integer> content = IntStream
                .rangeClosed(pageable.getOffset(), pageable.getOffset() + pageable.getPageSize() + 1).boxed()
                .collect(Collectors.toList());
        return new PageImpl<Integer>(content, pageable, 1000);
    }

}
