package jp.yoshikawaa.gfw.web.thymeleaf.processor;

import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

public abstract class AbstractHtmlAttributeProcessor extends AbstractAttributeTagProcessor {

    protected final String dialectPrefix;
    
    protected AbstractHtmlAttributeProcessor(String dialectPrefix, String attributeName, int precedence) {
        super(TemplateMode.HTML, dialectPrefix, null, false, attributeName, true, precedence, true);
        this.dialectPrefix = dialectPrefix;
    }

}
