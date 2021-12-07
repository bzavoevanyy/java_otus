package ru.otus.domain;

public interface CashCell {
    Denominations getDenomination();

    void add(int amount);

    void get(int amount);

    int getAll();
}
