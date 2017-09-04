package jp.yoshikawaa.gfw.web.thymeleaf.processor;

import org.thymeleaf.processor.element.IElementTagStructureHandler;

public interface IAttributeAccessor {

    void removeAttributes(IElementTagStructureHandler structureHandler);
}
