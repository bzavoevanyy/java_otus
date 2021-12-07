package ru.otus.service;

import ru.otus.domain.Atm;
import ru.otus.domain.CashCell;
import ru.otus.domain.Denominations;

import java.util.*;

public class AtmServiceImpl implements AtmService {
    private final Atm atm;

    public AtmServiceImpl(Atm atm) {
        this.atm = atm;
    }

    @Override
    public int putCash(Map<Denominations, Integer> cash) {

        var cashCells = atm.getCashCells();

        for (var entry : cash.entrySet()) {
            long count = cashCells.stream().filter(cashCell -> cashCell.getDenomination() == entry.getKey())
                    .peek(cashCell -> cashCell.add(entry.getValue())).count();
            if (count != 1) {
                throw new AtmOperationException("Atm out of service");
            }
        }
        return cash.entrySet().stream().map(entry -> entry.getKey().getValue() * entry.getValue()).reduce(Integer::sum)
                .orElseThrow(() -> new AtmOperationException("Atm out of service"));
    }

    @Override
    public Map<Denominations, Integer> getCash(int amount) {
        TreeSet<CashCell> cashCells = atm.getCashCells();
        Map<Denominations, Integer> cash = mapAmount(amount, cashCells);

        for (Map.Entry<Denominations, Integer> entry : cash.entrySet()) {
            long count = cashCells.stream().filter(cashCell -> cashCell.getDenomination().equals(entry.getKey()))
                    .peek(cashCell -> cashCell.get(entry.getValue())).count();
            if (count != 1) {
                throw new AtmOperationException("Atm out of service");
            }
        }
        return cash;
    }

    @Override
    public int getBalance() {
        return atm.getCashCells().stream().map(entry -> entry.getDenomination().getValue() * entry.getAll())
                .reduce(Integer::sum).orElseThrow(() -> new AtmOperationException("Atm out of service"));
    }

    private Map<Denominations, Integer> mapAmount(int amount, TreeSet<CashCell> cashCells) {

        Map<Denominations, Integer> cash = new HashMap<>();

        for (CashCell cashCell : cashCells.descendingSet()) {
            Denominations denomination = cashCell.getDenomination();
            int banknoteAvailable = cashCell.getAll();
            final var i = amount / denomination.getValue();
            if (i > 0) {
                if (i - banknoteAvailable > 0) {
                    cash.put(denomination, banknoteAvailable);
                    amount = amount % denomination.getValue() + (i - banknoteAvailable) * denomination.getValue();
                } else {
                    cash.put(denomination, i);
                    amount = amount % denomination.getValue();
                }
            }
        }
        if (amount > 0) {
            throw new AtmOperationException("Atm has not enough money");
        }
        return cash;
    }
}
