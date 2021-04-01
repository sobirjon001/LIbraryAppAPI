package com.cybertek.library1.utils;

import com.github.javafaker.Faker;

import static io.restassured.RestAssured.*;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class Utils{

  private static final Faker faker = new Faker();
  public static String user_id;
  public static Map<String, Object> userInTest;

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

}
