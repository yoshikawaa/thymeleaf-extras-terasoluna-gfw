package io.github.yoshikawaa.gfw.web.thymeleaf.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;
import org.terasoluna.gfw.web.el.Functions;
import org.thymeleaf.exceptions.TemplateProcessingException;

import io.github.yoshikawaa.gfw.web.thymeleaf.util.ReflectionUtils;

public class ReflectionUtilsTest {

    public ReflectionUtilsTest() {
        ReflectionUtils.newInstance(ReflectionUtils.class, true);
    }
    
    @Test
    public void testNewInstance() {

        assertThat(ReflectionUtils.newInstance(Object.class)).isInstanceOf(Object.class);
        assertThat(ReflectionUtils.newInstance(Object.class, false)).isInstanceOf(Object.class);
        assertThat(ReflectionUtils.newInstance(Functions.class, true)).isInstanceOf(Functions.class);
        assertThatThrownBy(() -> {
            ReflectionUtils.newInstance(Functions.class);
        }).isInstanceOf(TemplateProcessingException.class)
                .hasMessage("failed to create instance.")
                .hasCauseInstanceOf(IllegalAccessException.class);
    }
}
