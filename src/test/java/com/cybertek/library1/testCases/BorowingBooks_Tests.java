package com.cybertek.library1.testCases;


import com.cybertek.library1.utils.Utils;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@RunWith(JUnitPlatform.class)
@DisplayName("Borrowing Books Test Cases Suit")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("regression")
public class BorowingBooks_Tests extends Utils {

  @Test
  @Order(1)
  @DisplayName("GET /get_borrowed_books_by_user/{user_id}")
  public void get_borrowed_books_by_userId() {
    user_id = getMyId(studentToken);
    given()
            .log().all()
            .header("x-library-token", studentToken)
            .pathParam("user_id", user_id).
    when()
            .get("/get_borrowed_books_by_user/{user_id}").
    then()
            .log().status()
            .statusCode(200)
            .body("size()", greaterThan(0))
    ;
  }

  @Test
  @Order(2)
  @DisplayName("GET /get_book_list_for_borrowing")
  public void get_book_list_for_borrowing() {
    given()
            .log().all()
            .header("x-library-token", studentToken).
    when()
            .get("/get_book_list_for_borrowing").
    then()
            .log().status()
            .statusCode(200)
    ;
  }
}
