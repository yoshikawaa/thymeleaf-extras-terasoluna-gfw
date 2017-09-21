package org.thymeleaf.engine;

import org.thymeleaf.cache.NonCacheableCacheEntryValidity;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresource.StringTemplateResource;

public class TestTemplateDataBuilder {

    public static TemplateData build(final String template, final TemplateMode templateMode) {
        return new TemplateData(template, null, new StringTemplateResource(template), templateMode,
                NonCacheableCacheEntryValidity.INSTANCE);
    }

}