package com.clinic.xadmin.model.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SatuSehatPatientFilter {

  @NotNull
  private String searchBy;

  private String nik;

  private String motherNik;

  private String name;
  private String dateOfBirth;
  private String gender;

}
