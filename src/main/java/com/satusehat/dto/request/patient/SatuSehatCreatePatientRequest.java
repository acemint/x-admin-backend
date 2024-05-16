package com.satusehat.dto.request.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.satusehat.constant.KemkesURL;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SatuSehatCreatePatientRequest {

  private final String resourceType = "Patient";

  private final Meta meta = Meta.builder().build();

  private List<Identifier> identifier = new ArrayList<>();

  private final Boolean active = Boolean.TRUE;

  private final List<Name> name = new ArrayList<>();

  @JsonProperty(value = "telecom")
  private List<Telecommunication> telecommunications = new ArrayList<>();
  private String gender;

  @JsonProperty(value = "birthDate")
  private String dateOfBirth;

  @JsonProperty(value = "deceasedBoolean")
  private Boolean isDeceased;

  private List<Address> address = new ArrayList<>();

  // TODO: Investigate what this is for
  private Integer multipleBirthInteger = 0;

  @JsonProperty(value = "contact")
  private List<Contact> contacts = new ArrayList<>();

  @JsonProperty(value = "communication")
  private List<Communication> communications = new ArrayList<>();

  private List<Extension> extension = new ArrayList<>();

  @Data
  @Builder
  public static class Meta {

    private final List<String> profile = List.of(KemkesURL.StructureDefinition.PATIENT);

  }

  @Data
  @Builder
  public static class Identifier {

    private final String use;
    private final String system;
    private String value;

  }

  @Data
  @Builder
  public static class Name {

    private String use;
    private String text;

  }

  @Data
  @Builder
  public static class Telecommunication {

    private String use;
    private String value;
    private String system;

  }

  @Data
  @Builder
  public static class Address {

    private String use;
    private List<String> line;
    private String city;
    private String postalCode;
    private AddressExtension extension;


    @Data
    @Builder
    public static class AddressExtension {

      private String url;
      private DetailAddressExtension extension;


      @Data
      @Builder
      public static class DetailAddressExtension {

        private String url;
        private String valueCode;

      }
    }

  }

  @Data
  @Builder
  public static final class MaritalStatus {

    private MaritalStatusCode coding;
    private String text;

    @Data
    @Builder
    public static final class MaritalStatusCode {

      private String system;
      private String code;
      private String display;

    }
  }

  @Data
  @Builder
  public static final class Contact {

    private Relationship relationship;
    private Name name;
    private List<Telecommunication> telecommunication;

    @Data
    @Builder
    public static final class Relationship {

      private RelationshipCode coding;

      @Data
      @Builder
      public static final class RelationshipCode {

        private String system;
        private String code;

      }
    }

  }

  @Data
  @Builder
  public static final class Communication {

    private Language language;
    private Boolean preferred;


    @Data
    @Builder
    public static final class Language {

      private LanguageCode coding;
      private String text;

      @Data
      @Builder
      public static final class LanguageCode {

        private String system;
        private String code;
        private String display;

      }

    }

  }

  @Data
  @Builder
  public static final class Extension {

    private String url;
    private String any;

  }

}
