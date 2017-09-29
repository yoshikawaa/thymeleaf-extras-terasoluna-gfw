package jp.yoshikawaa.gfw.test.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.web.context.WebApplicationContext;

@Configuration
@PropertySource(value = "classpath:test.properties", ignoreResourceNotFound = true)
public class WebDriverConfig {

    protected static final Logger logger = LoggerFactory.getLogger(WebDriverConfig.class);

    @Autowired
    protected WebApplicationContext context;
    @Value("${test.server-url:http://localhost:8080}")
    protected String serverUrl;
    @Value("${test.context-path:}")
    protected String contextPath;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Profile({ "default", "mockmvc" })
    @Configuration
    @Import(MockMvcConfig.class)
    public class MockMvcWebDriverConfig {

        @Bean
        public WebDriver webDriver(MockMvc mvc) {
            logger.info("[Setup WebDriver] use HtmlUnitDriver connected with MockMvc.");
            return MockMvcHtmlUnitDriverBuilder.mockMvcSetup(mvc).contextPath(contextPath).build();
        }
    }

    @Profile("htmlunit")
    @Configuration
    public class HtmlUnitWebDriverConfig {

        @Bean
        public WebDriver webDriver() {
            logger.info("[Setup WebDriver] use HtmlUnitDriver connected with Actual Application.");
            return new HtmlUnitDriver();
        }
    }

    @Profile("firefox")
    @Configuration
    public class FireFoxWebDriverConfig {

        @Bean
        public WebDriver webDriver() {
            logger.info("[Setup WebDriver] use FirefoxDriver connected with Actual Application.");
            return new FirefoxDriver();
        }
    }
}
