package com.satusehat.dto.request.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.satusehat.constant.KemkesURL;
import com.satusehat.dto.request.commons.Address;
import com.satusehat.dto.request.commons.Telecommunication;
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

  @JsonProperty(value = "address")
  private List<Address> addresses = new ArrayList<>();

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

// EXAMPLE:
//	{
//	   "resourceType": "Patient",
//	   "meta": {
//	      "profile": [
//	         "https://fhir.kemkes.go.id/r4/StructureDefinition/Patient"
//	      ]
//	   },
//	   "identifier": [
//	      {
//	         "use": "official",
//	         "system": "https://fhir.kemkes.go.id/id/nik",
//	         "value": "3174031002891009"
//	      }
//	   ],
//	   "active": true,
//	   "name": [
//	      {
//	         "use": "official",
//	         "text": "John Smith"
//	      }
//	   ],
//	   "telecom": [
//	      {
//	         "system": "phone",
//	         "value": "08123456789",
//	         "use": "mobile"
//	      },
//	      {
//	         "system": "phone",
//	         "value": "+622123456789",
//	         "use": "home"
//	      },
//	      {
//	         "system": "email",
//	         "value": "john.smith@xyz.com",
//	         "use": "home"
//	      }
//	   ],
//	   "gender": "female",
//	   "birthDate": "1945-11-17",
//	   "deceasedBoolean": false,
//	   "address": [ {} ],
//	   "maritalStatus": {
//	      "coding": [
//	         {
//	            "system": "http://terminology.hl7.org/CodeSystem/v3-MaritalStatus",
//	            "code": "M",
//	            "display": "Married"
//	         }
//	      ],
//	      "text": "Married"
//	   },
//	   "multipleBirthInteger": 0,
//	   "contact": [
//	      {
//	         "relationship": [
//	            {
//	               "coding": [
//	                  {
//	                     "system": "http://terminology.hl7.org/CodeSystem/v2-0131",
//	                     "code": "C"
//	                  }
//	               ]
//	            }
//	         ],
//	         "name": {
//	            "use": "official",
//	            "text": "Jane Smith"
//	         },
//	         "telecom": [
//	            {
//	               "system": "phone",
//	               "value": "0690383372",
//	               "use": "mobile"
//	            }
//	         ]
//	      }
//	   ],
//	   "communication": [
//	      {
//	         "language": {
//	            "coding": [
//	               {
//	                  "system": "urn:ietf:bcp:47",
//	                  "code": "id-ID",
//	                  "display": "Indonesian"
//	               }
//	            ],
//	            "text": "Indonesian"
//	         },
//	         "preferred": true
//	      }
//	   ],
//	   "extension": [
//	      {
//	         "url": "https://fhir.kemkes.go.id/r4/StructureDefinition/birthPlace",
//	         "valueAddress": {
//	            "city": "Bandung",
//	            "country": "ID"
//	         }
//	      },
//	      {
//	         "url": "https://fhir.kemkes.go.id/r4/StructureDefinition/citizenshipStatus",
//	         "valueCode": "WNI"
//	      }
//	   ]
//	}