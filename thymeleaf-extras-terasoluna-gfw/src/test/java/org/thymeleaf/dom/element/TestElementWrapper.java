package org.thymeleaf.dom.element;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.templatewriter.XhtmlHtml5TemplateWriter;

public class TestElementWrapper {

    private static final Logger logger = LoggerFactory.getLogger(TestElementWrapper.class);
    private static final XhtmlHtml5TemplateWriter templateWriter = new XhtmlHtml5TemplateWriter();

    private Arguments arguments;
    private Element element;

    public TestElementWrapper(Arguments arguments, Element element) {
        this.arguments = arguments;
        this.element = element;
    }

    @Override
    public String toString() {
        return parse(element);
    }

    public Map<String, String> getAttributes() {
        return element.getAttributeMap().entrySet().stream().collect(HashMap<String, String>::new,
                (m, e) -> m.put(e.getValue().getOriginalName(), e.getValue().getOriginalValue()), Map::putAll);
    }

    public String getBody() {
        StringBuilder sb = new StringBuilder();
        for (Node node : element.getChildren()) {
            sb.append(parse(node));
        }
        return sb.toString();
    }

    private String parse(Node node) {
        try (StringWriter writer = new StringWriter()) {
            templateWriter.writeNode(arguments, writer, node);
            return writer.toString().replaceAll("\"", "'");
        } catch (IOException e) {
            logger.error("failed to parse element.", e);
        }
        return null;
    }

}
