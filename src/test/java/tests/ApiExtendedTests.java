package tests;

import models.lombok.*;
import models.pojo.LoginBodyModel;
import models.pojo.LoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
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

        LoginResponseModel response = step("Make request", () ->
                given(loginRequestSpec)
                .body(authData)

                .when()
                .post("/login")

                .then()
                        .spec(responseSpecWithStatus200)
                .extract().as(LoginResponseModel.class));

        step("Check response", () ->

                assertNotNull("Token should not be null", response.getToken()));


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
                        .post("/login")

                        .then()
                        .spec(responseSpecWithStatus200)
                        .extract().as(LoginResponseLombokModel.class));
        step("Check response", () ->

                assertNotNull("Token should not be null", response.getToken()));

    }

    @Test
    @DisplayName("API. Unsuccessful login test")
    void unsuccessfulLoginLombokTest() {
        LoginBodyLombokModel authData = new LoginBodyLombokModel();
        authData.setEmail("");
        authData.setPassword("cityslicka");

        ErrorResponseLombokModel response = step("Make request", () ->
                given(loginRequestSpec)
                        .body(authData)

                        .when()
                        .post("users/")

                        .then()
                        .spec(responseSpecWithStatus400)
                        .extract().as(ErrorResponseLombokModel.class));

        step("Check response", () ->
                assertEquals("Missing email or username", response.getError()));

    }

    @Test
    @DisplayName("API. Correct get request for single user by ID")
    void getUserByIdTest() {
        UserLombokModel userId = new UserLombokModel();
        userId.setUserID("5");

        UserResponseLombokModel response = step("Make request", () ->
                given(loginRequestSpec)
                        .body(userId)

                        .when()
                        .get("users/" + userId)

                        .then()
                        .spec(responseSpecWithStatus200)
                        .extract().as(UserResponseLombokModel.class));
        step("Check response", () ->
                assertEquals("data.email", response.getEmail()));


    }

    @Test
    @DisplayName("API. Create new user request")
    void createNewUserTest() {
        CreateUserLombokModel userData = new CreateUserLombokModel();
        userData.setUserName("morpheus");
        userData.setUserJob("leader");

        CreateUserResponseLombokModel response = step("Make request", () ->
                given(loginRequestSpec)
                        .body(userData)

                        .when()
                        .post("/users")

                        .then()
                        .spec(responseSpecWithStatus201)
                        .extract().as(CreateUserResponseLombokModel.class));
        step("Check response", () ->
                assertNotNull("createdAt", response.getCreatedAt()));


    }

    @Test
    @DisplayName("API. Update user request")
    void updateUserTest() {
        CreateUserLombokModel userData = new CreateUserLombokModel();
        userData.setUserName("morpheus");
        userData.setUserJob("designer");

        UpdateUserResponseLombokModel response = step("Make request", () ->
                given(loginRequestSpec)
                        .body(userData)

                        .when()
                        .put("/users/2")

                        .then()
                        .spec(responseSpecWithStatus200)
                        .extract().as(UpdateUserResponseLombokModel.class));
        step("Check response", () ->
                assertNotNull("updatedAt", response.getUpdatedAt()));


    }
}


