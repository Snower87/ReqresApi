package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.SingleUserResponse;
import models.UserData;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Работа с данными пользователей")
@Feature("Получает информацию о пользователях")
public class UserNotFoundTest {
    private final String BASE_URL = "https://reqres.in/api/users";

    @Test
    @Story("Пользователь не найден")
    @Description("Запрашиваемый пользователь не существует или не найден в системе. Ожидается приход пустого JSON-ответа")
    @Severity(SeverityLevel.MINOR)
    public void testGetListUsers() throws Exception {
        step("Отправка GET-запроса");
        Response response = RestAssured
                .given()
                .when()
                .get(BASE_URL + "/999")
                .then()
                .statusCode(404) // Проверяем, что статус код 404 Not Found
                .extract()
                .response();

        step("Проверка статус-кода");
        assertEquals(404, response.getStatusCode(), "код ответа не совпадает");

        step("Проверка, что ответ является пустым");
        String responceBody = response.getBody().asString();
        assertEquals("{}", responceBody, "Тело ответа не является пустым");
    }
}
