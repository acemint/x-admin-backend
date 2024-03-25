package com.clinic.xadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "xa_treatment")
public class Treatment {

  @Id
  @Column(name = "id")
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="clinic_id")
  private Clinic clinic;

  @Column(name = "code")
  private String code;

  @Column(name = "name")
  private String name;

  @Column(name = "price")
  private BigDecimal price;

  @OneToMany(mappedBy = "treatment", fetch = FetchType.LAZY)
  private Set<VisitTreatment> visitTreatments;

}
