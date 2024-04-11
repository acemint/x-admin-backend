package com.clinic.xadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

  @Id
  @Column(name = "id")
  private String id;

  @CreatedBy
  @Column(name = "created_by")
  private String createdBy;

  @LastModifiedBy
  @Column(name = "last_modified_by")
  private String lastModifiedBy;

  @CreatedDate
  @Column(name = "created_date")
  private LocalDateTime createdDate;

  @LastModifiedDate
  @Column(name = "last_modified_date")
  private LocalDateTime lastModifiedDate;

  @PrePersist
  protected void onCreate() {
    if (Objects.isNull(this.id)) {
      this.id = UUID.randomUUID().toString();
    }
  }


}
