package jp.yoshikawaa.gfw.test.support;

import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.StaticMessageSource;
import org.thymeleaf.Arguments;
import org.thymeleaf.TestArgumentsBuilder;
import org.thymeleaf.context.TestWebContextBuilder;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.element.TestElementBuilder;
import org.thymeleaf.dom.element.TestElementWrapper;
import org.thymeleaf.processor.AttributeNameProcessorMatcher;
import org.thymeleaf.processor.attr.AbstractMarkupRemovalAttrProcessor;

import jp.yoshikawaa.gfw.web.thymeleaf.dialect.TerasolunaGfwDialect;

public abstract class TerasolunaGfwAttrProcessorTestSupport extends LogbackMockSupport {

    private static final Logger logger = LoggerFactory.getLogger(TerasolunaGfwAttrProcessorTestSupport.class);
    private static final StaticMessageSource MESSAGE_SOURCE = new StaticMessageSource();
    private static final TerasolunaGfwDialect DIALECTS = new TerasolunaGfwDialect(MESSAGE_SOURCE);

    private final AbstractMarkupRemovalAttrProcessor processor;

    static {
        MESSAGE_SOURCE.addMessage("test1", Locale.ENGLISH, "test message 1.");
        MESSAGE_SOURCE.addMessage("test2", Locale.ENGLISH, "test message 2.");
        MESSAGE_SOURCE.addMessage("test3", Locale.ENGLISH, "test message 3.");
    }

    public TerasolunaGfwAttrProcessorTestSupport() {
        super();
        this.processor = null;
    }

    public TerasolunaGfwAttrProcessorTestSupport(Class<? extends AbstractMarkupRemovalAttrProcessor> processorClass) {
        super(processorClass);
        this.processor = getProcessor(processorClass);
    }

    protected TestElementWrapper process(String template) {
        return process(this.processor, template, null, null);
    }

    protected TestElementWrapper process(String template, Map<String, Object> attributes,
            Map<String, Object> variables) {
        return process(this.processor, template, attributes, variables);
    }

    protected TestElementWrapper process(AbstractMarkupRemovalAttrProcessor processor, String template,
            Map<String, Object> attributes, Map<String, Object> variables) {

        if (processor == null) {
            fail("processor must not be null.");
        }

        final WebContext context = TestWebContextBuilder.init().requestAttributes(attributes).variables(variables)
                .build();
        final Arguments arguments = TestArgumentsBuilder.build(context);
        final Element element = TestElementBuilder.standalone(template);

        processor.processAttribute(arguments, element, getAttributeName(processor));
        return new TestElementWrapper(arguments, element);
    }

    protected <T> T getProcessor(Class<T> clazz) {
        return clazz.cast(
                DIALECTS.getProcessors().stream().filter(p -> clazz.isAssignableFrom(p.getClass())).findFirst().get());
    }

    private String getAttributeName(AbstractMarkupRemovalAttrProcessor processor) {
        StringBuilder attributeName = new StringBuilder(DIALECTS.getPrefix() + ":");
        AttributeNameProcessorMatcher matcher = (AttributeNameProcessorMatcher) processor.getMatcher();
        try {
            Field f = AttributeNameProcessorMatcher.class.getDeclaredField("attributeName");
            f.setAccessible(true);
            attributeName.append((String) f.get(matcher));
        } catch (Exception e) {
            logger.error("fail to get attributeName field.");
        }
        return attributeName.toString();
    }

}
