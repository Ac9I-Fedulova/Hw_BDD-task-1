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

    public TransferPage() {
        subtitle.shouldBe(Condition.visible);
    }

    public DashboardPage transferMoney(int amount, DataHelper.Card fromCard) {
        amountField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        amountField.setValue(String.valueOf(amount));
        fromCardField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        fromCardField.setValue(fromCard.getCardNumber());
        transferButton.click();
        return new DashboardPage();
    }
}
