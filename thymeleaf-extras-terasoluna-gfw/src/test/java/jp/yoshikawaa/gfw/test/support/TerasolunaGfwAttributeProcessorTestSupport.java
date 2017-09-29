package jp.yoshikawaa.gfw.test.support;

import static org.junit.Assert.fail;

import java.util.Locale;
import java.util.Map;

import org.springframework.context.support.StaticMessageSource;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.TestStandaloneElementTagBuilder;
import org.thymeleaf.engine.TestWebEngineContextBuilder;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.TestElementTagStructureHandler;

import jp.yoshikawaa.gfw.web.thymeleaf.dialect.TerasolunaGfwDialect;
import jp.yoshikawaa.gfw.web.thymeleaf.processor.AbstractHtmlAttributeProcessor;

public abstract class TerasolunaGfwAttributeProcessorTestSupport extends LogbackMockSupport {

    private static final StaticMessageSource messageSource = new StaticMessageSource();
    private static final TerasolunaGfwDialect dialect = new TerasolunaGfwDialect(messageSource);

    private final AbstractHtmlAttributeProcessor processor;

    static {
        messageSource.addMessage("test1", Locale.ENGLISH, "test message 1.");
        messageSource.addMessage("test2", Locale.ENGLISH, "test message 2.");
        messageSource.addMessage("test3", Locale.ENGLISH, "test message 3.");
        TestWebEngineContextBuilder.addDialect(dialect);
    }

    public TerasolunaGfwAttributeProcessorTestSupport() {
        super();
        this.processor = null;
    }

    public TerasolunaGfwAttributeProcessorTestSupport(Class<? extends AbstractHtmlAttributeProcessor> processorClass) {
        super(processorClass);
        this.processor = getProcessor(processorClass);
    }

    protected TestElementTagStructureHandler process(String template) {
        return process(this.processor, template, null, null);
    }

    protected TestElementTagStructureHandler process(String template, Map<String, Object> attributes,
            Map<String, Object> variables) {
        return process(this.processor, template, attributes, variables);
    }

    protected TestElementTagStructureHandler process(AbstractHtmlAttributeProcessor processor, String template,
            Map<String, Object> attributes, Map<String, Object> variables) {

        if (processor == null) {
            fail("processor must not be null.");
        }

        final WebEngineContext context = TestWebEngineContextBuilder.from(template).attributes(attributes)
                .variables(variables).build();
        final IProcessableElementTag tag = TestStandaloneElementTagBuilder.from(template);
        final TestElementTagStructureHandler structureHandler = new TestElementTagStructureHandler(tag, processor);

        processor.process(context, tag, structureHandler);
        return structureHandler;
    }

    @SuppressWarnings("unchecked")
    private <T> T getProcessor(Class<T> clazz) {
        return (T) dialect.getProcessors(dialect.getPrefix()).stream().filter(p -> clazz.isAssignableFrom(p.getClass()))
                .findFirst().get();

    }

}
