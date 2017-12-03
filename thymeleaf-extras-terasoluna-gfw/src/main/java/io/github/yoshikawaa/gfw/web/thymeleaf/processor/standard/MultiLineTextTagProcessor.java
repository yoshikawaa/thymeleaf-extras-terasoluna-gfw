/**
 * Copyright (c) 2017 Atsushi Yoshikawa (https://yoshikawaa.github.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

/**
 * Attribute tag processor for multi-line text.
 * <p>
 * Escape text and convert line-break characters to {@code <br>
 * } element. Support {@code CR}, {@code LF}, and {@code CRLF} as line-break characters.
 * </p>
 * 
 * @author Atsushi Yoshikawa
 */
public class MultiLineTextTagProcessor extends AbstractAttributeRemovalAttributeTagProcessor {

    private static final TemplateMode TEMPLATE_MODE = TemplateMode.HTML;
    private static final String ATTRIBUTE_NAME = "mtext";
    private static final int PRECEDENCE = 1200;

    private static final Pattern PATTERN_LINE_BREAK = Pattern.compile("(\\r\\n|\\r|\\n)");
    private static final String TAG_LINE_BREAK = "<br>";

    /**
     * @param dialectPrefix prefix of attribute
     */
    public MultiLineTextTagProcessor(String dialectPrefix) {
        super(TEMPLATE_MODE, dialectPrefix, ATTRIBUTE_NAME, PRECEDENCE);
    }

    /**
     * {@inheritDoc}
     */
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
