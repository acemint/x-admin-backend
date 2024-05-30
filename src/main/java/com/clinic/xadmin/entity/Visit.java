package com.clinic.xadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

  @ManyToOne
  @JoinColumn(name ="room_id", nullable = false)
  private Room room;

  @Column(name = "code", nullable = false)
  private String code;

  @Column(name = "status", nullable = false)
  private String status;

  @Column(name = "start_time", nullable = false)
  private LocalDateTime startTime;

  @Column(name = "end_time")
  private LocalDateTime endTime;

  @Column(name = "satu_sehat_encounter_reference_id", nullable = false)
  private String satuSehatEncounterReferenceId;

}
