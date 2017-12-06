package io.github.yoshikawaa.gfw.web.thymeleaf.processor.standard;

import java.util.regex.Pattern;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Macro;
import org.thymeleaf.util.StringUtils;
import org.unbescape.html.HtmlEscape;

import io.github.yoshikawaa.gfw.web.thymeleaf.processor.AbstractAttributeRemovalAttrProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.util.ExpressionUtils;

public class MultiLineTextAttrProcessor extends AbstractAttributeRemovalAttrProcessor {

    private static final String ATTRIBUTE_NAME = "mtext";
    private static final int PRECEDENCE = 1200;

    private static final Pattern PATTERN_LINE_BREAK = Pattern.compile("(\\r\\n|\\r|\\n)");
    private static final String TAG_LINE_BREAK = "<br>";

    public MultiLineTextAttrProcessor(String dialectPrefix) {
        super(dialectPrefix, ATTRIBUTE_NAME);
    }

    @Override
    public int getPrecedence() {
        return PRECEDENCE;
    }

    @Override
    protected void process(final Arguments arguments, final Element element, final String attributeName) {

        String attributeValue = element.getAttributeValue(attributeName);
        String text = ExpressionUtils.execute(arguments, attributeValue, String.class);
        String escapedText = lineBreakToBr(HtmlEscape.escapeHtml5(text));

        element.addChild(new Macro(escapedText));
    }

    private String lineBreakToBr(String text) {
        return StringUtils.isEmpty(text) ? text : PATTERN_LINE_BREAK.matcher(text).replaceAll(TAG_LINE_BREAK);
    }

}
