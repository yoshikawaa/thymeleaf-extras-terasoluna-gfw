package io.github.yoshikawaa.gfw.web.thymeleaf.util;

import org.springframework.util.StringUtils;
import org.thymeleaf.Arguments;

public class MessageUtils {

    public static String resolveMessage(Arguments arguments, String code, Object[] args, String defaultMessage) {

        if (StringUtils.isEmpty(code)) {
            return defaultMessage;
        }

        return arguments.getConfiguration()
                .getMessageResolvers()
                .stream()
                .map(r -> r.resolveMessage(arguments, code, args))
                .filter(r -> r != null)
                .map(r -> r.getResolvedMessage())
                .findFirst()
                .orElse(defaultMessage);
    }

}
