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

import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.util.StringUtils;

public class ExpressionUtils {

    private static final Pattern PATTERN_EXPRESSION = Pattern.compile("(\\$|\\*|\\#|\\@)\\{(.+?)\\}");
    
    public static boolean isExpression(String expressionString) {
        return StringUtils.isEmpty(expressionString) ? false : PATTERN_EXPRESSION.matcher(expressionString).matches();
    }
    
    public static Object execute(Arguments arguments, String expressionString) {

        Configuration configuration = arguments.getConfiguration();
        IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);
        IStandardExpression expression = expressionParser.parseExpression(configuration, arguments, expressionString);
        return expression.execute(configuration, arguments);
    }

    public static <T extends Object> T execute(Arguments arguments, String expressionString, Class<T> clazz) {

        Object result = execute(arguments, expressionString);
        if (result != null && !clazz.isAssignableFrom(result.getClass())) {
            throw new TemplateInputException("expression result is not expected. expected:" + clazz.getName()
                    + " actual:" + result.getClass().getName());
        }
        return clazz.cast(result);
    }
    
    private ExpressionUtils() {
    }

}
