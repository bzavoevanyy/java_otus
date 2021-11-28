package ru.otus.service;

import org.junit.jupiter.api.*;
import ru.otus.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Atm test")
class AtmTest {
    private AtmService atmService;

    @BeforeEach
    void setUp() {
        TreeSet<CashCell> cashCells = new TreeSet<>((Comparator.comparing(CashCell::getDenomination)));
        cashCells.addAll(Set.of(
                new CashCellImpl(100),
                new CashCellImpl(500),
                new CashCellImpl(1000),
                new CashCellImpl(2000),
                new CashCellImpl(5000)));
        Atm myAtm = new MyAtm(cashCells);
        atmService = new AtmServiceImpl(myAtm);
    }

    @DisplayName("Method putCash to atm(zero balance) with right banknote")
    @Test
    @Order(1)
    void should_put_money_to_atm_cells_and_return_amount() {
        Map<Integer, Integer> cash = Map.of(
                1000, 2,
                500, 3,
                100, 2);
        var put = atmService.putCash(cash);
        assertThat(put).isEqualTo(3700);
    }

    @DisplayName("Method putCash test with wrong banknote")
    @Test
    @Order(2)
    void should_throw_exception_cell_not_found() {
        Map<Integer, Integer> cash = Map.of(
                1000, 2,
                500, 3,
                100, 2,
                50, 1);
        assertThatThrownBy(() -> atmService.putCash(cash)).hasMessage("Cash cell for 50 banknote not found");
    }

    @DisplayName("Method getCash if atm has enough money")
    @Test
    @Order(3)
    void should_return_right_map_of_banknotes() {
        Map<Integer, Integer> cash = Map.of(
                5000, 10,
                2000, 10,
                1000, 10,
                500, 10,
                100, 10);
        atmService.putCash(cash);
        final var money = atmService.getCash(8700);
        assertThat(money)
                .isNotNull()
                .hasSize(5)
                .contains(entry(5000, 1), entry(2000, 1), entry(1000, 1), entry(500, 1), entry(100, 2));
    }
    @DisplayName("Method getCash if atm has not enough money")
    @Test
    @Order(4)
    void should_throw_exception_not_enough_money() {
        assertThatThrownBy(() -> atmService.getCash(8700)).hasMessage("Atm has not enough money");
    }

    @DisplayName("Methods putCash and getBalance test")
    @Test
    @Order(5)
    void should_put_money_and_return_right_balance() {
        Map<Integer, Integer> cash = Map.of(
                1000, 2,
                500, 3,
                100, 2);
        var money = atmService.putCash(cash);
        money += atmService.putCash(cash);
        assertThat(atmService.getBalance()).isEqualTo(7400).isEqualTo(money);
    }
}