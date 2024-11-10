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
import models.SingleResource;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Работа с данными пользователей")
@Feature("Получает информацию о пользователях")
public class SingleResourceNotFoundTest {
    private final String BASE_URL = "https://reqres.in/api";
    public static final String someEndpoint = "/resource/199";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Story("Получение об несуществующем пользователе ")
    @Description("Получение данных о несуществующем пользователе. Ожидается приход пустого JSON-ответа")
    @Severity(SeverityLevel.CRITICAL)
    public void testSingleResourceNotFound() throws Exception {
        step("Отправка GET-запроса");
        Response response = RestAssured
                .given()
                .when()
                .get(BASE_URL + someEndpoint)
                .then()
                .statusCode(404) // Проверяем, что статус код 404
                .extract()
                .response();

        step("Десериализация ответа");
        SingleResource singleResource = objectMapper.readValue(response.asString(), SingleResource.class);

        step("Проверка статус-кода");
        assertEquals(404, response.getStatusCode(), "код ответа не совпадает");

        step("Проверка, что ответ является пустым");
        String responceBody = response.getBody().asString();
        assertEquals("{}", responceBody, "Тело отввета не является пустым");
    }
}
