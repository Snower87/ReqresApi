package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured. RestAssured;
import io.restassured.http.ContentType;
import io.restassured. response.Response;
import models.LoginRequest;
import models.LoginResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginUserTest {
    private final String BASE_URL = "https://reqres.in/api/login";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @CsvSource({
            "'eve.holt@reqres.in', 'pistol', true",
            "'michael.lawson@reqres.in', 'pistol', true",
            "'sydney@fife', '', false",
            //тесты на невалидность пароля:
            "'rachel.howell@reqres.in', 'pistol', true",
            "'rachel.howell@reqres.in', 'hahahaah', true",  //баг - д.б. статус код 400 + ошибка по паролю и не логиниться
            "'rachel.howell@reqres.in', 'PISTOL', true",    //баг - д.б. статус код 400 + ошибка по паролю и не логиниться
            "'rachel.howell@reqres.in', 'PiStol', true",    //баг - д.б. статус код 400 + ошибка по паролю и не логиниться
            //тест на пустое поле:
            "'', 'pistol', false",
            "'rachel.howell@reqres.in', '', false",
            //тест по длине поля: максимально и минимально допустимой длины символов
            "'r@reqres.in', 'pistol', false",
            "'rachel.howellAvQu14eobfp5KYfCq4QKlOi1OlRKMSJEZFmEGkr2RtKemEvADhEHdnmKnsn8WrcaQC6yyBBaNYBaLcNJCuVVPZPyFwhSFnEzjMSQhTSy76J39FWzh73Fi3cmow9WVZnTFg93erQXIQbbBQCdaKXTNoDR5rpopHh65LTk5ntVPoZLtwHqgm84dfGII5TpPgpjdM2b34hohzC3z9vKXCR7PohJKxzJag8WYI62Zjl03IYI0V4mrGAle9QsF4pxVnZa8YoPVsjXpbLhUV70jDOq2qXosjEhu6ve7GzpfrybUhAKuA0LUsKNJL6AziBCrZvf9L7pKa2rDoDIfydk7U8scI6p6jkQsKIbd0Gv9rccHQb8eHrPwK0I65cTBhMezmLz3ze4S6aE6tXw8IzEbZf8FsO2GCyiDMNQ4bJkCW0QAzQJG6STL8a5IBGzAftXrKQ2uCYnGtNOtogJGfJn6APJNqGJCDSLCe9xjwb6r3AOLynVelrN53KGgEQHPTbt67TeyhJ7bI04uTC0Q7sY3wrWuIbEyGNaltscEPnCCrRqfUnLOzfVaxdovsRUlavbwwrLKemIG3mJHD5bjs9yGEXEhf19BE9kCrsEXynwhXrNckjcBuW7qqzsg78BmrL8Rke8MfnkHniJVFnLezjpIFXLu67RtVBsK8TxxCg6tMNxA2KS5ECRzBj9sTbRvZhnSQcItNabxb8m1fMgmim3c92HtaL8XueDw4RgLqBmXbPGdDwjQtQ6u7My7QFapx7wBs344unKLjE7QNuwVBv3Mf1OSb31fJijgweOm4LQzBVpARcRwN2LudaHC90ENOVMFv2VAE199HAg5F3xyQIssor3lPOzfDO4lbP47Lh2qofcHzcBtJK5o2xllWJ6XaR6NWkC7XBnz2AJKf4Jq3CyetYgh0O0FFIJcEZuixNz2Z0iwb3YtwQtXinoeZGxTn9d4vIC7UO1PJcKDv75S6kk4q9USzOdo2U5YSUAatF8GBSJ@reqres.in', 'pistol', false",
            "'rachel.howell@reqres.in', 'p', true",         //баг - д.б. статус код 400 + ошибка по паролю и не логиниться
            "'rachel.howell@reqres.in', 'skCyUm07rRb2quWmLdlYuVWvm45qD01CJ2mBvwIaSvnjCGOkmeK9jk1gvhzJJ9UEJLbEkN4GMEDW8mjIriC5XMofWth4wFuy6Q1bAWXPxPJkPxx0cAntvzeQ9veLjojZHmfzOTPU7P6cWFdHkTVwWc3z0XT7HZyMDsV7lvE1C6Ae0ulrHsLh7XHmnP6LjeliTHQzc4R6ESuzxOkucwmD7AmLZA0iMOL9Y1Ii5RAirBuofI6GvYlvjgWOfnNprNBbOsMoxEB1cm0Kkl43a1DeLt0w2OKEiSwLp2J7e55pfHurVH05re3GPg5OEzNz0T4gXb5qGTWv51Yn2L8qmiM5RZWjzIo8W9soEF9C5O9STwqfhvwdmVwjlYTqbpzMAaVeUn85axVrnkCl2y0fi8tOeaNV6SfMWMNZT56yaRVH8DTpADGiFB85auUtlh5XVLXZWPWPkETpKcdXFIr7jJpilXuAeSdnSKQ8JmCVNLSaVjnD9SXbPMBidSEFvB6BrN15E3hsnSN70o7X4s6hW8JVlKrZuxy8xwUJbcgaErpg2AlLQzefRpC64S64M9WfM6GneOtDvhDxv0neX24LN2V6aPmXiEl6gD3rxCPmdifJ39ONP40nkWx1WCaY9YJHDlnOsF9c6gZ8eDUQ9p1yBrF3NTOUOX7FSMRkjQBHRDk6RgG7dANQcfhaOVzD4FtWtjy8n8Jod4PAYr7c1dgB2QVE1MGu0qr2Ni6DlmnRWA27INGrKfGW1AVpdUelLqUkKnSgPv57UhfGffS3nCyWJXAm1bS2FQ72kKfWcx5S2b5O8Qd3P5Fx7zs6nlBoNaFei7n5QXPBoc1kBEGeEfyt71DKzDyxPSTwLfOYPeEau8VeEUD5FgDxu9bmk7chgO6WywvPlIvlkKZrL0n9tJnqGOy5VvlvTMxpn1hBhE9paQ47ceLeAZvZZ43NpBepp6ZkJQL54HBhvAiMD20x3Db8YmvTzdtLkmysl8gUFjq8pgf5', true",
            //тесты на пробел:
            "' rachel.howell@reqres.in', 'pistol', false",
            "'rachel.howell @reqres.in', 'pistol', false",
            "'rachel. howell@reqres.in', 'pistol', false",
            "'rachel.  howell@reqres.in', 'pistol', false",
            "'rachel.howell@ reqres.in', 'pistol', false",
            "'rachel.howell@reqres.in ', 'pistol', false",
            "'rachel.howell@ reqres.in ', 'pistol', false",
            //тесты на спецсимволы:
            "'~rachel.howell@reqres.in', 'pistol', false",
            "'r@achel.howell@reqres.in', 'pistol', false",
            "'ra#chel.howell@reqres.in', 'pistol', false",
            "'ra!chel.howell@reqres.in', 'pistol', false",
            "'ra{}chel.howell@reqres.in', 'pistol', false",
            "'ra':?chel.howell@reqres.in', 'pistol', false",
            "'ra+_)(*&^%%$#@|chel.howell@reqres.in', 'pistol', false",
            //тесты на заглавные и строчные буквы в login/username:
            "'Rachel.howell@reqres.in', 'pistol', false",
            "'rAchel.howell@reqres.in', 'pistol', false",
            "'rAchel.howell@reqres.In', 'pistol', false",
            "'rachel.howell@Reqres.In', 'pistol', false",
            "'RACHEL.HOWELL@reqres.in', 'pistol', false",
            "'rachel.howell@REQRES.IN', 'pistol', false",
            "'RACHEL.HOWELL@REQRES.IN', 'pistol', false",
    })
    public void testLoginUser(String email, String password, boolean isSuccess) throws JsonProcessingException {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        step("Отправляем POST-запрос на логирование");
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post(BASE_URL)
                .then()
                .extract()
                .response();

        if (isSuccess) {
            step("Проверяем успешное логирование, статус код 200");
            assertEquals(200, response.getStatusCode(), "Статус код не соответствует 200");
            LoginResponse loginResponse =
                    objectMapper.readValue(response.asString(), LoginResponse.class);
            step("Проверяем, что в ответе присутствует token");
            assertNotNull(loginResponse.getToken(), "Token не должен быть null");
        } else {
            step("Проверяем неуспешное логирование, статус код 400");
            assertEquals(400, response.getStatusCode(), "Статус код не соответствует 400");
            LoginResponse loginResponse =
                    objectMapper.readValue(response.asString(), LoginResponse.class);
            String errorMessage = loginResponse.getError();
            step("Проверяем сообщение об ошибке");
            assertNotNull(errorMessage, "Сообщение об ошибке не должно быть null");
            switch (errorMessage) {
                case "user not found":
                    assertEquals("user not found", errorMessage, "Сообщение об ошибке не совпадает");
                    break;
                case "Missing password":
                    assertEquals("Missing password", errorMessage, "Сообщение об ошибке не совпадает");
                    break;
            }

        }
    }
}
