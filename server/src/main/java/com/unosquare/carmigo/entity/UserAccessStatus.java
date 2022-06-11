package com.unosquare.carmigo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_access_status")
public class UserAccessStatus {

  @Id
  @SequenceGenerator(name = "user_access_status_id_seq", sequenceName = "user_access_status_id_seq", allocationSize = 1)
  @GeneratedValue(generator = "user_access_status_id_seq", strategy = GenerationType.SEQUENCE)
  @Column(name = "id", updatable = false, nullable = false)
  private int id;

  @Column(name = "status", nullable = false)
  private String status;
}
