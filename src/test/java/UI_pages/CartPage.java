package UI_pages;

import UI_config.Page;
import UI_config.PageFactory;
import UI_config.Pages;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CartPage implements Page {

    private final SelenideElement checkoutBtn = $("#checkout");
    private final SelenideElement cartContainer = $(".cart_list");
    private final ElementsCollection cartItems = $$("div.inventory_item_name");

    public CheckoutPage clickCheckout() {
        checkoutBtn.shouldBe(visible).click();
        return PageFactory.getPage(Pages.CHECKOUT_PAGE);
    }

    public boolean isPageLoaded() {
        return cartContainer.shouldBe(visible).exists();
    }

    public boolean isProductInCart(String productName) {
        return cartItems.stream()
                .anyMatch(item -> item.getText().equalsIgnoreCase(productName));
    }

    @Override
    public String getPath() {
        return "/cart.html";
    }
}