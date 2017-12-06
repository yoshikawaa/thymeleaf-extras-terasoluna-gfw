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
package io.github.yoshikawaa.gfw.web.thymeleaf.dialect;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionEnhancingDialect;
import org.thymeleaf.processor.IProcessor;

import io.github.yoshikawaa.gfw.web.thymeleaf.expression.Query;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.message.MessagesPanelAttrProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.pagination.PaginationAttrProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.standard.MultiLineTextAttrProcessor;
import io.github.yoshikawaa.gfw.web.thymeleaf.processor.token.transaction.TransactionTokenAttrProcessor;

/**
 * Dialect for TERASOLUNA common libraries.
 * 
 * @see MessagesPanelAttrProcessor
 * @see PaginationAttrProcessor
 * @see TransactionTokenAttrProcessor
 * @see MultiLineTextAttrProcessor
 * @see Query
 */
public class TerasolunaGfwDialect extends AbstractDialect implements IExpressionEnhancingDialect {

    private static final String DIALECT_PREFIX = "t";
    private static final String EXPRESSION_NAME = "query";

    private final String dialectPrefix;

    /**
     * Using default dialect prefix.
     */
    public TerasolunaGfwDialect() {
        this(DIALECT_PREFIX);
    }

    /**
     * Using custom dialect prefix.
     * 
     * @param dialectPrefix prefix of attribute
     */
    public TerasolunaGfwDialect(String dialectPrefix) {
        super();
        this.dialectPrefix = dialectPrefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrefix() {
        return dialectPrefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<IProcessor> getProcessors() {
        Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new MessagesPanelAttrProcessor(dialectPrefix));
        processors.add(new PaginationAttrProcessor(dialectPrefix));
        processors.add(new TransactionTokenAttrProcessor(dialectPrefix));
        processors.add(new MultiLineTextAttrProcessor(dialectPrefix));
        return processors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getAdditionalExpressionObjects(IProcessingContext processingContext) {
        return Collections.singletonMap(EXPRESSION_NAME, new Query());
    }

}
