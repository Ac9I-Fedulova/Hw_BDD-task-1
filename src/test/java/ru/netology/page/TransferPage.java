package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement subtitle = $("h1").shouldBe(Condition.exactText("Пополнение карты"));
    private SelenideElement amountField = $("[data-test-id='amount'] .input__control");
    private SelenideElement fromCardField = $("[data-test-id='from'] .input__control");
    private SelenideElement transferButton = $("[data-test-id='action-transfer'].button");
    private SelenideElement errorMessage = $("[data-test-id='error-notification'] .notification__content");

    public TransferPage() {
        subtitle.shouldBe(Condition.visible);
    }

    public DashboardPage validTransferMoney(String amount, DataHelper.Card card) {
        transferMoney(amount, card);
        return new DashboardPage();
    }

    public void transferMoney(String amount, DataHelper.Card card) {
        amountField.setValue(amount);
        fromCardField.setValue(card.getCardNumber());
        transferButton.click();
    }

    public void findErrorMessage(String expectedText) {
        errorMessage.shouldBe(Condition.and("Проверка сообщения об ошибке", Condition.text(expectedText), Condition.visible));
    }
}
