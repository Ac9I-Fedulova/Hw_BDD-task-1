package ru.netology.test;

import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    @Test
    void shouldTransferPartOfAmountFromSecondToFirstCard() {  // часть со второй на первую
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        int initialBalanceFirstCard = dashboardPage.getCardBalance(DataHelper.getfirstCard());
        int initialBalanceSecondCard = dashboardPage.getCardBalance(DataHelper.getsecondCard());

        var transferPage = dashboardPage.getCard(DataHelper.getfirstCard()); // выбираем пополнить первую карту

        int amount = 100;
        var transferCard = transferPage.transferMoney(amount, DataHelper.getsecondCard());

        assertEquals(initialBalanceFirstCard + amount, transferCard.getCardBalance(DataHelper.getfirstCard()));
        assertEquals(initialBalanceSecondCard - amount, transferCard.getCardBalance(DataHelper.getsecondCard()));
    }

    @Test
    void shouldTransferAllAmountsFromFirstCardToSecond() {  // всю с первой на вторую
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        int initialBalanceFirstCard = dashboardPage.getCardBalance(DataHelper.getfirstCard());
        int initialBalanceSecondCard = dashboardPage.getCardBalance(DataHelper.getsecondCard());

        var transferPage = dashboardPage.getCard(DataHelper.getsecondCard()); // выбираем пополнить вторую карту

        int amount = initialBalanceFirstCard;
        var transferCard = transferPage.transferMoney(amount, DataHelper.getfirstCard());

        assertEquals(initialBalanceFirstCard - amount, transferCard.getCardBalance(DataHelper.getfirstCard()));
        assertEquals(initialBalanceSecondCard + amount, transferCard.getCardBalance(DataHelper.getsecondCard()));
    }

    @Test
    void shouldTransferFromSecondCardToFirstAmountGreaterThanBalance() { //суммы больше баланса со второй на первую
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        int initialBalanceFirstCard = dashboardPage.getCardBalance(DataHelper.getfirstCard());
        int initialBalanceSecondCard = dashboardPage.getCardBalance(DataHelper.getsecondCard());

        var transferPage = dashboardPage.getCard(DataHelper.getfirstCard()); // выбираем пополнить первую карту

        int amount = initialBalanceSecondCard + 222;
        var transferCard = transferPage.transferMoney(amount, DataHelper.getsecondCard());

        assertEquals(initialBalanceFirstCard + amount, transferCard.getCardBalance(DataHelper.getfirstCard()));
        assertEquals(initialBalanceSecondCard - amount, transferCard.getCardBalance(DataHelper.getsecondCard()));
    }

    @Test
    void shouldRemoveNegativeBalanceOfSecondCardWithAllAmountFromFirstCard() {  // вывести вторую карту из отрицательного баланса
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        int initialBalanceFirstCard = dashboardPage.getCardBalance(DataHelper.getfirstCard());
        int initialBalanceSecondCard = dashboardPage.getCardBalance(DataHelper.getsecondCard());

        var transferPage = dashboardPage.getCard(DataHelper.getsecondCard()); // выбираем пополнить вторую карту

        int amount = initialBalanceFirstCard;
        var transferCard = transferPage.transferMoney(amount, DataHelper.getfirstCard());

        assertEquals(initialBalanceFirstCard - amount, transferCard.getCardBalance(DataHelper.getfirstCard()));
        assertEquals(initialBalanceSecondCard + amount, transferCard.getCardBalance(DataHelper.getsecondCard()));
    }
}
