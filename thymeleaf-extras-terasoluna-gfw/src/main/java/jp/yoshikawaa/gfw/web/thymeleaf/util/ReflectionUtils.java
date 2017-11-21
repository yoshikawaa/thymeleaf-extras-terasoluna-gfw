package jp.yoshikawaa.gfw.web.thymeleaf.util;

import java.lang.reflect.Constructor;

import org.thymeleaf.exceptions.TemplateProcessingException;

public class ReflectionUtils {

    public static <T> T newInstance(Class<T> clazz) {
        return newInstance(clazz, false);
    }

    public static <T> T newInstance(Class<T> clazz, boolean isPrivate) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            if (isPrivate) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance();
        } catch (Exception e) {
            throw new TemplateProcessingException("failed to create instance.", e);
        }
    }
    
    private ReflectionUtils() {
    }
    
}
