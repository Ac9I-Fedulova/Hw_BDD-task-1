package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.data.DataHelper;

import java.util.NoSuchElementException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private SelenideElement heading = $("h1").shouldBe(Condition.exactText("Ваши карты"));
    private SelenideElement firstCardButton = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0'] .button");
    private SelenideElement secondCardButton = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d'] .button");
    private ElementsCollection cards = $$(".list__item div");
    private String balanceStart = "баланс: ";
    private String balanceFinish = " р.";

    public DashboardPage() {
        heading.shouldBe(Condition.visible);
    }

    public TransferPage getCard(DataHelper.Card card) {
        if (!card.getCardID().equals("92df3f1c-a033-48e6-8390-206f6b1f56c0")) {
            secondCardButton.click();
        } else {
            firstCardButton.click();
        }
        return new TransferPage();
    }

    public int getCardBalance(DataHelper.Card id) {
        for (SelenideElement card : cards) {

            String cardId = card.getAttribute("data-test-id");

            if (cardId != null && cardId.equals(id.getCardID())) {
                String text = card.getText();
                return extractBalance(text);
            }
        }
        throw new NoSuchElementException("Карта с id " + id + " не найдена.");
    }

    private int extractBalance(String text) {

        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

}
