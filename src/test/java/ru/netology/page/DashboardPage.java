package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.val;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private SelenideElement subtitle = $("h1").shouldBe(Condition.exactText("Ваши карты"));
    // Метод для получения элемента первой карты
    @Getter
    private SelenideElement firstCardId = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0']");
    // Метод для получения элемента второй карты
    @Getter
    private SelenideElement secondCardId = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d']");
    private SelenideElement firstCardButton = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0'] .button"); //кнопка пополнения первой карты
    private SelenideElement secondCardButton = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d'] .button"); //кнопка пополнения второй карты
    private String balanceStart = "баланс: ";
    private String balanceFinish = " р.";

    public DashboardPage() {
        subtitle.shouldBe(Condition.visible);
    }

    public TransferPage selectCard(SelenideElement cardId) {
        if (!cardId.equals(firstCardId)) {
            secondCardButton.click();
        } else {
            firstCardButton.click();
        }
        return new TransferPage();
    }

    public int getCardBalance(SelenideElement id) {
        val text = id.text();
        return extractBalance(text);
    }

    private int extractBalance(String text) {

        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}
