package com.clinic.xadmin.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Assertions;
import org.testcontainers.shaded.org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

public class IntegrationTestHelper {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final String FILE_SEPARATOR = "/";
  public static final String JSON_BASE_PATH_HINT = "json/";
  public static final String REQUEST_PATH_HINT = "request/";
  public static final String RESPONSE_PATH_HINT = "response/";

  public static <T> T readJsonFile(String fileName, Class<T> clazz, String... hints) {
    File inputStream = fileFetcher(fileName, hints);
    try {
      return objectMapper.readValue(inputStream, clazz);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String readJsonAsString(String fileName, String... hints) {
    File inputStream = fileFetcher(fileName, hints);
    try {
      return objectMapper.writeValueAsString(inputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static @Nullable File fileFetcher(String fileName, String... hints) {
    StringBuilder filePathBuilder = new StringBuilder();
    Arrays.stream(hints).forEach(h -> filePathBuilder.append(h).append(FILE_SEPARATOR));
    String resourcePath = filePathBuilder.append(fileName).toString();

    URL resourceFile = IntegrationTestHelper.class.getClassLoader().getResource(resourcePath);
    if (Objects.isNull(resourceFile)) {
      Assertions.fail("No file found in " + resourcePath);
    }
    return new File(resourceFile.getFile());
  }



}
