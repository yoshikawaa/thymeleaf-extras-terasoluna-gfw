package org.thymeleaf.engine;

import org.thymeleaf.cache.NonCacheableCacheEntryValidity;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresource.StringTemplateResource;

public class TestTemplateDataBuilder {

    public static TemplateData build(final String template) {
        return new TemplateData(template, null, new StringTemplateResource(template), TemplateMode.HTML,
                NonCacheableCacheEntryValidity.INSTANCE);
    }

}