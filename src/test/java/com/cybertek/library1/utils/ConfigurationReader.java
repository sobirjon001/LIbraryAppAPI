package com.cybertek.library1.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationReader {
  //In this class we will implement the repeated steps of reading
  // from configuration.properties file

  //#1- Create the object of Properties
  private static Properties properties = new Properties();

  static {
    try {
      //#2- Get the path
      FileInputStream file = new FileInputStream("configuration.properties");
      //#3- and open the file
      properties.load(file);
      //#4- closing the file in JVM Memory
      file.close();
    } catch (IOException e) {
      System.out.println("failed to from configuration.properties" + e.getMessage());
    }
  }

  //#5- Use the object to read from the configuration.properties file
  public static String getProperty(String keyword) {
    return properties.getProperty(keyword);
  }
}
