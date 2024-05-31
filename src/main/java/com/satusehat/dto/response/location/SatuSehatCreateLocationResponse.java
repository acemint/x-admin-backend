package com.satusehat.dto.response.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.satusehat.constant.coding.LocationPhysicalType;
import com.satusehat.dto.request.commons.Address;
import com.satusehat.dto.request.commons.Coordinate;
import com.satusehat.dto.request.commons.Individual;
import com.satusehat.dto.request.commons.Telecommunication;
import com.satusehat.dto.request.location.SatuSehatCreateLocationRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SatuSehatCreateLocationResponse {

  private String id;

}