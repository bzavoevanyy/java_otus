package ru.otus;

import io.grpc.ServerBuilder;
import ru.otus.service.RemoteSequenceServiceImpl;
import ru.otus.service.SequenceServiceImpl;

import java.io.IOException;

public class GRPCServer {
    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        final var sequenceService = new SequenceServiceImpl();
        final var remoteSequenceService = new RemoteSequenceServiceImpl(sequenceService);
        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(remoteSequenceService)
                .build();
        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}
