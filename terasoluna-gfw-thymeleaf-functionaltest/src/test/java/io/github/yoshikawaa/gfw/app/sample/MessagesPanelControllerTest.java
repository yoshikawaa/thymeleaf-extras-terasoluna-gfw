package io.github.yoshikawaa.gfw.app.sample;

import static io.github.yoshikawaa.gfw.test.matcher.WebClientMatcher.hasText;
import static io.github.yoshikawaa.gfw.test.matcher.WebClientMatcher.hasTexts;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import io.github.yoshikawaa.gfw.test.support.WebClientTestSupport;

public class MessagesPanelControllerTest extends WebClientTestSupport {

    @Override
    protected String path() {
        return "/sample/messages-panel";
    }

    @Test
    public void testMessage() throws Exception {
        HtmlPage page = client.getPage(url());

        hasTexts(page, "//div[@id='messages-panel-1' and @class='alert alert-success']/ul/li",
                "Business error occurred!", "</li><li>message</li>");
        hasTexts(page, "//div[@id='messages-panel-1' and @class='alert alert-success']/ul/li",
                "Business error occurred!", "</li><li>message</li>");
        hasTexts(page, "//div[@id='messages-panel-2' and @class='message message-success']/ul/li",
                "Business error occurred!", "</li><li>message</li>");
        hasTexts(page, "//div[@id='messages-panel-3' and @class='success']/ul/li", "Business error occurred!",
                "</li><li>message</li>");
        hasTexts(page, "//div[@id='messages-panel-4' and @class='alert alert-warn']/ul/li", "Business error occurred!",
                "</li><li>message</li>");
        hasTexts(page, "//table[@id='messages-panel-5' and @class='alert alert-success']/tbody/tr/td",
                "Business error occurred!", "</li><li>message</li>");
        hasTexts(page, "//div[@id='messages-panel-6' and @class='alert alert-success']/span",
                "Business error occurred!", "</li><li>message</li>");
        hasTexts(page, "//div[@id='messages-panel-7' and @class='alert alert-success']/ul/li",
                "Business error occurred!", "", "message");
        hasTexts(page, "//div[@id='messages-panel-8' and @class='alert alert-success']/ul/li", "custom message");
    }

    @Test
    public void testNonMessage() throws Exception {
        HtmlPage page = client.getPage(url() + "?non-message");

        List<?> elements = page.getByXPath("//div[@id='wrapper']/div/*");
        elements.stream().map(e -> (HtmlElement) e).forEach(e -> {
            assertThat(e.hasAttribute("class")).isFalse();
            assertThat(e.getChildElementCount()).isEqualTo(0);
        });
    }

    @Test
    public void testBusinessError() throws Exception {
        HtmlPage page = client.getPage(url() + "?business-error");

        hasText(page, "//div[@class='error']", "[e.xx.fw.8001] Business error occurred!");
        hasTexts(page, "//div[@class='error']/div[@class='alert alert-error']/ul/li", "error1", "error2");
    }
}
