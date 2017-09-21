package org.thymeleaf.engine;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.TestEngineConfigurationBuilder;
import org.thymeleaf.model.IStandaloneElementTag;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateparser.markup.HTMLTemplateParser;
import org.thymeleaf.templateresource.StringTemplateResource;

public final class TestStandaloneElementTagBuilder {

    private static final HTMLTemplateParser HTML_PARSER = new HTMLTemplateParser(2, 4096);
    private static final IEngineConfiguration TEMPLATE_ENGINE_CONFIGURATION = TestEngineConfigurationBuilder
            .build();

    public static StandaloneElementTag from(final String template) {
        final String templateName = "test";
        final TagObtentionTemplateHandler handler = new TagObtentionTemplateHandler();
        HTML_PARSER.parseStandalone(TEMPLATE_ENGINE_CONFIGURATION, templateName, templateName, null,
                new StringTemplateResource(template), TemplateMode.HTML, false, handler);
        return handler.tag;
    }

    private static class TagObtentionTemplateHandler extends AbstractTemplateHandler {
        private StandaloneElementTag tag;

        @Override
        public void handleStandaloneElement(final IStandaloneElementTag standaloneElementTag) {
            this.tag = StandaloneElementTag.asEngineStandaloneElementTag(standaloneElementTag);
        }
    }

}
