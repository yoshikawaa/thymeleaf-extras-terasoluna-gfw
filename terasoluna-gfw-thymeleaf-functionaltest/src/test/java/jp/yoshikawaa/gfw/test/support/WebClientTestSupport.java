package jp.yoshikawaa.gfw.test.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.gargoylesoftware.htmlunit.WebClient;

import jp.yoshikawaa.gfw.test.config.WebClientConfig;

@ContextConfiguration(classes = WebClientConfig.class)
public abstract class WebClientTestSupport extends TerasolunaGfwE2ETestSupport {

    @Autowired
    protected WebClient client;
}
