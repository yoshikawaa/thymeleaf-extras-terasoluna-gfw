package org.thymeleaf.engine;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.TestEngineConfigurationBuilder;
import org.thymeleaf.model.IStandaloneElementTag;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateparser.markup.HTMLTemplateParser;
import org.thymeleaf.templateresource.StringTemplateResource;

public final class TestElementTagBuilder {

    private static final HTMLTemplateParser HTML_PARSER = new HTMLTemplateParser(2, 4096);
    private static final IEngineConfiguration TEMPLATE_ENGINE_CONFIGURATION = TestEngineConfigurationBuilder.build();
    private static final String TEMPLATE_NAME = "test";
    private static final TagObtentionTemplateHandler HANDLER = new TagObtentionTemplateHandler();

    public static StandaloneElementTag standalone(final String template) {
        HTML_PARSER.parseStandalone(TEMPLATE_ENGINE_CONFIGURATION, TEMPLATE_NAME, TEMPLATE_NAME, null,
                new StringTemplateResource(template), TemplateMode.HTML, false, HANDLER);
        return HANDLER.tag;
    }

    private static class TagObtentionTemplateHandler extends AbstractTemplateHandler {
        private StandaloneElementTag tag;

        @Override
        public void handleStandaloneElement(final IStandaloneElementTag standaloneElementTag) {
            this.tag = StandaloneElementTag.asEngineStandaloneElementTag(standaloneElementTag);
        }
    }

}
