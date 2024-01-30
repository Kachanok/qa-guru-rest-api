package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import models.lombok.ErrorResponseLombokModel;
import models.lombok.LoginBodyLombokModel;
import models.lombok.LoginResponseLombokModel;
import models.pojo.LoginBodyModel;
import models.pojo.LoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.ApiExtendedTestsSpec.*;


public class ApiExtendedTests extends TestBase {


    @Test
    @DisplayName("API. Successful login test/Pojo")
    void successfulLoginPojoTest() {

        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

        LoginResponseModel response = given()
                .filter(new AllureRestAssured())
                .body(authData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("api/login")

                .then()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponseModel.class);

        assertNotNull(response.getToken(), "Token should not be null");


    }

    @Test
    @DisplayName("API. Successful login test/Lombok")
    void successfulLoginLombokTest() {
        LoginBodyLombokModel authData = new LoginBodyLombokModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

        LoginResponseLombokModel response = step("Make request", () ->
                given(loginRequestSpec)
                        .body(authData)

                        .when()
                        .post()

                        .then()
                        .spec(loginResponseSpec)
                        .extract().as(LoginResponseLombokModel.class));
        step("Check response", () ->

            assertNotNull("Token should not be null", response.getToken()));

    }

    @Test
    @DisplayName("API. Unsuccessful login test")
    void unsuccessfulLoginLombokTest() {
        LoginBodyLombokModel authData = new LoginBodyLombokModel();

        ErrorResponseLombokModel response = step("Make request", () ->
                given(loginRequestSpec)

                        .body(authData)

                        .when()
                        .post()
                        .then()
                        .spec(errorResponseSpec)
                        .extract().as(ErrorResponseLombokModel.class));

        step("Check response", () ->
                assertEquals("Missing email or username", response.getError()));

    }

    @Test
    @DisplayName("API. Correct get request for single user by ID")
    void getUserByIdTest() {
        LoginBodyLombokModel userId = new LoginBodyLombokModel();
        userId.setUserID("8");

        LoginResponseLombokModel response = step("Make request", () ->
                given(loginRequestSpec)
                        .body(userId)

                        .when()
                .get("users/" + userId)

                .then()
                        .spec(loginResponseSpec)
                        .extract().as(LoginResponseLombokModel.class));
        step("Check response", () ->
                assertEquals("data.email", is("lindsay.ferguson@reqres.in")));


    }

    @Test
    @DisplayName("API. Create new user request")
    void createNewUserTest() {
        String userData = "{\"name\": \"morpheus\",\"job\": \"leader\"}";

        given()
                .filter(new AllureRestAssured())
                .body(userData)
                .log().uri()
                .when()
                .post("/users")

                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("createdAt", is(notNullValue()));


    }

    @Test
    @DisplayName("API. Update user request")
    void updateUserTest() {
        String userData = "{\"name\": \"morpheus\", \"job\": \"designer\"}";

        given()
                .filter(new AllureRestAssured())
                .body(userData)
                .log().uri()
                .contentType(JSON)
                .when()
                .put("/users/2")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("morpheus"))
                .body("job", is("designer"))
                .body("updatedAt", is(notNullValue()));


    }

    @Test
    @DisplayName("API. Unsuccessful user registration test")
    void unsuccessfulRegisterTest() {
        String registerData = "{\"email\": \"eve.holt@reqres.in\"}";

        given()
                .filter(new AllureRestAssured())
                .body(registerData)
                .log().uri()
                .when()
                .post("/register")

                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));


    }

    @Test
    @DisplayName("API. Get list of resources request")
    void getListResourceTest() {


        given()
                .filter(new AllureRestAssured())
                .log().uri()
                .contentType(JSON)
                .when()
                .get("/unknown")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.page", is(notNullValue()))
                .body("data.total", is(notNullValue()));


    }

}
