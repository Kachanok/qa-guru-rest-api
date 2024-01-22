import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


public class ApiTests extends TestBase {


    @Test
    @DisplayName("Successful login test")
    void successfulLoginTest() {
        String authData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";

        given()
                .body(authData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/login")

                .then()
                .log().body()
                .statusCode(200)
                .body("token", is(notNullValue()));


    }

    @Test
    @DisplayName("Unsuccessful login test")
    void unsuccessfulLoginTest() {
        String authData = "";

        given()
                .body(authData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/login")

                .then()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));


    }

    @Test
    @DisplayName("Correct get request for single user by ID")
    void getUserByIdTest() {
        String userId = "8";

        given()

                .log().uri()
                .when()
                .get("/users/" + userId)

                .then()
                .log().body()
                .statusCode(200)
                .body("data.email", is("lindsay.ferguson@reqres.in"))
                .body("data.first_name", is("Lindsay"));


    }

    @Test
    @DisplayName("Create new user request")
    void createNewUserTest() {
        String userData = "{\"name\": \"morpheus\",\"job\": \"leader\"}";

        given()
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
    @DisplayName("Update user request")
    void updateUserTest() {
        String userData = "{\"name\": \"morpheus\", \"job\": \"designer\"}";

        given()
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
    @DisplayName("Unsuccessful user registration test")
    void unsuccessfulRegisterTest() {
        String registerData = "{\"email\": \"eve.holt@reqres.in\"}";

        given()
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
    @DisplayName("Get list of resources request")
    void getListResourceTest() {


        given()

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
