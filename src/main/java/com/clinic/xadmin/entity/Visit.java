package com.clinic.xadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "xa_visit")
public class Visit extends BaseEntity {

  @ManyToOne
  @JoinColumn(name ="practitioner_id", nullable = false)
  private Member practitioner;

  @ManyToOne
  @JoinColumn(name ="patient_id", nullable = false)
  private Member patient;

  @Column(name = "code", nullable = false)
  private String code;

  @Column(name = "cancelled", nullable = false)
  private Boolean cancelled;

  @Column(name = "startTime", nullable = false)
  private LocalDateTime startTime;

  @Column(name = "endTIme", nullable = false)
  private LocalDateTime endTime;

}
