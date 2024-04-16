package com.satusehat.dto.response.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResourceResponse {

  private Boolean active;
  private List<Address> address;
  private String birthDate;
  private Boolean deceasedBoolean;
  private List<Communication> communication;
  private String gender;
  private String id;
  private List<Telecom> telecom;


  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Address {

    private String city;
    private String country;
    private List<AddressExtension> extension;
    private List<String> line;
    private String use;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressExtension {

      private List<NestedAddressExtension> extension;
      private String url;

      @Data
      @NoArgsConstructor
      @AllArgsConstructor
      public static class NestedAddressExtension {

        private String url;
        private String valueCode;

      }

    }

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Communication {

    private Language language;
    private Boolean preferred;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Language {

      private String text;

      @Data
      @NoArgsConstructor
      @AllArgsConstructor
      public static class LanguageCoding {

        private String code;
        private String display;
        private String system;

      }

    }

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Telecom {

    private String system;
    private String use;
    private String value;

  }


}
