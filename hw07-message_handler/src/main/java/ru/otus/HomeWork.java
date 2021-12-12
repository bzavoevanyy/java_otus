package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.LoggerProcessor;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.ProcessorChangerField11AndField12;
import ru.otus.processor.homework.ProcessorThrowException;

import java.time.LocalDateTime;
import java.util.List;

public class HomeWork {


    public static void main(String[] args) {

        final List<Processor> processors = List.of(new LoggerProcessor(new ProcessorChangerField11AndField12()),
                new ProcessorThrowException(LocalDateTime::now));
        var complexProcessor = new ComplexProcessor(processors, ex -> System.out.println(ex.getMessage()));
        final var listener = new ListenerPrinterConsole();
        complexProcessor.addListener(listener);
        final var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field11("field11")
                .field12("field12")
                .field13(new ObjectForMessage())
                .build();
        final var result = complexProcessor.handle(message);
        System.out.println("Result: " + result);

    }
}
