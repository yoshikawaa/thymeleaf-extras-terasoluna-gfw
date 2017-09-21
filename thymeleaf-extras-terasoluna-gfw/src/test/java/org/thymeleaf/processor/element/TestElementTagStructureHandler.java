package org.thymeleaf.processor.element;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.engine.TemplateData;
import org.thymeleaf.inline.IInliner;
import org.thymeleaf.model.AttributeValueQuotes;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IProcessableElementTag;

public class TestElementTagStructureHandler implements IElementTagStructureHandler {

    private String elementCompleteName;

    private Map<String, String> attributes = new HashMap<>();
    private Map<String, Object> localVariables = new HashMap<>();

    private StringBuilder body = new StringBuilder();
    private boolean processable;

    public TestElementTagStructureHandler(IProcessableElementTag tag) {
        this.elementCompleteName = tag.getElementCompleteName();
        Arrays.stream(tag.getAllAttributes())
                .forEach(a -> this.attributes.put(a.getAttributeCompleteName(), a.getValue()));
    }

    public TestElementTagStructureHandler(IProcessableElementTag tag, AbstractElementTagProcessor processor) {
        this.elementCompleteName = tag.getElementCompleteName();
        if (processor == null) {
            Arrays.stream(tag.getAllAttributes())
                    .forEach(a -> this.attributes.put(a.getAttributeCompleteName(), a.getValue()));
        } else {
            Arrays.stream(tag.getAllAttributes())
                    .filter(a -> !a.getAttributeDefinition().getAttributeName()
                            .equals(processor.getMatchingAttributeName().getMatchingAttributeName()))
                    .forEach(a -> this.attributes.put(a.getAttributeCompleteName(), a.getValue()));
        }
    }

    @Override
    public void reset() {
        attributes.clear();
    }

    @Override
    public void setLocalVariable(String name, Object value) {
        localVariables.put(name, value);
    }

    @Override
    public void removeLocalVariable(String name) {
        localVariables.remove(name);
    }

    @Override
    public void setAttribute(String attributeName, String attributeValue) {
        attributes.put(attributeName, attributeValue);
    }

    @Override
    public void setAttribute(String attributeName, String attributeValue, AttributeValueQuotes attributeValueQuotes) {
        fail("not implemented.");
    }

    @Override
    public void replaceAttribute(AttributeName oldAttributeName, String attributeName, String attributeValue) {
        if (attributes.containsKey(oldAttributeName)) {
            attributes.remove(oldAttributeName);
            attributes.put(attributeName, attributeValue);
        } else {
            fail("attribute not exists.");
        }
    }

    @Override
    public void replaceAttribute(AttributeName oldAttributeName, String attributeName, String attributeValue,
            AttributeValueQuotes attributeValueQuotes) {
        fail("not implemented.");
    }

    @Override
    public void removeAttribute(String attributeName) {
        if (attributes.containsKey(attributeName)) {
            attributes.remove(attributeName);
        }
    }

    @Override
    public void removeAttribute(String prefix, String name) {
        String attributeName = prefix + ":" + name;
        if (attributes.containsKey(attributeName)) {
            attributes.remove(attributeName);
        }
    }

    @Override
    public void removeAttribute(AttributeName attributeName) {
        if (attributes.containsKey(attributeName)) {
            attributes.remove(attributeName);
        }
    }

    @Override
    public void setSelectionTarget(Object selectionTarget) {
        fail("not implemented.");
    }

    @Override
    public void setInliner(IInliner inliner) {
        fail("not implemented.");
    }

    @Override
    public void setTemplateData(TemplateData templateData) {
        fail("not implemented.");
    }

    @Override
    public void setBody(CharSequence text, boolean processable) {
        body.append(text);
        this.processable = processable;
    }

    @Override
    public void setBody(IModel model, boolean processable) {
        body.append(model.toString().replaceAll("\"", "'"));
        this.processable = processable;
    }

    @Override
    public void insertBefore(IModel model) {
        fail("not implemented.");
    }

    @Override
    public void insertImmediatelyAfter(IModel model, boolean processable) {
        fail("not implemented.");
    }

    @Override
    public void replaceWith(CharSequence text, boolean processable) {
        fail("not implemented.");
    }

    @Override
    public void replaceWith(IModel model, boolean processable) {
        fail("not implemented.");
    }

    @Override
    public void removeElement() {
        fail("not implemented.");
    }

    @Override
    public void removeTags() {
        fail("not implemented.");
    }

    @Override
    public void removeBody() {
        fail("not implemented.");
    }

    @Override
    public void removeAllButFirstChild() {
        fail("not implemented.");
    }

    @Override
    public void iterateElement(String iterVariableName, String iterStatusVariableName, Object iteratedObject) {
        fail("not implemented.");
    }

    @Override
    public String toString() {
        StringBuilder tag = new StringBuilder("<" + elementCompleteName);
        attributes.entrySet().forEach(a -> {
            if (StringUtils.hasText(a.getValue())) {
                tag.append(" " + a.getKey() + "='" + a.getValue() + "'");
            } else {
                tag.append(" " + a.getKey());
            }
        });

        if (body.length() > 0) {
            tag.append(">");
            tag.append(body);
            tag.append("</" + elementCompleteName + ">");
        } else {
            tag.append(" />");
        }

        return tag.toString();
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Map<String, Object> getLocalVariables() {
        return localVariables;
    }

    public String getBody() {
        return body.toString();
    }

    public boolean isProcessable() {
        return processable;
    }

}
