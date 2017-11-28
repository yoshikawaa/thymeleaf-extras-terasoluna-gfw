package io.github.yoshikawaa.gfw.web.thymeleaf.processor;

import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

public abstract class AbstractAttributeRemovalAttributeTagProcessor extends AbstractAttributeTagProcessor {

    protected AbstractAttributeRemovalAttributeTagProcessor(TemplateMode templateMode, String dialectPrefix,
            String attributeName, int precedence) {
        super(templateMode, dialectPrefix, null, false, attributeName, true, precedence, true);
    }

}
