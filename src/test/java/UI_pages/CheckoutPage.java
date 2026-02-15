package UI_pages;

import UI_config.Page;
import UI_config.PageFactory;
import UI_config.Pages;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class CheckoutPage implements Page {

    private final SelenideElement firstNameInput = $("#first-name");
    private final SelenideElement lastNameInput = $("#last-name");
    private final SelenideElement postalCodeInput = $("#postal-code");
    private final SelenideElement continueButton = $("#continue");
    private final SelenideElement finishButton = $("#finish");
    private final SelenideElement completeHeader = $(".complete-header");
    private final SelenideElement backHomeButton = $("#back-to-products");
    private final SelenideElement checkoutInfoContainer = $("#checkout_info_container");

    public CheckoutPage fillInformation(String first, String last, String zip) {
        firstNameInput.shouldBe(visible).setValue(first);
        lastNameInput.shouldBe(visible).setValue(last);
        postalCodeInput.shouldBe(visible).setValue(zip);
        continueButton.shouldBe(visible).click();
        return PageFactory.getPage(Pages.CHECKOUT_PAGE);
    }

    public void finishOrder() {
        finishButton.shouldBe(visible).click();
    }

    public boolean isPageLoaded() {
        return checkoutInfoContainer.shouldBe(visible).exists();
    }

    public boolean isOrderComplete() {
        return completeHeader.shouldBe(visible).exists();
    }

    public InventoryPage backHome() {
        backHomeButton.shouldBe(visible).click();
        return PageFactory.getPage(Pages.INVENTORY_PAGE);
    }

    @Override
    public String getPath() {
        return "/checkout.html";
    }
}