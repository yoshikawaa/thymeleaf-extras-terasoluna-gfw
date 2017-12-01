package io.github.yoshikawaa.gfw.web.thymeleaf.expression;

import org.terasoluna.gfw.web.el.Functions;

public class Query {

    public String params(Object params) {
        return Functions.query(params);
    }
}
