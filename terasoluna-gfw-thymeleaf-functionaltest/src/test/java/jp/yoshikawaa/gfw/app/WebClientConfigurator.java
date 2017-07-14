package jp.yoshikawaa.gfw.app;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.gargoylesoftware.htmlunit.WebClient;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration({ "classpath*:META-INF/spring/applicationContext.xml",
                "classpath*:META-INF/spring/spring-security.xml" }),
        @ContextConfiguration("classpath*:META-INF/spring/spring-mvc.xml") })
public abstract class WebClientConfigurator {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected WebClient client;
    @Value("${webclient.context-path}")
    protected String contextPath;

    @Value("${webclient.use-mock}")
    private boolean useMock;
    @Value("${webclient.server-url}")
    private String serverUrl;
    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        if (useMock) {
            logger.info("[Setup WebClient] use WebClient connected with MockMvc.");
            MockMvc mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).alwaysDo(log()).build();
            client = MockMvcWebClientBuilder.mockMvcSetup(mvc).contextPath(contextPath).build();
        } else {
            logger.info("[Setup WebClient] use WebClient connected with Actual Application.");
            client = new WebClient();
        }
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
    }

    public String url() {
        return serverUrl + contextPath + path();
    }
    
    protected abstract String path();
}
