package io.github.yoshikawaa.gfw.web.thymeleaf.processor.standard;

import java.util.regex.Pattern;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.StringUtils;
import org.unbescape.html.HtmlEscape;

import io.github.yoshikawaa.gfw.web.thymeleaf.processor.AbstractAttributeRemovalAttributeTagProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.ExpressionUtils;

public class MultiLineTextTagProcessor extends AbstractAttributeRemovalAttributeTagProcessor {

    private static final TemplateMode TEMPLATE_MODE = TemplateMode.HTML;
    private static final String ATTRIBUTE_NAME = "mtext";
    private static final int PRECEDENCE = 1200;

    private static final Pattern PATTERN_LINE_BREAK = Pattern.compile("(\\r\\n|\\r|\\n)");
    private static final String TAG_LINE_BREAK = "<br>";

    public MultiLineTextTagProcessor(String dialectPrefix) {
        super(TEMPLATE_MODE, dialectPrefix, ATTRIBUTE_NAME, PRECEDENCE);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
            String attributeValue, IElementTagStructureHandler structureHandler) {

        String text = ExpressionUtils.execute(context, attributeValue, String.class);
        String escapedText = lineBreakToBr(HtmlEscape.escapeHtml5(text));

        structureHandler.setBody(escapedText, false);
    }

    private String lineBreakToBr(String text) {
        return StringUtils.isEmpty(text) ? text : PATTERN_LINE_BREAK.matcher(text).replaceAll(TAG_LINE_BREAK);
    }

}
