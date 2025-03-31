package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement subtitle = $("h1").shouldBe(Condition.exactText("Ваши карты"));
    private ElementsCollection cards = $$(".list__item div");
    private String balanceStart = "баланс: ";
    private String balanceFinish = " р.";
    private SelenideElement reloadButton = $("[data-test-id='action-reload']");

    public DashboardPage() {
        subtitle.shouldBe(visible);
    }

    public TransferPage selectCard(DataHelper.Card card) {
        getCard(card).$(".button").click();
        return new TransferPage();
    }

    private SelenideElement getCard(DataHelper.Card card) {
        return cards.findBy(Condition.attribute("data-test-id", card.getCardID()));
    }

    public int getCardBalance(DataHelper.Card card) {
        val text = getCard(card).getText();
        return extractBalance(text);
    }

    private int extractBalance(String text) {

        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public void reloadDashboardPage() {
        reloadButton.click();
        subtitle.shouldBe(visible);
    }

    public void checkCardBalance(DataHelper.Card card, int expectedBalance) {
        getCard(card).shouldBe(visible).should(text(balanceStart + expectedBalance + balanceFinish));
    }
}
