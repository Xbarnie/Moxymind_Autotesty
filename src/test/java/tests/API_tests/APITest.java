package tests.API_tests;

import API_config.TestBaseAPI;
import API_config.Config;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("APITest")
public class APITest extends TestBaseAPI {

    private static final long RESPONSE_LIMIT_MS = 500;

    @Order(1)
    @Test
    public void getListUsers200() {
        Response response = given()
                .header("x-api-key", Config.API_KEY)
                .header("Content-Type", "application/json")
                .queryParam("page", 2)
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .extract()
                .response();

        //Rozparsovanie JSON odpovede
        JsonPath json = response.jsonPath();
        List<Map<String, Object>> data = json.getList("data");
        int total = json.getInt("total");

        //Overenie základných podmienok
        assertFalse(data.isEmpty(), "'data' musí obsahovať aspoň 1 používateľa");
        assertTrue(total > 0, "'total' musí byť väčšie než 0");
        assertTrue(total >= data.size(), "'total' musí byť väčšie alebo rovné počtu používateľov v 'data'");

        //Overenie, že každý používateľ má vyplnenu hodnotu 'last_name'
        data.forEach(user -> {
            String lastName = (String) user.get("last_name");
            assertNotNull(lastName, "'last_name' musí existovať");
            assertFalse(lastName.isEmpty(), "'last_name' nesmie byť prázdne");
        });

        //Vypis
        System.out.println("GET request /api/users?page=2 vrátil " + data.size() + " používateľov z celkového počtu " + total + " používateľov");

        //Bonusový task: overenie dátových typov
        //Vytvorenie mapy očakávaných polí a typov
        Map<String, Class<?>> expectedTypes = Map.of(
                "id", Integer.class,
                "email", String.class,
                "first_name", String.class,
                "last_name", String.class,
                "avatar", String.class
        );

        //Iteruje cez každého používateľa a kontroluje, či pole(key) existuje a či je správneho typu
        data.forEach(user -> {
            expectedTypes.forEach((key, type) -> {
                Object value = user.get(key);
                assertNotNull(value, "'" + key + "' musí existovať");
                assertTrue(type.isInstance(value), "'" + key + "' musí byť typu " + type.getSimpleName());
            });
        });
    }

    @Order(2)
    @Test
    public void createUser201() throws IOException {
        String bodyJson = Files.readString(Paths.get("src/test/resources/json/UserPOST.json"));
        String schemaJson = Files.readString(Paths.get("src/test/resources/json/Schema.json"));
        Response response = given()
                .header("x-api-key", Config.API_KEY)
                .header("Content-Type", "application/json")
                .body(bodyJson)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("createdAt", notNullValue())
                .body(matchesJsonSchema(schemaJson))
                .time(lessThan(RESPONSE_LIMIT_MS))
                .extract()
                .response();
        System.out.println("POST request /api/users prebehol úspešne, response time = " + response.getTime() + " ms");

        //Bonusový task: assert na validáciu JSON schémy
        boolean schemaValid = JsonSchemaValidator.matchesJsonSchema(schemaJson).matches(response.getBody().asString());
        assertTrue(schemaValid, "Response neodpovedá JSON schéme");
    }
}



