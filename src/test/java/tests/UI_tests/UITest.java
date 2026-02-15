package tests.UI_tests;

import UI_config.PageFactory;
import UI_config.Pages;
import UI_pages.CartPage;
import UI_pages.CheckoutPage;
import UI_pages.InventoryPage;
import UI_pages.LoginPage;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("UITest")
public class UITest {

    private LoginPage loginPage;
    String username = System.getenv("SAUCE_USERNAME");
    String password = System.getenv("SAUCE_PASSWORD");

    @BeforeEach
    public void openLoginPage() {
        Configuration.baseUrl = "https://www.saucedemo.com";
        Configuration.browser = "chrome";
        Configuration.headless = false;
        loginPage = PageFactory.getPage(Pages.LOGIN_PAGE);
        loginPage.openPage();
        loginPage.verifyLoginPageLoaded();
    }

    // TC1: Overenie prihlasenia a následného nákupu produktu, odhlásenia používateľa
    // Tento test pokrýva hlavný business scenár e-shopu (E2E).
    // Ak tento proces nefunguje, používateľ nemôže dokončiť nákup.
    // Test overuje autentifikáciu, prácu s košíkom, checkout proces aj logout.
    @Order(1)
    @Test
    public void successfulLoginCheckoutLogout() {
        InventoryPage inventoryPage = loginPage.loginAs(username, password);
        assertTrue(inventoryPage.isPageLoaded(), "Nezobrazil sa zoznam veci v eshope po prihlásení");
        inventoryPage.addBackpackToCart();
        assertEquals("1", inventoryPage.getCartCount(), "Košik neobsahuje správný počet položiek po pridání produktu");
        CartPage cartPage = inventoryPage.openCart();
        assertTrue(cartPage.isProductInCart("Sauce Labs Backpack"), "Produkt 'Sauce Labs Backpack' sa nenachádza v košíku");
        assertTrue(cartPage.isPageLoaded(), "Obsah košíka nie je zobrazený po kliknutí na ikonu košíku");
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        assertTrue(checkoutPage.isPageLoaded(), "Checkout stránka nie je načítaná po kliknutí na Checkout");
        checkoutPage.fillInformation("Janko", "Hrasko", "03104").finishOrder();
        assertTrue(checkoutPage.isOrderComplete(), "Objednávka nie je dokončená po vyplnení informácií a kliknutí na Finish");
        checkoutPage.backHome();
        LoginPage loginPageAfterLogout = inventoryPage.logout();
        assertTrue(loginPageAfterLogout.isPageLoaded(), "Používateľ sa nenachádza na LoginPage");
    }

    // TC2: Negatívny test - overenie chybovej správy pri neplatných prihlasovacích údajoch
    // Overuje, že systém nepovolí prístup s nesprávnymi údajmi a zobrazí správnu validačnú hlášku.
    @Order(2)
    @Test
    public void invalidLogin() {
        // Pokus o prihlásenie s nesprávnymi údajmi
        loginPage.loginWithInvalidCredentials("standard_user", "wrong_password");

        // Získanie chybovej správy zo stránky
        String actualErrorMessage = loginPage.getErrorMessage();

        // Očakávaná správa
        String expectedErrorMessage = "Epic sadface: Username and password do not match any user in this service";

        // Overenie, že chybová správa sa zhoduje s očakávanou
        assertEquals(expectedErrorMessage, actualErrorMessage, "Chybová správa sa nezhoduje s očakávanou správou!");
    }

    // TC3: Overenie funkčnosti zoradenia produktov
    // Zoradenie produktov priamo ovplyvňuje prehladnosť používateľa na stránke
    // Test overuje konzistenciu dát a správne správanie UI pri filtrovaní podľa názvu a ceny.
    @Order(3)
    @Test
    public void sortProducts() {
        InventoryPage inventoryPage = loginPage.loginAs(username, password);

        // Zoradenie produktov od A po Z
        inventoryPage.sortByText("Name (A to Z)");
        assertEquals("Sauce Labs Backpack", inventoryPage.getFirstProductTitle(), "Prvý produkt by mal byť 'Sauce Labs Backpack' po zoradení produktov podľa názvu A→Z");

        //List<Double> originalPrices = inventoryPage.getAllProductPrices();

        //Zoradenie produktov podla ceny od najmensej po najvyssiu
        inventoryPage.sortByText("Price (low to high)");

        // Získanie cien po zoradení
        List<Double> pricesAfterSort = inventoryPage.getAllProductPrices();

        // Skopíruje list a zoradi
        List<Double> expected = new ArrayList<>(pricesAfterSort);
        Collections.sort(expected);

        // Overenie, že produkty sú na stránke správne zoradené
        assertEquals(expected, pricesAfterSort, () -> {
            // Vytvorenie detailnej správy o prvom rozdiele
            for (int i = 0; i < pricesAfterSort.size(); i++) {
                if (!pricesAfterSort.get(i).equals(expected.get(i))) {
                    return "Produkty nie sú správne zoradené! " +
                            "Na pozícii " + (i + 1) + " je cena " + pricesAfterSort.get(i) +
                            ", očakávaná cena by mala byť " + expected.get(i) +
                            ". Najnižšia cena je " + expected.get(0);
                }
            }
            return "Produkty nie sú správne zoradené!";
        });
    }

    // TC4: Overenie odstránenia produktu z košíka.
    // Používateľ musí mať plnú kontrolu nad obsahom košíka.
    // Test zabezpečuje, že stav košíka sa po odstránení produktu aktualizuje a nedochádza k nekonzistentnosti dát.
    @Order(4)
    @Test
    public void removeProductFromCart() {
        InventoryPage inventoryPage = loginPage.loginAs(username, password);
        assertTrue(inventoryPage.isPageLoaded(), "Stránka košíka sa nenačítala");

        inventoryPage.addBackpackToCart();
        assertEquals("1", inventoryPage.getCartCount(), "Produkt sa nepridal do košíka");

        inventoryPage.removeBackpackFromCart();
        assertTrue(inventoryPage.isCartEmpty(), "Košík by mal byť po odstránení produktu prázdny");
    }
}
