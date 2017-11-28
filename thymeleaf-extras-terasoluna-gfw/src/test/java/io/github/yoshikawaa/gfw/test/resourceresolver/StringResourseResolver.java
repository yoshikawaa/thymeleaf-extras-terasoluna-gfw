package io.github.yoshikawaa.gfw.test.resourceresolver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;

public class StringResourseResolver implements IResourceResolver {

    public static final String NAME = "STRING";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public InputStream getResourceAsStream(TemplateProcessingParameters templateProcessingParameters,
            String resourceName) {
        return new ByteArrayInputStream(templateProcessingParameters.getTemplateName().getBytes());
    }

}
