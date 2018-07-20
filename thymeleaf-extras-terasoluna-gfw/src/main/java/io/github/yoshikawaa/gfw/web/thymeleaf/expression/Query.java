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
package io.github.yoshikawaa.gfw.web.thymeleaf.expression;

import static java.util.stream.Collectors.joining;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.springframework.beans.BeanUtils;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.terasoluna.gfw.web.el.Functions;
import org.terasoluna.gfw.web.el.ObjectToMapConverterWrapper;
import org.thymeleaf.expression.Uris;
import org.thymeleaf.util.MapUtils;

/**
 * Expression utility object for generating query parameters.
 * 
 * @author Atsushi Yoshikawa
 */
public class Query {

    /**
     * conversion service for format a value.
     */
    private static final FormattingConversionService CONVERSION_SERVICE = new DefaultFormattingConversionService();

    /**
     * converter from object to map.
     */
    private static final ObjectToMapConverterWrapper OBJECT_TO_MAP_CONVERTER = new ObjectToMapConverterWrapper(
            CONVERSION_SERVICE);

    private static final Uris URIS = new Uris();
    
    /**
     * Build query string from map or bean same as {@link Functions#query(Object)}.
     * <p>
     * Query string is encoded using {@link Uris#escapeQueryParam(String)}.
     * </p>
     * 
     * @see ObjectToMapConverterWrapper
     * @param params map or bean
     * @return query string. returns empty string if <code>params</code> is
     *         <code>null</code> or empty string or {@link Iterable} or
     *         {@link BeanUtils#isSimpleValueType(Class)}.
     */
    public String string(Object params) {
        return convert(params, e -> URIS.escapeQueryParam(e.getKey()) + "=" + URIS.escapeQueryParam(e.getValue()), "&");
    }

    /**
     * Build query string from map or bean for URL expression.
     * <p>
     * Query string is not encoded. You can encode it in URL expression ex. <code>@{/path(__${query.params(obj)}__)}</code>.
     * </p>
     * 
     * @see ObjectToMapConverterWrapper
     * @param params map or bean
     * @return query string. returns empty string if <code>params</code> is
     *         <code>null</code> or empty string or {@link Iterable} or
     *         {@link BeanUtils#isSimpleValueType(Class)}.
     */
    public String urlexpression(Object params) {
        return convert(params, e -> e.getKey() + "='" + e.getValue().replaceAll("'", "''") + "'", ",");
    }
    
    private String convert(Object params, Function<? super Entry<String, String>, ? extends String> mapper,
            CharSequence delimiter) {
        if (params == null || BeanUtils.isSimpleValueType(params.getClass())) {
            return "";
        }

        Map<String, String> map = OBJECT_TO_MAP_CONVERTER.convert(params);
        return MapUtils.isEmpty(map) ? "" : map.entrySet().stream().map(mapper).collect(joining(delimiter));
    }

}
