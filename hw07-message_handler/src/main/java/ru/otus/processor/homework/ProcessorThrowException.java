package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorThrowException implements Processor {
    DateTimeProvider dateTimeProvider;

    public ProcessorThrowException(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        final var second = dateTimeProvider.getDate().getSecond();
        if (second % 2 == 0) {
            throw new RuntimeException("the current second is even");
        }
        return message;
    }
}
