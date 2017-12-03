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
package io.github.yoshikawaa.gfw.web.thymeleaf.util;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

/**
 * Utility for handling {@link Expression}.
 * 
 * @author Atsushi Yoshikawa
 * @see Expression
 */
public class ExpressionUtils {

    private static final Pattern PATTERN_EXPRESSION = Pattern.compile("(\\$|\\*|\\#|\\@|\\~)\\{(.+?)\\}");

    /**
     * Verify whether or not be expression.
     * 
     * @param expressionString string to be verified
     * @return whether or not be expression
     */
    public static boolean isExpression(String expressionString) {
        return StringUtils.isEmpty(expressionString) ? false : PATTERN_EXPRESSION.matcher(expressionString).matches();
    }

    /**
     * Resolve expression.
     * 
     * @param context template context used to resolve expression
     * @param expressionString string to be resolved
     * @return resolved object
     */
    public static Object execute(final ITemplateContext context, String expressionString) {

        final IEngineConfiguration configuration = context.getConfiguration();
        final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
        final IStandardExpression expression = parser.parseExpression(context, expressionString);
        return expression.execute(context);
    }

    /**
     * Resolve expression type safely.
     * 
     * @param <T> resolved type
     * @param context template context used to resolve expression
     * @param expressionString string to be resolved
     * @param clazz resolved type
     * @return resolved object
     * @see #execute(ITemplateContext, String)
     */
    public static <T> T execute(final ITemplateContext context, String expressionString, Class<T> clazz) {

        Object result = execute(context, expressionString);
        if (result != null && !clazz.isAssignableFrom(result.getClass())) {
            throw new TemplateInputException("expression result type is not expected. expected:" + clazz.getName()
                    + " actual:" + result.getClass().getName());
        }
        return clazz.cast(result);
    }

    private ExpressionUtils() {
    }

}
