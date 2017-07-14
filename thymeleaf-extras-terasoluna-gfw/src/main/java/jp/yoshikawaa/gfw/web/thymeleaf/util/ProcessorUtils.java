package jp.yoshikawaa.gfw.web.thymeleaf.util;

import org.thymeleaf.model.IProcessableElementTag;

public class ProcessorUtils {

    public static String getAttributeValue(IProcessableElementTag tag, String dialectPrefix, String attributeName,
            String defaultValue) {
        return tag.hasAttribute(dialectPrefix, attributeName) ? tag.getAttributeValue(dialectPrefix, attributeName)
                : defaultValue;
    }

    public static int getAttributeValue(IProcessableElementTag tag, String dialectPrefix, String attributeName,
            int defaultValue) {
        return tag.hasAttribute(dialectPrefix, attributeName)
                ? Integer.valueOf(tag.getAttributeValue(dialectPrefix, attributeName)) : defaultValue;
    }

    public static boolean getAttributeValue(IProcessableElementTag tag, String dialectPrefix, String attributeName,
            boolean defaultValue) {
        return tag.hasAttribute(dialectPrefix, attributeName)
                ? Boolean.valueOf(tag.getAttributeValue(dialectPrefix, attributeName)) : defaultValue;
    }

}
