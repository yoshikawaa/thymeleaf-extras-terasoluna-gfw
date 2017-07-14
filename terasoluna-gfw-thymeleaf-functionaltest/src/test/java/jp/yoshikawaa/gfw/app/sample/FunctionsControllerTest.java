package jp.yoshikawaa.gfw.app.sample;

import static jp.yoshikawaa.gfw.app.WebClientMatcher.hasText;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import jp.yoshikawaa.gfw.app.WebClientConfigurator;

public class FunctionsControllerTest extends WebClientConfigurator {

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
