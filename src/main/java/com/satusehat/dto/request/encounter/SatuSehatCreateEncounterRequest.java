package com.satusehat.dto.request.encounter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.satusehat.constant.coding.ActCode;
import com.satusehat.constant.coding.ParticipationType;
import com.satusehat.dto.request.commons.Individual;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SatuSehatCreateEncounterRequest {

  private String resourceType = "Encounter";
  private String status;

  @JsonProperty(value = "identifier")
  private List<EncounterIdentifier> identifiers;

  @JsonProperty(value = "class")
  private ActCode actCode;

  @JsonProperty(value = "subject")
  private Individual patient;

  @JsonProperty(value = "participant")
  private List<Participant> participants;

  private Period period;

  @JsonProperty(value = "location")
  private List<Location> locations;

  @JsonProperty(value = "statusHistory")
  private List<StatusHistory> statusHistory;

  @JsonProperty(value = "serviceProvider")
  private Individual serviceProvider;

  @Data
  @Builder
  public static class EncounterIdentifier {

    private String system;

    @JsonProperty(value = "value")
    private String organizationId;

    public static class EncounterIdentifierBuilder {

      public EncounterIdentifierBuilder system(String organizationKey) {
        this.system = "http://sys-ids.kemkes.go.id/encounter/" + organizationKey;
        return this;
      }

    }

  }

  @Data
  @Builder
  public static class Participant {

    @JsonProperty(value = "type")
    private List<ListParticipant> participantType;

    @JsonProperty(value = "individual")
    private Individual individual;

  }


  @Data
  @Builder
  public static class ListParticipant {

    @JsonProperty(value = "coding")
    public List<ParticipationType> participationType;

  }

  @Data
  @Builder
  public static class Period {

    private String start;

  }

  @Data
  @Builder
  public static class Location {

    @JsonProperty(value = "location")
    private Individual locationData;

  }

  @Data
  @Builder
  public static class StatusHistory {

    private String status;
    private Period period;

  }

}

// EXAMPLE:
//	{
//	    "resourceType": "Encounter",
//	    "status": "arrived",
//	    "class": {
//	        "system": "http://terminology.hl7.org/CodeSystem/v3-ActCode",
//	        "code": "AMB",
//	        "display": "ambulatory"
//	    },
//	    "subject": {
//	        "reference": "Patient/100000030009",
//	        "display": "Budi Santoso"
//	    },
//	    "participant": [
//	        {
//	            "type": [
//	                {
//	                    "coding": [
//	                        {
//	                            "system": "http://terminology.hl7.org/CodeSystem/v3-ParticipationType",
//	                            "code": "ATND",
//	                            "display": "attender"
//	                        }
//	                    ]
//	                }
//	            ],
//	            "individual": {
//	                "reference": "Practitioner/N10000001",
//	                "display": "Dokter Bronsig"
//	            }
//	        }
//	    ],
//	    "period": {
//	        "start": "2022-06-14T07:00:00+07:00"
//	    },
//	    "location": [
//	        {
//	            "location": {
//	                "reference": "Location/b017aa54-f1df-4ec2-9d84-8823815d7228",
//	                "display": "Ruang 1A, Poliklinik Bedah Rawat Jalan Terpadu, Lantai 2, Gedung G"
//	            }
//	        }
//	    ],
//	    "statusHistory": [
//	        {
//	            "status": "arrived",
//	            "period": {
//	                "start": "2022-06-14T07:00:00+07:00"
//	            }
//	        }
//	    ],
//	    "serviceProvider": {
//	        "reference": "Organization/0397200a-764b-4e63-bdbd-8cd00e68b18e"
//	    },
//	    "identifier": [
//	        {
//	            "system": "http://sys-ids.kemkes.go.id/encounter/0397200a-764b-4e63-bdbd-8cd00e68b18e",
//	            "value": "P20240001"
//	        }
//	    ]
//	}