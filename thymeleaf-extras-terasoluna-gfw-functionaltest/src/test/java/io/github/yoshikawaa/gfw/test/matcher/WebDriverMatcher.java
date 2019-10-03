package io.github.yoshikawaa.gfw.test.matcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WebDriverMatcher {

    public static void click(WebDriver driver, String xpath) throws IOException {
        WebElement element = driver.findElement(By.xpath(xpath));
        assertThat(element).isNotNull();
        element.click();
    }

    public static void exists(WebDriver driver, String xpath) {
        WebElement element = driver.findElement(By.xpath(xpath));
        assertThat(element).isNotNull();
    }

    public static void notExists(WebDriver driver, String xpath) {
        try {
            driver.findElement(By.xpath(xpath));
        } catch (NoSuchElementException e) {
            return;
        }
        fail("element [" + xpath + "] is found.");
    }

    public static void hasText(WebDriver driver, String xpath, String expected) {
        WebElement element = driver.findElement(By.xpath(xpath));
        assertThat(element).isNotNull();
        assertThat(element.getText()).contains(expected);
    }

    public static void hasTexts(WebDriver driver, String xpath, String... expected) {
        List<WebElement> elements = driver.findElements(By.xpath(xpath));
        assertThat(elements).isNotNull();
        assertThat(elements).hasSize(expected.length);
        assertThat(elements.stream().map(e -> e.getText()).collect(Collectors.toList())).containsExactly(expected);
    }
}
