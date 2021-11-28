package ru.otus.domain;

import java.util.TreeSet;

public class MyAtm implements Atm {
    private final TreeSet<CashCell> cashCells;

    public MyAtm(TreeSet<CashCell> cashCells) {
        this.cashCells = cashCells;
    }

    public TreeSet<CashCell> getCashCells() {
        return cashCells;
    }
}
