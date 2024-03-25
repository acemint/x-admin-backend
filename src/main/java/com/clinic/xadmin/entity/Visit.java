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

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "xa_visit")
public class Visit {

  @Id
  @Column(name = "id")
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="patient_id")
  private Patient patient;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="employee_id")
  private Employee employee;

  @Column(name = "cancelled")
  private Boolean cancelled;

  @Column(name = "startTime")
  private LocalDateTime startTime;

  @Column(name = "endTIme")
  private LocalDateTime endTime;

  @OneToOne(mappedBy = "visit")
  private VisitTreatment visitTreatment;

}
