package io.github.yoshikawaa.gfw.test.config;

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
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.context.WebApplicationContext;

import com.gargoylesoftware.htmlunit.WebClient;

@Configuration
@PropertySource(value = "classpath:test.properties", ignoreResourceNotFound = true)
public class WebClientConfig {

    protected static final Logger logger = LoggerFactory.getLogger(WebClientConfig.class);

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
    public class MockMvcWebClientConfig {

        @Bean
        public WebClient webClient(MockMvc mvc) {
            logger.info("[Setup WebClient] use WebClient connected with MockMvc.");
            WebClient client = MockMvcWebClientBuilder.mockMvcSetup(mvc).contextPath(contextPath).build();
            client.getOptions().setThrowExceptionOnFailingStatusCode(false);
            return client;
        }
    }

    @Profile("htmlunit")
    @Configuration
    public class HtmlUnitWebClientConfig {

        @Bean
        public WebClient webClient() {
            logger.info("[Setup WebClient] use WebClient connected with Actual Application.");
            WebClient client = new WebClient();
            client.getOptions().setThrowExceptionOnFailingStatusCode(false);
            return client;
        }
    }
}
