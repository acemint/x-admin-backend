package com.satusehat.dto.request.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.satusehat.constant.KemkesURL;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SatuSehatCreatePatientRequest {

  private String id;
  private final String resourceType = "Patient";
  private final Meta meta = Meta.builder().build();
  private List<Identifier> identifier = new ArrayList<>();
  private final List<Name> name = new ArrayList<>();
  private final Boolean active = Boolean.TRUE;
  private List<Telecommunication> telecommunications = new ArrayList<>();
  private String gender;
  private String birthDate;
  private Boolean deceasedBoolean;
  private List<Address> address = new ArrayList<>();
  private final Integer multipleBirthInteger = 0;
  private List<Contact> contact = new ArrayList<>();
  private List<Communication> communication = new ArrayList<>();
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
    private String fullName;

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
