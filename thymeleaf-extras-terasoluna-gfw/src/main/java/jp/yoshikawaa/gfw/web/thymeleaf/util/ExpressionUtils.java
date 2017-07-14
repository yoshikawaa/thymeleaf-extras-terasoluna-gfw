package jp.yoshikawaa.gfw.web.thymeleaf.util;

import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

public class ExpressionUtils {

    @SuppressWarnings("unchecked")
    public static <T extends Object> T execute(Arguments arguments, String expressionString, Class<T> clazz) {

        Configuration configuration = arguments.getConfiguration();
        IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);
        IStandardExpression expression = expressionParser.parseExpression(configuration, arguments, expressionString);
        Object result = expression.execute(configuration, arguments);
        if (result != null && !clazz.isAssignableFrom(result.getClass())) {
            throw new TemplateInputException("expression result is not expected. expected:" + clazz.getName()
                    + " actual:" + result.getClass().getName());
        }
        return (T) result;
    }
}
