package API_config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

//Nastavuje URL pre API testy
public abstract class TestBaseAPI {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = Config.BASE_URL + "/api";
    }
}

