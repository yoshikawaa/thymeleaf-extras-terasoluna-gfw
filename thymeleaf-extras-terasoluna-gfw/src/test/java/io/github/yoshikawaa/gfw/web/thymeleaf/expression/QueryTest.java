package io.github.yoshikawaa.gfw.web.thymeleaf.expression;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class QueryTest {

    @Test
    public void testQuery() {
        // setup
        Param param = new Param("test", "success");
        Query query = new Query();
        
        // execute and assert.
        assertThat(query.params(param)).isEqualTo("name=test&value=success");
    }

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
