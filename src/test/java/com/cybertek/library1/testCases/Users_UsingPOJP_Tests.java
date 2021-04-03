package com.cybertek.library1.testCases;

import com.cybertek.library1.pojo.User;
import com.cybertek.library1.utils.Utils;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnitPlatform.class)
@DisplayName("All test cases related to User section in documentation using POJO")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("regression")
public class Users_UsingPOJP_Tests extends Utils {

  @Test
  @Order(1)
  @Tag("smoke")
  @DisplayName("POST using POJO /add_user")
  public void addOneUserTest() {
    User.student1.setRandomValues();
    user_id =
            given()
                    .log().all()
                    .header("x-library-token", librarianToken)
                    .contentType(ContentType.JSON)
                    .body(User.student1).
                    when()
                    .post("/add_user").
                    then()
                    .log().all()
                    .statusCode(200)
                    .body("message", is("The user has been created.")).
                    extract()
                    .path("user_id")
    ;
    User.student1.setId(user_id);
    System.out.println("User.student1 = " + User.student1);
  }

  @Test
  @Order(2)
  @Tag("smoke")
  @DisplayName("PATCH Using POJO /update_user")
  public void updateOneUserTest() {
    User.student1.setRandomValues();
    given()
            .log().all()
            .header("x-library-token", librarianToken)
            .contentType(ContentType.JSON)
            .body(User.student1).
            when()
            .patch("/update_user").
            then()
            .log().all()
            .statusCode(200)
            .body("message", is("The user updated."))
    ;
  }

  @Test
  @Order(3)
  @Tag("smoke")
  @DisplayName("GET compare with POJO /get_user_by_id/{id}")
  public void getOneUserByIdTest() {
    User user =
            given()
                    .log().all()
                    .header("x-library-token", librarianToken)
                    .pathParam("id", User.student1.getId()).
                    when()
                    .get("/get_user_by_id/{id}").
                    then()
                    .log().all()
                    .statusCode(200).
                    extract()
                    .body()
                    .as(User.class);
    System.out.println("user = " + user);
    System.out.println("User.student1 = " + User.student1);
    assertAll(() -> {
      assertEquals(User.student1.getId(), user.getId());
      assertEquals(User.student1.getFull_name(), user.getFull_name());
      assertEquals(User.student1.getEmail(), user.getEmail());
      assertEquals(User.student1.getPassword(), user.getPassword());
      assertEquals(User.student1.getUser_group_id(), user.getUser_group_id());
      assertEquals(User.student1.getStatus(), user.getStatus());
      assertEquals(User.student1.getStart_date(), user.getStart_date());
      assertEquals(User.student1.getEnd_date(), user.getEnd_date());
      assertEquals(User.student1.getAddress(), user.getAddress());

    });

  }
}
