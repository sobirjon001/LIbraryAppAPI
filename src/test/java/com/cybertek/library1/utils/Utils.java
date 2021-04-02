package com.cybertek.library1.utils;

import com.github.javafaker.Faker;

import static io.restassured.RestAssured.*;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class Utils{

  private static final Faker faker = new Faker();
  private static final Random r = new Random();
  public static String user_id;
  public static Map<String, Object> userInTest;
  public static Map<String, String> bookCategories;
  public static Map<String, Object> newBook;

  public static String librarianToken;
  public static String studentToken;

  @BeforeAll
  public static void init() {
    baseURI = ConfigurationReader.getProperty("base_url");
    basePath = ConfigurationReader.getProperty("base_path");
    librarianToken = getToken(
            ConfigurationReader.getProperty("librarian69"),
            ConfigurationReader.getProperty("librarian69Passwd")
    );
    studentToken = getToken(
            ConfigurationReader.getProperty("student133"),
            ConfigurationReader.getProperty("student133Password")
    );
  }

  @AfterAll
  public static void cleanup() {
    reset();
  }

  public static String getToken(String email, String password) {
    return given()
                  .contentType(ContentType.URLENC)
                  .formParam("email", email)
                  .formParam("password", password).
           when()
                  .post("login").
           then()
                  .statusCode(200).
           extract()
                  .path("token")
           ;
  }

  public static Map<String, Object> getNewUser() {
    Map<String, Object> userMap = new LinkedHashMap<>();
    userMap.put("full_name", faker.name().fullName());
    userMap.put("email", faker.internet().emailAddress());
    userMap.put("password", faker.internet().password());
    userMap.put("user_group_id", "3");
    userMap.put("status", "ACTIVE");
    userMap.put("start_date", LocalDate.now().toString());
    userMap.put("end_date", LocalDate.now().plusMonths(4L).toString());
    userMap.put("address", faker.address().fullAddress());

    return userMap;
  }

  public static Map<String, Object> getNewUser(String id) {
    Map<String, Object> userMap = new LinkedHashMap<>();
    userMap.put("full_name", faker.name().fullName());
    userMap.put("email", faker.internet().emailAddress());
    userMap.put("password", faker.internet().password());
    userMap.put("user_group_id", "3");
    userMap.put("status", "ACTIVE");
    userMap.put("start_date", LocalDate.now().toString());
    userMap.put("end_date", LocalDate.now().plusMonths(4L).toString());
    userMap.put("address", faker.address().fullAddress());
    userMap.put("id", id);

    userInTest = new LinkedHashMap<>(userMap);

    return userMap;
  }

  public static Map<String, Object> getInvalidUser(String id) {
    Map<String, Object> userMap = new LinkedHashMap<>();
    userMap.put("full_name", faker.name().fullName());
    userMap.put("email", faker.internet().emailAddress());
    userMap.put("password", faker.internet().password());
    userMap.put("user_group_id", "1");
    userMap.put("status", "ACTIVE");
    userMap.put("start_date", LocalDate.now().toString());
    userMap.put("end_date", LocalDate.now().plusMonths(4L).toString());
    userMap.put("address", faker.address().fullAddress());
    userMap.put("id", id);

    return userMap;
  }

  public static String[][] getTokens() {
    return new String[][]{{"librarian", librarianToken, "200"},
            {"student", studentToken, "200"},
            {"invalid", "invalid_token", "401"}
    };
  }

  public static Map<String, Object> getNewBook(){
    newBook = new LinkedHashMap<>();
    newBook.put("name", faker.book().title());
    newBook.put("isbn", faker.number().digits(8)+"");
    newBook.put("year", faker.number().numberBetween(1960, 2021));
    newBook.put("author", faker.book().author());
    List<String> ids = new ArrayList<>(bookCategories.keySet());
    newBook.put("book_category_id", ids.get(r.nextInt(ids.size())));
    newBook.put("description", faker.chuckNorris().fact());
    return newBook;
  }

  public static void updateBook() {
    newBook.put("name", faker.book().title());
    newBook.put("isbn", faker.number().digits(8)+"");
    newBook.put("year", faker.number().numberBetween(1960, 2021));
    newBook.put("author", faker.book().author());
    List<String> ids = new ArrayList<>(bookCategories.keySet());
    newBook.put("book_category_id", ids.get(r.nextInt(ids.size())));
    newBook.put("description", faker.chuckNorris().fact());
  }

}
