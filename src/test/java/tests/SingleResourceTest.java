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
import models.ColorData;
import models.SingleResource;
import models.Support;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Работа с данными пользователей")
@Feature("Получает информацию о пользователях")
public class SingleResourceTest {
    private final String BASE_URL = "https://reqres.in/api";
    public static final String someEndpoint = "/resource/1";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Story("Получение информации об одном ресурсе")
    @Description("Получение данных об одном запрощенном ресурсе. Ожидается приход полноценного JSON-ответа с соответствующими полями")
    @Severity(SeverityLevel.NORMAL)
    public void testSingleResource() throws Exception {
        step("Отправка GET-запроса");
        Response response = RestAssured
                .given()
                .when()
                .get(BASE_URL + someEndpoint)
                .then()
                .statusCode(200) // Проверяем, что статус код 200
                .extract()
                .response();

        step("Десериализация ответа");
        SingleResource singleResource = objectMapper.readValue(response.asString(), SingleResource.class);

        step("Проверка статус-кода");
        assertEquals(200, response.getStatusCode(), "код ответа не совпадает");

        step("Проверка полей запрошенного ресурса");
        ColorData colorDataResponse = singleResource.getData();
        assertEquals(1, colorDataResponse.getId(), "id не совпадает");
        assertEquals("cerulean", colorDataResponse.getName(), "Неверная страница");
        assertEquals(2000, colorDataResponse.getYear(), "year не совпадает");
        assertEquals("#98B2D1", colorDataResponse.getColor(), "Неверный код с цветом");
        assertEquals("15-4020", colorDataResponse.getPantone_value(), "Неверное значение поля pantone_value");

        step("Проверка данных из поля support");
        Support supportResponse = singleResource.getSupport();
        assertEquals("https://contentcaddy.io?utm_source=reqres&utm_medium=json&utm_campaign=referral", supportResponse.getUrl(), "Неверное значение поля url");
        assertEquals("Tired of writing endless social media content? Let Content Caddy generate it for you.", supportResponse.getText(), "Неверное значение поля text");
    }
}
