package ru.otus;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.RemoteSequenceServiceGrpc;
import ru.otus.protobuf.generated.RequestSequenceMessage;
import ru.otus.protobuf.generated.ResponseSequenceMessage;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class GRPCClient {
    private static final Logger logger = LoggerFactory.getLogger(GRPCClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final AtomicLong currentServerValue = new AtomicLong(0);

    public static void main(String[] args) throws InterruptedException {

        final var channel = ManagedChannelBuilder
                .forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        final var stub = RemoteSequenceServiceGrpc.newStub(channel);
        final var message = RequestSequenceMessage.newBuilder()
                .setFirstValue(0)
                .setLastValue(30)
                .build();
        var latch = new CountDownLatch(1);
        stub.getSequence(message, new StreamObserver<>() {
            @Override
            public void onNext(ResponseSequenceMessage value) {
                currentServerValue.set(value.getCurrentValue());
                logger.info("new value: {}", value.getCurrentValue());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                logger.info("\n\nComplete");
                latch.countDown();
            }
        });
        var currentValue = 0;
        var tempValue = 0;
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (currentServerValue.intValue() == tempValue) {
                currentValue = currentValue + 1;
            } else {
                currentValue = currentValue + currentServerValue.intValue() + 1;
            }

            logger.info("CurrentValue: {}", currentValue);
            tempValue = currentServerValue.intValue();
        }

        latch.await();

        channel.shutdown();
    }

}
