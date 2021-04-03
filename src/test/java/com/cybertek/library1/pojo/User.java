package com.cybertek.library1.pojo;

import com.cybertek.library1.utils.Variables;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User implements Variables {

  private String id;
  private String full_name;
  private String email;
  private String password;
  private String user_group_id;
  private String image;
  private String extra_data;
  private String status;
  private String is_admin;
  private String start_date;
  private String end_date;
  private String address;

  public static List<User> users = new ArrayList<>();
  public static User student1 = new User();
  public static User librarian1 = new User();

  public User() {}

  public User(
          String full_name,
          String email,
          String password,
          String user_group_id,
          String status,
          String start_date,
          String end_date,
          String address
  ) {
    this.full_name = full_name;
    this.email = email;
    this.password = password;
    this.user_group_id = user_group_id;
    this.status = status;
    this.start_date = start_date;
    this.end_date = end_date;
    this.address = address;
  }

  public void setRandomValues() {
    this.full_name = faker.name().fullName();
    this.email = faker.internet().emailAddress();
    this.password = faker.internet().password()+"";
    this.user_group_id = "2";
    this.status = "ACTIVE";
    this.start_date = LocalDate.now().toString();
    this.end_date = LocalDate.now().plusMonths(4L).toString();
    this.address = faker.address().fullAddress();
  }

  public String getId() {
    return id;
  }

  public String getFull_name() {
    return full_name;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getUser_group_id() {
    return user_group_id;
  }

  public String getStatus() {
    return status;
  }

  public String getStart_date() {
    return start_date;
  }

  public String getEnd_date() {
    return end_date;
  }

  public String getAddress() {
    return address;
  }

  public String getImage() {
    return image;
  }

  public String getExtra_data() {
    return extra_data;
  }

  public String getIs_admin() {
    return is_admin;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setFull_name(String full_name) {
    this.full_name = full_name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setUser_group_id(String user_group_id) {
    this.user_group_id = user_group_id;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setStart_date(String start_date) {
    this.start_date = start_date;
  }

  public void setEnd_date(String end_date) {
    this.end_date = end_date;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public void setExtra_data(String extra_data) {
    this.extra_data = extra_data;
  }

  public void setIs_admin(String is_admin) {
    this.is_admin = is_admin;
  }

  @Override
  public String toString() {
    return "User{" +
            ", id='" + id + '\'' +
            "full_name='" + full_name + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", user_group_id='" + user_group_id + '\'' +
            ", status='" + status + '\'' +
            ", start_date='" + start_date + '\'' +
            ", end_date='" + end_date + '\'' +
            ", address='" + address + '\'' +
            '}';
  }
}
