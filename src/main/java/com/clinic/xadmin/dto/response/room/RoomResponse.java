package com.clinic.xadmin.dto.response.room;

import com.clinic.xadmin.dto.response.member.MemberResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {

  private String code;
  private String name;
  private String description;

}
