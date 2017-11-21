package jp.yoshikawaa.gfw.test.config;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.terasoluna.gfw.web.logging.mdc.MDCClearFilter;
import org.terasoluna.gfw.web.logging.mdc.XTrackMDCPutFilter;

@Configuration
public class MockMvcConfig {

    @Autowired
    protected WebApplicationContext context;

    @Bean
    public MockMvc mockMvc() {
        return MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new MDCClearFilter(), new DelegatingFilterProxy("exceptionLoggingFilter", context),
                        new XTrackMDCPutFilter(), new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .alwaysDo(log())
                .build();
    }
}
