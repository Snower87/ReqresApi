package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetListResourcesTest {
    private final String BASE_URL = "https://reqres.in/api";
    public static final String someEndpoint = "/resource?page=1&per_page=12";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Story("Получение информации списке ресурсов")
    @Severity(SeverityLevel.MINOR)
    public void testSingleResource() throws Exception {
        Response response = RestAssured
                .given()
                .when()
                .get(BASE_URL + someEndpoint)
                .then()
                .statusCode(200) // Проверяем, что статус код 200
                .extract()
                .response();

        //Десериализация ответа
        ResourcesResponse resourcesResponse = objectMapper.readValue(response.asString(), ResourcesResponse.class);

        //Проверка статус-кода
        assertEquals(200, response.getStatusCode(), "код ответа не совпадает");

        //Проверка всех полей
        assertEquals(1, resourcesResponse.getPage(), "page не совпадает");
        assertEquals(12, resourcesResponse.getPer_page(), "per_page не совпадает");
        assertEquals(12, resourcesResponse.getTotal(), "total не совпадает");
        assertEquals(1, resourcesResponse.getTotal_pages(), "total_pages не совпадает");

        //Проверка данных из списка ресурсов: индексы 0, 6, 11
        List<ColorData> colorDataResponse = resourcesResponse.getData();
        ColorData colorDataByIndex0 = colorDataResponse.getFirst();
        assertEquals(1, colorDataByIndex0.getId(), "id не совпадает");
        assertEquals("cerulean", colorDataByIndex0.getName(), "Неверная страница");
        assertEquals(2000, colorDataByIndex0.getYear(), "year не совпадает");
        assertEquals("#98B2D1", colorDataByIndex0.getColor(), "Неверный код с цветом");
        assertEquals("15-4020", colorDataByIndex0.getPantone_value(), "Неверное значение поля pantone_value");

        ColorData colorDataByIndex6 = colorDataResponse.get(6);
        assertEquals(7, colorDataByIndex6.getId(), "id не совпадает");
        assertEquals("sand dollar", colorDataByIndex6.getName(), "Неверная страница");
        assertEquals(2006, colorDataByIndex6.getYear(), "year не совпадает");
        assertEquals("#DECDBE", colorDataByIndex6.getColor(), "Неверный код с цветом");
        assertEquals("13-1106", colorDataByIndex6.getPantone_value(), "Неверное значение поля pantone_value");

        ColorData colorDataByIndex11 = colorDataResponse.get(11);
        assertEquals(12, colorDataByIndex11.getId(), "id не совпадает");
        assertEquals("honeysuckle", colorDataByIndex11.getName(), "Неверная страница");
        assertEquals(2011, colorDataByIndex11.getYear(), "year не совпадает");
        assertEquals("#D94F70", colorDataByIndex11.getColor(), "Неверный код с цветом");
        assertEquals("18-2120", colorDataByIndex11.getPantone_value(), "Неверное значение поля pantone_value");

        Support supportResponse = resourcesResponse.getSupport();
        assertEquals("https://contentcaddy.io?utm_source=reqres&utm_medium=json&utm_campaign=referral", supportResponse.getUrl(), "Неверное значение поля url");
        assertEquals("Tired of writing endless social media content? Let Content Caddy generate it for you.", supportResponse.getText(), "Неверное значение поля text");
    }
}
