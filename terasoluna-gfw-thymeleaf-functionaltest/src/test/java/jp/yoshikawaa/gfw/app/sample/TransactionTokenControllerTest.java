package jp.yoshikawaa.gfw.app.sample;

import static jp.yoshikawaa.gfw.app.WebClientMatcher.click;
import static jp.yoshikawaa.gfw.app.WebClientMatcher.exists;
import static jp.yoshikawaa.gfw.app.WebClientMatcher.hasText;
import static jp.yoshikawaa.gfw.app.WebClientMatcher.notExists;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import jp.yoshikawaa.gfw.app.WebClientConfigurator;

public class TransactionTokenControllerTest extends WebClientConfigurator {

    @Override
    protected String path() {
        return "/sample/transaction-token";
    }

    @Test
    public void testToken() throws Exception {
        HtmlPage page = client.getPage(url());
        
        exists(page, "//form/input[@type='hidden' and @name='_csrf' and @value]");
        exists(page, "//form/input[@type='hidden' and @name='_TRANSACTION_TOKEN' and @value]");
        
        page = click(page, "//form/input[@type='submit']");
        
        hasText(page, "//div/span", "token check succeed.");
    }
    
    @Test
    public void testNonToken() throws Exception {
        HtmlPage page = client.getPage(url() + "?non-token");
        
        exists(page, "//form/input[@type='hidden' and @name='_csrf' and @value]");
        notExists(page, "//form/input[@type='hidden' and @name='_TRANSACTION_TOKEN' and @value]");
        
        page = click(page, "//form/input[@type='submit']");
        
        hasText(page, "//div[@class='error']", "[e.xx.fw.7001] Illegal screen flow detected!");
    }
}
