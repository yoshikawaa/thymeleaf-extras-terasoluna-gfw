package io.github.yoshikawaa.gfw.app.sample;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import io.github.yoshikawaa.gfw.test.support.WebClientTestSupport;

public class MultiLineTextControllerTest extends WebClientTestSupport {

    @Override
    protected String path() {
        return "/sample/mtext";
    }

    @Test
    public void testMultiLineText() throws Exception {
        HtmlPage page = client.getPage(url());

        assertThat(page.getElementById("text1").asXml()).contains("<br/>");
        assertThat(page.getElementById("text2").asXml()).contains("<br/>");
        assertThat(page.getElementById("text3").asXml()).contains("<br/>");
    }
}
