# thymeleaf-extras-terasoluna-gfw

[![Build Status](https://github.com/yoshikawaa/thymeleaf-extras-terasoluna-gfw/actions/workflows/maven.yml/badge.svg)](https://github.com/yoshikawaa/thymeleaf-extras-terasoluna-gfw/actions)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/f9b0a438e13748b2a603f8f9fbb83f6c)](https://www.codacy.com/gh/yoshikawaa/thymeleaf-extras-terasoluna-gfw/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=yoshikawaa/thymeleaf-extras-terasoluna-gfw&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/f9b0a438e13748b2a603f8f9fbb83f6c)](https://www.codacy.com/gh/yoshikawaa/thymeleaf-extras-terasoluna-gfw/dashboard?utm_source=github.com&utm_medium=referral&utm_content=yoshikawaa/thymeleaf-extras-terasoluna-gfw&utm_campaign=Badge_Coverage)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.yoshikawaa.gfw/spring-test-terasoluna-gfw.svg)](https://repo.maven.apache.org/maven2/io/github/yoshikawaa/gfw/thymeleaf-extras-terasoluna-gfw/)
[![License](https://img.shields.io/badge/license-Apache%202-blue.svg?style=flat)](https://github.com/yoshikawaa/thymeleaf-extras-terasoluna-gfw/blob/thymeleaf3/LICENSE.txt)

Thymeleaf custom dialect based on TERASOLUNA Framework 5.x JSP tag library.

> This is a personal experimental project unrelated to TERASOLUNA. TERASOLUNA is a registered trademark of NTT DATA Corporation.

## Notes

* Supports upper Java 11
* Supports Terasoluna 5.7.1.SP1
* Supports Thymeleaf 3.0.12

----

## Getting Start

### Configure dependency.

```xml
<dependency>
    <groupId>io.github.yoshikawaa.gfw</groupId>
    <artifactId>thymeleaf-extras-terasoluna-gfw</artifactId>
    <version>1.0.1</version>
</dependency>
```

### Configure `SpringTemplateEngine`.

Add dialect `TerasolunaGfwDialect` to `SpringTemplateEngine`.

* In Java Config
    ```java
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.addDialect(new TerasolunaGfwDialect());
        return templateEngine;
    }
    ```

    > If you develop with Spring Boot, you have only to define `TerasolunaGfwDialect` bean.

* In XML Namespace

    ```xml
    <bean id="templateEngine" class="org.thymeleaf.spring4.SpringTemplateEngine">
        <property name="templateResolver" ref="templateResolver" />
        <property name="additionalDialects">
            <set>
                <bean class="io.github.yoshikawaa.gfw.web.thymeleaf.dialect.TerasolunaGfwDialect" />
            </set>
        </property>
    </bean>
    ```

### Configure template HTMLs.

Add xml name space to template HTMLs.

```html
<!DOCTYPE html>
<html xmlns:t="https://io.github.yoshikawaa">
    <!-- omitted. -->
</html>
```

> By Default, name space is `t`.
> If you change name space, set `dialectPrefix` to `TerasolunaGfwDialect`.

----

## Features

* Attribute Processor
  - `t:messages-panel`
  - `t:pagination`
  - `t:transaction`
  - `t:mtext`
* Expression Object
  - `#query`

### `t:messages-panel`

Output messages as children elements.

```html
<div t:messages-panel>messages</div>
```

By default, the attribute named `resultMessages` is searched in order of request, session, and servlet context and outputted.

Supports messages of the following types.

* ResultMessages
* Any iteratable types
* Any single types

Supports a message of the following types.

* ResultMessage (as `ResultMessageUtils#resolveMessage`)
* Throwable (as `#getMessage`)
* String and Any types (as `#toString`)

You can customize with the following attribute values.

| name | description | default |
|------|-------------|---------|
| messages-panel | source of the messages as expression ex. `${messages}`. | `` |
| panel-class-name | 1st css class name to add. | `alart` |
| panel-type-class-prefix | 2nd css class name prefix to add. | `alart-` |
| messages-type | 2nd css class name suffix to add. (If use `ResultMessages`, automatically set) | `` |
| outer-element | tag name of list. | `ul` |
| inner-element | tag name of list element. | `li` |

### `t:pagination`

Output `Page` object as pagination links.

```html
<ul t:pagination>page links</ul>
```

By default, the expression `${page}` is interpreted and outputted.

You can customize with the following attribute values.

| name | description | default |
|------|-------------|---------|
| pagination | source of the page as expression ex. `${cotents}`. | `${page}` |
| inner-element | tag name of page link element. | `li` |
| disabled-class | class name of link inactive. | `disabled` |
| active-class | class name of link active. | `active` |
| first-link-text | caption of link to first page. | `<<` |
| previous-link-text | caption of link to previous page. | `<` |
| next-link-text | caption of link to next page. | `>` |
| last-link-text | caption of link to last page. | `>>` |
| max-display-count | count of display page number links. | `5` |
| disabled-href | URL of link inactive. | `javascript:void(0)` |
| href-tmpl | URL template as `@{/}`. If want to build query string from Objects, Maps, can use `#query.urlexpression()`. | `` |
| enable-link-of-current-page | enable to link to current page or not. | `false` |

### `t:transaction`

Output transaction token to hidden input field named `_TRANSACTION_TOKEN` without `th:action`.

```html
<input t:transaction />
```

By default, the attribute named `org.terasoluna.gfw.web.token.transaction.TransactionTokenInterceptor.NEXT_TOKEN` is searched in order of request, session, and servlet context and outputted.

> If you use `th:action`, `TransactionTokenRequestDataValueProcessor` automatically output the hidden fields as well.

You can customize with the following attribute values.

| name | description | default |
|------|-------------|---------|
| transaction | source of the token as expression ex. `${token}`. | `` |

### `t:mtext`

Escape text and convert line-break characters to `<br>` element.

```html
<span t:mtext="${text}">multi-line text</span>
```

Support `CR`, `LF`, and `CRLF` as line-break characters.

### `#query`

Build Query String same as `Functions#query()`.

```html
<a th:href="http://example.com/sample?${#query.string(form)}">
```

`#query.string()` is provided for building direct URI. This builds query string with URI encode using `#uris.escapeQueryParam()`.

```html
<ul t:pagination t:href-tmpl="@{/sample(__${#query.urlexpression(form)}__)}">page links</ul>
```

`#query.urlexpression()` is provided for using into URL Expression `@{}`. This builds query string without URI encode.
