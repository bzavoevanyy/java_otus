package ru.otus.domain;

import java.util.TreeSet;

public class MyAtm implements Atm {

    // Ячейки для хранения банкнот разного номинала
    private final TreeSet<CashCell> cashCells;

    public MyAtm(TreeSet<CashCell> cashCells) {
        this.cashCells = cashCells;
    }

    public TreeSet<CashCell>  getCashCells() {
        return cashCells;
    }
}
