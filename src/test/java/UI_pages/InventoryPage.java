package UI_pages;

import UI_config.Page;
import UI_config.PageFactory;
import UI_config.Pages;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class InventoryPage implements Page {

    private final SelenideElement inventoryList = $(".inventory_list");
    private final SelenideElement cartBadge = $(".shopping_cart_badge");
    private final SelenideElement burgerMenuBtn = $("#react-burger-menu-btn");
    private final SelenideElement logoutBtn = $("#logout_sidebar_link");
    private final SelenideElement cartBtn = $(".shopping_cart_link");
    private final SelenideElement addBackpackBtn = $("button[data-test='add-to-cart-sauce-labs-backpack']");
    private final SelenideElement sortDropdown = $(".product_sort_container");
    private final SelenideElement firstProduct = $$(".inventory_item_name").first();
    private final ElementsCollection productPrices = $$(".inventory_item_price");
    private final SelenideElement removeBackpackButton = $("#remove-sauce-labs-backpack");

    public InventoryPage addBackpackToCart() {
        addBackpackBtn.shouldBe(visible).click();
        return PageFactory.getPage(Pages.INVENTORY_PAGE);
    }

    public String getCartCount() {
        return cartBadge.getText();
    }

    public CartPage openCart() {
        cartBtn.click();
        return PageFactory.getPage(Pages.CART_PAGE);
    }

    public LoginPage logout() {
        burgerMenuBtn.shouldBe(visible).click();
        logoutBtn.shouldBe(visible).click();
        return PageFactory.getPage(Pages.LOGIN_PAGE);
    }

    public InventoryPage sortByText(String text) {
        sortDropdown.shouldBe(visible).selectOption(text);
        return PageFactory.getPage(Pages.INVENTORY_PAGE);
    }

    public String getFirstProductTitle() {
        return firstProduct.shouldBe(visible).getText();
    }

    public List<Double> getAllProductPrices() {
        return productPrices.stream()
                .map(p -> p.getText().replace("$", "")) // odstráni $
                .map(Double::parseDouble)                                               //prevedie na double typ
                .collect(Collectors.toList());
    }

    public boolean isPageLoaded() {
        return inventoryList.shouldBe(visible).exists();
    }

    public void removeBackpackFromCart() {
        removeBackpackButton.shouldBe(visible).click();
    }

    public boolean isCartEmpty() {
        return !cartBadge.exists();
    }

    @Override
    public String getPath() {
        return "/inventory.html";
    }
}