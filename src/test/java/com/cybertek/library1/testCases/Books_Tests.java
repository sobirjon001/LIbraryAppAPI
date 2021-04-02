package com.cybertek.library1.testCases;


import com.cybertek.library1.utils.Utils;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.Test;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.*;

@RunWith(JUnitPlatform.class)
@DisplayName("All test cases related to Books section in documentation")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("wip")
public class Books_Tests extends Utils {

  @Test
  @Order(1)
  @DisplayName("GET /get_book_categories")
  public void getBooksCategories() {
    JsonPath jp =
            given()
                    .log().all()
                    .contentType(ContentType.URLENC)
                    .formParam("token", librarianToken).
            when()
                    .get("/get_book_categories").
            then()
                    .log().all()
                    .statusCode(200).
            extract()
                    .jsonPath()
            ;
    int iterationNum = jp.getList("id").size();
    for (int i = 0; i < iterationNum; i++) {
      bookCategories.put(
              jp.getString("["+i+"].id"), jp.getString("["+i+"].name")
      );
    }
    System.out.println("bookCategories = " + bookCategories);
  }
}
