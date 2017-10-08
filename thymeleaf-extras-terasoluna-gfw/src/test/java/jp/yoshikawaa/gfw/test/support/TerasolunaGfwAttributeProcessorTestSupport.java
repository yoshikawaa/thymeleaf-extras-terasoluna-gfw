package jp.yoshikawaa.gfw.test.support;

import static org.junit.Assert.fail;

import java.util.Locale;
import java.util.Map;

import org.springframework.context.support.StaticMessageSource;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.engine.TestElementTagBuilder;
import org.thymeleaf.engine.TestWebEngineContextBuilder;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.TestElementTagStructureHandler;

import jp.yoshikawaa.gfw.web.thymeleaf.dialect.TerasolunaGfwDialect;
import jp.yoshikawaa.gfw.web.thymeleaf.processor.AbstractRemovalAttributeTagProcessor;

public abstract class TerasolunaGfwAttributeProcessorTestSupport extends LogbackMockSupport {

    private static final StaticMessageSource MESSAGE_SOURCE = new StaticMessageSource();
    private static final TerasolunaGfwDialect DIALECT = new TerasolunaGfwDialect(MESSAGE_SOURCE);

    private final AbstractRemovalAttributeTagProcessor processor;

    static {
        MESSAGE_SOURCE.addMessage("test1", Locale.ENGLISH, "test message 1.");
        MESSAGE_SOURCE.addMessage("test2", Locale.ENGLISH, "test message 2.");
        MESSAGE_SOURCE.addMessage("test3", Locale.ENGLISH, "test message 3.");
        TestWebEngineContextBuilder.addDialect(DIALECT);
    }

    public TerasolunaGfwAttributeProcessorTestSupport() {
        super();
        this.processor = null;
    }

    public TerasolunaGfwAttributeProcessorTestSupport(
            Class<? extends AbstractRemovalAttributeTagProcessor> processorClass) {
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

    protected TestElementTagStructureHandler process(AbstractRemovalAttributeTagProcessor processor, String template,
            Map<String, Object> attributes, Map<String, Object> variables) {

        if (processor == null) {
            fail("processor must not be null.");
        }

        final WebEngineContext context = TestWebEngineContextBuilder.from(template).requestAttributes(attributes)
                .variables(variables).build();
        final IProcessableElementTag tag = TestElementTagBuilder.standalone(template);
        final TestElementTagStructureHandler structureHandler = new TestElementTagStructureHandler(tag, processor);

        processor.process(context, tag, structureHandler);
        return structureHandler;
    }

    private <T> T getProcessor(Class<T> clazz) {
        return clazz.cast(DIALECT.getProcessors(DIALECT.getPrefix()).stream()
                .filter(p -> clazz.isAssignableFrom(p.getClass())).findFirst().get());

    }

}
