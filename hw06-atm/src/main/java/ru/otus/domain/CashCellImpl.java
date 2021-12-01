package ru.otus.domain;

import ru.otus.service.AtmOperationException;

import java.util.Objects;

public class CashCellImpl implements CashCell {
    private final Denominations denomination;
    private int amount;

    public CashCellImpl(Denominations denomination) {
        this.denomination = denomination;
    }

    @Override
    public Denominations getDenomination() {
        return this.denomination;
    }

    @Override
    public void add(int amount) {
        addCash(amount);
    }

    @Override
    public void get(int amount) {
        this.amount -= amount;
    }

    @Override
    public int getAll() {
        return this.amount;
    }

    private void addCash(int amount) {
        if (amount <= 0 || amount > 30) {
            throw new AtmOperationException("Wrong operation! Only from 1 to 30 banknotes");
        } else {
            this.amount += amount;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CashCellImpl cashCell = (CashCellImpl) o;
        return denomination == cashCell.denomination;
    }

    @Override
    public int hashCode() {
        return Objects.hash(denomination);
    }

}
