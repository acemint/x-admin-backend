package com.clinic.xadmin.model.member;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MemberFilter {

  @Nullable
  private String clinicCode;

  @Nullable
  private String name;

  private String role;

  private FilterIHSCode filterIHSCode;

  @Getter
  @Builder
  public static class FilterIHSCode {

    private boolean isNull;

  }

  @Builder.Default
  private Pageable pageable = Pageable.ofSize(10);

}
