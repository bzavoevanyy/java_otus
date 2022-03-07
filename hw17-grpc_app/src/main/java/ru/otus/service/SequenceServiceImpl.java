package ru.otus.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class SequenceServiceImpl implements SequenceService {
    @Override
    public List<Long> getSequence(Long firstValue, Long lastValue) {
        return LongStream.range(firstValue + 1, lastValue + 1).boxed().collect(Collectors.toList());
    }
}
