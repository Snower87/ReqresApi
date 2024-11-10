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
import models.UserData;
import models.UsersResponse;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

@Epic("Работа с данными пользователей")
@Feature("Получает информацию о пользователях")
public class GetListUsersTest {
    private final String BASE_URL = "https://reqres.in/api/users";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Story("Получение списка пользователей")
    @Description("Получение данных о полноценном/существующем списке пользователей на текущий момент в системе")
    @Severity(SeverityLevel.BLOCKER)
    public void testGetListUsers() throws Exception {
        step("Отправка GET-запроса");
        Response response = RestAssured
                .given()
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(200) // Проверяем, что статус код 200
                .extract()
                .response();

        step("Десериализация ответа");
        UsersResponse usersResponse = objectMapper.readValue(response.asString(), UsersResponse.class);

        step("Проверка статус-кода");
        assertEquals(200, response.getStatusCode(), "код ответа не совпадает");

        step("Проверка полей");
        assertEquals(6, usersResponse.getData().size(), "Количество пользователей не совпадает");
        assertEquals(1, usersResponse.getPage(), "Неверная страница");
        assertEquals(2, usersResponse.getTotal_pages(), "Неверное общее количество страниц");
        assertEquals(12, usersResponse.getTotal(), "Неверное количество страниц");

        step("Проверка email пользователей");
        for (UserData user : usersResponse.getData()) {
            assertTrue(user.getEmail().endsWith("@reqres.in"), "Email пользователя "
                    + user.getId() + " не оканчивается на @reqres.in");
        }
    }
}
