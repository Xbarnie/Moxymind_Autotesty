package UI_pages;

import UI_config.Page;
import UI_config.PageFactory;

import UI_config.Pages;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage implements Page {

    private final SelenideElement username = $("#user-name");
    private final SelenideElement password = $("#password");
    private final SelenideElement loginButton = $("#login-button");
    private final SelenideElement errorMessage = $(".error-message-container");

    public void verifyLoginPageLoaded() {
        username.shouldBe(visible);
        password.shouldBe(visible);
        loginButton.shouldBe(visible);
    }

    public InventoryPage loginAs(String user, String pass) {
        username.setValue(user);
        password.setValue(pass);
        loginButton.click();
        return PageFactory.getPage(Pages.INVENTORY_PAGE);
    }

    public LoginPage loginWithInvalidCredentials(String user, String pass) {
        username.setValue(user);
        password.setValue(pass);
        loginButton.click();
        return PageFactory.getPage(Pages.LOGIN_PAGE);
    }

    public String getErrorMessage() {
        return errorMessage.shouldBe(visible).getText();
    }

    public boolean isPageLoaded() {
        return loginButton.is(visible) && username.is(visible) && password.is(visible);
    }

    @Override
    public String getPath() {
        return "/";
    }
}
