package ru.otus.service;

import ru.otus.domain.Atm;
import ru.otus.domain.CashCell;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class AtmServiceImpl implements AtmService {
    private final Atm atm;

    public AtmServiceImpl(Atm atm) {
        this.atm = atm;
    }

    @Override
    public int putCash(Map<Integer, Integer> cash) {
        TreeSet<CashCell> cashCells = atm.getCashCells();
        for (Map.Entry<Integer, Integer> entry : cash.entrySet()) {
            long count = cashCells.stream().filter(cashCell -> cashCell.getDenomination() == entry.getKey())
                    .peek(cashCell -> cashCell.add(entry.getValue())).count();
            if (count != 1) {
                throw new AtmOperationException(String.format("Cash cell for %s banknote not found", entry.getKey()));
            }
        }
        return cash.entrySet().stream().map(entry -> entry.getKey() * entry.getValue()).reduce(Integer::sum)
                .orElseThrow(() -> new AtmOperationException("Atm out of service"));
    }

    @Override
    public Map<Integer, Integer> getCash(int amount) {
        Map<Integer, Integer> cash = mapAmount(amount);
        TreeSet<CashCell> cashCells = atm.getCashCells();
        for (Map.Entry<Integer, Integer> entry : cash.entrySet()) {
            long count = cashCells.stream().filter(cashCell -> cashCell.getDenomination() == entry.getKey())
                    .peek(cashCell -> cashCell.get(entry.getValue())).count();
            if (count != 1) {
                throw new AtmOperationException("Atm out of service");
            }
        }
        return cash;
    }

    @Override
    public int getBalance() {
        return atm.getCashCells().stream().map(entry -> entry.getDenomination() * entry.getAll())
                .reduce(Integer::sum).orElseThrow(() -> new AtmOperationException("Atm out of service"));
    }

    private Map<Integer, Integer> mapAmount(int amount) {
        TreeSet<CashCell> cashCells = atm.getCashCells();
        Map<Integer, Integer> cash = new HashMap<>();
        for (CashCell cashCell : cashCells.descendingSet()) {
            int denomination = cashCell.getDenomination();
            int banknoteAvailable = cashCell.getAll();
            final var i = amount / denomination;
            if (i > 0) {
                if (i - banknoteAvailable > 0) {
                    cash.put(denomination, banknoteAvailable);
                    amount = amount % denomination + (i - banknoteAvailable) * denomination;
                } else {
                    cash.put(denomination, i);
                    amount = amount % denomination;
                }
            }
        }
        if (amount > 0) {
            throw new AtmOperationException("Atm has not enough money");
        }
        return cash;
    }
}
