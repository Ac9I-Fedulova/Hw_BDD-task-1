package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MoneyTransferTest {
    DashboardPage dashboardPage;
    DataHelper.Card firstCard;
    DataHelper.Card secondCard;
    int firstCardBalance;
    int secondCardBalance;

    @BeforeEach
    void setUp() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
        firstCard = DataHelper.getFirstCard();
        secondCard = DataHelper.getSecondCard();
        firstCardBalance = dashboardPage.getCardBalance(firstCard);
        secondCardBalance = dashboardPage.getCardBalance(secondCard);
    }

    @Test
    void shouldTransferPartOfAmountFromSecondToFirstCard() {
        int amount = DataHelper.genRandomAmount(secondCardBalance);
        var transferPage = dashboardPage.selectCard(firstCard); // выбираем пополнить первую карту
        var expectedBalanceFirstCard = firstCardBalance + amount;
        var expectedBalanceSecondCard = secondCardBalance - amount;
        dashboardPage = transferPage.validTransferMoney(String.valueOf(amount), secondCard);
        dashboardPage.reloadDashboardPage();
        assertAll(() -> dashboardPage.checkCardBalance(firstCard, expectedBalanceFirstCard),
                () -> dashboardPage.checkCardBalance(secondCard, expectedBalanceSecondCard));
    }

    @Test
    void shouldTransferAllAmountsFromFirstCardToSecond() {
        int amount = firstCardBalance;
        var transferPage = dashboardPage.selectCard(secondCard); // выбираем пополнить вторую карту
        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        dashboardPage = transferPage.validTransferMoney(String.valueOf(amount), firstCard);
        dashboardPage.reloadDashboardPage();
        assertAll(() -> dashboardPage.checkCardBalance(firstCard, expectedBalanceFirstCard),
                () -> dashboardPage.checkCardBalance(secondCard, expectedBalanceSecondCard));
    }

    @Test
    void shouldGetErrorMessageIfAmountExceedsBalance() {
        int amount = DataHelper.genInvalidRandomAmount(secondCardBalance);
        var transferPage = dashboardPage.selectCard(firstCard); // выбираем пополнить первую карту
        transferPage.transferMoney(String.valueOf(amount), secondCard);
        assertAll(() -> transferPage.findErrorMessage("Недостаточно средств или установлен лимит"),
                () -> dashboardPage.reloadDashboardPage(),
                () -> dashboardPage.checkCardBalance(firstCard, firstCardBalance),
                () -> dashboardPage.checkCardBalance(secondCard, secondCardBalance));
    }
}
