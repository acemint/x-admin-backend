package com.clinic.xadmin.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Assertions;
import org.testcontainers.shaded.org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

public class IntegrationTestHelper {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  static {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  private static final String FILE_SEPARATOR = "/";
  public static final String JSON_HINT = "json";
  public static final String ENTITY_HINT = "entity";
  public static final String REQUEST_HINT = "request";
  public static final String RESPONSE_HINT = "response";

  public static <T> T readJsonFile(String fileName, Class<T> clazz, String... hints) {
    File inputStream = fileFetcher(fileName, hints);
    try {
      return objectMapper.readValue(inputStream, clazz);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T readJsonFile(String fileName, TypeReference<T> typeReference, String... hints) {
    File inputStream = fileFetcher(fileName, hints);
    try {
      return objectMapper.readValue(inputStream, typeReference);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T readJsonFileFromSpecificPath(String fileName, String jsonPath, Class<T> clazz, String... hints) {
    File inputStream = fileFetcher(fileName, hints);
    try {
      JsonNode jsonNode = objectMapper.readTree(inputStream);
      return objectMapper.readValue(jsonNode.get(jsonPath).asText(), clazz);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T readJsonFileFromSpecificPath(String fileName, String jsonPath, TypeReference<T> typeReference, String... hints) {
    File inputStream = fileFetcher(fileName, hints);
    try {
      JSONArray data = JsonPath.read(inputStream, jsonPath);
      return objectMapper.readValue(data.toJSONString(), typeReference);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static byte[] readJsonAsBytes(String fileName, String... hints) {
    File inputStream = fileFetcher(fileName, hints);
    try {
      Object object = objectMapper.readValue(inputStream, Object.class);
      return objectMapper.writeValueAsBytes(object);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> byte[] convertToByte(T data) {
    try {
      return objectMapper.writeValueAsBytes(data);
    } catch (JsonProcessingException e) {
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
