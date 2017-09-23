package org.thymeleaf;

import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.TemplateRepository;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.resourceresolver.ClassLoaderResourceResolver;
import org.thymeleaf.templateresolver.AlwaysValidTemplateResolutionValidity;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolution;

public class TestArgumentsBuilder {

    public static Arguments build(WebContext context) {
        final TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(new ClassLoaderTemplateResolver());
        final Configuration configuration = engine.getConfiguration();
        configuration.initialize();
        final TemplateProcessingParameters parameters = new TemplateProcessingParameters(configuration, "", context);
        final TemplateResolution resolution = new TemplateResolution("", "", new ClassLoaderResourceResolver(), "UTF-8", "HTML5", new AlwaysValidTemplateResolutionValidity());
        final TemplateRepository repository = new TemplateRepository(configuration);
        return new Arguments(engine, parameters, resolution, repository, null);
    }

}
