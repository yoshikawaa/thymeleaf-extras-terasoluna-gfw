package io.github.yoshikawaa.gfw.test.templateresolver;

import org.thymeleaf.exceptions.ConfigurationException;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import io.github.yoshikawaa.gfw.test.resourceresolver.StringResourseResolver;

public class StringTemplateResolver extends TemplateResolver {

    public StringTemplateResolver() {
        super();
        super.setResourceResolver(new StringResourseResolver());
    }

    @Override
    public void setResourceResolver(final IResourceResolver resourceResolver) {
        throw new ConfigurationException("Cannot set a resource resolver on " + this.getClass().getName()
                + ". If you want to set your own resource resolver, use " + TemplateResolver.class.getName()
                + "instead");
    }

}
