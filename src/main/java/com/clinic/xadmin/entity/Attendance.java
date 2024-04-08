package com.clinic.xadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "xa_attendance")
public class Attendance extends BaseEntity {

  @ManyToOne
  @JoinColumn(name ="employee_id", nullable = false)
  private Employee employee;

  @Column(name = "clock_in", nullable = false)
  private LocalDateTime clockIn;

  @Column(name = "clock_out", nullable = false)
  private LocalDateTime clockOut;

}
