package io.github.yoshikawaa.gfw.web.thymeleaf.util;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.util.StringUtils;

public class MessageUtils {

    public static String resolveMessage(Class<?> origin, ITemplateContext context, String code, Object[] args,
            String defaultMessage) {

        if (StringUtils.isEmpty(code)) {
            return defaultMessage;
        }
        String resolvedMessage = context.getMessage(origin, code, args, false);
        return StringUtils.isEmpty(resolvedMessage) ? defaultMessage : resolvedMessage;
    }

}
