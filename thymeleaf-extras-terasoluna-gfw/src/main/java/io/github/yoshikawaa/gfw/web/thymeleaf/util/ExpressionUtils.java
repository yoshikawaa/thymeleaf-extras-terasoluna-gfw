package io.github.yoshikawaa.gfw.web.thymeleaf.util;

import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

public class ExpressionUtils {

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
