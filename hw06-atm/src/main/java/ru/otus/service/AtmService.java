package ru.otus.service;

import java.util.Map;

public interface AtmService {
    int putCash(Map<Integer, Integer> cash);

    Map<Integer, Integer> getCash(int amount);

    int getBalance();
}
