package org.thymeleaf.context;

import java.util.Collections;
import java.util.Set;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.dialect.IDialect;

public final class TestEngineConfigurationBuilder {

    public static IEngineConfiguration build() {
        final TemplateEngine templateEngine = new TemplateEngine();
        return templateEngine.getConfiguration();
    }

    public static IEngineConfiguration build(final IDialect dialect) {
        return build(Collections.singleton(dialect));
    }

    public static IEngineConfiguration build(final Set<IDialect> dialects) {
        final TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setDialects(dialects);
        return templateEngine.getConfiguration();
    }

}