package com.cybertek.library1.testCases;

import com.cybertek.library1.utils.ConfigurationReader;
import com.cybertek.library1.utils.Utils;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnitPlatform.class)
@DisplayName("All test cases related to User section in documentation using Map")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("regression")
public class User_UsingMap_Tests extends Utils {

  @Test
  @Order(1)
  @Tag("smoke")
  @DisplayName("librarian valid credentials Post /login test")
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

  @Test
  @Order(2)
  @Tag("smoke")
  @DisplayName("librarian invalid credentials Post /login test")
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

  @Test
  @Order(3)
  @Tag("smoke")
  @DisplayName("POST /decode")
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

  @Test
  @Order(4)
  @DisplayName("POST negative test /decode")
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

  @Test
  @Order(5)
  @Tag("smoke")
  @DisplayName("POST using Map /add_user")
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

  @Test
  @Order(6)
  @Tag("smoke")
  @DisplayName("PATCH /update_user")
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

  @Test
  @Order(7)
  @DisplayName("PATCH negative test wrong edit /update_user")
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

  @Test
  @Order(8)
  @DisplayName("PATCH negative test Unauthorized user-Student /update_user")
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

  @Test
  @Order(8)
  @DisplayName("PATCH negative test Unauthorized user-NoBody /update_user")
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

  @Test
  @Order(9)
  @Tag("smoke")
  @DisplayName("GET /get_user_by_id/{id}")
  public void getOneUserByIdTest() {
    System.out.println("user_id = " + user_id);
    System.out.println("userInTest = " + userInTest);
    JsonPath jp =
    given()
            .log().all()
            .header("x-library-token", librarianToken)
            .pathParam("id", user_id).
    when()
            .get("/get_user_by_id/{id}").
    then()
            .log().all()
            .statusCode(200).
    extract().
            jsonPath()
    ;
    assertAll(() -> {
      assertEquals(jp.get("id"), userInTest.get("id"));
      assertEquals(jp.get("full_name"), userInTest.get("full_name"));
      assertEquals(jp.get("email"), userInTest.get("email"));
      assertEquals(jp.get("password"), userInTest.get("password"));
      assertEquals(jp.get("user_group_id"), userInTest.get("user_group_id"));
      assertEquals(jp.get("status"), userInTest.get("status"));
      assertEquals(jp.get("is_admin"), userInTest.get("is_admin"));
      assertEquals(jp.get("start_date"), userInTest.get("start_date"));
      assertEquals(jp.get("end_date"), userInTest.get("end_date"));
      assertEquals(jp.get("address"), userInTest.get("address"));
    });
  }

  @Test
  @Order(10)
  @DisplayName("GET negative test not found /get_user_by_id/{id}")
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

  @Test
  @Order(11)
  @Tag("smoke")
  @DisplayName("GET /get_all_users")
  public void getAllUsersTest() {
    List<String> allUsers =
            given()
                    .log().all()
                    .header("x-library-token", librarianToken).
            when()
                    .get("/get_all_users").
            then()
                    .log().all()
                    .statusCode(200).
            extract()
                    .jsonPath().getList("name")
            ;
    System.out.println("allUsers.size() = " + allUsers.size());
    Set<String> uniqueUsers = new LinkedHashSet<>(allUsers);
    System.out.println("uniqueUsers.size() = " + uniqueUsers.size());
  }

  @Test
  @Order(12)
  @DisplayName("GET Unauthorised User Student /get_all_users")
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

  @Test
  @Order(13)
  @DisplayName("GET Unauthorised User NoBody /get_all_users")
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

  @Test
  @Order(14)
  @Tag("smoke")
  @DisplayName("GET /get_user_groups")
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
