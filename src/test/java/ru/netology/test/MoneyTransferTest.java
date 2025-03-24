package ru.netology.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    @BeforeAll
    static void setUp() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    DashboardPage dashboardPage = new DashboardPage();

    @Test
    void shouldTransferPartOfAmountFromSecondToFirstCard() {  // часть суммы со второй на первую
        int initialBalanceFirstCard = dashboardPage.getCardBalance(dashboardPage.getFirstCardId());
        int initialBalanceSecondCard = dashboardPage.getCardBalance(dashboardPage.getSecondCardId());

        var transferPage = dashboardPage.selectCard(dashboardPage.getFirstCardId()); // выбираем пополнить первую карту
        int amount = DataHelper.genRandomAmount(initialBalanceSecondCard);
        var transferCard = transferPage.transferMoney(amount, DataHelper.getsecondCard());

        assertEquals(initialBalanceFirstCard + amount,
                transferCard.getCardBalance(dashboardPage.getFirstCardId()));
        assertEquals(initialBalanceSecondCard - amount,
                transferCard.getCardBalance(dashboardPage.getSecondCardId()));
    }

    @Test
    void shouldTransferAllAmountsFromFirstCardToSecond() {  // всю сумму с первой на вторую
        int initialBalanceFirstCard = dashboardPage.getCardBalance(dashboardPage.getFirstCardId());
        int initialBalanceSecondCard = dashboardPage.getCardBalance(dashboardPage.getSecondCardId());

        var transferPage = dashboardPage.selectCard(dashboardPage.getSecondCardId()); // выбираем пополнить вторую карту
        int amount = initialBalanceFirstCard;
        var transferCard = transferPage.transferMoney(amount, DataHelper.getfirstCard());

        assertEquals(initialBalanceFirstCard - amount,
                transferCard.getCardBalance(dashboardPage.getFirstCardId())); // 0
        assertEquals(initialBalanceSecondCard + amount,
                transferCard.getCardBalance(dashboardPage.getSecondCardId())); // 20000
    }

    @Test
    void shouldTransferFromSecondCardToFirstAmountGreaterThanBalance() { //суммы больше баланса со второй на первую

        int initialBalanceFirstCard = dashboardPage.getCardBalance(dashboardPage.getFirstCardId());
        int initialBalanceSecondCard = dashboardPage.getCardBalance(dashboardPage.getSecondCardId());

        var transferPage = dashboardPage.selectCard(dashboardPage.getFirstCardId()); // выбираем пополнить первую карту
        int amount = initialBalanceSecondCard + 222;
        var transferCard = transferPage.transferMoney(amount, DataHelper.getsecondCard());

        assertEquals(initialBalanceFirstCard + amount,
                transferCard.getCardBalance(dashboardPage.getFirstCardId())); // 20_222
        assertEquals(initialBalanceSecondCard - amount,
                transferCard.getCardBalance(dashboardPage.getSecondCardId())); // -222
    }

    @Test
    void shouldRemoveNegativeBalanceOfSecondCardWithAllAmountFromFirstCard() {  // вывести вторую карту из отрицательного баланса
        int initialBalanceFirstCard = dashboardPage.getCardBalance(dashboardPage.getFirstCardId());
        int initialBalanceSecondCard = dashboardPage.getCardBalance(dashboardPage.getSecondCardId());

        var transferPage = dashboardPage.selectCard(dashboardPage.getSecondCardId()); // выбираем пополнить вторую карту
        int amount = initialBalanceFirstCard;
        var transferCard = transferPage.transferMoney(amount, DataHelper.getfirstCard());

        assertEquals(initialBalanceFirstCard - amount,
                transferCard.getCardBalance(dashboardPage.getFirstCardId())); // 0
        assertEquals(initialBalanceSecondCard + amount,
                transferCard.getCardBalance(dashboardPage.getSecondCardId())); // 20 000
    }
}
