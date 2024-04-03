package com.clinic.xadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="patient_id", nullable = false)
  private Patient patient;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="employee_id", nullable = false)
  private Employee employee;

  @Column(name = "cancelled", nullable = false)
  private Boolean cancelled;

  @Column(name = "startTime", nullable = false)
  private LocalDateTime startTime;

  @Column(name = "endTIme", nullable = false)
  private LocalDateTime endTime;

}
