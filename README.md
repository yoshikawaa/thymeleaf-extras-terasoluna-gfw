# terasoluna-gfw-thymeleaf

[![Build Status](https://travis-ci.org/yoshikawaa/terasoluna-gfw-thymeleaf.svg?branch=thymeleaf2)](https://travis-ci.org/yoshikawaa/terasoluna-gfw-thymeleaf)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/95e7ad7beb0c4502872cda12213b9e07)](https://www.codacy.com/app/yoshikawaa/terasoluna-gfw-thymeleaf?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=yoshikawaa/terasoluna-gfw-thymeleaf&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/95e7ad7beb0c4502872cda12213b9e07)](https://www.codacy.com/app/yoshikawaa/terasoluna-gfw-thymeleaf?utm_source=github.com&utm_medium=referral&utm_content=yoshikawaa/terasoluna-gfw-thymeleaf&utm_campaign=Badge_Coverage)
[![License](https://img.shields.io/badge/license-Apache%202-blue.svg?style=flat)](https://github.com/yoshikawaa/terasoluna-gfw-thymeleaf/blob/thymeleaf2/LICENSE.txt)

A personal (experimental,hobby) project to create Thymeleaf custom dialect based on TERASOLUNA Framework 5.x JSP tag library.

## Notes

* Supports upper Java 8.
* Supports Thymeleaf 3.0 and 2.1.

----

## Getting Start

### Clone and install.

```bash
$ git clone -b thymeleaf2 https://github.com/yoshikawaa/terasoluna-gfw-thymeleaf.git
$ cd terasoluna-gfw-thymeleaf/thymeleaf-extras-terasoluna-gfw
$ mvn clean install
```

### Configure dependency.

Add dependency `thymeleaf-extras-terasoluna-gfw`.

```xml
<dependency>
    <groupId>jp.yoshikawaa.gfw</groupId>
    <artifactId>thymeleaf-extras-terasoluna-gfw</artifactId>
    <version><!--$VERSION$--></version>
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

* In XML Namespace

    ```xml
    <bean id="templateEngine" class="org.thymeleaf.spring4.SpringTemplateEngine">
        <property name="templateResolver" ref="templateResolver" />
        <property name="additionalDialects">
            <set>
                <bean class="jp.yoshikawaa.gfw.web.thymeleaf.dialect.TerasolunaGfwDialect" />
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

By Default, name space is `t`.
If you change name space, set `dialectPrefix` to `TerasolunaGfwDialect`.

----

## Function References

### Messages Panel Attribute Processor

```html
<div t:messages-panel="">messages</div>
```

Output messages as children elements.

By default, the attribute named `resultMessages` is searched in order of request, session, and servlet context and outputted.

Supports messages of the following types.

* ResultMessages
* Any iteratable types
* Any single types

Supports a message of the following types.

* ResultMessage (as `ResultMessageUtils#resolveMessage`)
* Throwable (as `#getMessage`)
* String and Any types (as `#toString`)

Can be customized with the following attribute values.

| name | description | default |
|:-----------------------:|:------------------------------------------------------------------------------:|:--------:|
| messages-panel | source of the messages as expression ex. `${messages}`. | `` |
| panel-class-name | 1st css class name to add. | `alart` |
| panel-type-class-prefix | 2nd css class name prefix to add. | `alart-` |
| messages-type | 2nd css class name suffix to add. (If use `ResultMessages`, automatically set) | `` |
| outer-element | tag name of list. | `ul` |
| inner-element | tag name of list element. | `li` |

### Pagination Attribute Processor

```html
<ul t:pagination="">page links</ul>
```

Output `Page` object as pagination links.

By default, the expression `${page}` is interpreted and outputted.

Can be customized with the following attribute values.

| name | description | default |
|:-------------------------------------:|:---------------------------------------------------------------------------------------------:|:--------------------:|
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
| href-tmpl | URL template as `@{/}`. | `` |
| criteria-query | URL parameters as `param1=a&param2=b`, or expression as `${#query.params(form)}`. | `` |
| disable-html-escape-of-criteria-query | sanitize `criteria-query` or not. | `false` |
| enable-link-of-current-page | enable to link to current page or not. | `false` |

### Transaction Token Attribute Processor

```html
<input t:transaction="" />
```

Output transaction token to hidden input field named `_TRANSACTION_TOKEN`.

By default, the attribute named `org.terasoluna.gfw.web.token.transaction.TransactionTokenInterceptor.NEXT_TOKEN` is searched in order of request, session, and servlet context and outputted.

Give it to the input field when you don't give `th:action` to the form.
(If you use `th:action`, `TransactionTokenRequestDataValueProcessor` output the hidden fields as well.)

Can be customized with the following attribute values.

| name | description | default |
|:-----------:|:-------------------------------------------------:|:-------:|
| transaction | source of the token as expression ex. `${token}`. | `` |

### Multi Line Text Attribute Processor

```html
<span t:mtext="${text}">multi-line text</span>
```

Escape text and convert line-break characters to `<br>` element.

Support `CR`, `LF`, and `CRLF` as line-break characters.

### Query Expression Object

```html
<ul t:pagination="" t:criteria-query="${#query.params(form)}">page links</ul>
```

Can use `Functions#query()` method in `terasoluna-gfw-web` as `#query.params()`.
