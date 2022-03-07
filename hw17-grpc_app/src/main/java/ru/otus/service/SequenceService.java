package ru.otus.service;

import java.util.List;

public interface SequenceService {
    List<Long> getSequence(Long firstValue, Long lastValue);
}
