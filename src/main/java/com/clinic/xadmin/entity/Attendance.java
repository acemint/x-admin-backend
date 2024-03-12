package com.clinic.xadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "xa_attendance")
public class Attendance {

  @Id
  @Column(name = "id")
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="employee_id")
  private Employee employee;

  @Column(name = "clock_in")
  private LocalDateTime clockIn;

  @Column(name = "clock_out")
  private LocalDateTime clockOut;

}
