package com.cybertek.library1.testCases;


import com.cybertek.library1.utils.Utils;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

@RunWith(JUnitPlatform.class)
@DisplayName("All test cases related to Books section in documentation")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("regression")
public class Books_Tests extends Utils {

  @ParameterizedTest(name = "{index} ==> testing with {0} credentials, code {2}")
  @MethodSource("getTokens")
  @Order(1)
  @DisplayName("GET /get_book_categories")
  public void getBooksCategories(String person, String token, String expectedCode) {
    JsonPath jp =
            given()
                    .log().all()
                    .header("x-library-token", token).
            when()
                    .get("/get_book_categories").
            then()
                    //.log().all()
                    .statusCode(Integer.parseInt(expectedCode)).
            extract()
                    .jsonPath()
            ;
    if(bookCategories == null) {
      bookCategories  = new LinkedHashMap<>();
      int iterationNum = jp.getList("id").size();
      for (int i = 0; i < iterationNum; i++) {
        bookCategories.put(
                jp.getString("[" + i + "].id"), jp.getString("[" + i + "].name")
        );
      }
      System.out.println("bookCategories = " + bookCategories);
    }
  }

  @Test
  @Order(2)
  @DisplayName("POST /add_book")
  public void addOneBook() {
    Map<String, Object> book = getNewBook();
    newBook.put("id",
            given()
                    .log().all()
                    .header("x-library-token", librarianToken)
                    .contentType(ContentType.JSON)
                    .body(book).
            when()
                    .post("/add_book").
            then()
                    .log().all()
                    .statusCode(200)
                    .body("message", is("The book has been created.")).
            extract()
                    .path("book_id")
    );
  }

  @Test
  @Order(3)
  @DisplayName("PATCH /update_book")
  public void updateOneBook() {
    updateBook();
    given()
            .log().all()
            .header("x-library-token", librarianToken)
            .contentType(ContentType.JSON)
            .body(newBook).
    when()
            .patch("/update_book").
    then()
            .log().all()
            .statusCode(200)
            .body("message", is("The book has been updated."))
    ;
  }

  @Test
  @Order(4)
  @DisplayName("GET /get_book_by_id/{id}")
  public void getOneBookById() {
    given()
            .log().all()
            .header("x-library-token", librarianToken)
            .pathParam("id", newBook.get("id")).
    when()
            .get("/get_book_by_id/{id}").
    then()
            .log().all()
            .statusCode(200)
            .body("id", is(newBook.get("id")))
            .body("name", is(newBook.get("name")))
            .body("isbn", is(newBook.get("isbn")))
            .body("year", is(newBook.get("year")+""))
            .body("author", is(newBook.get("author")))
            .body("book_category_id", is(newBook.get("book_category_id")))
            .body("description", is(newBook.get("description")))
    ;
  }

  @Test
  @Order(6)
  @DisplayName("GET Negative Test Requesting deleted Book /get_book_by_id/{id}")
  public void getOneBookByIdNegativeTest() {
    given()
            .log().all()
            .header("x-library-token", librarianToken)
            .pathParam("id", newBook.get("id")).
    when()
            .get("/get_book_by_id/{id}").
    then()
            .log().all()
            .statusCode(404)
            .body("error", is(newBook.get("Book does not exist")))
    ;
  }

  @Test
  @Order(5)
  @DisplayName("DELETE /delete_book/{id}")
  public void deleteOneBookById() {
    given()
            .log().all()
            .header("x-library-token", superUserToken)
            .pathParam("id", newBook.get("id")).
    when()
            .delete("/delete_book/{id}").
    then()
            .log().all()
            .statusCode(204)
    ;
  }

}
