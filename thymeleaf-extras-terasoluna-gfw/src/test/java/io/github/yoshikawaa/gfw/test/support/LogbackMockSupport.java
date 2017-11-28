package io.github.yoshikawaa.gfw.test.support;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.mockito.ArgumentMatcher;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

public abstract class LogbackMockSupport {

    protected final Class<?> mockLoggerClass;

    @SuppressWarnings("unchecked")
    private final Appender<ILoggingEvent> mockAppender = mock(Appender.class);

    public LogbackMockSupport() {
        this(null);
    }

    public LogbackMockSupport(Class<?> mockLoggerClass) {
        this.mockLoggerClass = mockLoggerClass;
    }

    @Before
    public void setup() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = (mockLoggerClass == null) ? loggerContext.getLogger(Logger.ROOT_LOGGER_NAME)
                : loggerContext.getLogger(mockLoggerClass);
        when(mockAppender.getName()).thenReturn("MOCK");
        logger.addAppender(mockAppender);
    }

    protected void assertLogMessage(String message) {
        verify(mockAppender).doAppend(argThat(new ArgumentMatcher<LoggingEvent>() {
            @Override
            public boolean matches(Object argument) {
                return ((LoggingEvent) argument).getFormattedMessage().contains(message);
            }
        }));
    }

}
