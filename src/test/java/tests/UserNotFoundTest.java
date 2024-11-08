package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.SingleUserResponse;
import models.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserNotFoundTest {
    private final String BASE_URL = "https://reqres.in/api/users";

    @Test
    @Story("Получение информации списке пользователей")
    @Severity(SeverityLevel.MINOR)
    public void testGetListUsers() throws Exception {
        Response response = RestAssured
                .given()
                .when()
                .get(BASE_URL + "/999")
                .then()
                .statusCode(404) // Проверяем, что статус код 404 Not Found
                .extract()
                .response();

        // Проверка email пользователей
        String responceBody = response.getBody().asString();
        assertEquals("{}", responceBody, "Тело отввета не является пустым");
    }
}
