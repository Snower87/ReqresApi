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
public class CreateUserTest {
    private final String BASE_URL = "https://reqres.in/api/users";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Story("Создание пользователя")
    @Description("Создание пользователя через POST (без указания поля job)")
    @Severity(SeverityLevel.BLOCKER)
    public void testCreateUserWithoutJob() throws Exception {
        UserCredetials user = new UserCredetials("morpheus");

        step("Отправка POST-запроса");
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(201) // Проверяем, что статус код 201
                .extract()
                .response();

        step("Десериализация JSON-ответа в объект UserModelResponse");
        UserModelResponse userModelResponse = objectMapper.readValue(response.asString(), UserModelResponse.class);

        step("Проверка статус-кода");
        assertEquals(201, response.getStatusCode(), "код ответа не совпадает");

        step("Проверяем, что в ответе присутствует id и createdAt");
        assertNotNull(userModelResponse.getId(), "ID не должен быть null");
        assertNotNull(userModelResponse.getCreatedAt(), "createdAt не должен быть null");

        step("Проверяем, что имя и работа совпадает с теми, что были отправлены");
        assertEquals(user.getName(), userModelResponse.getName(), "Имя пользователя не совпадает");
        assertEquals(user.getJob(), userModelResponse.getJob(), "Профессия пользователя не совпадает");
    }

    @Test
    @Story("Создание пользователя")
    @Description("Создание пользователя через POST (без указания поля name)")
    @Severity(SeverityLevel.BLOCKER)
    public void testCreateUserWithoutName() throws Exception {
        UserCredetials user = new UserCredetials();
        user.setJob("leader");

        step("Отправка POST-запроса");
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(201) // Проверяем, что статус код 201
                .extract()
                .response();

        step("Десериализация JSON-ответа в объект UserModelResponse");
        UserModelResponse userModelResponse = objectMapper.readValue(response.asString(), UserModelResponse.class);

        step("Проверка статус-кода");
        assertEquals(201, response.getStatusCode(), "код ответа не совпадает");

        step("Проверяем, что в ответе присутствует id и createdAt");
        assertNotNull(userModelResponse.getId(), "ID не должен быть null");
        assertNotNull(userModelResponse.getCreatedAt(), "createdAt не должен быть null");

        step("Проверяем, что имя и работа совпадает с теми, что были отправлены");
        assertEquals(user.getName(), userModelResponse.getName(), "Имя пользователя не совпадает");
        assertEquals(user.getJob(), userModelResponse.getJob(), "Профессия пользователя не совпадает");
    }

    @Test
    @Story("Создание пользователя")
    @Description("Создание пользователя через POST (с указанием всех полей: name + job)")
    @Severity(SeverityLevel.BLOCKER)
    public void testCreateUserWithNameAndJob() throws Exception {
        UserCredetials user = new UserCredetials("Volodia", "tester");

        step("Отправка POST-запроса");
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(201) // Проверяем, что статус код 201
                .extract()
                .response();

        step("Десериализация JSON-ответа в объект UserModelResponse");
        UserModelResponse userModelResponse = objectMapper.readValue(response.asString(), UserModelResponse.class);

        step("Проверка статус-кода");
        assertEquals(201, response.getStatusCode(), "код ответа не совпадает");

        step("Проверяем, что в ответе присутствует id и createdAt");
        assertNotNull(userModelResponse.getId(), "ID не должен быть null");
        assertNotNull(userModelResponse.getCreatedAt(), "createdAt не должен быть null");

        step("Проверяем, что имя и работа совпадает с теми, что были отправлены");
        assertEquals(user.getName(), userModelResponse.getName(), "Имя пользователя не совпадает");
        assertEquals(user.getJob(), userModelResponse.getJob(), "Профессия пользователя не совпадает");
    }
}
