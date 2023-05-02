package org.thymeleaf.web.servlet;

import org.thymeleaf.web.IWebExchange;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestWebExchangeBuilder {

    public static IWebExchange build(final HttpServletRequest request, final HttpServletResponse response) {
        return new JakartaServletWebExchange(
            new JakartaServletWebRequest(request),
            new JakartaServletWebSession(request),
            new JakartaServletWebApplication(request.getServletContext()),
            response);
    }
}