package io.github.yoshikawaa.gfw.app.sample;

import static io.github.yoshikawaa.gfw.test.matcher.WebClientMatcher.hasText;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import io.github.yoshikawaa.gfw.test.support.WebClientTestSupport;

public class FunctionsControllerTest extends WebClientTestSupport {

    @Override
    protected String path() {
        return "/sample/functions";
    }

    @Test
    public void testFunctionH() throws Exception {
        HtmlPage page = client.getPage(url());

        hasText(page, "//div[@id='naked']/span", "hoge");
        hasText(page, "//div[@id='escaped']", "<span>hoge</span>");
    }
}
