package ru.otus.processor.homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.model.Message;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("ProcessorThrowException test")
class ProcessorThrowExceptionTest {
    private ProcessorThrowException processor;
    private final DateTimeProvider dateTimeProvider = Mockito.mock(DateTimeProvider.class);
    private Message message;

    @BeforeEach
    void setUp() {
        processor = new ProcessorThrowException(dateTimeProvider);
        message = new Message.Builder(1L).field1("field1").build();
    }

    @Test
    @DisplayName("Should throw RunTimeException on even second")
    void processOnEvenSecond() {
        when(dateTimeProvider.getDate()).thenReturn(LocalDateTime.ofEpochSecond(2, 0, ZoneOffset.UTC));
        assertThatThrownBy(() -> processor.process(message)).isInstanceOf(RuntimeException.class)
                .hasMessage("the current second is even");
    }

    @Test
    @DisplayName("Does not throw any exception on odd second")
    void processOnOddSecond() {
        when(dateTimeProvider.getDate()).thenReturn(LocalDateTime.ofEpochSecond(1, 0, ZoneOffset.UTC));
        assertThatCode(() -> processor.process(message)).doesNotThrowAnyException();
    }
}