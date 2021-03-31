package test_cases;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import utils.ConfigurationReader;
import utils.Utils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@DisplayName("All test cases related to User section in documentation")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class User_Tests extends Utils {

  @DisplayName("librarian valid credentials Post /login test")
  @Test
  @Order(1)
  public void librarianValidLoginTest() {
    String validLibrarianEmail = ConfigurationReader.getProperty("librarian69");
    String validLibrarianPasswd = ConfigurationReader.getProperty("librarian69Passwd");
    given()
            .log().all()
            .contentType(ContentType.URLENC)
            .formParam("email", validLibrarianEmail)
            .formParam("password", validLibrarianPasswd).
    when()
            .post("/login").
    then()
            .log().all()
            .statusCode(200)
            .body("", is(not(blankOrNullString())))
    ;
  }

  @DisplayName("librarian invalid credentials Post /login test")
  @Test
  @Order(2)
  public void librarianInvalidLoginTest() {
    String validLibrarianEmail = ConfigurationReader.getProperty("librarian69");
    String invalidLibrarianPasswd = "fakePasswd";
    given()
            .log().all()
            .contentType(ContentType.URLENC)
            .formParam(validLibrarianEmail)
            .formParam(invalidLibrarianPasswd).
    when()
            .post("/login").
    then()
            .log().all()
            .statusCode(404)
            .body("error", is("Sorry, Wrong Email or Password"))
    ;
  }

  @DisplayName("POST /decode")
  @Test
  @Order(3)
  public void librarianDecodeTest() {
    given()
            .log().all()
            .contentType(ContentType.URLENC)
            .formParam("token", librarianToken).
    when()
            .post("/decode").
    then()
            .log().all()
            .statusCode(200)
            .body("email", is(ConfigurationReader.getProperty("librarian69")))
            .body("token", is(librarianToken))
    ;
  }

  @DisplayName("POST negative test /decode")
  @Test
  @Order(4)
  public void librarianDecodeNegativeTest() {
    given()
            .log().all()
            .contentType(ContentType.URLENC)
            .formParam("tokens", librarianToken).
            when()
            .post("/decode").
            then()
            .log().all()
            .statusCode(500)
            .body("error", is("Wrong number of segments"))
    ;
  }

  @DisplayName("POST using Map /add_user")
  @Test
  @Order(5)
  public void addOneUserTest() {
    user_id =
            given()
                    .log().all()
                    .header("x-library-token", librarianToken)
                    .contentType(ContentType.JSON)
                    .body(getNewUser()).
            when()
                    .post("/add_user").
            then()
                    .log().all()
                    .statusCode(200)
                    .body("message", is("The user has been created.")).
            extract()
                    .path("user_id")
            ;
    System.out.println("user_id = " + user_id);
  }

  @DisplayName("PATCH /update_user")
  @Test
  @Order(6)
  public void updateOneUserTest() {
    System.out.println("user_id = " + user_id);
    given()
            .log().all()
            .header("x-library-token", librarianToken)
            .contentType(ContentType.JSON)
            .body(getNewUser(user_id)).
    when()
            .patch("/update_user").
    then()
            .log().all()
            .statusCode(200)
            .body("message", is("The user updated."))
    ;
  }

  @DisplayName("PATCH negative test wrong edit /update_user")
  @Test
  @Order(7)
  public void updateOneUserNegativeTest() {
    System.out.println("user_id = " + user_id);
    given()
            .log().all()
            .header("x-library-token", librarianToken)
            .contentType(ContentType.JSON)
            .body(getInvalidUser(user_id)).
    when()
            .patch("/update_user").
    then()
            .log().all()
            .statusCode(200)
            .body("error", is("You do not add/edit admins."))
    ;
  }

  @DisplayName("PATCH negative test Unauthorized user-Student /update_user")
  @Test
  @Order(8)
  public void UpdateOneUserNegativeUnauthorizedUserStudentTest() {
    given()
            .log().all()
            .header("x-library-token", studentToken)
            .contentType(ContentType.JSON)
            .body(userInTest).
    when()
            .patch("/update_user").
    then()
            .log().all()
            .statusCode(403)
            .body("error", is("Unauthorized Access"))
    ;
  }

  @DisplayName("PATCH negative test Unauthorized user-NoBody /update_user")
  @Test
  @Order(8)
  public void UpdateOneUserNegativeUnauthorizedNoUserTest() {
    given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(userInTest).
    when()
            .patch("/update_user").
    then()
            .log().all()
            .statusCode(401)
    ;
  }

  @DisplayName("GET /get_user_by_id/{id}")
  @Test
  @Order(9)
  public void getOneUserByIdTest() {
    System.out.println("user_id = " + user_id);
    System.out.println("userInTest = " + userInTest);
    given()
            .log().all()
            .header("x-library-token", librarianToken)
            .pathParam("id", user_id).
    when()
            .get("/get_user_by_id/{id}").
    then()
            .log().all()
            .statusCode(200)
            .body("id", is(userInTest.get("id")))
            .body("full_name", is(userInTest.get("full_name")))
            .body("email", is(userInTest.get("email")))
            //.body("password", is(userInTest.get("password")))
            .body("user_group_id", is(userInTest.get("user_group_id")))
            .body("status", is(userInTest.get("status")))
            .body("is_admin", is("0"))
            .body("start_date", is(userInTest.get("start_date")))
            .body("end_date", is(userInTest.get("end_date")))
            .body("address", is(userInTest.get("address")))
    ;
  }

  @DisplayName("GET negative test not found /get_user_by_id/{id}")
  @Test
  @Order(10)
  public void getOneUserByIdNegativeTest() {
    given()
            .log().all()
            .header("x-library-token", librarianToken)
            .pathParam("id", 0).
    when()
            .get("/get_user_by_id/{id}").
    then()
            .log().all()
            .statusCode(404)
    ;
  }

  @DisplayName("GET /get_all_users")
  @Test
  @Order(11)
  public void getAllUsersTest() {
    List<String> allUsers =
            given()
                    .log().all()
                    .header("x-library-token", librarianToken).
            when()
                    .get("/get_all_users").
            then()
                    //.log().all()
                    .statusCode(200).
            extract()
                    .jsonPath().getList("name")
            ;
    System.out.println("allUsers.size() = " + allUsers.size());
    Set<String> uniqueUsers = new LinkedHashSet<>(allUsers);
    System.out.println("uniqueUsers.size() = " + uniqueUsers.size());
  }

  @DisplayName("GET Unauthorised User Student /get_all_users")
  @Test
  @Order(12)
  public void getAllUsersUnauthorizedUserStudentTest() {
    given()
            .log().all()
            .header("x-library-token", studentToken).
    when()
            .get("/get_all_users").
    then()
            .log().all()
            .statusCode(403)
            .body("error", is("Unauthorized Access"))
    ;
  }

  @DisplayName("GET Unauthorised User NoBody /get_all_users")
  @Test
  @Order(13)
  public void getAllUsersUnauthorizedUserNoBodyTest() {
    given()
            .log().all().
    when()
            .get("/get_all_users").
    then()
            .log().all()
            .statusCode(401)
    ;
  }

  @DisplayName("GET /get_user_groups")
  @Test
  @Order(14)
  public void getUserGroupsTest() {
    given()
            .log().all()
            .header("x-library-token", librarianToken).
    when()
            .get("/get_user_groups").
    then()
            .log().all()
            .statusCode(200)
            .body("[0].id", is("2"))
            .body("[0].name", is("Librarian"))
            .body("[1].id", is("3"))
            .body("[1].name", is("Students"))
    ;
  }
}
