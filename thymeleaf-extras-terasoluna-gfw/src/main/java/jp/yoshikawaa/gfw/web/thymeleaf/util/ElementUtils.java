package jp.yoshikawaa.gfw.web.thymeleaf.util;

import java.util.Arrays;

import org.thymeleaf.dom.Attribute;
import org.thymeleaf.dom.Element;

public class ElementUtils {

    public static String getAttributeValue(Element element, String dialectPrefix, String attributeName,
            String defaultValue) {
        return element.hasNormalizedAttribute(dialectPrefix, attributeName)
                ? element.getAttributeValueFromNormalizedName(dialectPrefix, attributeName) : defaultValue;
    }

    public static int getAttributeValue(Element element, String dialectPrefix, String attributeName, int defaultValue) {
        return element.hasNormalizedAttribute(dialectPrefix, attributeName)
                ? Integer.valueOf(element.getAttributeValueFromNormalizedName(dialectPrefix, attributeName))
                : defaultValue;
    }

    public static boolean getAttributeValue(Element element, String dialectPrefix, String attributeName,
            boolean defaultValue) {
        return element.hasNormalizedAttribute(dialectPrefix, attributeName)
                ? Boolean.valueOf(element.getAttributeValueFromNormalizedName(dialectPrefix, attributeName))
                : defaultValue;
    }

    public static void removeAttribute(Element element, String dialectPrefix, String attributeName) {
        Arrays.stream(Attribute.applyPrefixToAttributeName(attributeName, dialectPrefix)).forEach(a -> {
            if (element.hasAttribute(a)) {
                element.removeAttribute(a);
            }
        });
    }
}
