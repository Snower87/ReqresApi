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
import models.UsersResponse;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Работа с данными пользователей")
@Feature("Получает информацию о пользователях")
public class GetSingleUserTest {
    private final String BASE_URL = "https://reqres.in/api/users";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Story("Получение информации об одном/конкретном пользователе ")
    @Description("Получение данных о конкретном, существующем пользователе. Ожидается приход полноценного JSON-ответа, со всеми полями")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetListUsers() throws Exception {
        step("Отправка GET-запроса");
        Response response = RestAssured
                .given()
                .when()
                .get(BASE_URL + "/2")
                .then()
                .statusCode(200) // Проверяем, что статус код 200
                .extract()
                .response();

        step("Десериализация ответа");
        SingleUserResponse singleUserResponse = objectMapper.readValue(response.asString(), SingleUserResponse.class);

        step("Проверка статус-кода");
        assertEquals(200, response.getStatusCode(), "код ответа не совпадает");

        step("Проверка, что email пользователей оканчивается на @reqres.in");
        UserData user = singleUserResponse.getData();
        assertTrue(user.getEmail().endsWith("@reqres.in"), "Email пользователя "
                + user.getId() + " не оканчивается на @reqres.in");
    }
}
