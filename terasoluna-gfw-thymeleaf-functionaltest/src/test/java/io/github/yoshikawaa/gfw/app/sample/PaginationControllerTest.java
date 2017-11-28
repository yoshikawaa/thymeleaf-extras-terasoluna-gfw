package io.github.yoshikawaa.gfw.app.sample;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Iterator;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import io.github.yoshikawaa.gfw.test.support.WebClientTestSupport;

public class PaginationControllerTest extends WebClientTestSupport {

    @Override
    protected String path() {
        return "/sample/pagination";
    }

    @Test
    public void testPagination() throws Exception {
        HtmlPage page = client.getPage(url());

        new PaginationMatcher(page, "//ul[@id='pagination-1']", "li").matches("disabled", "javascript:void(0)", "<<")
                .matches("disabled", "javascript:void(0)", "<")
                .matches("disabled", "javascript:void(0)", "1")
                .matches("active", "?page=1&size=20", "2")
                .matches("active", "?page=2&size=20", "3")
                .matches("active", "?page=3&size=20", "4")
                .matches("active", "?page=4&size=20", "5")
                .matches("active", "?page=5&size=20", "6")
                .matches("active", "?page=6&size=20", "7")
                .matches("active", "?page=7&size=20", "8")
                .matches("active", "?page=8&size=20", "9")
                .matches("active", "?page=9&size=20", "10")
                .matches("active", "?page=1&size=20", ">")
                .matches("active", "?page=49&size=20", ">>")
                .end();

        new PaginationMatcher(page, "//ul[@id='pagination-2' and @class='pagination']", "li")
                .matches("disabled", "javascript:void(0)", "<<")
                .matches("disabled", "javascript:void(0)", "<")
                .matches("disabled", "javascript:void(0)", "1")
                .matches("active", "?page=1&size=20", "2")
                .matches("active", "?page=2&size=20", "3")
                .matches("active", "?page=3&size=20", "4")
                .matches("active", "?page=4&size=20", "5")
                .matches("active", "?page=5&size=20", "6")
                .matches("active", "?page=6&size=20", "7")
                .matches("active", "?page=7&size=20", "8")
                .matches("active", "?page=8&size=20", "9")
                .matches("active", "?page=9&size=20", "10")
                .matches("active", "?page=1&size=20", ">")
                .matches("active", "?page=49&size=20", ">>")
                .end();

        new PaginationMatcher(page, "//div[@id='pagination-3']", "span").matches("disabled", "javascript:void(0)", "<<")
                .matches("disabled", "javascript:void(0)", "<")
                .matches("disabled", "javascript:void(0)", "1")
                .matches("active", "?page=1&size=20", "2")
                .matches("active", "?page=2&size=20", "3")
                .matches("active", "?page=3&size=20", "4")
                .matches("active", "?page=4&size=20", "5")
                .matches("active", "?page=5&size=20", "6")
                .matches("active", "?page=6&size=20", "7")
                .matches("active", "?page=7&size=20", "8")
                .matches("active", "?page=8&size=20", "9")
                .matches("active", "?page=9&size=20", "10")
                .matches("active", "?page=1&size=20", ">")
                .matches("active", "?page=49&size=20", ">>")
                .end();

        new PaginationMatcher(page, "//ul[@id='pagination-4']", "li").matches("d", "javascript:void(0)", "<<")
                .matches("d", "javascript:void(0)", "<")
                .matches("d", "javascript:void(0)", "1")
                .matches("a", "?page=1&size=20", "2")
                .matches("a", "?page=2&size=20", "3")
                .matches("a", "?page=3&size=20", "4")
                .matches("a", "?page=4&size=20", "5")
                .matches("a", "?page=5&size=20", "6")
                .matches("a", "?page=6&size=20", "7")
                .matches("a", "?page=7&size=20", "8")
                .matches("a", "?page=8&size=20", "9")
                .matches("a", "?page=9&size=20", "10")
                .matches("a", "?page=1&size=20", ">")
                .matches("a", "?page=49&size=20", ">>")
                .end();

        new PaginationMatcher(page, "//ul[@id='pagination-5']", "li").matches("disabled", "javascript:void(0)", "first")
                .matches("disabled", "javascript:void(0)", "prev")
                .matches("disabled", "javascript:void(0)", "1")
                .matches("active", "?page=1&size=20", "2")
                .matches("active", "?page=2&size=20", "3")
                .matches("active", "?page=3&size=20", "4")
                .matches("active", "?page=4&size=20", "5")
                .matches("active", "?page=5&size=20", "6")
                .matches("active", "?page=6&size=20", "7")
                .matches("active", "?page=7&size=20", "8")
                .matches("active", "?page=8&size=20", "9")
                .matches("active", "?page=9&size=20", "10")
                .matches("active", "?page=1&size=20", "next")
                .matches("active", "?page=49&size=20", "last")
                .end();

        new PaginationMatcher(page, "//ul[@id='pagination-6']", "li").matches("disabled", "javascript:void(0)", "<<")
                .matches("disabled", "javascript:void(0)", "<")
                .matches("active", "?page=0&size=20", "1")
                .matches("active", "?page=1&size=20", "2")
                .matches("active", "?page=2&size=20", "3")
                .matches("active", "?page=3&size=20", "4")
                .matches("active", "?page=4&size=20", "5")
                .matches("active", "?page=1&size=20", ">")
                .matches("active", "?page=49&size=20", ">>")
                .end();

        new PaginationMatcher(page, "//ul[@id='pagination-7']", "li").matches("disabled", "javascript:void(0)", "<<")
                .matches("disabled", "javascript:void(0)", "<")
                .matches("disabled", "javascript:void(0)", "1")
                .matches("active", contextPath + "/sample/pagination/1?size=20", "2")
                .matches("active", contextPath + "/sample/pagination/2?size=20", "3")
                .matches("active", contextPath + "/sample/pagination/3?size=20", "4")
                .matches("active", contextPath + "/sample/pagination/4?size=20", "5")
                .matches("active", contextPath + "/sample/pagination/5?size=20", "6")
                .matches("active", contextPath + "/sample/pagination/6?size=20", "7")
                .matches("active", contextPath + "/sample/pagination/7?size=20", "8")
                .matches("active", contextPath + "/sample/pagination/8?size=20", "9")
                .matches("active", contextPath + "/sample/pagination/9?size=20", "10")
                .matches("active", contextPath + "/sample/pagination/1?size=20", ">")
                .matches("active", contextPath + "/sample/pagination/49?size=20", ">>")
                .end();

        new PaginationMatcher(page, "//ul[@id='pagination-8']", "li").matches("disabled", "javascript:void(0)", "<<")
                .matches("disabled", "javascript:void(0)", "<")
                .matches("disabled", "javascript:void(0)", "1")
                .matches("active", "?page=1&size=20", "2")
                .matches("active", "?page=2&size=20", "3")
                .matches("active", "?page=3&size=20", "4")
                .matches("active", "?page=4&size=20", "5")
                .matches("active", "?page=5&size=20", "6")
                .matches("active", "?page=6&size=20", "7")
                .matches("active", "?page=7&size=20", "8")
                .matches("active", "?page=8&size=20", "9")
                .matches("active", "?page=9&size=20", "10")
                .matches("active", "?page=1&size=20", ">")
                .matches("active", "?page=49&size=20", ">>")
                .end();
    }

    private class PaginationMatcher {
        private Iterator<HtmlElement> elements;

        private PaginationMatcher(DomNode page, String xpath, String elementTag) {
            HtmlElement element = page.getFirstByXPath(xpath);
            this.elements = element.getElementsByTagName(elementTag).iterator();
        }

        public PaginationMatcher matches(String cssClass, String href, String text) {
            HtmlElement element = elements.next();
            assertThat(element.getAttribute("class")).isEqualTo(cssClass);
            element = (HtmlElement) element.getFirstChild();
            assertThat(element.getAttribute("href")).isEqualTo(href);
            assertThat(element.asText()).isEqualTo(text);
            return this;
        }

        public void end() {
            assertThat(elements.hasNext()).isFalse();
        }
    }
}
