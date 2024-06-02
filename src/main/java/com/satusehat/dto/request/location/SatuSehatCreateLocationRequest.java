package com.satusehat.dto.request.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.satusehat.constant.coding.LocationPhysicalType;
import com.satusehat.dto.request.commons.Address;
import com.satusehat.dto.request.commons.Coordinate;
import com.satusehat.dto.request.commons.Individual;
import com.satusehat.dto.request.commons.Telecommunication;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SatuSehatCreateLocationRequest {

  private final String resourceType = "Location";

  private final String status = "active";

  private List<LocationIdentifier> identifier;

  private String name;

  private String description;

  private final String mode = "instance";

  @JsonProperty(value = "telecom")
  private List<Telecommunication> telecommunications = new ArrayList<>();

  @JsonProperty(value = "address")
  private Address address;

  private PhysicalType physicalType;

  private Coordinate position;

  private Individual managingOrganization;


  @Data
  @Builder
  public static class LocationIdentifier {

    private String system;

    @JsonProperty(value = "value")
    private String locationName;

    public static class LocationIdentifierBuilder {

      public LocationIdentifierBuilder system(String organizationKey) {
        this.system = "http://sys-ids.kemkes.go.id/location/" + organizationKey;
        return this;

      }

    }

  }

  @Data
  @Builder
  public static class PhysicalType {

    public List<LocationPhysicalType> coding;

  }

}

// EXAMPLE
// {
//    "resourceType": "Location",
//    "identifier": [
//        {
//            "system": "http://sys-ids.kemkes.go.id/location/{{Org_id}}",
//            "value": "G-2-R-1A"
//        }
//    ],
//    "status": "active",
//    "name": "Ruang 1A IRJT",
//    "description": "Ruang 1A, Poliklinik Bedah Rawat Jalan Terpadu, Lantai 2, Gedung G",
//    "mode": "instance",
//    "telecom": [ {} ],
//    "address": { },
//    "physicalType": {
//        "coding": [ { } ]
//    },
//    "position": {
//        "longitude": -6.23115426275766,
//        "latitude": 106.83239885393944,
//        "altitude": 0
//    },
//    "managingOrganization": {
//        "reference": "Organization/{{Org_id}}"
//    }
// }