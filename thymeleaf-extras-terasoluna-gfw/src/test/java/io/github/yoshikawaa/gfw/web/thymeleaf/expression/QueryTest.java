package io.github.yoshikawaa.gfw.web.thymeleaf.expression;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.thymeleaf.expression.Uris;

import com.google.common.collect.ImmutableMap;

public class QueryTest {

    private static final Query QUERY = new Query();
    
    @Test
    public void testQuery() {
        // setup
        Param param = new Param("test", "success");
        
        // execute and assert.
        assertThat(QUERY.string(param)).isEqualTo("name=test&value=success");
    }

    @Test
    public void testQueryWithEmpty() {
        // execute and assert.
        assertThat(QUERY.string(null)).isEqualTo("");
    }

    @Test
    public void testQueryWithSingleType() {
        // setup
        String param = "test";

        // execute and assert.
        assertThat(QUERY.string(param)).isEqualTo("");
    }

    @Test
    public void testQueryWithMap() {
        // setup
        Map<String, String> param = ImmutableMap.of("name", "test", "value", "success");

        // execute and assert.
        assertThat(QUERY.string(param)).isEqualTo("name=test&value=success");
    }

    @Test
    public void testQueryWithEmptyMap() {
        // setup
        Map<String, String> param = Collections.emptyMap();

        // execute and assert.
        assertThat(QUERY.string(param)).isEqualTo("");
    }

    @Test
    public void testQueryWithMapEncoded() {
        // setup
        Map<String, String> param = ImmutableMap.of("名前", "テスト", "値", "サクセス");
        Uris uris = new Uris();
        StringBuilder expected = new StringBuilder();
        expected.append(uris.escapeQueryParam("名前") + "=" + uris.escapeQueryParam("テスト"));
        expected.append("&" + uris.escapeQueryParam("値") + "=" + uris.escapeQueryParam("サクセス"));
        
        // execute and assert.
        assertThat(QUERY.string(param)).isEqualTo(expected.toString());
    }

    @Test
    public void testUrlExpression() {
        // setup
        Param param = new Param("test", "success");
        
        // execute and assert.
        assertThat(QUERY.urlexpression(param)).isEqualTo("name='test',value='success'");
    }

    @Test
    public void testUrlExpressionWithEscapeQuote() {
        // setup
        Param param = new Param("test", "'success'");
        
        // execute and assert.
        assertThat(QUERY.urlexpression(param)).isEqualTo("name='test',value='''success'''");
    }

    // Getter and Setter used in Expression Language.
    @SuppressWarnings("unused")
    private static class Param {
        private String name;
        private String value;

        public Param(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
