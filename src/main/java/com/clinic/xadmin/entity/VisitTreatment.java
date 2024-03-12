package com.clinic.xadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "xa_visit_treatment")
public class VisitTreatment {

  @Id
  @Column(name = "id")
  private String id;

  @ManyToOne
  @JoinColumn(name = "treatment_id")
  private Treatment treatment;

  @Column(name = "description")
  private String description;

  @OneToOne
  @JoinColumn(name ="visit_id")
  private Visit visit;


}
