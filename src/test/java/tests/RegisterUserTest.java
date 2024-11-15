package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured. RestAssured;
import io.restassured.http.ContentType;
import io.restassured. response.Response;
import models.RegisterRequest;
import models.RegisterResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegisterUserTest {
    private final String BASE_URL = "https://reqres.in/api/register";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @CsvSource({
            "'eve.holt@reqres.in', 'pistol', true",
            "'michael.lawson@reqres.in', 'pistol', true",
            "'sydney@fife', '', false"
    })
    public void testRegisterUser(String email, String password, boolean isSuccess) throws JsonProcessingException {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);

        step("Отправляем POST-запрос на регистрацию");
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(registerRequest)
                .when()
                .post(BASE_URL)
                .then()
                .extract()
                .response();

        if (isSuccess) {
            step("Проверяем успешную регистрацию, статус код 200");
            assertEquals(200, response.getStatusCode(), "Статус код не соответствует 200");
            RegisterResponse registerResponse =
                    objectMapper.readValue(response.asString(), RegisterResponse.class);
            step("Проверяем, что в ответе присутствует token");
            assertNotNull(registerResponse.getToken(), "Token не должен быть null");
        } else {
            step("Проверяем неуспешную регистрацию, статус код 400");
            assertEquals(400, response.getStatusCode(), "Статус код не соответствует 400");
            RegisterResponse registerResponse =
                    objectMapper.readValue(response.asString(), RegisterResponse.class);
            String errorMessage = registerResponse.getError();
            step("Проверяем сообщение об ошибке");
            assertNotNull(errorMessage, "Сообщение об ошибке не должно быть null");
            assertEquals("Missing password", errorMessage, "Сообщение об ошибке не совпадает");
        }
    }
}
