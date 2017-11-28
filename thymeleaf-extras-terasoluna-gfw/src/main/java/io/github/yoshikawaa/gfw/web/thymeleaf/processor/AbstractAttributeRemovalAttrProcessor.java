package io.github.yoshikawaa.gfw.web.thymeleaf.processor;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;

public abstract class AbstractAttributeRemovalAttrProcessor extends AbstractAttrProcessor {

    private final String dialectPrefix;

    protected AbstractAttributeRemovalAttrProcessor(String dialectPrefix, String attributeName) {
        super(attributeName);
        this.dialectPrefix = dialectPrefix;
    }

    protected String getDialectPrefix() {
        return dialectPrefix;
    }

    @Override
    protected ProcessorResult processAttribute(Arguments arguments, Element element, String attributeName) {

        process(arguments, element, attributeName);
        element.removeAttribute(attributeName);

        return ProcessorResult.OK;
    }

    protected abstract void process(final Arguments arguments, final Element element, final String attributeName);

}
