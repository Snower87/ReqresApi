package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SingleResourceNotFoundTest {
    private final String BASE_URL = "https://reqres.in/api";
    public static final String someEndpoint = "/resource/199";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Story("Одиночный ресурс не найден")
    @Severity(SeverityLevel.MINOR)
    public void testSingleResourceNotFound() throws Exception {
        Response response = RestAssured
                .given()
                .when()
                .get(BASE_URL + someEndpoint)
                .then()
                .statusCode(404) // Проверяем, что статус код 404
                .extract()
                .response();

        //Десериализация ответа
        SingleResource singleResource = objectMapper.readValue(response.asString(), SingleResource.class);

        //Проверка статус-кода
        assertEquals(404, response.getStatusCode(), "код ответа не совпадает");

        //Проверка, что ответ является пустым
        String responceBody = response.getBody().asString();
        assertEquals("{}", responceBody, "Тело отввета не является пустым");
    }
}
