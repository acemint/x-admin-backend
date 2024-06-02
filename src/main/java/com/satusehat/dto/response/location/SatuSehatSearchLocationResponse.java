package com.satusehat.dto.response.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.satusehat.constant.coding.LocationPhysicalType;
import com.satusehat.dto.request.commons.Address;
import com.satusehat.dto.request.commons.Individual;
import com.satusehat.dto.request.commons.Telecommunication;
import com.satusehat.dto.request.location.SatuSehatCreateLocationRequest;
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
public class SatuSehatSearchLocationResponse {

  private String id;
  private final String resourceType = "Location";

  private final String status = "active";

  private List<LocationIdentifier> identifier;

  private String name;

  private String description;

  private final String mode = "instance";

  @JsonProperty(value = "telecom")
  private List<Telecommunication> telecommunications = new ArrayList<>();

  private PhysicalType physicalType;

  @JsonProperty(value = "address")
  private Address address;

  private Individual managingOrganization;


  @Data
  @Builder
  public static class LocationIdentifier {

    private String system;

    @JsonProperty(value = "value")
    private String locationName;

  }

  @Data
  @Builder
  public static class PhysicalType {

    public final List<LocationPhysicalType> coding = new ArrayList<>();

  }

}