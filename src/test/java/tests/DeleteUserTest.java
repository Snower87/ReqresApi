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
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Работа с данными пользователей")
@Feature("Получает информацию о пользователях")
public class DeleteUserTest {
    private final String BASE_URL = "https://reqres.in/api/users";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Story("Удаление пользователя")
    @Description("Удаление существующего пользователя")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteUser() throws Exception {
        step("Отправка DELETE-запроса");
        Response response = RestAssured
                .given()
                .when()
                .delete(BASE_URL + "/2")
                .then()
                .statusCode(204) // Проверяем, что статус код 204
                .extract()
                .response();

        step("Проверка статус-кода");
        assertEquals(204, response.getStatusCode(), "код ответа не совпадает");

        step("Проверка, что ответ пришел пустым");
        String responceBody = response.getBody().asString();
        assertEquals("", responceBody, "Тело ответа не является пустым");
    }
}
