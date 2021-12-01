package ru.otus.service;

import org.junit.jupiter.api.*;
import ru.otus.domain.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static ru.otus.domain.Denominations.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Atm test")
public class AtmTest {
    private AtmService atmService;

    @BeforeEach
    void setUp() {

        TreeSet<CashCell> cashCells = Arrays.stream(Denominations.values()).map(CashCellImpl::new)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(CashCell::getDenomination))));
        Atm myAtm = new MyAtm(cashCells);
        atmService = new AtmServiceImpl(myAtm);
    }

    @DisplayName("Method putCash to atm(zero balance) with right banknote")
    @Test
    @Order(1)
    void should_put_money_to_atm_cells_and_return_amount() {
        Map<Denominations, Integer> cash = Map.of(
                THOUSAND, 2,
                FIVE_HUNDRED, 3,
                HUNDRED, 2);
        var put = atmService.putCash(cash);
        assertThat(put).isEqualTo(3700);
    }

    @DisplayName("Method putCash test with wrong banknote amount")
    @Test
    @Order(2)
    void should_throw_exception_wrong_banknote_amount() {
        Map<Denominations, Integer> cash = Map.of(
                THOUSAND, 2,
                FIVE_HUNDRED, 3,
                HUNDRED, -2);
        assertThatThrownBy(() -> atmService.putCash(cash)).hasMessage("Wrong operation! Only from 1 to 30 banknotes");
    }

    @DisplayName("Method getCash if atm has enough money")
    @Test
    @Order(3)
    void should_return_right_map_of_banknotes() {
        final var cash = Arrays.stream(values()).map(denominations -> entry(denominations, 10))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        atmService.putCash(cash);
        final var money = atmService.getCash(8700);
        assertThat(money)
                .isNotNull()
                .hasSize(5)
                .contains(entry(FIVE_THOUSAND, 1), entry(TWO_THOUSAND, 1), entry(THOUSAND, 1),
                        entry(FIVE_HUNDRED, 1), entry(HUNDRED, 2));
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
        Map<Denominations, Integer> cash = Map.of(
                THOUSAND, 2,
                FIVE_HUNDRED, 3,
                HUNDRED, 2);
        var money = atmService.putCash(cash);
        money += atmService.putCash(cash);
        assertThat(atmService.getBalance()).isEqualTo(7400).isEqualTo(money);
    }
}