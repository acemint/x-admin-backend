package com.clinic.xadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "xa_visit_treatment")
public class VisitTreatment extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "treatment_id")
  private Treatment treatment;

  @Column(name = "description")
  private String description;

  @OneToOne
  @JoinColumn(name ="visit_id")
  private Visit visit;


}
