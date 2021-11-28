package ru.otus.domain;

public interface CashCell {
    int getDenomination();

    void add(int amount);

    void get(int amount);

    int getAll();
}
