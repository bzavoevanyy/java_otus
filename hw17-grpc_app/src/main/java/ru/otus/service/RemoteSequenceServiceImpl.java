package ru.otus.service;


import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.RemoteSequenceServiceGrpc;
import ru.otus.protobuf.generated.RequestSequenceMessage;
import ru.otus.protobuf.generated.ResponseSequenceMessage;

public class RemoteSequenceServiceImpl extends RemoteSequenceServiceGrpc.RemoteSequenceServiceImplBase {

    private final SequenceService sequenceService;

    public RemoteSequenceServiceImpl(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @Override
    public void getSequence(RequestSequenceMessage request, StreamObserver<ResponseSequenceMessage> responseObserver) {
        final var sequence = sequenceService.getSequence(request.getFirstValue(), request.getLastValue());
        sequence.forEach(currentValue -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            responseObserver.onNext(ResponseSequenceMessage.newBuilder().setCurrentValue(currentValue).build());
        });
        responseObserver.onCompleted();
    }
}
