package UI_config;

import UI_pages.CartPage;
import UI_pages.CheckoutPage;
import UI_pages.InventoryPage;
import UI_pages.LoginPage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PageFactory {

    private static final Map<Pages, Supplier<Page>> PAGE_SUPPLIERS = new HashMap<>();

    static {
        //Registracia stránok do mapy
        PAGE_SUPPLIERS.put(Pages.LOGIN_PAGE, LoginPage::new);
        PAGE_SUPPLIERS.put(Pages.INVENTORY_PAGE, InventoryPage::new);
        PAGE_SUPPLIERS.put(Pages.CART_PAGE, CartPage::new);
        PAGE_SUPPLIERS.put(Pages.CHECKOUT_PAGE, CheckoutPage::new);
    }

    //Metoda na získanie inštancie stránky podľa enumu
    @SuppressWarnings("unchecked")
    public static <T extends Page> T getPage(Pages page) {
        Objects.requireNonNull(page, "page must not be null");
        Supplier<Page> supplier = PAGE_SUPPLIERS.get(page);
        if (supplier == null) {
            throw new IllegalArgumentException("Page not found: " + page);
        }
        return (T) supplier.get();
    }
}