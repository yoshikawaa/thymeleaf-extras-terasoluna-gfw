package io.github.yoshikawaa.gfw.test.engine;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import org.springframework.context.support.StaticMessageSource;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring4.messageresolver.SpringMessageResolver;

import io.github.yoshikawaa.gfw.web.thymeleaf.dialect.TerasolunaGfwDialect;

public class TerasolunaGfwTestEngine extends TestEngine {

    private static final StaticMessageSource MESSAGE_SOURCE = new StaticMessageSource();
    private static final Set<IDialect> DIALECTS = Collections.singleton(new TerasolunaGfwDialect());
    private static final SpringMessageResolver MESSAGE_RESOLVER = new SpringMessageResolver();

    static {
        MESSAGE_SOURCE.addMessage("test1", Locale.ENGLISH, "test message 1.");
        MESSAGE_SOURCE.addMessage("test2", Locale.ENGLISH, "test message 2.");
        MESSAGE_SOURCE.addMessage("test3", Locale.ENGLISH, "test message 3.");
        MESSAGE_SOURCE.addMessage("args1", Locale.ENGLISH, "args [{0},{1},{2}].");
        MESSAGE_RESOLVER.setMessageSource(MESSAGE_SOURCE);
    }

    public TerasolunaGfwTestEngine() {
        super();
        engine.setAdditionalDialects(DIALECTS);
        engine.setMessageResolver(MESSAGE_RESOLVER);
    }

}
