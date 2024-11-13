package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.UserCredetials;
import models.UserModelResponse;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Epic("Работа с данными пользователей")
@Feature("Получает информацию о пользователях")
public class UpdateUserTest {
    private final String BASE_URL = "https://reqres.in/api/users";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Story("Обновление данных пользователя")
    @Description("Обновление данных конкретного пользователя через PUT-запрос")
    @Severity(SeverityLevel.BLOCKER)
    public void testUpdateUserPut() throws Exception {
        UserCredetials user = new UserCredetials("morpheus");

        step("Отправка PUT-запроса");
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .put(BASE_URL + "/2")
                .then()
                .statusCode(200) // Проверяем, что статус код 200
                .extract()
                .response();

        step("Десериализация JSON-ответа в объект UserModelResponse");
        UserModelResponse userModelResponse = objectMapper.readValue(response.asString(), UserModelResponse.class);

        step("Проверка статус-кода");
        assertEquals(200, response.getStatusCode(), "код ответа не совпадает");

        step("Проверяем, что в ответе присутствует id и createdAt");
        assertNotNull(userModelResponse.getUpdatedAt(), "getUpdatedAt не должен быть null");

        step("Проверяем, что имя и работа совпадает с теми, что были отправлены");
        assertEquals(user.getName(), userModelResponse.getName(), "Имя пользователя не совпадает");
        assertEquals(user.getJob(), userModelResponse.getJob(), "Профессия пользователя не совпадает");
    }

    @Test
    @Story("Обновление данных пользователя")
    @Description("Обновление данных конкретного пользователя через PATCH-запрос")
    @Severity(SeverityLevel.BLOCKER)
    public void testUpdateUserPatch() throws Exception {
        UserCredetials user = new UserCredetials("zerpheus");

        step("Отправка PATCH-запроса");
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .patch(BASE_URL + "/2")
                .then()
                .statusCode(200) // Проверяем, что статус код 200
                .extract()
                .response();

        step("Десериализация JSON-ответа в объект UserModelResponse");
        UserModelResponse userModelResponse = objectMapper.readValue(response.asString(), UserModelResponse.class);

        step("Проверка статус-кода");
        assertEquals(200, response.getStatusCode(), "код ответа не совпадает");

        step("Проверяем, что в ответе присутствует id и createdAt");
        assertNotNull(userModelResponse.getUpdatedAt(), "getUpdatedAt не должен быть null");

        step("Проверяем, что имя и работа совпадает с теми, что были отправлены");
        assertEquals(user.getName(), userModelResponse.getName(), "Имя пользователя не совпадает");
        assertEquals(user.getJob(), userModelResponse.getJob(), "Профессия пользователя не совпадает");
    }
}
