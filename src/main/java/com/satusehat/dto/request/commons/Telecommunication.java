package com.satusehat.dto.request.commons;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class Telecommunication {

  private String system;
  private String use;
  private String value;

  public Telecommunication buildForPhoneNumber(String phoneNumber, @Nullable String use) {
    this.system = "phone";
    this.use = Optional.ofNullable(use).orElse("-");;
    this.value = "+" + phoneNumber;
    return this;
  }

  public Telecommunication buildForEmail(String email, @Nullable String use) {
    this.system = "email";
    this.use = Optional.ofNullable(use).orElse("-");
    this.value = email;
    return this;
  }

  public Telecommunication buildForWebsite(String url, @Nullable String use) {
    this.system = "url";
    this.use = Optional.ofNullable(use).orElse("-");
    this.value = url;
    return this;
  }

  public Telecommunication buildForFax(String fax, @Nullable String use) {
    this.system = "fax";
    this.use = Optional.ofNullable(use).orElse("-");
    this.value = fax;
    return this;
  }


}
