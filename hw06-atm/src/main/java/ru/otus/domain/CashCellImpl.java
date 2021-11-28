package ru.otus.domain;

public class CashCellImpl implements CashCell {
    private final int denomination;
    private int amount;

    public CashCellImpl(int denomination) {
        this.denomination = denomination;
    }

    @Override
    public int getDenomination() {
        return this.denomination;
    }

    @Override
    public void add(int amount) {
        this.amount += amount;
    }

    @Override
    public void get(int amount) {
        this.amount -= amount;
    }

    @Override
    public int getAll() {
        return this.amount;
    }
}
