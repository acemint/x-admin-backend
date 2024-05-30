package com.satusehat.dto.response.encounter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.satusehat.constant.coding.ActCode;
import com.satusehat.constant.coding.ParticipationType;
import com.satusehat.dto.request.commons.Individual;
import com.satusehat.dto.request.encounter.SatuSehatCreateEncounterRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SatuSehatCreateEncounterResponse {

  private String id;
  private String resourceType = "Encounter";
  private String status;

  @JsonProperty(value = "identifier")
  private List<SatuSehatCreateEncounterRequest.EncounterIdentifier> identifiers;

  @JsonProperty(value = "class")
  private ActCode actCode;

  @JsonProperty(value = "subject")
  private Individual patient;

  @JsonProperty(value = "participant")
  private List<SatuSehatCreateEncounterRequest.Participant> participants;

  private SatuSehatCreateEncounterRequest.Period period;

  @JsonProperty(value = "location")
  private List<SatuSehatCreateEncounterRequest.Location> locations;

  @JsonProperty(value = "statusHistory")
  private List<SatuSehatCreateEncounterRequest.StatusHistory> statusHistory;

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
    private final List<ListParticipant> participantType = new ArrayList<>();

    @JsonProperty(value = "individual")
    private Individual individual;

  }


  @Data
  @Builder
  public static class ListParticipant {

    @JsonProperty(value = "coding")
    public final List<ParticipationType> participationType = new ArrayList<>();

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
