package ru.otus.domain;

public enum Denominations {
    HUNDRED(100),
    FIVE_HUNDRED(500),
    THOUSAND(1000),
    TWO_THOUSAND(2000),
    FIVE_THOUSAND(5000);

    private final int value;

    Denominations(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

