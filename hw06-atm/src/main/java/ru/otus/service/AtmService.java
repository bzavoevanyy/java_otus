package ru.otus.service;

import ru.otus.domain.Denominations;

import java.util.Map;

public interface AtmService {
    int putCash(Map<Denominations, Integer> cash);

    Map<Denominations, Integer> getCash(int amount);

    int getBalance();
}
