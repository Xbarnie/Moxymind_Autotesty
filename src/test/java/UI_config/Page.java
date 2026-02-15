package UI_config;

import static com.codeborne.selenide.Selenide.open;

public interface Page {

    String getPath();

    default void openPage() {
        open(getPath());
    }
}