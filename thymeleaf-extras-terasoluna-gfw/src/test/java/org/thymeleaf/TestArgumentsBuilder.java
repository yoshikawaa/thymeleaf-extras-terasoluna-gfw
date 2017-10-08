package org.thymeleaf;

import java.util.Set;

import org.thymeleaf.context.IContext;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.resourceresolver.ClassLoaderResourceResolver;
import org.thymeleaf.templateresolver.AlwaysValidTemplateResolutionValidity;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolution;

public class TestArgumentsBuilder {

    private static final String ENCODING = "UTF-8";
    private static final String TEMPLATE_MODE = "HTML5";

    public static Arguments build(IContext context) {
        return build(context, null);
    }

    public static Arguments build(IContext context, Set<IDialect> additionalDialects) {
        final TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(new ClassLoaderTemplateResolver());
        if (additionalDialects != null) {
            templateEngine.setAdditionalDialects(additionalDialects);
        }
        final Configuration configuration = templateEngine.getConfiguration();
        configuration.initialize();
        final TemplateProcessingParameters parameters = new TemplateProcessingParameters(configuration, "", context);
        final TemplateResolution resolution = new TemplateResolution("", "", new ClassLoaderResourceResolver(),
                ENCODING, TEMPLATE_MODE, new AlwaysValidTemplateResolutionValidity());
        final TemplateRepository repository = new TemplateRepository(configuration);
        return new Arguments(templateEngine, parameters, resolution, repository, null);
    }

}
