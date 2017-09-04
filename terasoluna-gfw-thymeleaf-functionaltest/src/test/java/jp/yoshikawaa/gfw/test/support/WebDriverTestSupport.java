package jp.yoshikawaa.gfw.test.support;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import jp.yoshikawaa.gfw.test.config.WebDriverConfig;

@ContextConfiguration(classes = WebDriverConfig.class)
public abstract class WebDriverTestSupport extends TerasolunaGfwE2ETestSupport {

    @Autowired
    protected WebDriver driver;
}
