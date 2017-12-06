/**
 * Copyright (c) 2017 Atsushi Yoshikawa (https://yoshikawaa.github.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.yoshikawaa.gfw.web.thymeleaf.processor;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;

public abstract class AbstractAttributeRemovalAttrProcessor extends AbstractAttrProcessor {

    private final String dialectPrefix;

    protected AbstractAttributeRemovalAttrProcessor(String dialectPrefix, String attributeName) {
        super(attributeName);
        this.dialectPrefix = dialectPrefix;
    }

    protected String getDialectPrefix() {
        return dialectPrefix;
    }

    @Override
    protected ProcessorResult processAttribute(Arguments arguments, Element element, String attributeName) {

        process(arguments, element, attributeName);
        element.removeAttribute(attributeName);

        return ProcessorResult.OK;
    }

    protected abstract void process(final Arguments arguments, final Element element, final String attributeName);

}
