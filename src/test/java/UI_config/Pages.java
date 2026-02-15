package UI_config;

/**
 * Enum reprezentujúci všetky dostupné stránky v aplikácii.
 * Každá položka musí mať zodpovedajúcu triedu v balíku UI_pages.
 * Konvencia: LOGIN_PAGE → LoginPage.java
 */

//PageFactory automaticky validuje pri štarte
public enum Pages {
    LOGIN_PAGE,
    CART_PAGE,
    INVENTORY_PAGE,
    CHECKOUT_PAGE,
}
