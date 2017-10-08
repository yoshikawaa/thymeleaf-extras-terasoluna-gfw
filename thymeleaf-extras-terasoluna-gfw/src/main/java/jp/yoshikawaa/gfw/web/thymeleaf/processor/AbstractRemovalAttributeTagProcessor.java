package jp.yoshikawaa.gfw.web.thymeleaf.processor;

import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

public abstract class AbstractRemovalAttributeTagProcessor extends AbstractAttributeTagProcessor {

    protected AbstractRemovalAttributeTagProcessor(TemplateMode templateMode, String dialectPrefix, String attributeName,
            int precedence) {
        super(templateMode, dialectPrefix, null, false, attributeName, true, precedence, true);
    }

}
