package io.github.yoshikawaa.gfw.web.thymeleaf.util;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

public class ExpressionUtils {

    private static final Pattern PATTERN_EXPRESSION = Pattern.compile("(\\$|\\*|\\#|\\@|\\~)\\{(.+?)\\}");

    public static boolean isExpression(String expressionString) {
        return StringUtils.isEmpty(expressionString) ? false : PATTERN_EXPRESSION.matcher(expressionString).matches();
    }

    public static Object execute(final ITemplateContext context, String expressionString) {

        final IEngineConfiguration configuration = context.getConfiguration();
        final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
        final IStandardExpression expression = parser.parseExpression(context, expressionString);
        return expression.execute(context);
    }

    public static <T> T execute(final ITemplateContext context, String expressionString, Class<T> clazz) {

        Object result = execute(context, expressionString);
        if (result != null && !clazz.isAssignableFrom(result.getClass())) {
            throw new TemplateInputException("expression result type is not expected. expected:" + clazz.getName()
                    + " actual:" + result.getClass().getName());
        }
        return clazz.cast(result);
    }
}
