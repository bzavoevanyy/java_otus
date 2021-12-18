package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorChangerField11AndField12 implements Processor {

    @Override
    public Message process(Message message) {
        final var field11 = message.getField11();
        return message.toBuilder().field11(message.getField12()).field12(field11).build();
    }
}
