package io.github.yoshikawaa.gfw.test.support;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration({ "classpath*:META-INF/spring/applicationContext.xml",
                "classpath*:META-INF/spring/spring-security.xml" }),
        @ContextConfiguration("classpath*:META-INF/spring/spring-mvc.xml") })
@PropertySource(value = "classpath:test.properties", ignoreResourceNotFound = true)
public abstract class TerasolunaGfwE2ETestSupport {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${test.server-url:http://localhost:8080}")
    protected String serverUrl;
    @Value("${test.context-path:}")
    protected String contextPath;

    public String url() {
        return serverUrl + contextPath + path();
    }

    protected abstract String path();
}
