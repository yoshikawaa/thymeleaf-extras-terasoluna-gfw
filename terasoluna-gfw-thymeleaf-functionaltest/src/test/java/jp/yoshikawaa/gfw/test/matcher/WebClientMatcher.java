package jp.yoshikawaa.gfw.test.matcher;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class WebClientMatcher {

    public static HtmlPage click(DomNode page, String xpath) throws IOException {
        HtmlElement element = page.getFirstByXPath(xpath);
        assertThat(element).isNotNull();
        return element.click();
    }

    public static void exists(DomNode page, String xpath) {
        HtmlElement element = page.getFirstByXPath(xpath);
        assertThat(element).isNotNull();
    }

    public static void notExists(DomNode page, String xpath) {
        HtmlElement element = page.getFirstByXPath(xpath);
        assertThat(element).isNull();
    }

    public static void hasText(DomNode page, String xpath, String expected) {
        HtmlElement element = page.getFirstByXPath(xpath);
        assertThat(element).isNotNull();
        assertThat(element.asText()).contains(expected);
    }

    public static void hasTexts(DomNode page, String xpath, String... expected) {
        List<?> elements = page.getByXPath(xpath);
        assertThat(elements).isNotNull();
        assertThat(elements).hasSize(expected.length);
        assertThat(elements.stream().map(e -> ((HtmlElement) e).asText()).collect(Collectors.toList()))
                .containsExactly(expected);
    }
}
