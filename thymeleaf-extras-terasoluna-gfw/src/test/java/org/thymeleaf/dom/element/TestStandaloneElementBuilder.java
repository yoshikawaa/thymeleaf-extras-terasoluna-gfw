package org.thymeleaf.dom.element;

import java.util.StringTokenizer;

import org.springframework.util.StringUtils;
import org.thymeleaf.dom.Element;

public final class TestStandaloneElementBuilder {

    public static Element from(final String template) {
        StringTokenizer token = new StringTokenizer(template.replace("<", "").replace("/>", ""), " ");
        Element element = new Element(token.nextToken());
        while (token.hasMoreTokens()) {
            String attribute = token.nextToken();
            if (StringUtils.hasText(attribute) && attribute.contains("=")) {
                int i = attribute.indexOf("=");
                element.setAttribute(attribute.substring(0, i),
                        attribute.substring(i + 2).replaceAll("'", "").replaceAll("\"", ""));
            }
        }
        element.setParent(new Element("body"));
        return element;
    }

}
